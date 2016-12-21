package com.example.jack.cglohas._01_Register.Fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.GetUserImageTask;
import com.example.jack.cglohas.R;

import static android.R.attr.id;
import static com.example.jack.cglohas.Common.rb;

public class MemberCenterFragment extends Fragment {
    View view;
    ImageView userImg;
    TextView userName,userPhone,userBirthDate,userEmail,registerDate;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("Member Center");
        view = inflater.inflate(R.layout.fragment_member_center, container, false);
        String url = Common.URL + "AD_UserImageServlet";
        int id = rb.getUserID();
        int imageSize = 250;
        new GetUserImageTask(userImg).execute(url, id, imageSize);
        userImg = (ImageView) view.findViewById(R.id.userImg);
        userName = (TextView) view.findViewById(R.id.userName);
        userName.setText(rb.getName());
        userPhone = (TextView) view.findViewById(R.id.userPhone);
        userPhone.setText(rb.getCellphone().substring(0,5)+"*****");
        userBirthDate = (TextView) view.findViewById(R.id.userBirthDate);
        userBirthDate.setText(rb.getBirthdate1());
        userEmail = (TextView) view.findViewById(R.id.userEmail);
        userEmail.setText(rb.getEmail().substring(0,5)+"*****");
        registerDate = (TextView) view.findViewById(R.id.registerDate);
        registerDate.setText(rb.getCreateTime().toString().substring(0,rb.getCreateTime().toString().lastIndexOf(":")));






        return view;
    }


}
