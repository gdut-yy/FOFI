package net.fofi.app.improve.account.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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

import net.fofi.app.R;
import net.fofi.app.api.RetrofitHelper;
import net.fofi.app.api.remote.FOFIApi;
import net.fofi.app.improve.account.User;
import net.fofi.app.improve.account.base.AccountBaseActivity;
import net.fofi.app.util.TDevice;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Interceptor;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 注册界面第二步 RegisterStepTwoActivity
 * Created by ZYY on 2018/2/27.
 */

public class RegisterStepTwoActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    public static final String PHONE_NUM = "phoneNum";

    @BindView(R.id.ly_register_bar)
    LinearLayout mLlRegisterBar;

    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.tv_navigation_label)
    TextView tvNavigationLabel;
    @BindView(R.id.iv_register_username_icon)
    ImageView ivRegisterUsernameIcon;
    @BindView(R.id.et_register_username)
    EditText etRegisterUsername;
    @BindView(R.id.iv_register_username_del)
    ImageView ivRegisterUsernameDel;
    @BindView(R.id.ll_register_two_username)
    LinearLayout llRegisterTwoUsername;
    @BindView(R.id.iv_register_pwd_icon)
    ImageView ivRegisterPwdIcon;
    @BindView(R.id.et_register_pwd_input)
    EditText etRegisterPwdInput;
    @BindView(R.id.iv_register_pwd_del)
    ImageView ivRegisterPwdDel;
    @BindView(R.id.ll_register_two_pwd)
    LinearLayout llRegisterTwoPwd;
    @BindView(R.id.tv_register_man)
    TextView tvRegisterMan;
    @BindView(R.id.tv_register_female)
    TextView tvRegisterFemale;
    @BindView(R.id.bt_register_submit)
    Button btRegisterSubmit;
    @BindView(R.id.lay_register_two_container)
    LinearLayout layRegisterTwoContainer;

    private int mTopMargin;

    private String phone;

    // 网络类、FOFIApi
    private Call<User> callback;
    private User user;
    private FOFIApi fofiApi;

    /**
     * show register step two activity
     * @param context context
     * @param phone 手机号
     */
    public static void show(Context context, String phone) {
        Intent intent = new Intent(context, RegisterStepTwoActivity.class);
        intent.putExtra(PHONE_NUM, phone);
        context.startActivity(intent);
    }


    @Override
    protected int getContentView() {
        return R.layout.activity_main_register_step_two;
    }

    @Override
    protected void initWidget() {
        super.initWidget();

        TextView tvLabel = (TextView) mLlRegisterBar.findViewById(R.id.tv_navigation_label);
        tvLabel.setText(R.string.login_register_hint);

        etRegisterUsername.addTextChangedListener(new TextWatcher() {
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

                String smsCode = etRegisterPwdInput.getText().toString().trim();

                if (!TextUtils.isEmpty(smsCode)) {
                    btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btRegisterSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

                if (length > 0) {
                    ivRegisterUsernameDel.setVisibility(View.VISIBLE);
                } else {
                    ivRegisterUsernameDel.setVisibility(View.INVISIBLE);
                }

                if (length > 12) {
                    showToastForKeyBord(R.string.register_username_error);
                    llRegisterTwoUsername.setBackgroundResource(R.drawable.bg_login_input_error);
                } else {
                    llRegisterTwoUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                }
            }
        });
        etRegisterUsername.setOnFocusChangeListener(this);
        etRegisterPwdInput.setOnFocusChangeListener(this);
        etRegisterPwdInput.addTextChangedListener(new TextWatcher() {
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
                if (length < 6) {
                    llRegisterTwoPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                } else {
                    llRegisterTwoPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                }
                String username = etRegisterUsername.getText().toString().trim();
                if (!TextUtils.isEmpty(username)) {
                    btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btRegisterSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btRegisterSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btRegisterSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();//必须要调用,用来注册本地广播
        Intent intent = getIntent();
//        mPhoneToken = (PhoneToken) intent.getSerializableExtra(PHONE_TOKEN_KEY);
        phone = (String) intent.getStringExtra(PHONE_NUM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLlRegisterBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
        mLlRegisterBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @OnClick({R.id.ib_navigation_back, R.id.iv_register_username_del, R.id.tv_register_man, R.id.tv_register_female, R.id.bt_register_submit, R.id.lay_register_two_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.iv_register_username_del:
                // 清空用户名
                etRegisterUsername.setText(null);
                break;
            case R.id.tv_register_man:
                // 选择性别为男性
                if (tvRegisterMan.getTag() != null) {
                    Drawable left = getResources().getDrawable(R.mipmap.btn_gender_male_normal);
                    tvRegisterMan.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    tvRegisterMan.setTag(null);
                } else {
                    Drawable left = getResources().getDrawable(R.mipmap.btn_gender_male_actived);
                    tvRegisterMan.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    tvRegisterMan.setTag(false);
                    Drawable female = getResources().getDrawable(R.mipmap.btn_gender_female_normal);
                    tvRegisterFemale.setCompoundDrawablesWithIntrinsicBounds(female, null, null, null);
                    tvRegisterFemale.setTag(null);
                }
                break;
            case R.id.tv_register_female:
                // 选择性别为女性
                if (tvRegisterFemale.getTag() != null) {
                    Drawable left = getResources().getDrawable(R.mipmap.btn_gender_female_normal);
                    tvRegisterFemale.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    tvRegisterFemale.setTag(null);
                } else {
                    Drawable left = getResources().getDrawable(R.mipmap.btn_gender_female_actived);
                    tvRegisterFemale.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
                    tvRegisterFemale.setTag(true);

                    Drawable men = getResources().getDrawable(R.mipmap.btn_gender_male_normal);
                    tvRegisterMan.setCompoundDrawablesWithIntrinsicBounds(men, null, null, null);
                    tvRegisterMan.setTag(null);
                }
                break;
            case R.id.bt_register_submit:
                // 完成
                requestRegisterUserInfo();
                break;
            case R.id.lay_register_two_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    private void requestRegisterUserInfo() {

        String username = etRegisterUsername.getText().toString().trim();
        String pwd = etRegisterPwdInput.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(pwd)) {
            return;
        }

//        if (!TDevice.hasInternet()) {
//            showToastForKeyBord(R.string.tip_network_error);
//            return;
//        }

//        int gender = 0;
        String gender = null;

        Object isMan = tvRegisterMan.getTag();
        if (isMan != null) {
            gender = "male";
        }

        Object isFemale = tvRegisterFemale.getTag();
        if (isFemale != null) {
            gender = "female";
        }

        fofiApi = new RetrofitHelper().getFofiApi();
//        Log.d("RegisterStepTwo:", "phone="+phone);
//        Log.d("RegisterStepTwo:", "pwd="+pwd);
//        Log.d("RegisterStepTwo:", "username="+username);
//        Log.d("RegisterStepTwo:", "gender="+gender);
        callback = fofiApi.register(phone, getSha1(pwd), username, gender);

        callback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("RegisterStepTwo:", "注册成功");
                // 注册成功跳转到登录页面
                LoginActivity.show(RegisterStepTwoActivity.this);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("RegisterStepTwo:", "注册失败");
            }
        });

//        OSChinaApi.register(username, getSha1(pwd), gender, mPhoneToken.getToken(), mHandler);
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        int id = view.getId();
        switch (id) {
            case R.id.et_register_username:
                if (hasFocus) {
                    llRegisterTwoUsername.setActivated(true);
                    llRegisterTwoPwd.setActivated(false);
                }
                break;
            case R.id.et_register_pwd_input:
                if (hasFocus) {
                    llRegisterTwoPwd.setActivated(true);
                    llRegisterTwoUsername.setActivated(false);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onGlobalLayout() {
        final LinearLayout layRegisterTwoUsername = this.llRegisterTwoUsername;
        Rect keypadRect = new Rect();

        mLlRegisterBar.getWindowVisibleDisplayFrame(keypadRect);

        int screenHeight = mLlRegisterBar.getRootView().getHeight();
        int keypadHeight = screenHeight - keypadRect.bottom;

        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }

        if (keypadHeight > 0 && layRegisterTwoUsername.getTag() == null) {
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layRegisterTwoUsername.getLayoutParams();
            final int topMargin = layoutParams.topMargin;
            this.mTopMargin = topMargin;
            layRegisterTwoUsername.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    layRegisterTwoUsername.requestLayout();
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();

        } else if (keypadHeight == 0 && layRegisterTwoUsername.getTag() != null) {
            final int topMargin = mTopMargin;
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layRegisterTwoUsername.getLayoutParams();
            layRegisterTwoUsername.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    layRegisterTwoUsername.requestLayout();
                }
            });
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();

        }
    }
}
