package com.example.cambiosformatokuka.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cambiosformatokuka.models.Guia;

import java.util.List;

@Dao
public interface GuiaDao {

    @Query("SELECT * FROM Guia ORDER BY id ASC")
    List<Guia> getAllGuias();

    @Insert
    void insert(Guia guia);

    @Update
    void update(Guia guia);

    @Delete
    void delete(Guia guia);

    @Query("SELECT * FROM Guia WHERE id = :guiaId LIMIT 1")
    Guia findById(int guiaId);

    @Query("SELECT descripcion FROM Guia WHERE id = :guiaId")
    String getTituloGuiaById(int guiaId);

}
