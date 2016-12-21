package com.example.jack.cglohas._01_Register.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._04_listItems.OrderItemBean;
import com.example.jack.cglohas._05_order.OrderBean;
import com.example.jack.cglohas._05_order.OrderItemDAOBean;

import java.util.List;

/**
 * Created by Steven on 2016/10/17.
 */

public class MemberOrderDetailFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);
        final OrderBean ib = (OrderBean)getArguments().getSerializable("orderBean");
        List<OrderItemDAOBean> oib = ib.getItems();
        getActivity().setTitle("Order Detail");
        TextView total = (TextView) view.findViewById(R.id.total);
        total.setText(String.valueOf((int)(ib.getTotalAmount()+ib.getDiscount())));
        TextView discount  = (TextView) view.findViewById(R.id.discount);
        discount.setText(String.valueOf(ib.getDiscount()));
        TextView totalFinal  = (TextView) view.findViewById(R.id.totalFinal);
        totalFinal.setText(String.valueOf((int)ib.getTotalAmount()));

        RecyclerView rvOrderlist = (RecyclerView) view.findViewById(R.id.rvOrderlist);
        if (rvOrderlist != null) {
            rvOrderlist.setLayoutManager(new LinearLayoutManager(getActivity()));
            //getItemList()自訂方法向後端要資料(itemBean)
            if (oib == null || oib.isEmpty()||ib==null) {
                Common.showToast(getActivity(), String.valueOf(oib.size()));
                Common.showToast(getActivity(), R.string.msg_NoItemsFound);
            } else {
                rvOrderlist.setAdapter(new OrderlistAdapter(getActivity(), oib));
            }
        }
        return view;

    }

    private class OrderlistAdapter extends RecyclerView.Adapter<OrderlistAdapter.MyViewHolder> {
        Context context;
        List<OrderItemDAOBean> orderList;

        public OrderlistAdapter(Context context, List<OrderItemDAOBean> orderList) {
            this.context = context;
            this.orderList = orderList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name,unitPrice,amount,itemTotalprice,discountItem,priceWithoutDiscount;
            View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                name = (TextView) itemView.findViewById(R.id.name);
                unitPrice = (TextView) itemView.findViewById(R.id.unitPrice);
                amount = (TextView) itemView.findViewById(R.id.amount);
                itemTotalprice = (TextView) itemView.findViewById(R.id.itemTotalprice);
                discountItem = (TextView) itemView.findViewById(R.id.discountItem);
                priceWithoutDiscount = (TextView) itemView.findViewById(R.id.priceWithoutDiscount);
            }
        }

        @Override
        public int getItemCount() {
            return orderList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.order_detail_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final OrderItemDAOBean order = orderList.get(position);
            holder.name.setText(order.getName());
            holder.unitPrice.setText(String.valueOf((int)order.getUnitPrice()));
            holder.amount.setText(String.valueOf((int)order.getAmount()));
            holder.itemTotalprice.setText(String.valueOf((int)(order.getAmount()*order.getUnitPrice())));
            holder.discountItem.setText(String.valueOf(order.getDiscount()));
            holder.priceWithoutDiscount.setText(String.valueOf((int)(order.getAmount()*order.getUnitPrice()*order.getDiscount())));

        }
    }

}
