package com.example.jack.cglohas._03_Recipes;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jack.cglohas.R;
import com.example.jack.cglohas._03_Recipes.typeFragment.AllRecipesFragmgnet;
import com.example.jack.cglohas._03_Recipes.typeFragment.DessertRecipesFragment;
import com.example.jack.cglohas._03_Recipes.typeFragment.EggRecipesFragment;
import com.example.jack.cglohas._03_Recipes.typeFragment.MeatRecipesFragment;
import com.example.jack.cglohas._03_Recipes.typeFragment.NoodleRecipesFragment;
import com.example.jack.cglohas._03_Recipes.typeFragment.SeaFoodRecipesFragment;
import com.example.jack.cglohas._03_Recipes.typeFragment.SoupRecipesFragment;
import com.example.jack.cglohas._03_Recipes.typeFragment.StarterRecipesFragment;
import com.example.jack.cglohas._03_Recipes.typeFragment.VegRecipesFragment;


import java.util.ArrayList;
import java.util.List;


public class RecipesFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_scrollable_tabs, container, false);
        getActivity().setTitle("Recipes");
//        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new AllRecipesFragmgnet(), "ALL");
        adapter.addFrag(new VegRecipesFragment(), "Veg");
        adapter.addFrag(new SeaFoodRecipesFragment(), "SeaFood");
        adapter.addFrag(new StarterRecipesFragment(), "Starter");
        adapter.addFrag(new SoupRecipesFragment(), "Soup");
        adapter.addFrag(new DessertRecipesFragment(), "Dessert");
        adapter.addFrag(new MeatRecipesFragment(), "Meat");
        adapter.addFrag(new EggRecipesFragment(), "Egg");
        adapter.addFrag(new NoodleRecipesFragment(), "Noodle");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
