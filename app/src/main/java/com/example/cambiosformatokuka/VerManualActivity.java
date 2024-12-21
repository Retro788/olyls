package com.example.cambiosformatokuka;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

public class VerManualActivity extends AppCompatActivity {

    private TextView tituloTextView;
    private TextView contenidoTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_manual);

        tituloTextView = findViewById(R.id.textViewTituloManual);
        contenidoTextView = findViewById(R.id.textViewContenidoManual);

        // Obtener los datos del Intent
        String titulo = getIntent().getStringExtra("titulo");
        String contenido = getIntent().getStringExtra("contenido");

        // Mostrar los datos
        tituloTextView.setText(titulo);
        contenidoTextView.setText(contenido);

        String archivoRuta = getIntent().getStringExtra("manualRuta");

        if (archivoRuta != null) {
            // Intentar abrir el archivo PDF desde el almacenamiento interno
            abrirPdf(archivoRuta);
        } else {
            contenidoTextView.setText("Archivo no disponible");
        }
    }

    // Método para abrir el archivo PDF utilizando FileProvider
    private void abrirPdf(String archivoRuta) {
        File file = new File(archivoRuta);
        if (file.exists()) {
            Uri pdfUri;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // Para Android 7.0 (API 24) y superior, usar FileProvider
                pdfUri = FileProvider.getUriForFile(this, "com.example.cambiosformatokuka.fileprovider", file);
            } else {
                // Para versiones anteriores, usar Uri.fromFile
                pdfUri = Uri.fromFile(file);
            }

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pdfUri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Otorgar permisos de lectura
            startActivity(intent);
        } else {
            Toast.makeText(this, "Archivo no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    // Sobrescribir el método onBackPressed para regresar a la actividad principal de manuales
    @Override
    public void onBackPressed() {
        // Usamos FLAG_ACTIVITY_CLEAR_TOP para limpiar la pila y regresar a ManualActivity
        Intent resultIntent = new Intent(this, ManualActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Limpiar la pila de actividades
        startActivity(resultIntent);

        // Llamada a la implementación base
        super.onBackPressed();
    }

}



