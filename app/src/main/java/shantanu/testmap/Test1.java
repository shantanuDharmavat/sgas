package shantanu.testmap;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.GeomagneticField;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import Util.DataParser;
import Util.EncodeHelper;
import Util.GlobalVariable;
import Util.InternetServiceHelper;
import Util.MapPopUp_ListAdaptor;
import Util.NetworkCallHandler;
import Util.ScaleBar;
import Util.TileProviderFactory;
import Util.getUrl;
import model.IncidentModel;
import model.NotificationModel;

import static shantanu.testmap.R.id.map;

/**
 * Created by Abhijit on 13-12-2016.
 */

public class Test1 extends ActualMapActivity implements OnMapReadyCallback, android.location.LocationListener {

    public static String[] vvv_lay0_key_el = {"cgs_boundary", "pipeline", "mrs"};
    public static String[] vvv_lay0_value = {"cgs_boundary", "pipeline", "mrs"};
    public static boolean[] vvv_lay0_bol = {true, false, false};

    private Boolean GPS_CURRENT_LOCATION = false;

    GlobalVariable globalVariable = GlobalVariable.getInstance();
    // constant ==============================
    public static String[] selected_key;//= vvv_lay2_key.clone();
    public static String[] selected_value;//=vvv_lay2_value.clone();
    public static boolean[] set_visibility;// =vvv_lay2_bol.clone();

    String str_popup_arr[];
    ArrayList<String> final_pop_up_key = new ArrayList<>();
    ArrayList<String> final_pop_up_value = new ArrayList<>();
    String str_popup_arr_key[];
    String str_popup_arr_value[];
    //layer list end ==============================


    private ProgressDialog mProgressDialog;
    TileOverlay[] tileOverlay;
    TileProvider[] wmsTileProvider;
    private GoogleMap mMap;

    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;

    CameraPosition cameraPosition;
    double db_left = 0.0;
    double db_top = 0.0;
    double db_right = 0.0;
    double db_bottom = 0.0;
    Point clicked_xy;
    ImageButton layer_stack, layer_info, toggle_gps;
    TextView distanceTextView;
    int selected_layer_info = 0;
    static int selected_mapLayer = 0;
    int map_width = 0;
    int map_height = 0;
    String str_pop_up;
    private int PERMISSION_CODE_1 = 23;
    private LocationManager lm;
    private String TAG = "TEST 1";

    Boolean fromPushNotification = false;
    Boolean fromIncidentList = false;
    LatLng newLatLong;
    MarkerOptions marker;
    static int call = 0;
    boolean mapReadyCalled = true;
    Map<String, String> attributeMap = new TreeMap<>();

    GeomagneticField geomagneticField;
    Polyline polyLine;
    boolean directionenabled = false;
    Marker selectedMarker;
    ScaleBar mScaleBar;
    String markerFlag = "1";
    Marker mPositionMarker;


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("broadcast recieved", "" + intent.getExtras());
            if (intent.getExtras().getBoolean("incident")) {
                int size = globalVariable.notificationModelList.size();
                Double lat = Double.valueOf(globalVariable.notificationModelList.get(size - 1).getLatitude());
                Double lon = Double.valueOf(globalVariable.notificationModelList.get(size - 1).getLongitude());
                String title = globalVariable.notificationModelList.get(size - 1).getPro_uid();
                markerMaker(lat, lon, title);
                Log.e("test1", "inside test1");
            }

        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "OnCreate");

        if (getIntent().hasExtra("incident"))
            fromPushNotification = getIntent().getExtras().getBoolean("incident");
        else if (getIntent().hasExtra("incidentList"))
            fromIncidentList = getIntent().getExtras().getBoolean("incidentList");

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest();
            return;
        }


        distanceTextView = (TextView) findViewById(R.id.textViewDistance);
        initiateGooglePlayService();
        setLayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest();
            return;
        }
        /*Criteria crit = new Criteria();
        crit.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = lm.getBestProvider(crit, true);*/

//        lm.getLastKnownLocation(provider);
//        lm.requestLocationUpdates(2,50,crit,this,);
//        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2, 50, this);
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 50, this);
//        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,this);
//        lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 2, 50, this);

        LocalBroadcastManager.getInstance(this).registerReceiver(receiver,
                new IntentFilter("notification"));
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest();
            return;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void startDemo() {
        Log.e(TAG, "startDemo");
        layer_stack = (ImageButton) findViewById(R.id.layer_stack);
        layer_info = (ImageButton) findViewById(R.id.layer_info);
        toggle_gps = (ImageButton) findViewById(R.id.toggle_gps);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionRequest();
            return;
        }
        lm.removeUpdates(this);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation == null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }
        setUpMap();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        permissionRequest();

        try {
            if (location.hasBearing()) {
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            /*cameraPosition = new CameraPosition.Builder()
                    .target(latLng)
                    .zoom(mMap.getCameraPosition().zoom)
                    .bearing(location.getBearing())
//                    .tilt(0)
                    .build();

            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
            }

            if (directionenabled) {
                String lat1, lon1, lat2, lon2;
                lat1 = String.valueOf(location.getLatitude());
                lon1 = String.valueOf(location.getLongitude());
                if (selectedMarker != null) {
                    lat2 = String.valueOf(selectedMarker.getPosition().latitude);
                    lon2 = String.valueOf(selectedMarker.getPosition().longitude);
                    new GetDistance().execute(lat1, lon1, lat2, lon2);
                }
            }

            if (location == null)
                return;

            if (mPositionMarker == null) {

                mPositionMarker = mMap.addMarker(new MarkerOptions()
                        .flat(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin))
                        .anchor(0.5f, 0.5f)
                        .position(new LatLng(location.getLatitude(), location
                                        .getLongitude())));
            }

            animateMarker(mPositionMarker, location); // Helper method for smooth
            // animation

//            mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location
//                    .getLatitude(), location.getLongitude())));

            Log.e("LocationChanged", "LAT: " + location.getLongitude() + " LONG: " + location.getLongitude());
            Log.e("Location bearing", "" + location.getBearing());
            Log.e("Location bundle keyset", "" + location.getExtras().toString());
//        Toast.makeText(Test1.this, "LOCATION CHANGED" + location.toString(), Toast.LENGTH_LONG).show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private void setLayer() {
        switch (Util.GlobalVariable.MAP_ID) {

            case 0:
                selected_key = vvv_lay0_key_el.clone();
                selected_value = vvv_lay0_value.clone();
                set_visibility = vvv_lay0_bol.clone();
                break;
        }
        tileOverlay = new TileOverlay[selected_value.length];
        wmsTileProvider = new TileProvider[selected_value.length];
    }

    @Override
    public void onMapReady(GoogleMap map) {
        super.onMapReady(map);

        /**
         * Work around for onMapReady being called multiple times onCreate
         * */
        if (mapReadyCalled) {
            mapReadyCalled = false;
            Log.e(TAG, "onMapReady");
            mMap = map;
            setUpMap();

            LatLng latLng = new LatLng(GlobalVariable.DEFAULTLAT, GlobalVariable.DEFAULTLONG);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));

            permissionRequest();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissionRequest();
            }
            mMap.setMyLocationEnabled(false);
//            mMap.setMyLocationEnabled(true);

            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
//            mMap.setPadding(10, 10, 10, 10);

            new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            LatLngBounds region = mMap.getProjection().getVisibleRegion().latLngBounds;

            if (fromPushNotification) {
                for (int i = 0; i < globalVariable.notificationModelList.size(); i++) {
                    Log.e("notification call", "" + (call++));

                    Double lat = Double.valueOf(globalVariable.notificationModelList.get(i).getLatitude());
                    Double lon = Double.valueOf(globalVariable.notificationModelList.get(i).getLongitude());

                    String titleString = globalVariable.notificationModelList.get(i).getPro_uid();

//                String[] key = globalVariable.notificationModelList.get(i).final_pop_up_key.toArray(new String[globalVariable.notificationModelList.get(i).final_pop_up_key.size()]);
//                String[] value = globalVariable.notificationModelList.get(i).final_pop_up_value.toArray(new String[globalVariable.notificationModelList.get(i).final_pop_up_value.size()]);

                    markerMaker(lat, lon, titleString);

                }
            } else if (fromIncidentList) {
                Log.e("incidentlist call", "" + (++call));
                Double lat = Double.valueOf(globalVariable.selectedIncidentModel.getLatitude());
                Double lng = Double.valueOf(globalVariable.selectedIncidentModel.getLongitude());
                markerFlag = globalVariable.selectedIncidentModel.getFlag();

                String titleString = globalVariable.selectedIncidentModel.getIncident_id();

//            String[] key = GlobalVariable.selectedIncidentModel.getFinal_pop_up_key().toArray(new String[GlobalVariable.selectedIncidentModel.getFinal_pop_up_key().size()]);
//            String[] value = GlobalVariable.selectedIncidentModel.getFinal_pop_up_value().toArray(new String[GlobalVariable.selectedIncidentModel.getFinal_pop_up_value().size()]);

                markerMaker(lat, lng, titleString);
            }

            db_top = region.northeast.latitude;
            db_left = region.southwest.longitude;

            db_right = region.northeast.longitude;
            db_bottom = region.southwest.latitude;


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Log.e("inside onSaveInstance","true");
        if (fromIncidentList) {
            outState.putBoolean("fromIncidentList", true);
            outState.putStringArrayList("key", globalVariable.selectedIncidentModel.getFinal_pop_up_key());
            outState.putStringArrayList("value", globalVariable.selectedIncidentModel.getFinal_pop_up_value());
        }else if (fromPushNotification){
            int size = globalVariable.notificationModelList.size();
            outState.putInt("size",size);
            outState.putBoolean("fromIncidentList",false);
            for (int i = 0; i < size; i++){
                outState.putStringArrayList("key"+i,globalVariable.notificationModelList.get(i).getFinal_pop_up_key());
                outState.putStringArrayList("value"+i,globalVariable.notificationModelList.get(i).getFinal_pop_up_value());
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("onRestoreInstance",""+savedInstanceState.getBoolean("fromIncidentList"));
        if (savedInstanceState.getBoolean("fromIncidentList")) {

            IncidentModel model = new IncidentModel();

            model.setFinal_pop_up_key(savedInstanceState.getStringArrayList("key"));
            model.setFinal_pop_up_value(savedInstanceState.getStringArrayList("value"));

            for (int i = 0; i < model.getFinal_pop_up_key().size(); i++){
                Log.e("key index " + i + " ",""+model.getFinal_pop_up_key().get(i));
            }

            Log.e("flag index",""+model.getFinal_pop_up_key().indexOf("FLAG"));
            model.setFlag(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("FLAG")));
            model.setLongitude(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("LON")));
            model.setLatitude(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("LAT")));
            model.setAddress_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ADDRESS OF INCIDENT")));
            model.setAttribute_1(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ATTRIBUTE 1")));
            model.setAttribute_2(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ATTRIBUTE 2")));
            model.setAttribute_3(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("ATTRIBUTE 3")));
            model.setCause_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("CAUSE OF INCIDENT")));
            model.setCity_id(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("CITY ID")));
            model.setDate_time(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("DATE TIME")));
            model.setDescription_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("DESCRIPTION OF INCIDENT")));
            model.setGid(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("GID")));
            model.setObj_part_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("OBJ PART OF INCIDENT")));
            model.setSap_notification(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("SAP NOTIFICATION")));
            model.setIncident_id(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("INC ID")));
            model.setReport_by(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("REPORT BY")));
            globalVariable.selectedIncidentModel = model;
        }

        else if (!savedInstanceState.getBoolean("fromIncidentList")){
            int size = savedInstanceState.getInt("size");
            for (int i = 0; i < size; i++){
                NotificationModel model = new NotificationModel();

                model.setFinal_pop_up_key(savedInstanceState.getStringArrayList("key"+i));
                model.setFinal_pop_up_value(savedInstanceState.getStringArrayList("value"+i));

//                model.setFlag(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("flag")));
                model.setLongitude(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("longitude")));
                model.setLatitude(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("latitude")));
                model.setAddress_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("Address_of_incident")));
                model.setAttribute_1(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("attribute_1")));
                model.setAttribute_2(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("attribute_2")));
                model.setAttribute_3(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("attribute_3")));
                model.setCause_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("cause_of_incident")));
                model.setCity_id(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("city_id")));
                model.setDate_time(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("date_time")));
                model.setDescription_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("description_of_incident")));
//                model.setGid(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("gid")));
                model.setObj_part_of_incident(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("obj_part_of_incident")));
                model.setSap_notification(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("sap_notification")));
//                model.setIncident_id(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("inc_id")));
                model.setReport_by(model.getFinal_pop_up_value().get(model.getFinal_pop_up_key().indexOf("report_by")));

                globalVariable.notificationModelList.add(model);
            }
        }
    }

    public void layerSwitcher(View v) {
        switch (v.getId()) {
            case R.id.layer_stack:
                AlertDialog dialog1 = new AlertDialog.Builder(this)
                        .setTitle("Select a category to display")
                        .setMultiChoiceItems(selected_key, set_visibility, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int indexSelected, boolean isChecked) {
                                if (isChecked) {
                                    tileOverlay[indexSelected].setVisible(true);// = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider[indexSelected]));
                                    //Log.e("><><>", "checked index " + indexSelected);

                                } else {

                                    tileOverlay[indexSelected].setVisible(false);
                                    //Log.e("><><>", "unchecked  checked index " + indexSelected);
                                }
                            }
                        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Your code when user clicked on OK
                                    }
                                }
                        ).create();
                dialog1.show();
                break;
            case R.id.layer_info:
                // int selectedElement=-1; //global variable to store state
                AlertDialog dialog2;
                //final String[] selectFruit= new String[]{"Blacklist","Whitelist"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Choose a category for information");
                builder.setSingleChoiceItems(selected_key, selected_layer_info,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected_layer_info = which;
                                // Toast.makeText(Test1.this, GlobalVariable.selected_key[which]+":"+ which + " Selected", Toast.LENGTH_LONG).show();
                                //  dialog.dismiss();
                            }
                        });
                builder.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog2 = builder.create();
                dialog2.show();
                break;

            case R.id.toggle_gps:
                GPS_CURRENT_LOCATION = true;
                gotoLocation();
                break;

            case R.id.imageViewDirection:
                Log.e("direction Button", "Clicked");
                GlobalVariable globalVariable = GlobalVariable.getInstance();
                globalVariable.setmGlobalCurrentLocation(mCurrentLocation);
                Intent intent = new Intent(Test1.this, DirectionMap.class);
                startActivity(intent);
                break;

            case R.id.imageViewMapLayer:
                AlertDialog dialogMap;
                //final String[] selectFruit= new String[]{"Blacklist","Whitelist"};
                final String[] mapLayerList = new String[]{"Satellite", "Terrain", "Hybrid"};
                AlertDialog.Builder builderMap = new AlertDialog.Builder(this);
                builderMap.setTitle("Choose a Map Layer");
                builderMap.setSingleChoiceItems(mapLayerList, selected_mapLayer,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selected_mapLayer = which;
                                setUpMap();
                                // Toast.makeText(Test1.this, GlobalVariable.selected_key[which]+":"+ which + " Selected", Toast.LENGTH_LONG).show();
                                //  dialog.dismiss();
                            }
                        });
                builderMap.setPositiveButton("ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialogMap = builderMap.create();
                dialogMap.show();
                break;

        }
    }

    private void setUpMap() {
        for (int i = 0; i < selected_value.length; i++) {
            wmsTileProvider[i] = TileProviderFactory.getOsgeoWmsTileProvider(selected_value[i]);
            tileOverlay[i] = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(wmsTileProvider[i]));
            if (set_visibility[i] == true)
                tileOverlay[i].setVisible(true);
            else
                tileOverlay[i].setVisible(false);
        }
        if (selected_mapLayer == 0) {
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);//MAP_TYPE_TERRAIN
            distanceTextView.setTextColor(Color.WHITE);
        } else if (selected_mapLayer == 1) {
            mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);//MAP_TYPE_SATELLITE
            distanceTextView.setTextColor(Color.BLACK);
        } else if (selected_mapLayer == 2) {
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            distanceTextView.setTextColor(Color.WHITE);
        }
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                onMapClickMethod(latLng);
            }
        });

        RelativeLayout container = (RelativeLayout) findViewById(R.id.main_view);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(800, 800);

        params.addRule(RelativeLayout.CENTER_HORIZONTAL);
        params.addRule(RelativeLayout.CENTER_VERTICAL);

        mScaleBar = new ScaleBar(this, mMap);
        mScaleBar.setLayoutParams(params);
    }

    private void gotoLocation() {
        try {
            if (GPS_CURRENT_LOCATION) {
                Log.e("inside location", "gpl location");
                if (mMap != null) {

                    final GoogleMap.OnMyLocationChangeListener mCurrentLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
                        @Override
                        public void onMyLocationChange(Location location) {
                            mCurrentLocation = location;
                        }
                    };
                    permissionRequest();
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mCurrentLocation = getLastKnownLocation();
                        if (mCurrentLocation != null) {
                            Criteria criteria = new Criteria();
                            criteria.setAccuracy(Criteria.ACCURACY_FINE);
                            LatLng latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
//                            mMap.setMyLocationEnabled(true);
                            mMap.setMyLocationEnabled(false);
                            mMap.getUiSettings().setCompassEnabled(true);
                            mMap.setOnMyLocationChangeListener(mCurrentLocationChangeListener);
                            GPS_CURRENT_LOCATION = false;
                        } else {
                            Log.e("mCurrentLocation", "null");
                        }
                    } else {
                        Log.e("permission", "not set");
                    }

                } else {
                    Log.e("mMap", "is null");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void permissionRequest() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsFine();
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsCoarse();
        }
    }

    public void requestPermissionsFine() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE_1);
    }

    public void requestPermissionsCoarse() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE_1);
    }

    public void onMapClickMethod(LatLng point) {
        Log.e(TAG, "onMapClickMethod");
        SupportMapFragment mMapFragment1 = (SupportMapFragment) (getSupportFragmentManager().findFragmentById(map));
        mMapFragment1.getMapAsync(Test1.this);
        map_width = mMapFragment1.getView().getWidth();
        map_height = mMapFragment1.getView().getHeight();

        LatLngBounds region = mMap.getProjection().getVisibleRegion().latLngBounds;
        db_left = region.southwest.longitude;
        db_top = region.northeast.latitude;
        db_right = region.northeast.longitude;
        db_bottom = region.southwest.latitude;
        clicked_xy = mMap.getProjection().toScreenLocation(point);
        new getFeatureInfo().execute();

    }

    private void initiateGooglePlayService() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class getFeatureInfo extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(Test1.this);
            mProgressDialog.setMessage("Fetching data .. ");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                URL url;
                url = new URL("http://203.129.224.73:8080/geoserver/wms?" + //  /wms
                        "service=WMS&" +
                        "version=1.1.1&" +
                        "srs=EPSG:4326&" +
                        "bbox=" + db_left + "," + db_bottom +
                        "," + db_right + "," + db_top + "&" +
                        "styles=&" +
                        "&buffer=40&" +
                        "info_format=application/json&" +
                        "request=GetFeatureInfo&" +
                        "layers=sgl:" + selected_value[selected_layer_info] + "&" +
                        "query_layers=sgl:" + selected_value[selected_layer_info] + "&" +//geopolis
                        "width=" + map_width + "&" +
                        "height=" + map_height + "&" +
                        "x=" + clicked_xy.x + "&" +
                        "y=" + clicked_xy.y);

                Log.e(">>>>>>>>>>>>>>>>>>", " " + url.toString());
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file                                                                                                                                                                                              length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 10 * 1024);
                // Output stream to write file in SD card
                // Convert response to string
                try {
                    //Log.e("log_tag 123", "Error converting result ");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    input.close();
                    str_pop_up = sb.toString();
                    //Log.e(">>>>>>>>>>>>>>>>>>", " " + str_pop_up.toString());

                } catch (Exception e) {
                    Log.e("log_tag 862", "Error converting result " + e.toString());
                }

            } catch (Exception e) {
                Log.e("Error 184: ", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            //TextView content = new TextView(Test1.this);
            //content.setText("" + str_pop_up);
            Log.e("geojson ", ">  " + str_pop_up);
            try {

                if (str_pop_up != null) {//|| !str_pop_up.isEmpty()
                    JSONObject json = new JSONObject(str_pop_up);
                    JSONArray features = new JSONArray();

                    if (json.length() > 0) {
                        //JSONArray properties = new JSONArray();

                        for (Iterator<String> iter = json.keys(); iter.hasNext(); ) {
                            String key = iter.next();
                            //String value = (String) json.get(key);
                            Log.e("geojson ", ">  " + key);
                        }

                        if (json.has("features")) {
                            features = json.getJSONArray("features");
                            //Log.e("log Val ", "=====  ");

                        } else {
                            str_popup_arr = new String[str_popup_arr.length];
                        }
                        //Log.e("Length :"," >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+features.length());

                        if (features.length() == 0) {
                            //str_popup_arr = new String[str_popup_arr.length];
                            str_popup_arr = new String[0];
                        }
                        final_pop_up_key.clear();
                        final_pop_up_value.clear();

                        boolean data_there = false;
                        String valueM;
                        Set<String> keyM;
                        for (int i = 0; i < features.length(); ++i) {
                            JSONObject rec = features.getJSONObject(i);
                            //int id = rec.getInt("id");
                            // properties = json.getJSONArray("properties");
                            String str_propertie = rec.getString("properties");
                            JSONObject properties = new JSONObject(str_propertie);
                            for (Iterator<String> iter = properties.keys(); iter.hasNext(); ) {
                                String key = iter.next();
                                String value = properties.get(key).toString();

                                Log.e("geojson Greece  ", "Key:  " + key + " Value : " + value);
                                if (!value.equalsIgnoreCase("null"))// do not accept the null values
                                {
                                    key = key.replace("_", "").toUpperCase();

                                    /*final_pop_up_key.add(key);
                                    final_pop_up_value.add(value);*/
                                    attributeMap.put(key, value);
                                }

                            }

                            keyM = attributeMap.keySet();

                            for (String keyd : keyM) {
                                final_pop_up_key.add(keyd);
                                valueM = attributeMap.get(keyd);
                                final_pop_up_value.add(valueM);
                            }

                            data_there = true;
                            Log.e(" count --->> split  >> ", " ");
                            //str_popup_arr = str_propertie.split(",");
                            //Log.e("data :"," "+str_propertie);
                            //str_popup_arr = str_propertie.split(":");
                        }
                        if (data_there) {
                            str_popup_arr_key = final_pop_up_key.toArray(new String[final_pop_up_key.size()]);
                            str_popup_arr_value = final_pop_up_value.toArray(new String[final_pop_up_value.size()]);
                            custom_alert();
                        } else {
                            Toast.makeText(Test1.this, "No data at this location ", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(Test1.this, "No Data for this location", Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void custom_alert() {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Information List");
        View view = getLayoutInflater().inflate(R.layout.map_pop_up_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.map_pop_up_lv);
        // Change MyActivity.this and myListOfItems to your own values
        MapPopUp_ListAdaptor clad = new MapPopUp_ListAdaptor(Test1.this, str_popup_arr_key, str_popup_arr_value);
        lv.setAdapter(clad);
        //lv.setOnItemClickListener(........);
        dialog.setContentView(view);
        dialog.show();
    }

    public void custom_alertMarker(String titleString) {
        final Dialog dialog = new Dialog(this);
        dialog.setTitle("Information List");
        View view = getLayoutInflater().inflate(R.layout.map_pop_up_layout, null);
        ListView lv = (ListView) view.findViewById(R.id.map_pop_up_lv);
        // Change MyActivity.this and myListOfItems to your own values

        if (fromPushNotification && titleString != null && !titleString.equals("")) {
            String[] key;
            String[] value;
            for (int i = 0; i < globalVariable.notificationModelList.size(); i++) {
                if (globalVariable.notificationModelList.get(i).getPro_uid().equals(titleString)) {
                    key = globalVariable.notificationModelList.get(i).final_pop_up_key.toArray(new String[globalVariable.notificationModelList.get(i).final_pop_up_key.size()]);
                    value = globalVariable.notificationModelList.get(i).final_pop_up_value.toArray(new String[globalVariable.notificationModelList.get(i).final_pop_up_value.size()]);
                    MapPopUp_ListAdaptor clad = new MapPopUp_ListAdaptor(Test1.this, key, value);
                    lv.setAdapter(clad);
                    //lv.setOnItemClickListener(........);
                    dialog.setContentView(view);
                    dialog.show();
                }
            }

        } else if (fromIncidentList) {
            String[] key = globalVariable.selectedIncidentModel.getFinal_pop_up_key().toArray(new String[globalVariable.selectedIncidentModel.getFinal_pop_up_key().size()]);
            String[] value = globalVariable.selectedIncidentModel.getFinal_pop_up_value().toArray(new String[globalVariable.selectedIncidentModel.getFinal_pop_up_value().size()]);
            MapPopUp_ListAdaptor clad = new MapPopUp_ListAdaptor(Test1.this, key, value);
            lv.setAdapter(clad);
            //lv.setOnItemClickListener(........);
            dialog.setContentView(view);
            dialog.show();
        }
    }

    private Location getLastKnownLocation() {
        List<String> providers = lm.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsFine();
            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionsCoarse();
            }
            Location l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,)
            if (l == null) {
                l = lm.getLastKnownLocation(provider);
                Log.e("inside last location", "" + l.getBearing());
            }

            if (l == null) {
                Log.e("inside getLastLocation", "lm = null");
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }

        /*if (bestLocation.hasBearing())
        animateMarker(mPositionMarker, bestLocation);*/
        return bestLocation;
    }

    protected int getLayoutId() {
        return R.layout.activity_test1;
    }

    private void markerMaker(Double lat, Double lon, final String titleString) {
        if (markerFlag.equalsIgnoreCase("1"))
            marker = new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(titleString).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        else if (markerFlag.equalsIgnoreCase("2") || markerFlag.equalsIgnoreCase("3"))
            marker = new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(titleString).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        else
            marker = new MarkerOptions()
                    .position(new LatLng(lat, lon))
                    .title(titleString).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        final LatLng latLng1 = new LatLng(lat, lon);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng1, 12));
        mMap.addMarker(marker).showInfoWindow();
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.e("marker", "" + marker.getId());
                dialogueMaker(latLng1, titleString, marker);
                selectedMarker = marker;
//                custom_alertMarker(key,value);
                return false;
            }
        });
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                if (new InternetServiceHelper().isInternetAvailable()) {
                    // Fetching the data from web service
                    data = new getUrl().downloadUrl(url[0]);
                    return data;
                }else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(Test1.this);
                    alert.setTitle("Alert!");
                    alert.setMessage("please check your Internet connection and try again");
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    alert.show();
                }
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return "failure";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (!result.equalsIgnoreCase("failure")) {
                ParserTask parserTask = new ParserTask();

                // Invokes the thread for parsing the JSON data
                parserTask.execute(result);
            }

        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(7);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                polyLine = mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }

    private void dialogueMaker(final LatLng latLng, final String titleString, final Marker marker1) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Action");

        alertDialog.setMessage("Select an action to perform");

        alertDialog.setIcon(R.drawable.map_marker);

        alertDialog.setPositiveButton("Direction", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (polyLine != null)
                    polyLine.remove();
                String lat1, lat2, lon1, lon2;
                lat1 = String.valueOf(getLastKnownLocation().getLatitude());
                lon1 = String.valueOf(getLastKnownLocation().getLongitude());
                lat2 = String.valueOf(marker1.getPosition().latitude);
                lon2 = String.valueOf(marker1.getPosition().longitude);
                new GetDistance().execute(lat1, lon1, lat2, lon2);
                    directionTask(marker1.getPosition());
                    directionenabled = true;

            }
        });

        alertDialog.setNegativeButton("Information", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                custom_alertMarker(marker1.getTitle());
                dialog.cancel();
            }
        });

        alertDialog.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("marker1 Id", "" + marker1.getId());
                Log.e("old LATLONG ", "" + marker1.getPosition().toString());

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(Test1.this);
                alertDialog.setTitle("Change Marker Location");
                alertDialog.setIcon(R.drawable.map_marker);
                alertDialog.setPositiveButton("Current Location", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Test1.this);
                        alertBuilder.setTitle("Alert!");
                        alertBuilder.setMessage("Are you sure you want to change the location");
                        alertBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                double lat = getLastKnownLocation().getLatitude();
                                double lng = getLastKnownLocation().getLongitude();
                                LatLng latLng1 = new LatLng(lat, lng);
                                marker1.setPosition(latLng1);
                                if (fromIncidentList) {
                                    globalVariable.selectedIncidentModel.setLongitude(String.valueOf(marker1.getPosition().longitude));
                                    globalVariable.selectedIncidentModel.setLatitude(String.valueOf(marker1.getPosition().latitude));
                                    locationUpdate(marker1.getTitle(), marker1.getPosition(), new EncodeHelper().md5(globalVariable.IMEI), globalVariable.selectedIncidentModel.getCity_id());
                                }
                            }
                        });
                        alertBuilder.show();
                    }
                });
                alertDialog.setNegativeButton("Drag Marker", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker1.setDraggable(true);
                        Toast.makeText(Test1.this, "long press the marker to move it", Toast.LENGTH_LONG).show();
                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {
                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {

                                Log.e("new LATLONG ", "" + marker.getPosition().toString());
                                marker.setDraggable(false);
                                Log.e("marker drag Id", "" + marker.getId());

                                AlertDialog.Builder alertBuild = new AlertDialog.Builder(Test1.this);
                                alertBuild.setTitle("Alert!");
                                alertBuild.setMessage("Are you sure you want to change the location");
                                alertBuild.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (fromIncidentList) {
                                            globalVariable.selectedIncidentModel.setLatitude(String.valueOf(marker1.getPosition().latitude));
                                            globalVariable.selectedIncidentModel.setLongitude(String.valueOf(marker1.getPosition().longitude));
                                            locationUpdate(marker1.getTitle(), marker1.getPosition(), new EncodeHelper().md5(globalVariable.IMEI), globalVariable.selectedIncidentModel.getCity_id());
                                        } else if (fromPushNotification) {
                                            for (int i = 0; i < globalVariable.notificationModelList.size(); i++) {
                                                if (globalVariable.notificationModelList.get(i).getPro_uid().equals(marker1.getTitle())) {
                                                    globalVariable.notificationModelList.get(i).setLatitude(String.valueOf(marker1.getPosition().latitude));
                                                    globalVariable.notificationModelList.get(i).setLongitude(String.valueOf(marker1.getPosition().longitude));
                                                    locationUpdate(marker1.getTitle(), marker1.getPosition(), new EncodeHelper().md5(globalVariable.IMEI), globalVariable.notificationModelList.get(i).getCity_id());
                                                }
                                            }
                                        }
                                    }
                                });
                                alertBuild.show();
                            }
                        });
                    }
                });
                alertDialog.show();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

    private void directionTask(LatLng destination) {
        Location currentLocation = getLastKnownLocation();
        LatLng origin = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        LatLng dest = destination;

        // Getting URL to the Google Directions API
        String url = new getUrl().getUrl(origin, dest);
        Log.d("onMapClick", url.toString());
        FetchUrl FetchUrl = new FetchUrl();

        // Start downloading json data from Google Directions API
        FetchUrl.execute(url);
        //move map camera
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> allLocation = new ArrayList<LatLng>();
        allLocation.add(origin);
        allLocation.add(dest);

        for (LatLng location : allLocation) {
            builder.include(location);
        }
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),100));
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    private void locationUpdate(String id, LatLng latLng, String encodedImei, String cityId) {
        String lat, lon;
        lat = String.valueOf(latLng.latitude);
        lon = String.valueOf(latLng.longitude);

        new updateLocationTask().execute(id, lat, lon, encodedImei, cityId);
    }

    private class updateLocationTask extends AsyncTask<String, Void, String> {
        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Test1.this);
            dialog.setTitle("Updating...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (dialog.isShowing())
                dialog.cancel();
        }

        @Override
        protected String doInBackground(String... params) {
            Log.e("parametrs", "pro_uid : " + params[0] + " lat : " + params[1] + " lon : " + params[2] + " imei : " + params[3] + " city id : " + params[4]);
            String successString = new NetworkCallHandler().locationUpdate(params[0], params[1], params[2], params[3], params[4]);
            Log.e("Success String", "" + successString);
            return "";
        }
    }

    private class GetDistance extends AsyncTask<String, Void, String> {
        String parsedDistance="";
        String response="";

        @Override
        protected String doInBackground(String... params) {
            try {
                String url = ("http://maps.googleapis.com/maps/api/directions/json?origin=" + params[0] + "," + params[1] + "&destination=" + params[2] + "," + params[3] + "&sensor=false&units=metric&mode=driving");

                response = new NetworkCallHandler().getDistance(url);

                JSONObject jsonObject = new JSONObject(response);
                JSONArray array = jsonObject.getJSONArray("routes");
                JSONObject routes = array.getJSONObject(0);
                JSONArray legs = routes.getJSONArray("legs");
                JSONObject steps = legs.getJSONObject(0);
                JSONObject distance = steps.getJSONObject("distance");
                parsedDistance = distance.getString("text");
                Log.e("distance", "a " + parsedDistance);
                return parsedDistance;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null && !s.isEmpty())
                distanceTextView.setText("Distance: " + s);
        }
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

}