package com.example.jack.cglohas._03_Recipes;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.example.jack.cglohas._04_listItems.ItemGetImageTask;
import com.example.jack.cglohas._04_listItems.OrderItemBean;
import com.example.jack.cglohas._04_listItems.SingleItemFragment;
import com.example.jack.cglohas._04_listItems.item_bean;
import com.example.jack.cglohas._05_order.buyListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dvd2631 on 2016/10/14.
 */

public class SinglerRecipeFragment extends Fragment {
    private static final String TAG = "TAG";
    TextView recipeName;
    ImageView ivItem,add;
    RecyclerView rvRecipeItems,rvRecipeProcess;
    RecipeDetailBean recipes;
    Button addFav;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_single_recipe, container, false);
        final recipesBean rb = (recipesBean)getArguments().getSerializable("itemBean");
        recipeName = (TextView) view.findViewById(R.id.recipeName);
        add = (ImageView) view.findViewById(R.id.add);
        recipeName.setText(rb.getName());
        rvRecipeItems = (RecyclerView) view.findViewById(R.id.rvRecipeItems);
        rvRecipeProcess = (RecyclerView) view.findViewById(R.id.rvRecipeProcess);
        addFav = (Button) view.findViewById(R.id.addFav);
        if(Common.login==false){
            addFav.setVisibility(View.GONE);
        }else{
            addFav.setVisibility(View.VISIBLE);
            addFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String result = addFavToServer(Common.rb.getUserID(),rb.getRecipesID());
                    if(result.equalsIgnoreCase("OK")){
                        Toast.makeText(getActivity(),"Add success!",Toast.LENGTH_SHORT).show();
                        add.setX(150);
                        add.setY(400);
                        TranslateAnimation anim = new TranslateAnimation(150, 150,400, 100);
                        anim.setDuration(800);
                        add.setVisibility(View.VISIBLE);
                        anim.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation a) {}
                            public void onAnimationRepeat(Animation a) {}
                            public void onAnimationEnd(Animation a) { add.setVisibility(View.GONE);}
                        });
                        add.startAnimation(anim);
                    }else{
                        Toast.makeText(getActivity(),"Failed to add",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if (rvRecipeItems != null && rvRecipeProcess!=null) {
            rvRecipeItems.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            rvRecipeProcess.setLayoutManager(new LinearLayoutManager(getActivity()));
            //getItemList()自訂方法向後端要資料(itemBean)
            recipes = getRecipeItems(rb.getRecipesID());
            if (recipes == null) {
                Common.showToast(getActivity(), R.string.msg_NoItemsFound);
            } else {
                rvRecipeItems.setAdapter(new RecipeItemAdapter(getActivity(), recipes.getRecipeItems()));
                rvRecipeProcess.setAdapter(new RecipeProcessAdapter(getActivity(), recipes.getProcess()));
            }
        }


        ivItem = (ImageView) view.findViewById(R.id.ivItem);
        int imageSize = 250;
        String url = Common.URL + "AD_RecipesServlet";
        new RecipesGetImageTask(ivItem).execute(url, rb.getRecipesID(), imageSize);

        return view;
    }

    //設定顯示商品 RecyclerView.Adapter 以及 holder
    private class RecipeItemAdapter extends RecyclerView.Adapter<RecipeItemAdapter.MyViewHolder> {
        Context context;
        List<recipes_itemBean> itemList;

        public RecipeItemAdapter(Context context, List<recipes_itemBean> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView recipeItem;
            View itemView;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView=itemView;
                recipeItem = (TextView) itemView.findViewById(R.id.recipeItem);
            }
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.recipe_item_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final recipes_itemBean item = recipes.getRecipeItems().get(position);
            final item_bean itemName = recipes.getRecipeName().get(position);
            holder.recipeItem.setText(itemName.getName()+"  "+item.getDetail());
            holder.recipeItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle= new Bundle();
                    bundle.putSerializable("itemBean",itemName);
                    SingleItemFragment sif = new SingleItemFragment();
                    sif.setArguments(bundle);
                    Common.switchFragment(SinglerRecipeFragment.this,sif,"123");
                    getActivity().setTitle(itemName.getName());
                    return;
                }
            });
        }
    }


    //設定顯示商品 RecyclerView.Adapter 以及 holder
    private class RecipeProcessAdapter extends RecyclerView.Adapter<RecipeProcessAdapter.MyViewHolder> {
        Context context;
        List<processBean> itemList;

        public RecipeProcessAdapter(Context context, List<processBean> itemList) {
            this.context = context;
            this.itemList = itemList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView processNo,process;
            View itemView;
            ImageView ivProcess;

            public MyViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                processNo = (TextView) itemView.findViewById(R.id.processNo);
                process = (TextView) itemView.findViewById(R.id.process);
                ivProcess = (ImageView) itemView.findViewById(R.id.ivProcess);
            }
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.process_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final processBean process = recipes.getProcess().get(position);
            int imageSize = 200;
            String url = Common.URL + "AD_ProcessImgServlet";
            new ProcessGetImageTask(holder.ivProcess).execute(url, process.getProcessID(),process.getRecipeID(), imageSize);
            holder.processNo.setText("Step "+process.getProcessID()+":");
            //holder.itemView.setLayoutParams(new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT));
            holder.process.setText(process.getProcess());
        }
    }



    private RecipeDetailBean getRecipeItems(int id) {

        RecipeDetailBean recipe_detail = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "AD_RecipesDetailServlet";
            //ItemGetAllTask.java 向後端要資料
            try {
                recipe_detail = new RecipesGetDetailTask().execute(url,id).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return recipe_detail;
    }

    private String addFavToServer(int userID,int recipeID) {

        String result = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "AD_addFav";
            //ItemGetAllTask.java 向後端要資料
            try {
                result = new addFavTask().execute(url,userID,recipeID).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }

        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return result;
    }

}
