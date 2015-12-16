package com.example.asy.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity {
    LatLng MyLocation;
    private GoogleMap map;
    Intent intent;
    TextView logView;
    Button bt1;
    Button bt2;
    double lng;
    double lat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logView = (TextView) findViewById(R.id.log);
        logView.setText("GPS 가 잡혀야 좌표가 구해짐");

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                lat = location.getLatitude();
                lng = location.getLongitude();
                logView.setText("latitude: " + lat + ", longitude: " + lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


        bt1 = (Button) findViewById(R.id.bt_1);
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLocation = new LatLng( lat, lng);
                map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
                Marker me = map.addMarker(new MarkerOptions().position(MyLocation).title("me"));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(MyLocation, 15));
                map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
            }
        });

        bt2 = (Button) findViewById(R.id.bt_2);
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this , DataActivity.class);
                intent.putExtra("TextIn", logView.getText().toString());
                startActivity(intent);
            }
        });


    }
}
