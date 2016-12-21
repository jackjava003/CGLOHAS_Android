package com.example.jack.cglohas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {

    TextView txtMessage;
    // Animation
    Animation animFadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("C.G.LOHAS");

        txtMessage = (TextView) findViewById(R.id.txtMessage);

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        // set animation listener
        animFadein.setAnimationListener(this);
        txtMessage.setTypeface(null, Typeface.BOLD_ITALIC);
        txtMessage.setVisibility(View.VISIBLE);
        // start the animation
        txtMessage.startAnimation(animFadein);

    }
    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation

        // check for fade in animation
        if (animation == animFadein) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, FrontPage.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
    }
    @Override
    public void onAnimationRepeat(Animation animation) {}

    @Override
    public void onAnimationStart(Animation animation) {}
}

