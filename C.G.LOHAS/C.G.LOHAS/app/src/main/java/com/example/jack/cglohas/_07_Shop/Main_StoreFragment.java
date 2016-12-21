package com.example.jack.cglohas._07_Shop;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;

import java.util.List;


public class Main_StoreFragment extends Fragment {

    private static final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("Store");
        View view = inflater.inflate(R.layout.fragment_main_store, container, false);
        RecyclerView rvMainStores = (RecyclerView) view.findViewById(R.id.rvMainStores);
        if (rvMainStores != null) {
            rvMainStores.setLayoutManager(new LinearLayoutManager(getActivity()));
            List<Main_Store_Bean> mainStores = getMainStoreList();
            if (mainStores == null || mainStores.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoMainStoreFound);
            } else {
                rvMainStores.setAdapter(new MainStoreAdapter(getActivity(), mainStores));
            }
        }
        return view;
    }


    private class MainStoreAdapter extends RecyclerView.Adapter<MainStoreAdapter.MyViewHolder> {
        Context context;
        List<Main_Store_Bean> msbList;

        public MainStoreAdapter(Context context, List<Main_Store_Bean> msbList) {
            this.context = context;
            this.msbList = msbList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivMainStore;
            TextView tvStorename;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivMainStore = (ImageView) itemView.findViewById(R.id.ivMainStore);
                tvStorename = (TextView) itemView.findViewById(R.id.tvStorename);
            }
        }


        @Override
        public int getItemCount() {
            return msbList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.mainstore_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Main_Store_Bean msb = msbList.get(position);
            String url = Common.URL + "ShopServlet";
            int id = msb.getStoreid();
            int imageSize = 250;
            new MainStoreGetImageTask(holder.ivMainStore).execute(url, id, imageSize);
            holder.tvStorename.setText(msb.getStorename());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("MainStoreBean", msb);
//                    bundle.putInt("storeid", msb.getStoreid());
                    ShopFragment shopFragment = new ShopFragment();
                    shopFragment.setArguments(bundle);
                    Common.switchFragment(Main_StoreFragment.this, shopFragment, "mainToShop");

                    return;
//                    Intent intent = new Intent(context, ShopFragment.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("Main_Store_Bean", msb);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                }
            });
        }
    }


    private List<Main_Store_Bean> getMainStoreList() {
        List<Main_Store_Bean> Main_Store_Bean = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "ShopServlet";
            try {
                Main_Store_Bean = new MainStoreGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return Main_Store_Bean;
    }




}
