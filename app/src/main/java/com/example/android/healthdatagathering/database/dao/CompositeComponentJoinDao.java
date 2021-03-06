package com.example.android.healthdatagathering.database.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.healthdatagathering.database.entity.CompositeComponentJoin;
import com.example.android.healthdatagathering.database.entity.HealthDataComplex;
import com.example.android.healthdatagathering.database.entity.HealthDataComposite;

import java.util.List;

@Dao
public interface CompositeComponentJoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CompositeComponentJoin compjoin);

    @Query("SELECT * FROM healthdatacomplex INNER JOIN composite_component_join ON" +
            " healthdatacomplex.id = composite_component_join.componentId WHERE" +
            " composite_component_join.compositeId =:compositId")
    List<HealthDataComplex> loadComponentsForComposite(final long compositId);

    @Query("SELECT * FROM healthdatacomposite INNER JOIN composite_component_join ON" +
            " healthdatacomposite.id = composite_component_join.compositeId WHERE" +
            " composite_component_join.componentId =:componentId")
    List<HealthDataComposite> loadCompositeForComponent(final long componentId);

}
