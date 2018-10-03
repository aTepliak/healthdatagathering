package com.example.android.healthdatagathering.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;


import java.util.List;

@Dao
public interface HealthDataAtomicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(HealthDataAtomic component);

    @Update(onConflict = OnConflictStrategy.IGNORE)
    void update (HealthDataAtomic component);


    @Delete
    void deleteComponent (HealthDataAtomic component);


    @Query("SELECT * FROM healthdataatomic ORDER BY id")
    List<HealthDataAtomic> loadAllComplex();

}
