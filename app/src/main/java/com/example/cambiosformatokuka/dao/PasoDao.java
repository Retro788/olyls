package com.example.cambiosformatokuka.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cambiosformatokuka.models.Paso;

import java.util.List;

@Dao
public interface PasoDao {

    @Query("SELECT * FROM Paso WHERE guiaId = :guiaId ORDER BY id ASC")
    List<Paso> getPasosByGuiaId(int guiaId);

    @Insert
    void insert(Paso paso);

    @Update
    void update(Paso paso);

    @Delete
    void delete(Paso paso);
}
