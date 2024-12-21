package com.example.cambiosformatokuka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.cambiosformatokuka.adapters.GuiaAdapter;
import com.example.cambiosformatokuka.models.Guia;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GuiaActivity extends AppCompatActivity {

    private RecyclerView guiaRecyclerView;
    private GuiaAdapter guiaAdapter;
    private AppDatabase db;

    // Nuevos TextViews para la barra superior e inferior
    private TextView textViewCerrarSesion, textViewSalir, textViewRegresar, textViewAgregarGuia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guia);

        // Inicialización de la base de datos y componentes de la interfaz
        db = AppDatabase.getInstance(this);
        guiaRecyclerView = findViewById(R.id.recyclerViewGuia);

        // Nuevas referencias a los TextViews
        textViewCerrarSesion = findViewById(R.id.textViewCerrarSesion);
        textViewSalir = findViewById(R.id.textViewSalir);
        textViewRegresar = findViewById(R.id.textViewRegresar);
        textViewAgregarGuia = findViewById(R.id.textViewAgregarGuia);

        // Configuración del RecyclerView
        guiaRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar la lista de guías y configurar adaptador
        loadGuias();

        // Acción para "Cerrar sesión"
        textViewCerrarSesion.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("is_authenticated", false);  // Cambiar el estado de autenticación
            editor.apply();

            // Regresar a la pantalla de Login
            Intent intent = new Intent(GuiaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();  // Finalizar esta actividad
        });

        // Acción para "Salir"
        textViewSalir.setOnClickListener(view -> finish());

        // Acción para "Regresar"
        textViewRegresar.setOnClickListener(view -> {
            onBackPressed();  // Regresa a la actividad anterior
        });

        // Acción para "Agregar nueva guía"
        textViewAgregarGuia.setOnClickListener(view -> {
            Intent intent = new Intent(GuiaActivity.this, AgregarGuiaActivity.class);
            startActivityForResult(intent, 1); // Código 1 para identificar esta solicitud
        });
    }

    // Método para cargar las guías desde la base de datos
    private void loadGuias() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Guia> guias = db.guiaDao().getAllGuias();
            runOnUiThread(() -> {
                guiaAdapter = new GuiaAdapter(guias, this::onGuiaClick);
                guiaRecyclerView.setAdapter(guiaAdapter);
            });
        });
    }

    // Sobrescribir el método para manejar el resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recargar la lista de guías si se agregó o actualizó una guía
            loadGuias();
        }
    }

    // Acción al seleccionar una guía (para ver o editar los pasos)
    private void onGuiaClick(Guia guia) {
        Intent intent = new Intent(this, PasoActivity.class);
        intent.putExtra("guiaId", guia.getId());
        startActivity(intent);
    }
}


