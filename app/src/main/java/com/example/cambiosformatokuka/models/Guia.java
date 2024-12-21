package com.example.cambiosformatokuka.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Guia {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String descripcion;

    // Constructor
    public Guia(String descripcion) {
        this.descripcion = descripcion;
    }

    // Getter para 'id'
    public int getId() {
        return id;
    }

    // Setter para 'id' (Room lo usará al insertar nuevas guías)
    public void setId(int id) {
        this.id = id;
    }

    // Getter para 'descripcion'
    public String getDescripcion() {
        return descripcion;
    }

    // Setter para 'descripcion'
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}

