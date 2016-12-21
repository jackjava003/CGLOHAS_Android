package com.example.jack.cglohas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jack.cglohas._01_Register.New_Register;
import com.example.jack.cglohas._02_Login.LoginDialogActivity;

/**
 * Created by JACK on 2016/10/9.
 */

public class FrontPage extends AppCompatActivity {
    Button LoginButton;
    Button LogOutButton;
    Button RegisterButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.front_page);
        setTitle("C.G.LOHAS");
        LoginButton = (Button) findViewById(R.id.LoginButton);
        LogOutButton = (Button) findViewById(R.id.LogOutButton);
        RegisterButton = (Button) findViewById(R.id.Register);
        TextView Skip = (TextView) findViewById(R.id.Skip);
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(FrontPage.this, InitFragmentActivity.class);
                startActivity(intent);
            }
        });

        if(Common.login==false){
            LoginButton.setVisibility(View.VISIBLE);
            LogOutButton.setVisibility(View.INVISIBLE);
            RegisterButton.setVisibility(View.VISIBLE);
            LoginButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(FrontPage.this, LoginDialogActivity.class);
                    startActivity(intent);
                    //FrontPage.this.finish();
                }
            });
            RegisterButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(FrontPage.this, New_Register.class);
                    startActivity(intent);
                   // FrontPage.this.finish();
                }
            });
        }else{
            LoginButton.setVisibility(View.INVISIBLE);
            LogOutButton.setVisibility(View.VISIBLE);
            RegisterButton.setVisibility(View.INVISIBLE);
            LogOutButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Common.login = false;
                    Common.rb = null;
                    Common.userPhoto=null;
                    Intent intent = new Intent();
                    intent.setClass(FrontPage.this, FrontPage.class);
                    startActivity(intent);
                    FrontPage.this.finish();
                }
            });
        }
    }

}
