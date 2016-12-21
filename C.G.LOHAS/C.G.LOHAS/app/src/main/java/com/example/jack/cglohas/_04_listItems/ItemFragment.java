package com.example.jack.cglohas._04_listItems;

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
import com.example.jack.cglohas._04_listItems.typeFragment.FruitFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.MeatFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.EggFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.JuiceFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.AllProductFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.SeaFoodFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.DessertFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.DryFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.SauceFragment;
import com.example.jack.cglohas._04_listItems.typeFragment.VegFragment;

import java.util.ArrayList;
import java.util.List;


public class ItemFragment extends Fragment {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.activity_scrollable_tabs, container, false);
        getActivity().setTitle("Ingredients");
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
        adapter.addFrag(new AllProductFragment(), "All");
        adapter.addFrag(new VegFragment(), "Veg");
        adapter.addFrag(new SauceFragment(), "Sauce");
        adapter.addFrag(new EggFragment(), "Egg");
        adapter.addFrag(new MeatFragment(), "Meat");
        adapter.addFrag(new DessertFragment(), "Dessert");
        adapter.addFrag(new SeaFoodFragment(), "SeaFood");
        adapter.addFrag(new FruitFragment(), "Fruit");
        adapter.addFrag(new JuiceFragment(), "Juice");
        adapter.addFrag(new DryFragment(), "Dry goods");
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
