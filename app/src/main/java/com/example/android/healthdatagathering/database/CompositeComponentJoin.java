package com.example.android.healthdatagathering.database;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;

@Entity(tableName = "composite_component_join",
        primaryKeys = { "compositeId", "componentId" },
        foreignKeys = {
                @ForeignKey(entity = HealthDataComponent.class,
                        parentColumns = "id",
                        childColumns = "componentId"),
                @ForeignKey(entity = HealthDataComposite.class,
                        parentColumns = "id",
                        childColumns = "compositeId")
        })
public class CompositeComponentJoin {
    public long compositeId;
    public long componentId;

    public CompositeComponentJoin(long compositeId, long componentId) {
        this.compositeId = compositeId;
        this.componentId = componentId;
    }
}
