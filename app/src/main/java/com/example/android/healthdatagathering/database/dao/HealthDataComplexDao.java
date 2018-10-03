package com.example.android.healthdatagathering.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.healthdatagathering.database.entity.HealthData;
import com.example.android.healthdatagathering.database.entity.HealthDataComplex;
import com.example.android.healthdatagathering.database.entity.HealthDataComposite;

import java.util.List;
@Dao
public interface HealthDataComplexDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HealthDataComplex component);


    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update (HealthDataComplex component);


    @Delete
    void deleteComponent (HealthDataComplex component);


    @Query("SELECT * FROM healthdatacomplex ORDER BY id")
    List<HealthDataComplex> loadAllComplex();


}
