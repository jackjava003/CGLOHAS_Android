package com.example.jack.cglohas._04_listItems.typeFragment;

import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._04_listItems.ItemGetAllTask;
import com.example.jack.cglohas._04_listItems.ItemGetImageTask;
import com.example.jack.cglohas._04_listItems.OrderItemBean;
import com.example.jack.cglohas._04_listItems.SingleItemFragment;
import com.example.jack.cglohas._04_listItems.item_bean;
import com.example.jack.cglohas._05_order.buyListFragment;

import java.util.List;
import java.util.Set;


public class SauceFragment extends Fragment {
    private static final String TAG = "TAG";
    TextView showCount, totalPrice;
    ImageView add;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //設定inflater為一recycleView
        getActivity().setTitle("Ingredients");
        View view = inflater.inflate(R.layout.fragment_item, container, false);
        RecyclerView rvItems = (RecyclerView) view.findViewById(R.id.rvItems);
        add = (ImageView) view.findViewById(R.id.add);
        if (rvItems != null) {
            rvItems.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            //getItemList()自訂方法向後端要資料(itemBean)
            List<item_bean> item = getItemList();
            if (item == null || item.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoItemsFound);
            } else {
                rvItems.setAdapter(new ItemAdapter(getActivity(), item));
            }


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
        rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                Fragment fragment = new buyListFragment();
                Common.switchFragment(SauceFragment.this, fragment, "message");
                return;
            }
        });

        return view;
    }


    //設定顯示商品 RecyclerView.Adapter 以及 holder
    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
        Context context;
        List<item_bean> itemList;

        public ItemAdapter(Context context, List<item_bean> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivItem, minus, plus, addToCart;
            TextView tvName;
            TextView tvInfo;
            TextView tvPrice, itemID, priceDiscount, qty;
            View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvInfo = (TextView) itemView.findViewById(R.id.tvInfo);
                tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
                itemID = (TextView) itemView.findViewById(R.id.itemID);
                priceDiscount = (TextView) itemView.findViewById(R.id.Price_Discount);
                minus = (ImageView) itemView.findViewById(R.id.minus);
                plus = (ImageView) itemView.findViewById(R.id.plus);
                qty = (TextView) itemView.findViewById(R.id.qty);
                addToCart = (ImageView) itemView.findViewById(R.id.addToCart);



            }
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.item_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final item_bean item = itemList.get(position);
            String url = Common.URL + "ItemServlet";
            int id = item.getItemID();
            Log.d("ID", String.valueOf(id));
            int imageSize = 250;
            new ItemGetImageTask(holder.ivItem).execute(url, id, imageSize);
            holder.itemID.setText(String.valueOf(item.getItemID()));
            holder.tvName.setText(item.getName());
            holder.tvInfo.setText(item.getInfo());
            holder.tvPrice.setText("Price:" + String.valueOf(item.getPrice()) + " NTD");
            holder.tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.qty.setText(String.valueOf(Integer.parseInt(holder.qty.getText().toString()) + 1));
                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if ((Integer.parseInt(holder.qty.getText().toString())) > 0) {
                        holder.qty.setText(String.valueOf(Integer.parseInt(holder.qty.getText().toString()) - 1));
                    }
                }
            });

            holder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int itemQty = Integer.parseInt(holder.qty.getText().toString());
                    OrderItemBean oi = new OrderItemBean(item.getName(), item.getItemID(), itemQty, item.getPrice(), item.getDiscount());
                    oi.setStorage(item.getStorage());
                    if (itemQty <= 0) {
                        return;
                    }
                    ;
                    if (Common.cart.get(item.getItemID()) == null) {
                        if (itemQty > item.getStorage()) {
                            Toast.makeText(getActivity(), "This item is out of stock", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Common.cart.put(item.getItemID(), oi);
                    } else {
                        OrderItemBean oib = Common.cart.get(item.getItemID());
                        if ((itemQty + oib.getQty()) > item.getStorage()) {
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

            if (item.getDiscount() != 1.0) {
                holder.tvPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                holder.tvPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                holder.tvPrice.getPaint().setStyle(Paint.Style.FILL);
                holder.tvPrice.getPaint().setColor(Color.RED);
                holder.priceDiscount.setVisibility(View.VISIBLE);
                holder.priceDiscount.setText("Special：" + String.valueOf((int) (item.getPrice() * item.getDiscount())) + " NTD");
                holder.priceDiscount.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle= new Bundle();
                    bundle.putSerializable("itemBean",item);
                    SingleItemFragment sif = new SingleItemFragment();
                    sif.setArguments(bundle);
                    Common.switchFragment(SauceFragment.this,sif,"123");
                    getActivity().setTitle(item.getName());
                    return;
                }
            });
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, ItemMarkerActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("item", item);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                }
//            });
        }
    }

    private List<item_bean> getItemList() {
        List<item_bean> items = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "ItemServlet";
            //ItemGetAllTask.java 向後端要資料
            try {
                items = new ItemGetAllTask().execute(url,"調味料").get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return items;
    }


}
