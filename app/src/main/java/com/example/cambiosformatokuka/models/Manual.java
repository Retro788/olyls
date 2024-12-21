package com.example.cambiosformatokuka.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Manual {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String titulo;
    private String archivoRuta;

    // Constructor, getters y setters
    public Manual(String titulo, String archivoRuta) {
        this.titulo = titulo;
        this.archivoRuta = archivoRuta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getArchivoRuta() {
        return archivoRuta;
    }

    public void setArchivoRuta(String archivoRuta) {
        this.archivoRuta = archivoRuta;
    }
}

