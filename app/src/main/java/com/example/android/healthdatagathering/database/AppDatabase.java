package com.example.android.healthdatagathering.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

import com.example.android.healthdatagathering.database.dao.CompositeComponentJoinDao;
import com.example.android.healthdatagathering.database.dao.CompositeCompositeJoinDao;
import com.example.android.healthdatagathering.database.dao.HealthDataAtomicDao;
import com.example.android.healthdatagathering.database.dao.HealthDataComplexDao;
import com.example.android.healthdatagathering.database.dao.HealthDataCompositeDao;
import com.example.android.healthdatagathering.database.entity.CompositeComponentJoin;
import com.example.android.healthdatagathering.database.entity.CompositeCompositeJoin;
import com.example.android.healthdatagathering.database.entity.HealthData;
import com.example.android.healthdatagathering.database.entity.HealthDataAtomic;
import com.example.android.healthdatagathering.database.entity.HealthDataComplex;
import com.example.android.healthdatagathering.database.entity.HealthDataComposite;

@Database(entities = {HealthDataComposite.class, HealthDataComplex.class, HealthDataAtomic.class,
        CompositeCompositeJoin.class, CompositeComponentJoin.class},
        version = 1,exportSchema = false)
@TypeConverters({DateConverter.class, FloatArrayListConverter.class, StringArrayListConverter.class,
HealthDataAtomicArrayListConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    private static final String LOG_TAG = AppDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "healthdata";
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }
    public abstract CompositeComponentJoinDao  compositeComponentDao();
    public abstract CompositeCompositeJoinDao compositeCompositeJoinDao();
    public abstract HealthDataAtomicDao healthDataAtomicDao();
    public abstract HealthDataComplexDao healthDataComplexDao();
    public abstract HealthDataCompositeDao healthDataCompositeDao();

}
