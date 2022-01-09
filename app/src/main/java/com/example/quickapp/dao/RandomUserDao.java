package com.example.quickapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.quickapp.entity.RandomUser;

import java.util.List;

@Dao
public interface RandomUserDao {

    @Insert
    void insert(RandomUser randomUser);

    @Query("select * from RandomUser")
    List<RandomUser> getAllRandomUser();

    @Query("delete from RandomUser")
    void delete();
}
