package sg.edu.np.madgroupyassignment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class HCCDriveMap extends Fragment implements OnMapReadyCallback, TaskLoadedCallback {

    private ApiInterface apiInterface;
    private MapView mapView;
    private GoogleMap googleMap;
    private List<LatLng> polylist;
    private PolylineOptions polylineOptions;
    private Polyline polyline;
    private LatLng origin;
    private LatLng destination;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hcc_drive_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        mapView = view.findViewById(R.id.hccdrivemap);

        Bundle bundle = this.getArguments();
        Double originlat = bundle.getDouble("pos2lat");
        Double originlng = bundle.getDouble("pos2lng");
        Double destlat = bundle.getDouble("pos1lat");
        Double destlng = bundle.getDouble("pos1lng");

        origin = new LatLng(originlat, originlng);
        destination = new LatLng(destlat, destlng);

        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        String url = getUrl(origin, destination, "driving");

        new FetchUrl(HCCDriveMap.this.getContext()).execute(url,"driving");
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map){
        googleMap = map;
        googleMap.setBuildingsEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(false);
    }

    public String getUrl(LatLng origin, LatLng dest, String directionMode){
        String originstr = "origin=" + origin.latitude + "," + origin.longitude;
        String deststr = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + directionMode;
        String parameters = originstr + "&" + deststr + "&" + mode;
        String output = "json";
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters
                + "&key=<keyvaluehere>";
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (polyline != null){
            polyline.remove();
        }
        googleMap.addPolyline((PolylineOptions) values[0]);
        Log.d("isitget",  values.toString());
        mapView.getMapAsync(this);
    }
}