package com.example.android.healthdatagathering.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.healthdatagathering.database.entity.HealthData;
import com.example.android.healthdatagathering.database.entity.HealthDataComponent;
import com.example.android.healthdatagathering.database.entity.HealthDataComposite;

import java.util.List;

@Dao
public interface HealthDataCompositeDao {



    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertComponent(HealthDataComposite  composite);




    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateComposite (HealthDataComposite composite);


    @Delete
    void deleteComposite (HealthDataComposite composite);


    @Query("SELECT * FROM healthdatacomposite ORDER BY id")
    List<HealthDataComposite> loadAllComposites();

}
