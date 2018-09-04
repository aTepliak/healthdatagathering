package com.example.android.healthdatagathering.database.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;

@Entity(tableName = "composite_component_join",
        primaryKeys = { "compositeId", "componentId" },
        foreignKeys = {
                @ForeignKey(entity = HealthDataComponent.class,
                        parentColumns = "id",
                        childColumns = "componentId"),
                @ForeignKey(entity = HealthDataComposite.class,
                        parentColumns = "id",
                        childColumns = "compositeId")
        },indices = {@Index("componentId")} )
public class CompositeComponentJoin {
    public long getCompositeId() {
        return compositeId;
    }

    public void setCompositeId(long compositeId) {
        this.compositeId = compositeId;
    }

    public long getComponentId() {
        return componentId;
    }

    public void setComponentId(long componentId) {
        this.componentId = componentId;
    }

    public long compositeId;
    public long componentId;

    public CompositeComponentJoin(long compositeId, long componentId) {
        this.compositeId = compositeId;
        this.componentId = componentId;
    }
}
