package com.example.jack.cglohas._04_listItems;

//import idv.ron.myproject.Shop.ShopGetImageTask;
public class ItemMarkerActivity{}
//public class ItemMarkerActivity extends AppCompatActivity
//        implements OnMapReadyCallback {
//    private final static String TAG = "ItemMarkerActivity";
//    private GoogleMap map;
//    private Shop shop;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.shop_marker_activity);
//        shop = (Shop) getIntent().getExtras().getSerializable("shop");
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getSupportFragmentManager()
//                        .findFragmentById(R.id.fmMap);
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//        this.map = map;
//        setUpMap();
//        locationNameToMarker(shop.getAddress());
//        map.setInfoWindowAdapter(new MyInfoWindowAdapter());
//        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                Intent intent = new Intent(ItemMarkerActivity.this, ShopDetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("shop", shop);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });
//    }
//
//    private void setUpMap() {
//        map.setMyLocationEnabled(true);
//        map.getUiSettings().setZoomControlsEnabled(true);
//    }
//
//    private void locationNameToMarker(String locationName) {
//        map.clear();
//        Geocoder geocoder = new Geocoder(ItemMarkerActivity.this);
//        List<Address> addressList = null;
//        int maxResults = 1;
//        try {
//            addressList = geocoder
//                    .getFromLocationName(locationName, maxResults);
//        } catch (IOException e) {
//            Log.e(TAG, e.toString());
//        }
//
//        if (addressList == null || addressList.isEmpty()) {
//            showToast(R.string.msg_LocationNameNotFound);
//        } else {
//            Address address = addressList.get(0);
//
//            LatLng position = new LatLng(address.getLatitude(),
//                    address.getLongitude());
//
//            String snippet = address.getAddressLine(0);
//
//            map.addMarker(new MarkerOptions().position(position)
//                    .title(locationName).snippet(snippet));
//
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(position).zoom(15).build();
//            map.animateCamera(CameraUpdateFactory
//                    .newCameraPosition(cameraPosition));
//        }
//    }
//
//    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
//        private final View infoWindow;
//
//        MyInfoWindowAdapter() {
//            infoWindow = View.inflate(ItemMarkerActivity.this, R.layout.custom_info_window, null);
//        }
//
//        @Override
//        public View getInfoWindow(Marker marker) {
//
//            ImageView ivLogo = ((ImageView) infoWindow
//                    .findViewById(R.id.ivLogo));
//            String url = Common.URL + "ShopServlet";
//            int id = shop.getLocationid();
//            int imageSize = 400;
//            Bitmap bitmap = null;
//            try {
//                // passing null and calling get() means not to run FindImageByIdTask.onPostExecute()
//                bitmap = new ShopGetImageTask(null).execute(url, id, imageSize).get();
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (bitmap != null) {
//                ivLogo.setImageBitmap(bitmap);
//            } else {
//                ivLogo.setImageResource(R.drawable.default_image);
//            }
//
//            String title = marker.getTitle();
//            TextView tvTitle = ((TextView) infoWindow
//                    .findViewById(R.id.tvTitle));
//            tvTitle.setText(title);
//
//            String snippet = marker.getSnippet();
//            TextView tvSnippet = ((TextView) infoWindow
//                    .findViewById(R.id.tvSnippet));
//            tvSnippet.setText(snippet);
//            return infoWindow;
//        }
//
//        @Override
//        public View getInfoContents(Marker marker) {
//            return null;
//        }
//    }
//
//
//
//    private void showToast(int messageResId) {
//        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        askPermissions();
//    }
//
//    private static final int REQ_PERMISSIONS = 0;
//
//    // New Permission see Appendix A
//    private void askPermissions() {
//        String[] permissions = {
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//        };
//
//        Set<String> permissionsRequest = new HashSet<>();
//        for (String permission : permissions) {
//            int result = ContextCompat.checkSelfPermission(this, permission);
//            if (result != PackageManager.PERMISSION_GRANTED) {
//                permissionsRequest.add(permission);
//            }
//        }
//
//        if (!permissionsRequest.isEmpty()) {
//            ActivityCompat.requestPermissions(this,
//                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
//                    REQ_PERMISSIONS);
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case REQ_PERMISSIONS:
//                String text = "";
//                for (int i = 0; i < grantResults.length; i++) {
//                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                        text += permissions[i] + "\n";
//                    }
//                }
//                if (!text.isEmpty()) {
//                    text += getString(R.string.text_NotGranted);
//                    Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//                }
//                break;
//        }
//    }

//}
