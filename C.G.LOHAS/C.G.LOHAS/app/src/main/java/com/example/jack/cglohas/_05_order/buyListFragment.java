package com.example.jack.cglohas._05_order;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._01_Register.New_Register;
import com.example.jack.cglohas._02_Login.LoginDialogActivity;
import com.example.jack.cglohas._04_listItems.ItemGetImageTask;
import com.example.jack.cglohas._04_listItems.OrderItemBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dvd2631 on 2016/10/13.
 */

public class buyListFragment extends Fragment {

    TextView count,totalCount,TotalPrice,buylistdiscount,buylistFinalTotal;
    Button submitBuy;
    List<OrderItemBean> buylist = new ArrayList<>(Common.cart.values());
    int totalQty = 0;
    double currentTotalPrice=0;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_buylist, container, false);
        getActivity().setTitle("Order list");
        RecyclerView rvBuyList = (RecyclerView) view.findViewById(R.id.rvBuyList);
        TextView loginMessage = (TextView) view.findViewById(R.id.loginMessage);
        count = (TextView) view.findViewById(R.id.count);
        totalCount=(TextView) view.findViewById(R.id.totalCount);
        TotalPrice = (TextView) view.findViewById(R.id.TotalPrice);
        buylistdiscount = (TextView) view.findViewById(R.id.buylistdiscount);
        buylistFinalTotal = (TextView) view.findViewById(R.id.buylistFinalTotal);
        submitBuy=(Button) view.findViewById(R.id.submitBuy);
        submitBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(TotalPrice.getText().toString())<=0){
                    Toast.makeText(getActivity(),"You have not purchased any products",Toast.LENGTH_SHORT).show();
                    return;
                }else if(Common.login==false){
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginDialogActivity.class);
                    startActivity(intent);
                    return;
                }else{
                    Fragment fragment = new FinalDetailFragment();
                    Common.switchFragment(buyListFragment.this, fragment, "message");
                    //轉到結帳頁面
                }
            }
        });

        if(Common.login==true){
            loginMessage.setText(Common.rb.getName()+"'s shopping list");
        }
        if (rvBuyList != null) {
            rvBuyList.setLayoutManager(new LinearLayoutManager(getActivity()));
            if (Common.cart == null || Common.cart.isEmpty()) {
                Common.showToast(getActivity(), "You have not purchased any products");
                rvBuyList.setAdapter(new BuyListAdapter(getActivity(), buylist));
            } else {
                rvBuyList.setAdapter(new BuyListAdapter(getActivity(), buylist));
                itemOnchange();
            }


        }

        final RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.totalInfo);
        rvBuyList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        rl.setVisibility(View.VISIBLE);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        rl.setVisibility(View.GONE);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:

                        break;
                }

            }
        });

        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl.setVisibility(View.GONE);
                return;
            }
        });

        return view;
    }

    private void itemOnchange(){
        count.setText(String.valueOf(Common.cart.size()));
        totalQty=0;
        currentTotalPrice=0;
        for(OrderItemBean oib : buylist){
            totalQty += oib.getQty();
            currentTotalPrice += oib.getUnitPrice()*oib.getDiscount()*oib.getQty();
        }
        totalCount.setText(String.valueOf(totalQty));
        TotalPrice.setText(String.valueOf((int)currentTotalPrice));
        buylistdiscount.setText(String.valueOf(((int)(currentTotalPrice/1000))*100));
        buylistFinalTotal.setText(String.valueOf(((int)currentTotalPrice)-(((int)(currentTotalPrice/1000))*100)));
    }

    private class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.MyViewHolder> {
        Context context;
        List<OrderItemBean> buyList;

        public BuyListAdapter(Context context, List<OrderItemBean> buyList) {
            this.context = context;
            this.buyList = buyList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivItem,minus,plus;
            TextView itemName;
            TextView tvPrice,Price_Discount,qty;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
                itemName = (TextView) itemView.findViewById(R.id.itemName);
                tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
                Price_Discount = (TextView) itemView.findViewById(R.id.Price_Discount);
                minus = (ImageView) itemView.findViewById(R.id.minus);
                plus = (ImageView) itemView.findViewById(R.id.plus);
                qty = (TextView) itemView.findViewById(R.id.qty);
            }
        }

        @Override
        public int getItemCount() {
            return buyList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.buy_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final OrderItemBean oib = buyList.get(position);
            //Toast.makeText(getActivity(),oib.getName(),Toast.LENGTH_SHORT).show();
            String url = Common.URL + "ItemServlet";
            int id = oib.getItemid();
            int imageSize = 250;
            new ItemGetImageTask(holder.ivItem).execute(url, id, imageSize);
            holder.itemName.setText(oib.getName());
            holder.tvPrice.setText("Price:" +String.valueOf((int)oib.getUnitPrice())+ " NTD");
            holder.tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            holder.qty.setText(String.valueOf(oib.getQty()));
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(Integer.parseInt(holder.qty.getText().toString())>0) {
                        OrderItemBean ob = Common.cart.get(oib.getItemid());
                        ob.setQty(ob.getQty() - 1);
                        holder.qty.setText(String.valueOf(oib.getQty()));
                        itemOnchange();
                    }else {

                    }
                }
            });

            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderItemBean ob = Common.cart.get(oib.getItemid());
                    if ((Integer.parseInt(holder.qty.getText().toString())) > oib.getStorage()) {
                        Toast.makeText(getActivity(), "This item is out of stock", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    ob.setQty(ob.getQty()+1);
                    holder.qty.setText(String.valueOf(ob.getQty()));
                    itemOnchange();
                }
            });

            if(oib.getDiscount()!=1.0){
                holder.tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                holder.tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvPrice.getPaint().setStyle(Paint.Style.FILL);
                holder.tvPrice.getPaint().setColor(Color.RED);
                holder.Price_Discount.setVisibility(View.VISIBLE);
                holder.Price_Discount.setText("Special："+String.valueOf((int)(oib.getUnitPrice()*oib.getDiscount()))+" NTD");
                holder.Price_Discount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }


//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ShopMarkerActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("shop", shop);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
        }
    }



}
