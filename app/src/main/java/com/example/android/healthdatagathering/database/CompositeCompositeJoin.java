package com.example.android.healthdatagathering.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;


/**
 *implementing many to many relation. One composite can be a part of many other components
 *and one composite may include many other composites
 */

@Entity(tableName = "composite_composite_join",
        primaryKeys = { "compositeParentId", "compositeChildId" },
        foreignKeys = {
                @ForeignKey(entity = HealthDataComposite.class,
                        parentColumns = "id",
                        childColumns = "compositeParentId"),

                @ForeignKey(entity = HealthDataComposite.class,
                        parentColumns = "id",
                        childColumns = "compositeChildId")
        })
public class CompositeCompositeJoin {
    private long compositeParentId;
    private long compositeChildId;

    public CompositeCompositeJoin(long compositeParentId, long compositeChildId) {
        this.compositeParentId = compositeParentId;
        this.compositeChildId = compositeChildId;
    }
}
