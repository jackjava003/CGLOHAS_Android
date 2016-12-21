package com.example.jack.cglohas._02_Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.FrontPage;
import com.example.jack.cglohas.HomeFragment;
import com.example.jack.cglohas.InitFragmentActivity;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._00_GlobalService.RegularExpressionUtil;
import com.example.jack.cglohas._01_Register.New_Register;
import com.example.jack.cglohas._01_Register.register_bean;


public class LoginDialogActivity extends AppCompatActivity {
    private static final int REQ_LOGIN = 1;
    private EditText etUser;
    private EditText etPassword;
    private TextView tvMessage, link_signup;
    private SharedPreferences pref;
    private Button btLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        setTitle("Login");
        findViews();
        pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        String account = pref.getString("Account", "");
        String password = pref.getString("Password", "");
        if (account.length() != 0 && password.length() != 0) {
            etUser.setText(account);
            etPassword.setText(password);
        }
        setResult(RESULT_CANCELED);
    }

    private void findViews() {
        etUser = (EditText) findViewById(R.id.etUser);
        etPassword = (EditText) findViewById(R.id.etPassword);
        btLogin = (Button) findViewById(R.id.btLogin);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        link_signup = (TextView) findViewById(R.id.link_signup);
        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginDialogActivity.this, New_Register.class);
                startActivity(intent);
                //LoginDialogActivity.this.finish();
            }
        });
        btLogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String result = "";

                if (user.length() <= 0 || password.length() <= 0) {
                    showMessage(R.string.msg_EmptyUserOrPassword);
                    return;
                } else if (RegularExpressionUtil.isAcValid(user) == false || RegularExpressionUtil.isPwValid(password) == false) {
                    showMessage(R.string.msg_WrongUserOrPassword);
                    return;
                } else {
                    if (Common.networkConnected(LoginDialogActivity.this)) {
                        String url = Common.URL + "AD_LoginServlet";
                        try {
                            register_bean rb = new LoginResultTask().execute(url, user, password).get();
                            //Log.e("FINALL",result);
                            if (rb != null) {

                                Common.rb = rb;
                                Common.login = true;
                                pref.edit().putString("Account", user).putString("Password", password).commit();
                                result = "OK";
                                //this line fail to toast BIG-5
                                // /Common.showToast(LoginDialogActivity.this, rb.getName() + R.string.wellcome);
                                Common.showToast(LoginDialogActivity.this, "Wellcome  " + rb.getName() + "   , Login Sucess!");
                                Intent intent = new Intent();
                                intent.setClass(LoginDialogActivity.this, InitFragmentActivity.class);
                                startActivity(intent);
                            } else {
                                result = "NG";
                                Common.showToast(LoginDialogActivity.this, "Account does not exist or the password is incorrect");
                                return;
                            }

                        } catch (Exception e) {
                            Log.e("LOGIN ERROR", e.toString());
                        }
                    } else {
                        Common.showToast(LoginDialogActivity.this, R.string.msg_NoNetwork);
                    }

                    //LoginDialogActivity.this.finish();

                    //setContentView(R.layout.home_fragment);
//                                    Button btLogin = (Button) findViewById(R.id.btLogin);
//                                    btLogin.setVisibility(View.INVISIBLE);
//                        Intent homeIntent = new Intent(this, HomeFragment.class);
//                        startActivityForResult(homeIntent, REQ_LOGIN);
                }

//                    if (isUserValid(user, password)) {
//                        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
//                                MODE_PRIVATE);
//                        pref.edit()
//                                .putBoolean("login", true)
//                                .putString("user", user)
//                                .putString("password", password)
//                                .apply();
//                        setResult(RESULT_OK);
//                        finish();
//                    } else {
//                        showMessage(R.string.msg_InvalidUserOrPassword);
//                    }
            }
        });
    }

//        @Override
//        protected void onStart() {
//            super.onStart();
//
//            if (login) {
//                String name = pref.getString("user", "");
//                String password = pref.getString("password", "");
//                if (isUserValid(name, password)) {
//                    setResult(RESULT_OK);
//                    finish();
//                } else {
//                    showMessage(R.string.msg_InvalidUserOrPassword);
//                }
//            }



    private void showMessage(int msgResId) {
        tvMessage.setTypeface(null, Typeface.BOLD_ITALIC);
        tvMessage.setText(msgResId);

    }
//
//        private boolean isUserValid(String name, String password) {
//            // 應該連線至server端檢查帳號密碼是否正確
//            return name.equals("a");
//        }

}


