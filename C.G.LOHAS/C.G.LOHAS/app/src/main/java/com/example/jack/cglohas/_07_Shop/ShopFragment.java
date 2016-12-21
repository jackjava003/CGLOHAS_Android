package com.example.jack.cglohas._07_Shop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class ShopFragment extends Fragment  implements OnMapReadyCallback {

    private static final String TAG = "ShopFragment";
    private GoogleMap map;
    private int storeid = 1;
    TextView tvShops;
    Button shopDesc;
    RecyclerView rvShops;
    List<Shop> shop;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //fragment傳fragment , 從bundle拿東西
        //        Bundle bundle = getArguments();
        //      final Main_Store_Bean msb = bundle.getSerializable("MainStoreBean");
        final Main_Store_Bean msb = (Main_Store_Bean) getArguments().getSerializable("MainStoreBean");
        storeid = msb.getStoreid();
        Log.i(ShopFragment.TAG, String.valueOf(storeid));
        getActivity().setTitle(msb.getStorename());


        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        tvShops=(TextView)view.findViewById(R.id.tvShops);
        tvShops.setText(msb.getStorename());
        shopDesc=(Button)view.findViewById(R.id.shopDesc);
        shopDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),msb.getShortdesc(),Toast.LENGTH_LONG).show();
            }
        });
        rvShops= (RecyclerView) view.findViewById(R.id.rvShops);
        if (rvShops != null) {
            rvShops.setLayoutManager(new LinearLayoutManager(getActivity()));
            shop = getShopList();
            if (shop == null || shop.isEmpty()) {
                Common.showToast(getActivity(), R.string.msg_NoShopsFound);
            } else {
                rvShops.setAdapter(new ShopAdapter(getActivity(), shop));
            }
        }
        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.fmMap);
        mapFragment.getMapAsync(this);
        return view;
    }


    private class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
        Context context;
        List<Shop> shopList;

        public ShopAdapter(Context context, List<Shop> shopList) {
            this.context = context;
            this.shopList = shopList;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivShop;
            TextView tvName;
            TextView tvAddress;

            public MyViewHolder(View itemView) {
                super(itemView);
                ivShop = (ImageView) itemView.findViewById(R.id.ivShop);
                tvName = (TextView) itemView.findViewById(R.id.tvName);
                tvAddress = (TextView) itemView.findViewById(R.id.tvAddress);
            }
        }

        @Override
        public int getItemCount() {
            return shopList.size();
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            View itemView = layoutInflater.inflate(R.layout.shop_list, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final Shop shop = shopList.get(position);
            String url = Common.URL + "ShopServlet";
            int id = shop.getLocationid();
            int imageSize = 250;
            new ShopGetImageTask(holder.ivShop).execute(url, id, imageSize, storeid);
//            new ShopGetImageTask(holder.ivShop).execute(url, id, imageSize);
            holder.tvName.setText(shop.getS_name());
            holder.tvAddress.setText(shop.getAddress());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShopDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", shop);
                    bundle.putInt("storeid", storeid);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    private List<Shop> getShopList() {
        List<Shop> shops = null;
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "ShopServlet";
            try {
                shops = new ShopGetAllTask().execute(url, storeid).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(getActivity(), R.string.msg_NoNetwork);
        }
        return shops;
    }




    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        setUpMap();
        for(Shop sb:shop) {
            final Shop shopbean = sb;
            locationNameToMarker(shopbean.getAddress());
            map.setInfoWindowAdapter(new MyInfoWindowAdapter(shopbean));
            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    Intent intent = new Intent(getActivity(), ShopDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("shop", shopbean);
                    bundle.putInt("storeid", storeid);
                    intent.putExtras(bundle);
                    startActivity(intent);

//                    Intent intent = new Intent(ShopMarkerActivity.this, ShopDetailActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("shop", shopbean);
//                    intent.putExtras(bundle);
//                    startActivity(intent);
                }
            });
        }
    }

    private void setUpMap() {
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private void locationNameToMarker(String locationName) {
        //map.clear();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = null;
        int maxResults = 1;
        try {
            addressList = geocoder
                    .getFromLocationName(locationName, maxResults);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }

        if (addressList == null || addressList.isEmpty()) {
            Toast.makeText(getActivity(),R.string.msg_LocationNameNotFound,Toast.LENGTH_SHORT);
        } else {
            Address address = addressList.get(0);

            LatLng position = new LatLng(address.getLatitude(),
                    address.getLongitude());

            String snippet = address.getAddressLine(0);

            map.addMarker(new MarkerOptions().position(position)
                    .title(locationName).snippet(snippet));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(position).zoom(15).build();
            map.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }
    }

    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private final View infoWindow;
        Shop shop;
        MyInfoWindowAdapter(Shop shop) {
            this.shop=shop;
            infoWindow = View.inflate(getActivity(), R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {

            ImageView ivLogo = ((ImageView) infoWindow
                    .findViewById(R.id.ivLogo));
            String url = Common.URL + "ShopServlet";
            int id = shop.getLocationid();
            int imageSize = 200;
            Bitmap bitmap = null;
            try {
                // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
                bitmap = new ShopGetImageTask(null).execute(url, id, imageSize, storeid).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (bitmap != null) {
                ivLogo.setImageBitmap(bitmap);
            } else {
                ivLogo.setImageResource(R.drawable.default_image);
            }

            String title = marker.getTitle();
            TextView tvTitle = ((TextView) infoWindow
                    .findViewById(R.id.tvTitle));
            tvTitle.setText(title);

            String snippet = marker.getSnippet();
            TextView tvSnippet = ((TextView) infoWindow
                    .findViewById(R.id.tvSnippet));
            tvSnippet.setText(snippet);
            return infoWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }
    }


}
