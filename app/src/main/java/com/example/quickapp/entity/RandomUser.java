package com.example.quickapp.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class RandomUser {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "response_data")
    private String clobdata;

    public RandomUser(String clobdata) {
        this.clobdata = clobdata;
    }

    @NonNull
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClobdata() {
        return clobdata;
    }

    public void setClobdata(String clobdata) {
        this.clobdata = clobdata;
    }
}
