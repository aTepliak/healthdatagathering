package com.example.android.healthdatagathering.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.android.healthdatagathering.database.entity.CompositeCompositeJoin;
import com.example.android.healthdatagathering.database.entity.HealthDataComposite;

import java.util.List;

@Dao
public interface CompositeCompositeJoinDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CompositeCompositeJoin compjoin);


    @Query("SELECT * FROM healthdatacomposite INNER JOIN composite_composite_join ON" +
            " healthdatacomposite.id = composite_composite_join.compositeParentId WHERE" +
            " composite_composite_join.compositeChildId =:compositeChildId")
    List<HealthDataComposite> loadCompositeParentForCompositeChild(final long compositeChildId);


    @Query("SELECT * FROM healthdatacomposite INNER JOIN composite_composite_join ON" +
            " healthdatacomposite.id = composite_composite_join.compositeChildId WHERE" +
            " composite_composite_join.compositeParentId =:compositeParentId")
    List<HealthDataComposite> loadCompositeChildrenForCompositeParent(final long compositeParentId);


}
