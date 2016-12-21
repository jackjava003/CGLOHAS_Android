package com.example.jack.cglohas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.example.jack.cglohas._01_Register.register_bean;
import com.example.jack.cglohas._04_listItems.OrderItemBean;

import java.util.LinkedHashMap;
import java.util.Map;


public class Common{
    public final static String FILE_NAME = "C.G.LOHAS";
    private static final String TAG = "Common";
    public final static String PREF_FILE = "preference";
    public static Map<Integer, OrderItemBean> cart = new LinkedHashMap<>();
    public static Boolean login = false;
    public static register_bean rb = null;
    public static Bitmap userPhoto = null;

   //public static String URL = "http://ec2-52-65-236-247.ap-southeast-2.compute.amazonaws.com:8080//CGLOHASAD/";
   public static String URL = "http://192.168.1.101:8080/CGLOHASAD/";
   //public static String URL = "http://localhost:8080/CGLOHASAD/";




    // check if the device connect to the network
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager conManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static  void switchFragment(Fragment oldFragment,Fragment newFragment,String message) {
        FragmentManager fragmentManager = oldFragment.getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction =
                fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.body, newFragment);
        fragmentTransaction.addToBackStack(message);
        fragmentTransaction.commit();
    }

}
