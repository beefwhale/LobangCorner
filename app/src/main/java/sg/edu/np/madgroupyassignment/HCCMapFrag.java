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
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.android.gms.location.LocationRequest;
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

public class HCCMapFrag extends Fragment implements OnMapReadyCallback{

    //Global variables
    private MapView mapView;
    private GoogleMap googleMap;
    private Geocoder geocoder;
    private FusedLocationProviderClient client;
    private Marker position1, position2;
    private Polyline polyline;
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

        //When user clicks go there, get user's current location
        hcctrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client = LocationServices.getFusedLocationProviderClient(getContext());
                getCurrentLocation();
            }
        });
    }

    public void getCurrentLocation() {
        //Check for permissions
        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(@NonNull Location location) {
                    if (location != null) {
                        //Map sync
                        mapView.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                //Get new location lat lng
                                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                                //Check if polyline already exist in the map, if exist, remove line and previous user's marker location
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
                            }
                        });
                    }
                    //If location is null
                    else {
                        Toast.makeText(getActivity(), "Unable to get your location", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        //Request permission
        else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    //Google map toolbar disabled, zoom controls and buildings enabled
    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);

        //Set the default map to show the stall location, geocoder to get the lat lng from the name
        try {
            List<Address> addressList = geocoder.getFromLocationName(hccaddr, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng addressloc = new LatLng(address.getLatitude(), address.getLongitude());
                position1 = googleMap.addMarker(new MarkerOptions()
                        .position(addressloc)
                        .title(hccname));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(addressloc, 18));
            }
            //If the address list is empty, geocoder cannot find a lat lng for address, default view on Singapore
            else {
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
}