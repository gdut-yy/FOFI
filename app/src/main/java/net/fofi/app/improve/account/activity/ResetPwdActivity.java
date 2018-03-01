package net.fofi.app.improve.account.activity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 重置密码界面 ResetPwdActivity
 * Created by ZYY on 2018/2/27.
 */

public class ResetPwdActivity extends AccountBaseActivity implements View.OnClickListener, View.OnFocusChangeListener,
        ViewTreeObserver.OnGlobalLayoutListener {

    public static final String PHONE_NUM = "phoneNum";

    @BindView(R.id.ly_reset_bar)
    LinearLayout mLlResetBar;

    @BindView(R.id.ib_navigation_back)
    ImageButton ibNavigationBack;
    @BindView(R.id.tv_navigation_label)
    TextView tvNavigationLabel;
    @BindView(R.id.iv_reset_pwd_icon)
    ImageView ivResetPwdIcon;
    @BindView(R.id.et_reset_pwd)
    EditText etResetPwd;
    @BindView(R.id.iv_reset_pwd_del)
    ImageView ivResetPwdDel;
    @BindView(R.id.ll_reset_pwd)
    LinearLayout llResetPwd;
    @BindView(R.id.bt_reset_submit)
    Button btResetSubmit;
    @BindView(R.id.lay_reset_container)
    LinearLayout layResetContainer;

    private int mTopMargin;

    private String phone;

    // 网络类、FOFIApi
    private Call<User> callback;
    private User user;
    private FOFIApi fofiApi;

    /**
     * show the resetPwdActivity
     * @param context context
     * @param phone 手机号
     */
    public static void show(Context context, String phone) {
        Intent intent = new Intent(context, ResetPwdActivity.class);
        intent.putExtra(PHONE_NUM, phone);
        context.startActivity(intent);
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main_reset_pwd;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        TextView tvLabel = (TextView) mLlResetBar.findViewById(R.id.tv_navigation_label);
        tvLabel.setText(R.string.reset_pwd_label);
        etResetPwd.setOnFocusChangeListener(this);
        etResetPwd.addTextChangedListener(new TextWatcher() {
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
                if (length >= 6) {
                    ivResetPwdDel.setVisibility(View.VISIBLE);
                    llResetPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    btResetSubmit.setBackgroundResource(R.drawable.bg_login_submit);
                    btResetSubmit.setTextColor(getResources().getColor(R.color.white));
                } else {
                    if (length <= 0) {
                        ivResetPwdDel.setVisibility(View.GONE);
                        llResetPwd.setBackgroundResource(R.drawable.bg_login_input_ok);
                    } else {
                        ivResetPwdDel.setVisibility(View.VISIBLE);
                        llResetPwd.setBackgroundResource(R.drawable.bg_login_input_error);
                    }
                    btResetSubmit.setBackgroundResource(R.drawable.bg_login_submit_lock);
                    btResetSubmit.setTextColor(getResources().getColor(R.color.account_lock_font_color));
                }

            }
        });
    }

    @Override
    protected void initData() {
        super.initData();//必须要调用,用来注册本地广播
        Intent intent = getIntent();
//        mPhoneToken = (PhoneToken) intent.getSerializableExtra(RegisterStepTwoActivity.PHONE_TOKEN_KEY);
        phone = (String) intent.getStringExtra(PHONE_NUM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLlResetBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideKeyBoard(getCurrentFocus().getWindowToken());
        mLlResetBar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @OnClick({R.id.ib_navigation_back, R.id.iv_reset_pwd_del, R.id.bt_reset_submit, R.id.lay_reset_container})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;
            case R.id.iv_reset_pwd_del:
                // 清空密码
                etResetPwd.setText(null);
                break;
            case R.id.bt_reset_submit:
                // 确认
                requestResetPwd();
                break;
            case R.id.lay_reset_container:
                hideKeyBoard(getCurrentFocus().getWindowToken());
                break;
        }
    }

    private void requestResetPwd() {
        String tempPwd = etResetPwd.getText().toString().trim();
        if (TextUtils.isEmpty(tempPwd) || tempPwd.length() < 6) {
            //showToastForKeyBord(R.string.reset_pwd_hint);
            return;
        }
//        if (!TDevice.hasInternet()) {
//            showToastForKeyBord(R.string.tip_network_error);
//            return;
//        }

//        OSChinaApi.resetPwd(getSha1(tempPwd), mPhoneToken.getToken(), mHandler);

        fofiApi = new RetrofitHelper().getFofiApi();
        callback = fofiApi.resetPwd(phone, getSha1(tempPwd));

        callback.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                Log.d("ResetPwd:", "密码重置成功");
                user = response.body();
                Log.d("ResetPwd:", "新密码"+user.getPassword());
                // 密码重置成功后跳转回登录页面
                LoginActivity.show(ResetPwdActivity.this);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("ResetPwd:", "密码重置失败");
            }
        });

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        if (hasFocus) {
            llResetPwd.setActivated(true);
        }
    }

    @Override
    public void onGlobalLayout() {
        final LinearLayout kayResetPwd = this.llResetPwd;
        Rect keypadRect = new Rect();

        mLlResetBar.getWindowVisibleDisplayFrame(keypadRect);

        int screenHeight = mLlResetBar.getRootView().getHeight();

        int keypadHeight = screenHeight - keypadRect.bottom;

        if (keypadHeight > 0) {
            updateKeyBoardActiveStatus(true);
        } else {
            updateKeyBoardActiveStatus(false);
        }

        if (keypadHeight > 0 && kayResetPwd.getTag() == null) {
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) kayResetPwd.getLayoutParams();
            final int topMargin = layoutParams.topMargin;
            this.mTopMargin = topMargin;
            kayResetPwd.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    kayResetPwd.requestLayout();
                }
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();


        } else if (keypadHeight == 0 && kayResetPwd.getTag() != null) {
            final int topMargin = mTopMargin;
            final LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) kayResetPwd.getLayoutParams();
            kayResetPwd.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float animatedValue = (float) animation.getAnimatedValue();
                    layoutParams.topMargin = (int) (topMargin * animatedValue);
                    kayResetPwd.requestLayout();
                }
            });
            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();

        }
    }
}
