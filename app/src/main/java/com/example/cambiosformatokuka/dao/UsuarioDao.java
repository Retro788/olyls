package com.example.cambiosformatokuka.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cambiosformatokuka.models.Usuario;

@Dao
public interface UsuarioDao {

    @Insert
    void insert(Usuario usuario);

    @Query("SELECT * FROM Usuario WHERE id = :userId LIMIT 1")
    Usuario getUserById(String userId);
}
