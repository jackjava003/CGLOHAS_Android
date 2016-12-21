package com.example.jack.cglohas._07_Shop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jack.cglohas.Common;
import com.example.jack.cglohas.R;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class ShopDetailActivity extends AppCompatActivity {
    private static final String TAG = "ShopDetailActivity";
    private Shop shop;
    private ImageView ivShop;
    private TextView tvS_Name, tvName, tvAddress;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        shop = (Shop) getIntent().getExtras().getSerializable("shop");
        findViews();
        connectLocationService();
    }

    private void connectLocationService() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            Log.i(TAG, "GoogleApiClient connected");
                            lastLocation = LocationServices.FusedLocationApi
                                    .getLastLocation(googleApiClient);

                            LocationRequest locationRequest = LocationRequest.create()
                                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                    .setInterval(10000)
                                    .setSmallestDisplacement(1000);


                            LocationListener locationListener = new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    lastLocation = location;
                                }
                            };
                            LocationServices.FusedLocationApi.requestLocationUpdates(
                                    googleApiClient, locationRequest, locationListener);
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    })
                    .build();
        }
        googleApiClient.connect();
    }


    private void findViews() {
        ivShop = (ImageView) findViewById(R.id.ivShop);
//        tvS_Name = (TextView) findViewById(R.id.tvS_Name);;
        tvName = (TextView) findViewById(R.id.tvName);
        tvAddress = (TextView) findViewById(R.id.tvAddress);

        if (shop != null) {
            String url = Common.URL + "ShopServlet";
            int id = shop.getLocationid();
            int imageSize = 600;
            Bitmap bitmap = null;
            try {
                // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
                bitmap = new ShopGetImageTask(null).execute(url, id, imageSize,shop.getStoreid()).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (bitmap != null) {
                ivShop.setImageBitmap(bitmap);
            } else {
                ivShop.setImageResource(R.drawable.default_image);
            }
            tvName.setText(shop.getS_name());
            tvAddress.setText(shop.getAddress());
        }
    }

    public void onDirectClick(View view) {
        Address address = getAddress(shop.getAddress());
        if (address == null) {
            Toast.makeText(this, R.string.msg_LocationNotAvailable, Toast.LENGTH_SHORT).show();
            return;
        }

        double fromLat = lastLocation.getLatitude();
        double fromLng = lastLocation.getLongitude();
        double toLat = address.getLatitude();
        double toLng = address.getLongitude();

        direct(fromLat, fromLng, toLat, toLng);
    }

    private Address getAddress(String locationName) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;

        try {
            addressList = geocoder.getFromLocationName(locationName, 1);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        if (addressList == null || addressList.isEmpty()) {
            return null;
        } else {
            return addressList.get(0);
        }
    }

    private void direct(double fromLat, double fromLng, double toLat,
                        double toLng) {
        String uriStr = String.format(Locale.US,
                "http://maps.google.com/maps?saddr=%f,%f&daddr=%f,%f", fromLat,
                fromLng, toLat, toLng);
        Intent intent = new Intent();
        intent.setClassName("com.google.android.apps.maps",
                "com.google.android.maps.MapsActivity");
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uriStr));
        startActivity(intent);
    }
}
