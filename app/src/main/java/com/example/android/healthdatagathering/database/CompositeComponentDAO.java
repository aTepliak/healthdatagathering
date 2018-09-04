package com.example.android.healthdatagathering.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CompositeComponentDAO {
    @Query("SELECT * FROM healthdatacomponent INNER JOIN composite_component_join ON" +
            " healthdatacomponent.id = composite_component_join.componentId WHERE" +
            " composite_component_join.compositeId =:compositId")
    List<HealthData> getComponentsForComposite(final int compositId);


}
