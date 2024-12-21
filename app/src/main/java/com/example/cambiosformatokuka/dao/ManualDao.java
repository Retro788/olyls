package com.example.cambiosformatokuka.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cambiosformatokuka.models.Manual;

import java.util.List;

@Dao
public interface ManualDao {

    @Query("SELECT * FROM Manual ORDER BY id ASC")
    List<Manual> getAllManuales();

    @Insert
    void insert(Manual manual);

    @Update
    void update(Manual manual);

    @Delete
    void delete(Manual manual);

    @Query("SELECT * FROM Manual WHERE id = :manualId LIMIT 1")
    Manual findById(int manualId);
}
