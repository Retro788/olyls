package com.example.cambiosformatokuka.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Paso {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int guiaId;
    private String descripcion;
    private String imagenRuta;

    // Constructor modificado para aceptar descripcion e imagenRuta
    public Paso(int guiaId, String descripcion, String imagenRuta) {
        this.guiaId = guiaId;
        this.descripcion = descripcion;
        this.imagenRuta = imagenRuta;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuiaId() {
        return guiaId;
    }

    public void setGuiaId(int guiaId) {
        this.guiaId = guiaId;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenRuta() {
        return imagenRuta;
    }

    public void setImagenRuta(String imagenRuta) {
        this.imagenRuta = imagenRuta;
    }
}
