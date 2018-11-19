package com.example.android.healthdatagathering.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.healthdatagathering.database.entity.HealthDataComplex;

import java.util.List;

@Dao
public interface HealthDataComplexDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(HealthDataComplex component);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(HealthDataComplex component);

    @Delete
    void deleteComponent(HealthDataComplex component);

    @Query("SELECT * FROM HealthDataComplex")
    List<HealthDataComplex> loadAllComplex();

}
