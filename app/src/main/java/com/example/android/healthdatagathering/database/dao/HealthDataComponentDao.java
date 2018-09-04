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
public interface HealthDataComponentDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HealthDataComponent component);





    //void insertComposite(HealthDataComposite composite);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void updateComponent (HealthDataComponent component);


    @Delete
    void deleteComponent (HealthDataComponent component);


    @Query("SELECT * FROM healthdatacomponent ORDER BY id")
    List<HealthDataComponent> loadAllComponents();


}
