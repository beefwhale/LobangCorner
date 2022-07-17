package sg.edu.np.madgroupyassignment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.ButtCap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HCCMapFrag extends Fragment implements OnMapReadyCallback, LocationListener{

    private MapView mapView;
    private GoogleMap googleMap;
    private Geocoder geocoder;
    private ApiInterface apiInterface;
    private List<LatLng> polylist;
    private PolylineOptions polylineOptions;
    FusedLocationProviderClient client;
    Marker position1, position2;
    Polyline polyline;

    private String hccname;
    private String hccaddr;

    //Empty Constructor
    public HCCMapFrag() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hcc_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        Bundle bundle = this.getArguments();
        hccaddr = bundle.getString("stalladdr");
        hccname = bundle.getString("stallname");

        TextView maptitle = view.findViewById(R.id.hccaddrtxt);
        TextView hccloctext = view.findViewById(R.id.hccloctext);
        mapView = view.findViewById(R.id.hccmapview);
        Button hcctrack = view.findViewById(R.id.hcctrack);
        geocoder = new Geocoder(this.getContext());

        maptitle.setText(hccname);
        hccloctext.setText(hccaddr);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        //When user clicks go there
        hcctrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = LocationServices.getFusedLocationProviderClient(getContext());
                getCurrentLocation();
                Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .baseUrl("https://maps.googleapis.com/")
                        .build();
                apiInterface = retrofit.create(ApiInterface.class);
            }
        });
    }

    public void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    if (location != null) {
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                                if (polyline != null){
                                    polyline.remove();
                                    position2.remove();
                                    position2 = googleMap.addMarker(new MarkerOptions()
                                            .position(latLng).title("Your Location"));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                }
                                else{
                                    position2 = googleMap.addMarker(new MarkerOptions()
                                            .position(latLng).title("Your Location"));
                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                                }
                                PolylineOptions polylineOptions = new PolylineOptions()
                                        .add(position1.getPosition(), position2.getPosition())
                                        .color(Color.BLUE);
                                polyline = googleMap.addPolyline(polylineOptions);
                                getDirection(position2.getPosition().toString(), position1.getPosition().toString());

                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Unable to get your location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        try {
            List<Address> addressList = geocoder.getFromLocationName(hccaddr, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng addressloc = new LatLng(address.getLatitude(), address.getLongitude());
                position1 = googleMap.addMarker(new MarkerOptions()
                        .position(addressloc)
                        .title(hccname));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressloc, 18));
            } else {
                //Default viewing Singapore
                LatLng sg = new LatLng(1.3521, 103.8198);
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(sg)
                        .title("Could not find matching address");
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sg, 10));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        mapView.getMapAsync(this);
    }

    public void getDirection(String origin, String destination){
        apiInterface.getDirection("driving", origin, destination, "AIzaSyB4SvXLLKqYV_vG4gxIfW-AKnzdshJzkN0")
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Result>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Result result) {
                        polylist = new ArrayList<>();
                        Log.d("mylog", result.getRoutes().toString());
                        List<HCCRoute> routesList = result.getRoutes();
                        for (HCCRoute route:routesList){
                            String polyline = route.getOverviewPolyline().getPoints();
                            polylist.addAll(decodePoly(polyline));
                        }
                        polylineOptions = new PolylineOptions();
                        polylineOptions.addAll(polylist);
                        polylineOptions.color(Color.BLUE);
                        polylineOptions.startCap(new ButtCap());
                        googleMap.addPolyline(polylineOptions);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    //Method to decode string to Polyline LatLng
    public List<LatLng> decodePoly(String encoded){
        List<LatLng> poly = new ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while(index < len){
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~ (result >> 1): (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b& 0x1f) << shift;
                shift += 5;
            }while (b >= 0x20);
            int dlng = (((result & 1) != 0 ? ~ (result >> 1) : (result>> 1)));
            lng += dlng;

            LatLng p = new LatLng(((double) lat/1E5), ((double) lng/1E5));
            poly.add(p);
        }
        return poly;
    }
}