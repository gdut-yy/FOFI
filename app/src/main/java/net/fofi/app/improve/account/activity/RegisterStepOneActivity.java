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

import com.mob.MobSDK;

import net.fofi.app.R;
import net.fofi.app.api.RetrofitHelper;
import net.fofi.app.api.remote.FOFIApi;
import net.fofi.app.improve.account.User;
import net.fofi.app.improve.account.Users;
import net.fofi.app.improve.account.base.AccountBaseActivity;
import net.fofi.app.improve.utils.parser.RichTextParser;
import net.fofi.app.util.TDevice;

import butterknife.BindView;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册界面第一步 RegisterStepOneActivity
 * 输入手机号，判断该手机号是否已经注册
 * 是 则 Toast提示用户
 * 否 则 发送验证码
 * Created by ZYY on 2018/2/27.
 */

public class RegisterStepOneActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    @BindView(R.id.ly_retrieve_bar)
    LinearLayout mLayBackBar;

    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.tv_navigation_label)
    TextView tvNavigationLabel;
    @BindView(R.id.iv_login_logo)
    ImageView ivLoginLogo;
    @BindView(R.id.iv_register_username_icon)
    ImageView ivRegisterUsernameIcon;
    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.iv_register_username_del)
    ImageView ivRegisterUsernameDel;
    @BindView(R.id.ll_register_phone)
    LinearLayout llRegisterPhone;
    @BindView(R.id.iv_register_auth_code_icon)
    ImageView ivRegisterAuthCodeIcon;
    @BindView(R.id.et_register_auth_code)
    EditText etRegisterAuthCode;
    @BindView(R.id.tv_register_sms_call)
    TextView tvRegisterSmsCall;
    @BindView(R.id.ll_register_sms_code)
    LinearLayout llRegisterSmsCode;
    @BindView(R.id.bt_register_submit)
    Button btRegisterSubmit;
    @BindView(R.id.lay_register_one_container)
    LinearLayout layRegisterOneContainer;

    private boolean mMachPhoneNum;

    private CountDownTimer mTimer;

//    private int mRequestType = 1;//1. 请求发送验证码  2.请求phoneToken

    // 网络类、FOFIApi
    private Call<String> callback;
    private User user;
    private FOFIApi fofiApi;

    private int mLogoHeight;
    private int mLogoWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 初始化 MobSDK
        MobSDK.init(this);
    }

    /**
     * show the register activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, RegisterStepOneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_register_step_one;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        TextView label = (TextView) mLayBackBar.findViewById(R.id.tv_navigation_label);
        label.setVisibility(View.INVISIBLE);

        etRegisterUsername.addTextChangedListener(
                new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        int length = s.length();
                        if (length > 0) {
                            ivRegisterUsernameDel.setVisibility(View.VISIBLE);
                        } else {
                            ivRegisterUsernameDel.setVisibility(View.INVISIBLE);
                        }
                    }

                    @SuppressWarnings("deprecation")
                    @Override
                    public void afterTextChanged(Editable s) {
                        int length = s.length();
                        String input = s.toString();
                        mMachPhoneNum = RichTextParser.machPhoneNum(input);

                        if (mMachPhoneNum) {
                            String smsCode = etRegisterAuthCode.getText().toString().trim();

                            if (!TextUtils.isEmpty(smsCode)) {
                                btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                                btRegisterSubmit.setTextColor(getResources().getColor(R.color.white));
                            } else {
                                btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                                btRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                            }
                        } else {
                            btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                            btRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                        }

                        if (length > 0 && length < 11) {
                            llRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                            tvRegisterSmsCall.setAlpha(0.4f);
                        } else if (length == 11) {
                            if (mMachPhoneNum) {
                                llRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_ok);
                                if (tvRegisterSmsCall.getTag() == null) {
                                    tvRegisterSmsCall.setAlpha(1.0f);
                                } else {
                                    tvRegisterSmsCall.setAlpha(0.4f);
                                }
                            } else {
                                llRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                                showToastForKeyBord(R.string.hint_username_ok);
                                tvRegisterSmsCall.setAlpha(0.4f);
                            }
                        } else if (length > 11) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                            llRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_error);
                        } else if (length <= 0) {
                            tvRegisterSmsCall.setAlpha(0.4f);
                            llRegisterPhone.setBackgroundResource(R.drawable.bg_login_input_ok);
                        }
                    }
                }

        );
        etRegisterUsername.setOnFocusChangeListener(this);
        etRegisterAuthCode.setOnFocusChangeListener(this);
        etRegisterAuthCode.addTextChangedListener(new TextWatcher() {
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
                    btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btRegisterSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
                llRegisterSmsCode.setBackgroundResource(R.drawable.bg_login_input_ok);
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
        mLayBackBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
        mLayBackBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);

        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    }


    @Override
    @OnClick({R.id.ib_navigation_back, R.id.iv_register_username_del, R.id.tv_register_sms_call,
            R.id.bt_register_submit, R.id.lay_register_one_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.iv_register_username_del:
                // 清空手机号
                etRegisterUsername.setText(null);
                break;
            case R.id.tv_register_sms_call:
                // 获取验证码
                isRegister();
                break;
            case R.id.bt_register_submit:
                // 注册
                requestRegister();
                break;
            case R.id.lay_register_one_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    private void isRegister(){
        String phoneNumber = etRegisterUsername.getText().toString().trim();
        fofiApi = new RetrofitHelper().getFofiApi();
        callback = fofiApi.isRegister(phoneNumber);

        callback.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("RegisterStepOne","该手机号已被注册");
                Log.d("RegisterStepOne","手机号-"+response.body());
                showToastForKeyBord("该手机号已经被注册");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("RegisterStepOne","该手机号还未被注册");
                requestSmsCode();
            }
        });
    }

    private void requestRegister() {

        String smsCode = etRegisterAuthCode.getText().toString().trim();
        if (!mMachPhoneNum || TextUtils.isEmpty(smsCode)) {
            //showToastForKeyBord(R.string.hint_username_ok);
            return;
        }

//        if (!TDevice.hasInternet()) {
//            showToastForKeyBord(R.string.tip_network_error);
//            return;
//        }

//        mRequestType = 2;
//        String phoneNumber = etRegisterUsername.getText().toString().trim();
//        OSChinaApi.validateRegisterInfo(phoneNumber, smsCode, mHandler);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String phoneNumber = etRegisterUsername.getText().toString().trim();
                String smsCode = etRegisterAuthCode.getText().toString().trim();
                submitCode("86", phoneNumber, smsCode);
            }
        }).start();

    }

    private void requestSmsCode() {
        if (!mMachPhoneNum) {
            //showToastForKeyBord(R.string.hint_username_ok);
            return;
        }
//        if (!TDevice.hasInternet()) {
//            showToastForKeyBord(R.string.tip_network_error);
//            return;
//        }

        if (tvRegisterSmsCall.getTag() == null) {
//            mRequestType = 1;
            tvRegisterSmsCall.setAlpha(0.6f);
            tvRegisterSmsCall.setTag(true);
            mTimer = new CountDownTimer(60 * 1000, 1000) {

                @SuppressLint("DefaultLocale")
                @Override
                public void onTick(long millisUntilFinished) {
                    tvRegisterSmsCall.setText(String.format("%s%s%d%s",
                            getResources().getString(R.string.register_sms_hint), "(", millisUntilFinished / 1000, ")"));
                }

                @Override
                public void onFinish() {
                    tvRegisterSmsCall.setTag(null);
                    tvRegisterSmsCall.setText(getResources().getString(R.string.register_sms_hint));
                    tvRegisterSmsCall.setAlpha(1.0f);
                }
            }.start();

//            OSChinaApi.sendSmsCode(phoneNumber, OSChinaApi.REGISTER_INTENT, mHandler);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    String phoneNumber = etRegisterUsername.getText().toString().trim();
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
                    // 验证成功跳转到注册页面
                    RegisterStepTwoActivity.show(RegisterStepOneActivity.this, phone);
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
            case R.id.et_register_username:
                if (hasFocus) {
                    llRegisterPhone.setActivated(true);
                    llRegisterSmsCode.setActivated(false);
                }
                break;
            case R.id.et_register_auth_code:
                if (hasFocus) {
                    llRegisterSmsCode.setActivated(true);
                    llRegisterPhone.setActivated(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {

        final ImageView ivLogo = this.ivLoginLogo;

        Rect keypadRect = new Rect();

        mLayBackBar.getWindowVisibleDisplayFrame(keypadRect);

        int screenHeight = mLayBackBar.getRootView().getHeight();

        int keypadHeight = screenHeight - keypadRect.bottom;
        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }

        if (keypadHeight > 0 && ivLogo.getTag() == null) {
            final int height = ivLogo.getHeight();
            final int width = ivLogo.getWidth();
            this.mLogoHeight = height;
            this.mLogoWidth = width;
            ivLogo.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivLogo.getLayoutParams();

                    layoutParams.height = (int) (height * animatedValue);
                    layoutParams.width = (int) (width * animatedValue);
                    ivLogo.requestLayout();
                    ivLogo.setAlpha(animatedValue);
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();


        } else if (keypadHeight == 0 && ivLogo.getTag() != null) {
            final int height = mLogoHeight;
            final int width = mLogoWidth;
            ivLogo.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ivLogo.getLayoutParams();
                    layoutParams.height = (int) (height * animatedValue);
                    layoutParams.width = (int) (width * animatedValue);
                    ivLogo.requestLayout();
                    ivLogo.setAlpha(animatedValue);
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        }
    }
}
