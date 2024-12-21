package com.example.cambiosformatokuka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button buttonGuia, buttonManual;
    private TextView textViewCerrarSesion, textViewSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Verificar si el usuario está autenticado antes de mostrar la interfaz principal
        if (!isUserAuthenticated()) {
            // Si no está autenticado, redirigir al LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Finaliza la actividad actual para que el usuario no pueda regresar a ella sin estar autenticado
            return;  // Detener el resto del código si el usuario no está autenticado
        }

        setContentView(R.layout.activity_main);

        // Inicializar los botones y textViews
        buttonGuia = findViewById(R.id.buttonGuiaCambioFormato);
        buttonManual = findViewById(R.id.buttonProcedimientosManuales);
        textViewCerrarSesion = findViewById(R.id.textViewCerrarSesion);
        textViewSalir = findViewById(R.id.textViewSalir);

        // Evento para el botón "Guía Cambio de Formato"
        buttonGuia.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GuiaActivity.class);
            startActivity(intent);
        });

        // Evento para el botón "Procedimientos y Manuales"
        buttonManual.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ManualActivity.class);
            startActivity(intent);
        });

        // Evento para "Cerrar sesión"
        textViewCerrarSesion.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("is_authenticated", false);  // Cambiar el estado de autenticación a falso
            editor.apply();

            // Redirigir al LoginActivity
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Cerrar la actividad actual
        });

        // Evento para "Salir"
        textViewSalir.setOnClickListener(view -> {
            // Finalizar la actividad actual y salir de la aplicación
            finish();
            System.exit(0);
        });
    }

    // Método para verificar si el usuario está autenticado (por ejemplo, usando SharedPreferences)
    private boolean isUserAuthenticated() {
        SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return sharedPreferences.getBoolean("is_authenticated", false);  // Retorna 'true' si el usuario está autenticado
    }
}


