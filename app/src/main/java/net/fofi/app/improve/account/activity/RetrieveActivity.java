package net.fofi.app.improve.account.activity;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.MobSDK;

import net.fofi.app.R;
import net.fofi.app.api.RetrofitHelper;
import net.fofi.app.api.remote.FOFIApi;
import net.fofi.app.improve.account.User;
import net.fofi.app.improve.account.base.AccountBaseActivity;
import net.fofi.app.improve.utils.parser.RichTextParser;
import net.fofi.app.util.TDevice;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 忘记密码界面第一步 RetrieveActivity
 * 输入手机号，判断该手机号是否已经注册
 * 是 则 发送验证码
 * 否 则 Toast提示用户
 * Created by ZYY on 2018/2/27.
 */

public class RetrieveActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(R.id.ly_retrieve_bar)
    LinearLayout mLlRetrieveBar;

    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.tv_navigation_label)
    TextView tvNavigationLabel;
    @BindView(R.id.iv_retrieve_tel_icon)
    ImageView ivRetrieveTelIcon;
    @BindView(R.id.et_retrieve_tel)
    EditText etRetrieveTel;
    @BindView(R.id.iv_retrieve_tel_del)
    ImageView ivRetrieveTelDel;
    @BindView(R.id.ll_retrieve_tel)
    LinearLayout llRetrieveTel;
    @BindView(R.id.iv_retrieve_code_icon)
    ImageView ivRetrieveCodeIcon;
    @BindView(R.id.et_retrieve_code_input)
    EditText etRetrieveCodeInput;
    @BindView(R.id.retrieve_sms_call)
    TextView retrieveSmsCall;
    @BindView(R.id.ll_retrieve_code)
    LinearLayout llRetrieveCode;
    @BindView(R.id.bt_retrieve_submit)
    Button btRetrieveSubmit;
    @BindView(R.id.tv_retrieve_label)
    TextView tvRetrieveLabel;
    @BindView(R.id.lay_retrieve_container)
    LinearLayout layRetrieveContainer;

    private boolean mMachPhoneNum;

    private CountDownTimer mTimer;

    private int mRequestType;

    private int mTopMargin;

    // 网络类、FOFIApi
    private Call<String> callback;
    private User user;
    private FOFIApi fofiApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobSDK.init(this);
    }

    /**
     * show the retrieve activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, RetrieveActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_retrieve_pwd;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        TextView tvLabel = (TextView) mLlRetrieveBar.findViewById(R.id.tv_navigation_label);
        tvLabel.setText(R.string.retrieve_pwd_label);
        etRetrieveTel.setOnFocusChangeListener(this);
        etRetrieveTel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.length();
                if (length > 0) {
                    ivRetrieveTelDel.setVisibility(View.VISIBLE);
                } else {
                    ivRetrieveTelDel.setVisibility(View.INVISIBLE);
                }
            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                String input = s.toString();
                mMachPhoneNum = RichTextParser.machPhoneNum(input);

                //对提交控件的状态判定
                if (mMachPhoneNum) {
                    String smsCode = etRetrieveCodeInput.getText().toString().trim();

                    if (!TextUtils.isEmpty(smsCode)) {
                        btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                        btRetrieveSubmit.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                        btRetrieveSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                    }
                } else {
                    btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btRetrieveSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

                if (length > 0 && length < 11) {
                    llRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
                    retrieveSmsCall.setAlpha(0.4f);
                } else if (length == 11) {
                    if (mMachPhoneNum) {
                        llRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_ok);
                        if (retrieveSmsCall.getTag() == null) {
                            retrieveSmsCall.setAlpha(1.0f);
                        } else {
                            retrieveSmsCall.setAlpha(0.4f);
                        }
                    } else {
                        llRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
                        showToastForKeyBord(R.string.hint_username_ok);
                        retrieveSmsCall.setAlpha(0.4f);
                    }
                } else if (length > 11) {
                    retrieveSmsCall.setAlpha(0.4f);
                    llRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_error);
                } else if (length <= 0) {
                    retrieveSmsCall.setAlpha(0.4f);
                    llRetrieveTel.setBackgroundResource(R.drawable.bg_login_input_ok);
                }

            }
        });
        etRetrieveCodeInput.setOnFocusChangeListener(this);
        etRetrieveCodeInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @SuppressWarnings("deprecation")
            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0 && mMachPhoneNum) {
                    btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btRetrieveSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btRetrieveSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btRetrieveSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
                llRetrieveCode.setBackgroundResource(R.drawable.bg_login_input_ok);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();//必须要调用,用来注册本地广播
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLlRetrieveBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
        mLlRetrieveBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }

    @OnClick({R.id.ib_navigation_back, R.id.iv_retrieve_tel_del, R.id.retrieve_sms_call,
            R.id.bt_retrieve_submit, R.id.tv_retrieve_label, R.id.lay_retrieve_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.iv_retrieve_tel_del:
                // 清空手机号
                etRetrieveTel.setText(null);
                break;
            case R.id.retrieve_sms_call:
                // 获取验证码
                isRegister();
                break;
            case R.id.bt_retrieve_submit:
                // 判断用户是否已经注册
                requestRetrievePwd();
                break;
            case R.id.tv_retrieve_label:
                // ??
                break;
            case R.id.lay_retrieve_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    private void isRegister(){
        String phoneNumber = etRetrieveTel.getText().toString().trim();
        fofiApi = new RetrofitHelper().getFofiApi();
        callback = fofiApi.isRegister(phoneNumber);

        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("RegisterStepOne","该手机号已被注册");
                requestSmsCode();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("RegisterStepOne","该手机号还未被注册");
                // Toast提醒用户
                showToastForKeyBord("该手机号还未被注册,请注册");
            }
        });
    }

    private void requestRetrievePwd() {

        String smsCode = etRetrieveCodeInput.getText().toString().trim();
        if (!mMachPhoneNum || TextUtils.isEmpty(smsCode)) {
            // showToastForKeyBord(R.string.hint_username_ok);
            return;
        }

//        if (!TDevice.hasInternet()) {
//            showToastForKeyBord(R.string.tip_network_error);
//            return;
//        }
        mRequestType = 2;
        String phoneNumber = etRetrieveTel.getText().toString().trim();
//        OSChinaApi.validateRegisterInfo(phoneNumber, smsCode, mHandler);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String phoneNumber = etRetrieveTel.getText().toString().trim();
                String smsCode = etRetrieveCodeInput.getText().toString().trim();
                submitCode("86", phoneNumber, smsCode);
            }
        }).start();

    }

    private void requestSmsCode() {
        if (!mMachPhoneNum) {
            //showToastForKeyBord(R.string.hint_username_ok);
            return;
        }
        if (!TDevice.hasInternet()) {
            showToastForKeyBord(R.string.tip_network_error);
            return;
        }

        if (retrieveSmsCall.getTag() == null) {
            mRequestType = 1;
            retrieveSmsCall.setAlpha(0.6f);
            retrieveSmsCall.setTag(true);
            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    retrieveSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    retrieveSmsCall.setTag(null);
                    retrieveSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    retrieveSmsCall.setAlpha(1.0f);
                }
            }.start();
//            String phoneNumber = etRetrieveTel.getText().toString().trim();
//            OSChinaApi.sendSmsCode(phoneNumber, OSChinaApi.REGISTER_INTENT, mHandler);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    String phoneNumber = etRetrieveTel.getText().toString().trim();
                    sendCode("86", phoneNumber);
                }
            }).start();

        } else {
//            AppContext.showToast(getResources().getString(R.string.register_sms_wait_hint), Toast.LENGTH_SHORT);
        }
    }

    // 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
    public void sendCode(String country, String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Log.d("RegisterStepOne:", "成功得到验证码");
                } else {
                    // TODO 处理错误的结果
                    Log.d("RegisterStepOne:", "未能得到验证码");
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }

    // 提交验证码，其中的code表示验证码，如“1357”
    public void submitCode(String country, final String phone, String code) {
        // 注册一个事件回调，用于处理提交验证码操作的结果
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理验证成功的结果
                    Log.d("RegisterStepOne:", "验证码验证成功");
                    // 验证成功跳转到重置密码页面
                    ResetPwdActivity.show(RetrieveActivity.this, phone);
                } else {
                    // TODO 处理错误的结果
                    Log.d("RegisterStepOne:", "验证码验证失败");
                }

            }
        });
        // 触发操作
        SMSSDK.submitVerificationCode(country, phone, code);
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        int id = view.getId();
        switch (id) {
            case R.id.et_retrieve_tel:
                if (hasFocus) {
                    llRetrieveTel.setActivated(true);
                    llRetrieveCode.setActivated(false);
                }
                break;
            case R.id.et_retrieve_code_input:
                if (hasFocus) {
                    llRetrieveCode.setActivated(true);
                    llRetrieveTel.setActivated(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {
        final LinearLayout layRetrieveTel = this.llRetrieveTel;
        Rect KeypadRect = new Rect();

        mLlRetrieveBar.getWindowVisibleDisplayFrame(KeypadRect);

        int screenHeight = mLlRetrieveBar.getRootView().getHeight();

        int keypadHeight = screenHeight - KeypadRect.bottom;

        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }

        if (keypadHeight > 0 && layRetrieveTel.getTag() == null) {
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layRetrieveTel.getLayoutParams();
            final int topMargin = layoutParams.topMargin;
            this.mTopMargin = topMargin;
            layRetrieveTel.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    layRetrieveTel.requestLayout();
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();


        } else if (keypadHeight == 0 && layRetrieveTel.getTag() != null) {
            final int topMargin = mTopMargin;
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layRetrieveTel.getLayoutParams();
            layRetrieveTel.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    layRetrieveTel.requestLayout();
                }
            });
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        }

    }
}
