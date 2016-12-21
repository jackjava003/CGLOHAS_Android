package com.example.jack.cglohas._01_Register.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.GetUserImageTask;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._01_Register.MemberOrderTask;
import com.example.jack.cglohas._05_order.OrderBean;

import java.util.List;

import static com.example.jack.cglohas.Common.rb;

public class MemberOrderFragment extends Fragment {
    String TAG = "MemberOrderFragment";
    View view;
    RecyclerView rvMemOrder;
    TextView memberOrder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("Member Center");
        view = inflater.inflate(R.layout.fragment_member_order, container, false);
        rvMemOrder = (RecyclerView) view.findViewById(R.id.rvMemOrder);
        if (rvMemOrder != null) {
            rvMemOrder.setLayoutManager(new LinearLayoutManager(getActivity()));
            //getItemList()自訂方法向後端要資料(itemBean)
            List<OrderBean> orderList = getOrderList(Common.rb.getUserID());
            if (orderList == null || orderList.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoItemsFound);
            } else {
                rvMemOrder.setAdapter(new OrderAdapter(getActivity(), orderList));
            }
        }
        return view;
    }

    private class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
        Context context;
        List<OrderBean> orderList;

        public OrderAdapter(Context context, List<OrderBean> orderList) {
            this.context = context;
            this.orderList = orderList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView orderID,orderDate,TotalPrice,Add;
            View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                orderID = (TextView) itemView.findViewById(R.id.orderID);
                orderDate = (TextView) itemView.findViewById(R.id.orderDate);
                TotalPrice = (TextView) itemView.findViewById(R.id.TotalPrice);
                Add = (TextView) itemView.findViewById(R.id.Add);
            }
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.order_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final OrderBean order = orderList.get(position);
            holder.orderID.setText(String.valueOf(order.getOrderID()));
            holder.orderDate.setText(order.getOrderDate().toString().substring(0,(order.getOrderDate().toString().indexOf(":")-2)));
            holder.TotalPrice.setText(String.valueOf((int)order.getTotalAmount()));
            holder.Add.setText(order.getShippingAddress());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("orderBean",order);
                    MemberOrderDetailFragment srf = new MemberOrderDetailFragment();
                    srf.setArguments(bundle);
                    Common.switchFragment(MemberOrderFragment.this,srf,"123");
                    return;
                }
            });
        }
    }

    private List<OrderBean> getOrderList(int userID) {
        List<OrderBean> orderList = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "AD_MemberOrder";
            //ItemGetAllTask.java 向後端要資料
            try {
                orderList = new MemberOrderTask().execute(url,userID).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return orderList;
    }

}
