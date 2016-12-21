package com.example.jack.cglohas;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeFragment extends Fragment {
    View view;
    Button btLogout;
    Button btLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("G.C.LOHAS");
        view = inflater.inflate(R.layout.home_fragment, container, false);
        final View coordinatorLayout = view.findViewById(R.id.coordinatorLayout);

        //右下(+)圓形選單
        View btAdd = view.findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Having a CoordinatorLayout in your view hierarchy
                // allows Snackbar to enable certain features,
                // such as swipe-to-dismiss and
                // automatically moving of widgets like FloatingActionButton.
                Snackbar.make(
                        coordinatorLayout,
                        "Add button clicked",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        });
        return view;
    }


}
