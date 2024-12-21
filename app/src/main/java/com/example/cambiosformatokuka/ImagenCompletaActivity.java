package com.example.cambiosformatokuka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ImagenCompletaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagen_completa);

        ImageView fullscreenImageView = findViewById(R.id.fullscreenImageView);

        // Recibir la ruta de la imagen desde el Intent
        String imagePath = getIntent().getStringExtra("imagenRuta");
        if (imagePath != null) {
            Glide.with(this).load(imagePath).into(fullscreenImageView);
        }
    }
}
