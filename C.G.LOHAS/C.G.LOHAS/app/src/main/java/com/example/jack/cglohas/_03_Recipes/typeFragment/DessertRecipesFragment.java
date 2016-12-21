package com.example.jack.cglohas._03_Recipes.typeFragment;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._03_Recipes.RecipesGetAllTask;
import com.example.jack.cglohas._03_Recipes.RecipesGetImageTask;
import com.example.jack.cglohas._03_Recipes.SinglerRecipeFragment;
import com.example.jack.cglohas._03_Recipes.recipesBean;
import com.example.jack.cglohas._04_listItems.ItemGetAllTask;
import com.example.jack.cglohas._04_listItems.ItemGetImageTask;
import com.example.jack.cglohas._04_listItems.OrderItemBean;
import com.example.jack.cglohas._04_listItems.SingleItemFragment;
import com.example.jack.cglohas._04_listItems.item_bean;
import com.example.jack.cglohas._05_order.buyListFragment;

import java.util.List;
import java.util.Set;


public class DessertRecipesFragment extends Fragment {

    private static final String TAG = "TAG";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        //設定inflater為一recycleView
        getActivity().setTitle("Recipes");
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        RecyclerView rvRecipes = (RecyclerView) view.findViewById(R.id.rvRecipes);
        if (rvRecipes != null) {
            rvRecipes.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            //getItemList()自訂方法向後端要資料(itemBean)
            List<recipesBean> recipes = getRecipesList();
            if (recipes == null || recipes.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoItemsFound);
            } else {
                rvRecipes.setAdapter(new ItemAdapter(getActivity(), recipes));
            }
        }
        return view;
    }


    //設定顯示商品 RecyclerView.Adapter 以及 holder
    private class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.MyViewHolder> {
        Context context;
        List<recipesBean> itemList;

        public ItemAdapter(Context context, List<recipesBean> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivItem;
            TextView recipeName;
            TextView recipeType;
            View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
                recipeName = (TextView) itemView.findViewById(R.id.recipeName);
                recipeType = (TextView) itemView.findViewById(R.id.recipeType);
            }
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.recipes_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final recipesBean item = itemList.get(position);
            String url = Common.URL + "AD_RecipesServlet";
            int id = item.getRecipesID();
            Log.d("ID", String.valueOf(id));
            int imageSize = 250;
            new RecipesGetImageTask(holder.ivItem).execute(url, id, imageSize);
            holder.recipeName.setText(item.getName());
            holder.recipeType.setText(item.getType());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("itemBean",item);
                    SinglerRecipeFragment srf = new SinglerRecipeFragment();
                    srf.setArguments(bundle);
                    Common.switchFragment(DessertRecipesFragment.this,srf,"123");
                    getActivity().setTitle(item.getName());
                    return;
                }
            });
        }
    }

    private List<recipesBean> getRecipesList() {
        List<recipesBean> recipes = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "AD_RecipesServlet";
            //ItemGetAllTask.java 向後端要資料
            try {
                recipes = new RecipesGetAllTask().execute(url,"甜點").get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return recipes;
    }
}