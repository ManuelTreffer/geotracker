package com.example.andi.geotracker;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity {

    MapView map = null;
    List<Track> trackPoints;
    List<GeoPoint> geoPoints;
    Polyline polyline;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.mapactivity);

        map = (MapView) findViewById(R.id.mapView);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(17f);

        database = Room.databaseBuilder(this, AppDatabase.class, "track_db").allowMainThreadQueries().build();

        this.trackPoints = database.getTrackerDao().getAllTracks();
        this.geoPoints = new ArrayList<>();
        this.polyline = new Polyline();

        switch (trackPoints.size()) {

            case 0:

            case 1:

                Toast.makeText(this, "Nicht genug Positionen getrackt", Toast.LENGTH_SHORT).show();
            default:

                for (int i = 0; i < trackPoints.size(); i++) {
                    GeoPoint point = new GeoPoint(trackPoints.get(i).getLatitude(), trackPoints.get(i).getLongtitude());
                    geoPoints.add(point);
                }

                polyline.setPoints(this.geoPoints);
                polyline.setColor(Color.RED);
                map.getOverlayManager().add(polyline);
                mapController.setCenter(geoPoints.get(0));

        }
    }
}
