package com.example.cambiosformatokuka.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {

    @PrimaryKey
    @NonNull
    public String id; // El campo 'id' es la clave primaria

    public String passwordEncrypted; // Contraseña encriptada

    // Constructor
    public Usuario(String id, String passwordEncrypted) {
        this.id = id;
        this.passwordEncrypted = passwordEncrypted;
    }

    // Getter para 'id'
    public String getId() {
        return id;
    }

    // Setter para 'id'
    public void setId(String id) {
        this.id = id;
    }

    // Getter para 'passwordEncrypted'
    public String getPasswordEncrypted() {
        return passwordEncrypted;
    }

    // Setter para 'passwordEncrypted'
    public void setPasswordEncrypted(String passwordEncrypted) {
        this.passwordEncrypted = passwordEncrypted;
    }
}
