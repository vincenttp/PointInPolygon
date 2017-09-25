package smartfren.com.pointinpolygon;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.internal.zzr;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapLongClickListener, GoogleMap.OnPoiClickListener {
    GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    GPSTracker gpsTracker;
    List<LatLng> latLngs = new ArrayList<>();
    Marker marker, marker2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        gpsTracker = new GPSTracker(getApplicationContext());

        latLngs.add(new LatLng(-6.18517,106.82553));
        latLngs.add(new LatLng(-6.18478,106.82577));
        latLngs.add(new LatLng(-6.1851,106.82674));
        latLngs.add(new LatLng(-6.18541,106.82651));
        latLngs.add(new LatLng(-6.18549,106.82627));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnPoiClickListener(this);

        PolygonOptions polygonOptions = new PolygonOptions().add(latLngs.get(0), latLngs.get(1), latLngs.get(2), latLngs.get(3), latLngs.get(4));

        googleMap.addPolygon(polygonOptions);

        LatLng point = new LatLng(-6.18515, 106.82599);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 15));

        marker = mMap.addMarker(new MarkerOptions()
                .position(point)
                .title("Smartfren")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_favorite)));

        LatLng point1 = new LatLng(gpsTracker.getLocation().getLatitude(), gpsTracker.getLocation().getLongitude());
        mMap.addMarker(new MarkerOptions()
            .position(point1)
            .title("Lokasi Sekarang"));

        LatLng point2 = new LatLng(-6.1859893798828125, 106.82556152343749);
        marker2 = mMap.addMarker(new MarkerOptions()
                .position(point2)
                .title("Lokasi Testing")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_favorite)));

        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        d();
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_feedback));
        if (PolyUtil.containsLocation(marker.getPosition(), latLngs, false)){
            Toast.makeText(getApplicationContext(), "Point di dalam polygon", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Point di luar polygon", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title(String.valueOf(latLng)));
    }

    @Override
    public void onPoiClick(PointOfInterest poi) {
        Toast.makeText(getApplicationContext(), "Clicked: " +
                        poi.name + "\nPlace ID:" + poi.placeId +
                        "\nLatitude:" + poi.latLng.latitude +
                        " Longitude:" + poi.latLng.longitude,
                Toast.LENGTH_SHORT).show();
    }

    private void d(){
        marker.setIcon(null);
        marker2.setIcon(null);
    }
}
