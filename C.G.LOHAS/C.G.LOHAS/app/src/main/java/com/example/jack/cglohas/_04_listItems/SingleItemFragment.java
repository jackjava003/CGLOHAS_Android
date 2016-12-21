package com.example.jack.cglohas._04_listItems;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._05_order.buyListFragment;

import java.util.List;
import java.util.Set;

/**
 * Created by dvd2631 on 2016/10/14.
 */

public class SingleItemFragment extends Fragment {
    private static final String TAG = "TAG";
    TextView showCount, totalPrice,qty,itemInfo,tvPrice,priceDiscount,itemName;
    ImageView minus,plus,addToCart,ivItem;
    ImageView add;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_single_item, container, false);
        final item_bean ib = (item_bean)getArguments().getSerializable("itemBean");
        getActivity().setTitle(ib.getName());
        add = (ImageView) view.findViewById(R.id.add);
        itemName = (TextView) view.findViewById(R.id.tvName);
        itemName.setText(ib.getName());
        ivItem = (ImageView) view.findViewById(R.id.ivItem);
        minus = (ImageView) view.findViewById(R.id.minus);
        plus = (ImageView) view.findViewById(R.id.plus);
        qty = (TextView) view.findViewById(R.id.qty);
        addToCart = (ImageView) view.findViewById(R.id.addToCart);
        String url = Common.URL + "ItemServlet";
        int id = ib.getItemID();
        Log.d("ID", String.valueOf(id));
        int imageSize = 250;
        new ItemGetImageTask(ivItem).execute(url, id, imageSize);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString()) + 1));
            }
        });
        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((Integer.parseInt(qty.getText().toString())) > 0) {
                    qty.setText(String.valueOf(Integer.parseInt(qty.getText().toString()) - 1));
                }
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int itemQty = Integer.parseInt(qty.getText().toString());
                OrderItemBean oi = new OrderItemBean(ib.getName(), ib.getItemID(), itemQty, ib.getPrice(), ib.getDiscount());
                oi.setStorage(ib.getStorage());
                if (itemQty <= 0) {
                    return;
                }
                ;
                if (Common.cart.get(ib.getItemID()) == null) {
                    if (itemQty > ib.getStorage()) {
                        Toast.makeText(getActivity(), "This item is out of stock", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Common.cart.put(ib.getItemID(), oi);
                } else {
                    OrderItemBean oib = Common.cart.get(ib.getItemID());
                    if ((itemQty + oib.getQty()) > ib.getStorage()) {
                        Toast.makeText(getActivity(), "This item is out of stock", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    oib.setQty(itemQty + oib.getQty());
                }
                TranslateAnimation anim = new TranslateAnimation(1246, 1246,1800, 973);
                anim.setDuration(800);
                add.setVisibility(View.VISIBLE);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation a) {}
                    public void onAnimationRepeat(Animation a) {}
                    public void onAnimationEnd(Animation a) { add.setVisibility(View.GONE);}
                });
                add.startAnimation(anim);
                if (Common.cart.size() >= 10) {
                    showCount.setText(String.valueOf(Common.cart.size()));
                    showCount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                } else {
                    showCount.setText(String.valueOf(Common.cart.size()));
                }
                Set<Integer> set = Common.cart.keySet();
                double subTotal = 0;
                for (int n : set) {
                    double price = Common.cart.get(n).getUnitPrice();
                    double discount = Common.cart.get(n).getDiscount();
                    int qty = Common.cart.get(n).getQty();
                    subTotal += price * discount * qty;
                }
                totalPrice.setText("Subtotal: " + (int) subTotal + " NTD");

            }
        });

        itemInfo = (TextView) view.findViewById(R.id.tvInfo);
        itemInfo.setText(ib.getInfo());
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvPrice.setText("Price："+String.valueOf(ib.getPrice())+" NTD");
        priceDiscount = (TextView) view.findViewById(R.id.Price_Discount);
        if (ib.getDiscount() != 1.0) {
            tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            tvPrice.getPaint().setStyle(Paint.Style.FILL);
            tvPrice.getPaint().setColor(Color.RED);
            priceDiscount.setVisibility(View.VISIBLE);
            priceDiscount.setText("Special：" + String.valueOf((int) (ib.getPrice() * ib.getDiscount())) + " NTD");
        }


        showCount = (TextView) view.findViewById(R.id.showCount);
        totalPrice = (TextView) view.findViewById(R.id.TotalPrice);
        if (Common.cart != null || (Common.cart.isEmpty() == false)) {
            showCount.setText(String.valueOf(Common.cart.size()));
            Set<Integer> set = Common.cart.keySet();
            double subTotal = 0;
            for (int n : set) {
                double price = Common.cart.get(n).getUnitPrice();
                double discount = Common.cart.get(n).getDiscount();
                int qty = Common.cart.get(n).getQty();
                subTotal += price * discount * qty;
            }
            totalPrice.setText("Subtotal: " + (int) subTotal + " NTD");
        }

        final RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.cartlay);

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new buyListFragment();
                Common.switchFragment(SingleItemFragment.this, fragment, "message");
                return;
            }
        });


        return view;
    }

}
