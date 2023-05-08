package xyz.fycz.myreader.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import io.reactivex.disposables.Disposable;
import xyz.fycz.myreader.R;
import xyz.fycz.myreader.application.App;
import xyz.fycz.myreader.base.BaseActivity;
import xyz.fycz.myreader.base.BitIntentDataManager;
import xyz.fycz.myreader.base.observer.MySingleObserver;
import xyz.fycz.myreader.common.APPCONST;
import xyz.fycz.myreader.databinding.ActivityLoginBinding;
import xyz.fycz.myreader.greendao.DbManager;
import xyz.fycz.myreader.model.user.Result;
import xyz.fycz.myreader.model.user.User;
import xyz.fycz.myreader.model.user.UserService2;
import xyz.fycz.myreader.ui.dialog.DialogCreator;
import xyz.fycz.myreader.ui.dialog.LoadingDialog;
import xyz.fycz.myreader.util.CodeUtil;
import xyz.fycz.myreader.util.CyptoUtils;
import xyz.fycz.myreader.util.ToastUtils;
import xyz.fycz.myreader.util.utils.NetworkUtils;
import xyz.fycz.myreader.util.utils.StringUtils;

/**
 * @author fengyue
 * @date 2020/9/18 22:27
 */
public class LoginActivity extends BaseActivity implements TextWatcher {

    private ActivityLoginBinding binding;
    private String code;
    private Disposable loginDisp;
    private LoadingDialog dialog;
    private User user;

    @Override
    protected void bindView() {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        setStatusBarColor(R.color.colorPrimary, true);
        getSupportActionBar().setTitle("登录");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        dialog = new LoadingDialog(this, "正在登录", () -> {
            if (loginDisp != null) {
                loginDisp.dispose();
            }
        });
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        createCaptcha();
        String username = UserService2.INSTANCE.readUsername();
        binding.etUser.getEditText().setText(username);
        binding.etUser.getEditText().requestFocus(username.length());
        //监听内容改变 -> 控制按钮的点击状态
        binding.etUser.getEditText().addTextChangedListener(this);
        binding.etPassword.getEditText().addTextChangedListener(this);
    }

    @Override
    protected void initClick() {
        super.initClick();
        binding.ivCaptcha.setOnClickListener(v -> createCaptcha());

        binding.btLogin.setOnClickListener(v -> {


            binding.btLogin.setEnabled(false);
            final String loginName = binding.etUser.getEditText().getText().toString().trim();
            String loginPwd = binding.etPassword.getEditText().getText().toString();
            List<User> list= DbManager.getInstance().getSession().getUserDao().loadAll();
            for (User u:list){
                if (u.getUserName().equals(loginName)&&u.getPassword().equals(loginPwd)){
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    return;
                }
            }
            ToastUtils.showError("登录失败！");
        });

        binding.tvForgotPwd.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, AuthEmailActivity.class);
            startActivity(intent);
        });

        binding.tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginSuccess() {
        UserService2.INSTANCE.writeConfig(user);
        UserService2.INSTANCE.writeUsername(user.getUserName());
        Intent intent = new Intent();
        intent.putExtra("isLogin", true);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    public void createCaptcha() {
        code = CodeUtil.getInstance().createCode();
        Bitmap codeBitmap = CodeUtil.getInstance().createBitmap(code);
        binding.ivCaptcha.setImageBitmap(codeBitmap);
    }


    /**
     * 当有控件获得焦点focus 自动弹出键盘
     * 1. 点击软键盘的enter键 自动收回键盘
     * 2. 代码控制 InputMethodManager
     * requestFocus
     * showSoftInput:显示键盘 必须先让这个view成为焦点requestFocus
     * <p>
     * hideSoftInputFromWindow 隐藏键盘
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //隐藏键盘
            //1.获取系统输入的管理器
            InputMethodManager inputManager =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            //2.隐藏键盘
            inputManager.hideSoftInputFromWindow(binding.etUser.getWindowToken(), 0);

            //3.取消焦点
            View focusView = getCurrentFocus();
            if (focusView != null) {
                focusView.clearFocus(); //取消焦点
            }

            //getCurrentFocus().clearFocus();

            //focusView.requestFocus();//请求焦点
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        //禁止输入中文
        StringUtils.isNotChinese(s);
        //判断两个输入框是否有内容
        if (binding.etUser.getEditText().getText().toString().length() > 0 &&
                binding.etPassword.getEditText().getText().toString().length() > 0 ) {
            //按钮可以点击
            binding.btLogin.setEnabled(true);
        } else {
            //按钮不能点击
            binding.btLogin.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == APPCONST.REQUEST_AUTH_EMAIL) {
                loginSuccess();
            }
        }
    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
    }
}
