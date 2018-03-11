package net.fofi.app.improve.account.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.fofi.app.R;
import net.fofi.app.api.RetrofitHelper;
import net.fofi.app.api.remote.FOFIApi;
import net.fofi.app.improve.account.User;
import net.fofi.app.improve.account.base.AccountBaseActivity;
import net.fofi.app.improve.account.constants.UserConstants;
import net.fofi.app.mobim.ui.MainActivity;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 登录界面 LoginActivity
 * Created by ZYY on 2018/2/26.
 */

public class LoginActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    public static final String HOLD_USERNAME_KEY = "holdUsernameKey";

    // bufferknife 自动绑定部分
    @BindView(R.id.ly_retrieve_bar)
    LinearLayout mLayBackBar;

    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.tv_navigation_label)
    TextView tvNavigationLabel;

    @BindView(R.id.iv_login_logo)
    ImageView ivLoginLogo;
    @BindView(R.id.iv_login_username_icon)
    ImageView ivLoginUsernameIcon;
    @BindView(R.id.et_login_username)
    EditText etLoginUsername;
    @BindView(R.id.iv_login_username_del)
    ImageView ivLoginUsernameDel;
    @BindView(R.id.ll_login_username)
    LinearLayout llLoginUsername;

    @BindView(R.id.iv_login_pwd_icon)
    ImageView ivLoginPwdIcon;
    @BindView(R.id.et_login_pwd)
    EditText etLoginPwd;
    @BindView(R.id.iv_login_pwd_del)
    ImageView ivLoginPwdDel;
    @BindView(R.id.ll_login_pwd)
    LinearLayout llLoginPwd;
    @BindView(R.id.iv_login_hold_pwd)
    ImageView ivLoginHoldPwd;

    @BindView(R.id.tv_login_forget_pwd)
    TextView tvLoginForgetPwd;
    @BindView(R.id.bt_login_submit)
    Button btLoginSubmit;
    @BindView(R.id.bt_login_register)
    Button btLoginRegister;
    @BindView(R.id.lay_login_container)
    LinearLayout layLoginContainer;

    @BindView(R.id.ib_login_weibo)
    ImageView ibLoginWeibo;
    @BindView(R.id.ib_login_wx)
    ImageView ibLoginWx;
    @BindView(R.id.ib_login_qq)
    ImageView ibLoginQq;

    @BindView(R.id.ll_login_options)
    LinearLayout llLoginOptions;
    @BindView(R.id.ll_login_pull)
    LinearLayout llLoginPull;
    @BindView(R.id.ll_login_layer)
    View llLoginLayer;

    // 网络类、FOFIApi
    private Call<User> callback;
    private User user;
    private FOFIApi fofiApi;

    private int mLogoHeight;
    private int mLogoWidth;

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * show the login activity
     *
     * @param context context
     */
    public static void show(Activity context, int requestCode) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * show the login activity
     *
     * @param fragment fragment
     */
    public static void show(Fragment fragment, int requestCode) {
        Intent intent = new Intent(fragment.getActivity(), LoginActivity.class);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_login;
    }

    @Override
    protected void initWidget(){
        super.initWidget();
        llLoginLayer.setVisibility(View.GONE);
        etLoginUsername.setOnFocusChangeListener(this);
        etLoginUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String username = s.toString().trim();
                if (username.length() > 0) {
                    llLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                    ivLoginUsernameDel.setVisibility(View.VISIBLE);
                } else {
                    llLoginUsername.setBackgroundResource(R.drawable.bg_login_input_ok);
                    ivLoginUsernameDel.setVisibility(View.INVISIBLE);
                }

                String pwd = etLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        etLoginPwd.setOnFocusChangeListener(this);
        etLoginPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                if (length > 0) {
                    llLoginPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    ivLoginPwdDel.setVisibility(View.VISIBLE);
                } else {
                    ivLoginPwdDel.setVisibility(View.INVISIBLE);
                }

                String username = etLoginUsername.getText().toString().trim();
                if (TextUtils.isEmpty(username)) {
                    showToastForKeyBord(R.string.message_username_null);
                }
                String pwd = etLoginPwd.getText().toString().trim();
                if (!TextUtils.isEmpty(pwd)) {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btLoginSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btLoginSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }
            }
        });

        tvNavigationLabel.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void initData() {
        super.initData();//必须要,用来注册本地广播

        //初始化控件状态数据
        SharedPreferences sp = getSharedPreferences(UserConstants.HOLD_ACCOUNT, Context.MODE_PRIVATE);
        String holdUsername = sp.getString(HOLD_USERNAME_KEY, null);
        //String holdPwd = sp.getString(HOLD_PWD_KEY, null);
        //int holdStatus = sp.getInt(HOLD_PWD_STATUS_KEY, 0);//0第一次默认/1用户设置保存/2用户设置未保存

        etLoginUsername.setText(holdUsername);
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
    }

    // bufferknife 自动绑定部分
    @Override
    @OnClick({R.id.ib_navigation_back, R.id.et_login_username, R.id.iv_login_username_del,
            R.id.et_login_pwd, R.id.iv_login_pwd_del, R.id.iv_login_hold_pwd,
            R.id.tv_login_forget_pwd, R.id.bt_login_submit, R.id.bt_login_register,
            R.id.lay_login_container, R.id.ib_login_weibo, R.id.ib_login_wx,
            R.id.ib_login_qq, R.id.ll_login_pull})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.et_login_username:
                // 用户名（手机号）
                etLoginPwd.clearFocus();
                etLoginUsername.setFocusableInTouchMode(true);
                etLoginUsername.requestFocus();
                break;
            case R.id.iv_login_username_del:
                // 清空用户名（手机号）
                etLoginUsername.setText(null);
                break;
            case R.id.et_login_pwd:
                // 密码
                etLoginUsername.clearFocus();
                etLoginPwd.setFocusableInTouchMode(true);
                etLoginPwd.requestFocus();
                break;
            case R.id.iv_login_pwd_del:
                // 清空密码
                etLoginPwd.setText(null);
                break;
            case R.id.iv_login_hold_pwd:
                // 记住密码
                break;
            case R.id.tv_login_forget_pwd:
                // 忘记密码
                RetrieveActivity.show(LoginActivity.this);
                break;
            case R.id.bt_login_submit:
                // 用户登录
                loginRequest();
                break;
            case R.id.bt_login_register:
                // 用户注册
                RegisterStepOneActivity.show(LoginActivity.this);
                break;
            case R.id.lay_login_container:
                break;
            case R.id.ib_login_weibo:
                // 第三方-微博登录
                Toast.makeText(getApplicationContext(), "微博登录接口（待实现）", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_login_wx:
                // 第三方-微信登录
                Toast.makeText(getApplicationContext(), "微信登录接口（待实现）", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ib_login_qq:
                // 第三方-QQ登录
                Toast.makeText(getApplicationContext(), "QQ登录接口（待实现）", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ll_login_pull:
                llLoginPull.animate().cancel();
                llLoginLayer.animate().cancel();

                int height = llLoginOptions.getHeight();
                float progress = (llLoginLayer.getTag() != null && llLoginLayer.getTag() instanceof Float) ?
                        (float) llLoginLayer.getTag() : 1;
                int time = (int) (360 * progress);

                if (llLoginPull.getTag() != null) {
                    llLoginPull.setTag(null);
                    glide(height, progress, time);
                } else {
                    llLoginPull.setTag(true);
                    upGlide(height, progress, time);
                }
                break;
        }
    }

    /**
     * menu up glide
     *
     * @param height   height
     * @param progress progress
     * @param time     time
     */
    private void upGlide(int height, float progress, int time) {
        llLoginPull.animate()
                .translationYBy(height * progress)
                .translationY(0)
                .setDuration(time)
                .start();
        llLoginLayer.animate()
                .alphaBy(1 - progress)
                .alpha(1)
                .setDuration(time)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        llLoginLayer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            llLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            llLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }
                })
                .start();
    }

    /**
     * menu glide
     *
     * @param height   height
     * @param progress progress
     * @param time     time
     */
    private void glide(int height, float progress, int time) {
        llLoginPull.animate()
                .translationYBy(height - height * progress)
                .translationY(height)
                .setDuration(time)
                .start();

        llLoginLayer.animate()
                .alphaBy(1 * progress)
                .alpha(0)
                .setDuration(time)
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            llLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (animation instanceof ValueAnimator) {
                            llLoginLayer.setTag(((ValueAnimator) animation).getAnimatedValue());
                        }
                        llLoginLayer.setVisibility(View.GONE);
                    }
                })
                .start();
    }


    private void loginRequest() {

        String tempUsername = etLoginUsername.getText().toString().trim();
        String tempPwd = etLoginPwd.getText().toString().trim();

//        Log.d("LoginActivity", "tempUsername-"+tempUsername);
//        Log.d("LoginActivity", "tempPwd-"+tempPwd);
//        Log.d("LoginActivity", "getSha1(tempPwd)-"+getSha1(tempPwd));

        if (!TextUtils.isEmpty(tempPwd) && !TextUtils.isEmpty(tempUsername)) {
            //登录成功,请求数据进入用户个人中心页面

//            if (TDevice.hasInternet()) {
//                requestLogin(tempUsername, tempPwd);
//            } else {
//                showToastForKeyBord(R.string.footer_type_net_error);
//            }
            requestLogin(tempUsername, tempPwd);

        } else {
            showToastForKeyBord(R.string.login_input_username_hint_error);
        }

    }

    private void requestLogin(String tempUsername, String tempPwd) {
        fofiApi = new RetrofitHelper().getFofiApi();
        callback = fofiApi.login(tempUsername, getSha1(tempPwd));

        // 发送网络请求(异步)
        callback.enqueue(new Callback<User>() {
            // 请求成功时回调
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("Login", "登录成功");
                user = response.body();
                // 跳转
                MainActivity.show(LoginActivity.this, user);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            // 请求失败时候的回调
            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("Login", "登录失败");
                showToastForKeyBord("登录异常");
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        int id = v.getId();

        if (id == R.id.et_login_username) {
            if (hasFocus) {
                llLoginUsername.setActivated(true);
                llLoginPwd.setActivated(false);
            }
        } else {
            if (hasFocus) {
                llLoginPwd.setActivated(true);
                llLoginUsername.setActivated(false);
            }
        }
    }

    @Override
    public void onGlobalLayout() {
        final ImageView ivLogo = this.ivLoginLogo;
        Rect KeypadRect = new Rect();

        mLayBackBar.getWindowVisibleDisplayFrame(KeypadRect);

        int screenHeight = mLayBackBar.getRootView().getHeight();

        int keypadHeight = screenHeight - KeypadRect.bottom;

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
                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
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
                    ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
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
