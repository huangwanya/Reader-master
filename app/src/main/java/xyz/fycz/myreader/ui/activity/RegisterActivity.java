package xyz.fycz.myreader.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import io.reactivex.disposables.Disposable;
import xyz.fycz.myreader.R;
import xyz.fycz.myreader.application.App;
import xyz.fycz.myreader.base.BaseActivity;
import xyz.fycz.myreader.base.MyTextWatcher;
import xyz.fycz.myreader.base.observer.MySingleObserver;
import xyz.fycz.myreader.common.APPCONST;
import xyz.fycz.myreader.databinding.ActivityRegisterBinding;
import xyz.fycz.myreader.greendao.DbManager;
import xyz.fycz.myreader.model.user.Result;
import xyz.fycz.myreader.model.user.User;
import xyz.fycz.myreader.model.user.UserService2;
import xyz.fycz.myreader.ui.dialog.DialogCreator;
import xyz.fycz.myreader.ui.dialog.LoadingDialog;
import xyz.fycz.myreader.util.CodeUtil;
import xyz.fycz.myreader.util.CyptoUtils;
import xyz.fycz.myreader.util.ToastUtils;
import xyz.fycz.myreader.util.utils.StringUtils;

/**
 * @author fengyue
 * @date 2020/9/18 22:37
 */
public class RegisterActivity extends BaseActivity {

    private ActivityRegisterBinding binding;

    private String code;
    private String username = "";
    private String password = "";
    private String email = "";
    private String emailCode = "";
    private String keyc = "";
    private String inputCode = "";
    private LoadingDialog dialog;
    private Disposable disp;

    @Override
    protected void bindView() {
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void setUpToolbar(Toolbar toolbar) {
        super.setUpToolbar(toolbar);
        setStatusBarColor(R.color.colorPrimary, true);
        getSupportActionBar().setTitle("注册");
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        dialog = new LoadingDialog(this, "正在注册", () -> {
            if (disp != null) {
                disp.dispose();
            }
        });
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        createCaptcha();
        binding.etUsername.requestFocus();
        binding.etUsername.getEditText().addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                StringUtils.isNotChinese(s);
                username = s.toString();
                if (username.length() < 6 || username.length() > 14) {
                    showTip("用户名必须在6-14位之间");
                } else if (!username.substring(0, 1).matches("^[A-Za-z]$")) {
                    showTip("用户名只能以字母开头");
                } else if (!username.matches("^[A-Za-z0-9-_]+$")) {
                    showTip("用户名只能由数字、字母、下划线、减号组成");
                } else {
                    binding.tvRegisterTip.setVisibility(View.GONE);
                }
                checkNotNone();
            }
        });

        binding.etPassword.getEditText().addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                password = s.toString();
                if (password.length() < 8 || password.length() > 16) {
                    showTip("密码必须在8-16位之间");
                } else if (password.matches("^\\d+$")) {
                    showTip("密码不能是纯数字");
                } else {
                    binding.tvRegisterTip.setVisibility(View.GONE);
                }
                checkNotNone();
            }
        });




    }

    @Override
    protected void initClick() {
        super.initClick();

        binding.btRegister.setOnClickListener(v -> {
            if (!username.matches("^[A-Za-z][A-Za-z0-9]{5,13}$")) {
                DialogCreator.createTipDialog(this, "用户名格式错误",
                        "用户名必须在6-14位之间\n用户名只能以字母开头\n用户名只能由数字、字母、下划线、减号组成");
            } else if (password.matches("^\\d+$") || !password.matches("^.{8,16}$")) {
                DialogCreator.createTipDialog(this, "密码格式错误",
                        "密码必须在8-16位之间\n密码不能是纯数字");
            } else {
                List<User> list=DbManager.getInstance().getSession().getUserDao().loadAll();
                for (User u:list){
                    if (u.getUserName().equals(username)){
                        ToastUtils.showError("已存在用户!");
                        return;

                    }
                }
                User user = new User();
                user.setUserName(username);
                user.setPassword(password);
                DbManager.getInstance().getSession().getUserDao().insert(user);
                ToastUtils.showSuccess("注册成功!");
                finish();
            }
        });
    }

    public void createCaptcha() {
        code = CodeUtil.getInstance().createCode();
        Bitmap codeBitmap = CodeUtil.getInstance().createBitmap(code);
        binding.ivCaptcha.setImageBitmap(codeBitmap);
    }

    public void showTip(String tip) {
        binding.tvRegisterTip.setVisibility(View.VISIBLE);
        binding.tvRegisterTip.setText(tip);
    }


    public void checkNotNone() {
        binding.btRegister.setEnabled(!"".equals(username) &&
                !"".equals(password));
    }

    @Override
    protected void onDestroy() {
        dialog.dismiss();
        super.onDestroy();
    }
}
