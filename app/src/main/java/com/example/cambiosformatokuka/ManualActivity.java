package com.example.cambiosformatokuka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cambiosformatokuka.adapters.ManualAdapter;
import com.example.cambiosformatokuka.models.Manual;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ManualActivity extends AppCompatActivity {

    private RecyclerView manualRecyclerView;
    private Button agregarManualButton;
    private ManualAdapter manualAdapter;
    private AppDatabase db;

    // Nuevos TextViews
    private TextView textViewCerrarSesion;
    private TextView textViewSalir;
    private TextView textViewRegresar;
    private TextView textViewAgregarManual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);

        // Inicialización de la base de datos y componentes de la interfaz
        db = AppDatabase.getInstance(this);
        manualRecyclerView = findViewById(R.id.recyclerViewManual);
        //agregarManualButton = findViewById(R.id.buttonAgregarManual);

        // Nuevas referencias a los TextViews
        textViewCerrarSesion = findViewById(R.id.textViewCerrarSesion);
        textViewSalir = findViewById(R.id.textViewSalir);
        textViewRegresar = findViewById(R.id.textViewRegresar);
        textViewAgregarManual = findViewById(R.id.textViewAgregarManual);

        // Configuración del RecyclerView
        manualRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar manuales y configurar adaptador
        loadManuales();

        // Acción para agregar un nuevo manual
        /*agregarManualButton.setOnClickListener(view -> {
            Intent intent = new Intent(ManualActivity.this, AgregarManualActivity.class);
            startActivity(intent);
        });*/

        // Acciones para los TextViews
        textViewCerrarSesion.setOnClickListener(view -> {
            // Acción para cerrar sesión
            // Implementa la lógica para cerrar sesión aquí
        });

        textViewSalir.setOnClickListener(view -> {
            // Acción para salir de la aplicación
            finishAffinity();
        });

        textViewRegresar.setOnClickListener(view -> {
            // Acción para regresar a la actividad anterior
            onBackPressed();
        });

        textViewAgregarManual.setOnClickListener(view -> {
            // Acción para agregar un nuevo manual
            Intent intent = new Intent(ManualActivity.this, AgregarManualActivity.class);
            startActivity(intent);
        });
    }

    // Método para cargar manuales desde la base de datos
    private void loadManuales() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<Manual> manuales = db.manualDao().getAllManuales();
            runOnUiThread(() -> {
                manualAdapter = new ManualAdapter(manuales, this::onManualClick);
                manualRecyclerView.setAdapter(manualAdapter);
            });
        });
    }

    // Acción al seleccionar un manual
    private void onManualClick(Manual manual) {
        Intent intent = new Intent(this, VerManualActivity.class);
        intent.putExtra("manualRuta", manual.getArchivoRuta());
        intent.putExtra("titulo", manual.getTitulo());
        startActivity(intent);
    }

    // Recargar los manuales cuando la actividad se reinicie
    @Override
    protected void onResume() {
        super.onResume();
        loadManuales(); // Recargar los manuales al regresar
    }
}


