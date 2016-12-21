package com.example.jack.cglohas;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.jack.cglohas._01_Register.Fragment.MemberCenterFragment;
import com.example.jack.cglohas._01_Register.MemberFragment;
import com.example.jack.cglohas._01_Register.New_Register;
import com.example.jack.cglohas._02_Login.LoginDialogActivity;
import com.example.jack.cglohas._03_Recipes.RecipesFragment;
import com.example.jack.cglohas._04_listItems.ItemFragment;
import com.example.jack.cglohas._07_Shop.Main_StoreFragment;
import com.example.jack.cglohas._07_Shop.ShopFragment;

import static com.example.jack.cglohas.Common.rb;

/**
 * Created by JACK on 2016/10/10.
 */

public class InitFragmentActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    public ImageView userPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_fragment);
        setUpActionBar();
        //左側攔
        initDrawer();
        initBody();


    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // home icon will keep still without calling syncState()
        actionBarDrawerToggle.syncState();
    }

    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    //左上hamberger選單
    private void initDrawer() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.text_Open, R.string.text_Close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        userPhoto = (ImageView) findViewById(R.id.ivUser);
        TextView tvUserName = (TextView) findViewById(R.id.tvUserName);
        TextView tvEmail = (TextView) findViewById(R.id.tvEmail);


        NavigationView view_start = (NavigationView) findViewById(R.id.navigation_start);
        Menu menuNav = view_start.getMenu();
        if (Common.login == true) {
            if (Common.userPhoto == null) {
                String url = Common.URL + "AD_UserImageServlet";
                int id = rb.getUserID();
                int imageSize = 250;
                new GetUserImageTask(userPhoto).execute(url, id, imageSize);
            } else {
                userPhoto.setImageBitmap(Common.userPhoto);
            }
            tvUserName.setText(rb.getName());
            tvEmail.setText(rb.getEmail().substring(0,5)+"*****");
            MenuItem LogOut = menuNav.findItem(R.id.LogOut);
            MenuItem MemberCenter = menuNav.findItem(R.id.MemberCenter);
            MenuItem Login = menuNav.findItem(R.id.Login);
            MenuItem RegisterNow = menuNav.findItem(R.id.RegisterNow);
            Login.setVisible(false);
            LogOut.setVisible(true);
            MemberCenter.setVisible(true);
            RegisterNow.setVisible(false);
        }else{
            MenuItem RegisterNow = menuNav.findItem(R.id.RegisterNow);
            MenuItem LogOut = menuNav.findItem(R.id.LogOut);
            MenuItem MemberCenter = menuNav.findItem(R.id.MemberCenter);
            MenuItem Login = menuNav.findItem(R.id.Login);
            Login.setVisible(true);
            LogOut.setVisible(false);
            MemberCenter.setVisible(false);
            RegisterNow.setVisible(true);
        }
        view_start.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            //不同的選項被點擊就開啟不同的fragment　//switchFragment( ) 自訂方法
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                Fragment fragment;
                switch (menuItem.getItemId()) {
                    case R.id.item_Home:
                        initBody();
                        break;
                    case R.id.Login:
                        Intent intent_Login = new Intent();
                        intent_Login.setClass(InitFragmentActivity.this, LoginDialogActivity.class);
                        startActivity(intent_Login);
                        break;
                    case R.id.RegisterNow:
                        Intent intent_reg = new Intent();
                        intent_reg.setClass(InitFragmentActivity.this, New_Register.class);
                        startActivity(intent_reg);
                        break;
                    case R.id.MemberCenter:
                        fragment = new MemberFragment();
                        switchFragment(fragment);
                        break;
                    case R.id.item_Recipes:
                        fragment = new RecipesFragment();
                        switchFragment(fragment);
                        break;
                    case R.id.drawer_Shops:
                        fragment = new Main_StoreFragment();
                        switchFragment(fragment);
                        setTitle(R.string.text_Shops);
                        break;
                    //內有註解
                    case R.id.item_Items:
                        fragment = new ItemFragment();
                        switchFragment(fragment);
                        break;
                    case R.id.LogOut:
                        Common.login = false;
                        Common.rb = null;
                        Common.userPhoto = null;
                        Intent intent = new Intent();
                        intent.setClass(InitFragmentActivity.this, FrontPage.class);
                        startActivity(intent);
                        break;
                    default:
                        initBody();
                        break;
                }
                return true;
            }
        });
    }

    private void initBody() {
        Fragment fragment = new HomeFragment();
        switchFragment(fragment);
        setTitle(R.string.text_CGLOHAS);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void switchFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        //fragmentTransaction.add(fragment,"123");
        fragmentTransaction.replace(R.id.body, fragment);
        fragmentTransaction.addToBackStack("message");
        fragmentTransaction.commit();


        // will update the "progress" propriety of seekbar until it reaches progress
//        ObjectAnimator animation = ObjectAnimator.ofInt(seekbar, "progress", progress);
//        animation.setDuration(500); // 0.5 second
//        animation.setInterpolator(new DecelerateInterpolator());
//        animation.start();
    }
}
