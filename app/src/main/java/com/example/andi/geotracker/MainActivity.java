package com.example.andi.geotracker;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity implements LocationListener {

    private boolean tracksAvailable = false;
    private boolean currentTrack = false;
    Button tracking;
    Button showTracking;
    TextView lastPosition;
    AppDatabase database;
    LocationManager locationManager;
    Location lastLocation;

    private static final int REQUEST_PERMISSION = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tracking = findViewById(R.id.tracking);
        showTracking = findViewById(R.id.showTracking);
        lastPosition = findViewById(R.id.lastPosition);
        this.locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        database = Room.databaseBuilder(this, AppDatabase.class, "track_db").allowMainThreadQueries().build();

        if (database.getTrackerDao().getAllTracks().size() == 0) {
            showTracking.setEnabled(false);
        }

        tracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentTrack) {
                    startTracking();
                    currentTrack = true;
                    changeButtonLayout();
                } else {
                    stopTracking();
                    changeButtonLayout();
                }
            }
        });

        showTracking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeButtonLayout();
                showTracking();
            }
        });
    }

    protected void changeButtonLayout() {
        if (tracksAvailable) {
            showTracking.setEnabled(true);
        }

        if (currentTrack) {
            this.tracking.setText("TRACKING STOPPEN");
        } else {
            this.tracking.setText("TRACKING STARTEN");
        }
    }

    public void startTracking() {
        if (!currentTrack) {
            this.database.getTrackerDao().deleteTrack();
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5, this);
            this.lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION);
        }
    }

    protected void stopTracking() {
        this.tracksAvailable = true;
        this.currentTrack = false;
        changeButtonLayout();
        locationManager.removeUpdates(this);
    }

    public void showTracking() {
        startActivity(new Intent(this, MapActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTracking();
        } else {
            Toast.makeText(this, "Zugriff verweigert!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Track track = Track.create(location);
        database.getTrackerDao().insertTrack(track);
        this.lastPosition.setText("Letzte Position: " + track.getLatitude() + " | " + track.getLongtitude());
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
}
