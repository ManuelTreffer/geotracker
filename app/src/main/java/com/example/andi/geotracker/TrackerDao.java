package com.example.andi.geotracker;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;


@Dao
public interface TrackerDao {

    @Query("SELECT * FROM track ORDER BY timestamp ASC")
    public List<Track> getAllTracks();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    public void insertTrack (Track track);

    @Query("DELETE FROM Track")
    public void deleteTrack ();


}
