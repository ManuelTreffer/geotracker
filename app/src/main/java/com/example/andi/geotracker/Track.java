package com.example.andi.geotracker;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.location.Location;

@Entity
public class Track {

    @PrimaryKey(autoGenerate = true)
    protected int id;
    protected double latitude;
    protected double longtitude;
    protected long timestamp;

    public static Track create(Location location) {
        Track track = new Track();
        track.setLatitude(location.getLatitude());
        track.setLongtitude(location.getLongitude());
        track.timestamp = System.currentTimeMillis();
        return track;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
