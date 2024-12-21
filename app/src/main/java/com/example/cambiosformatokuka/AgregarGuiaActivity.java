package com.example.cambiosformatokuka;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cambiosformatokuka.models.Guia;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgregarGuiaActivity extends AppCompatActivity {

    private EditText descripcionEditText;
    private Button guardarButton;
    private AppDatabase db;
    private int guiaId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_guia);

        // Inicialización de los componentes y la base de datos
        db = AppDatabase.getInstance(this);
        descripcionEditText = findViewById(R.id.editTextDescripcion);
        guardarButton = findViewById(R.id.buttonGuardarGuia);

        // Verificación de si estamos editando o agregando una nueva guía
        guiaId = getIntent().getIntExtra("guiaId", -1);
        if (guiaId != -1) {
            Guia guia = db.guiaDao().findById(guiaId);
            descripcionEditText.setText(guia.getDescripcion());
        }

        // Acción del botón para guardar
        guardarButton.setOnClickListener(view -> saveGuia());
    }

    // Método para guardar la guía en la base de datos
    private void saveGuia() {
        String descripcion = descripcionEditText.getText().toString();
        if (descripcion.isEmpty()) {
            Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            if (guiaId == -1) {
                db.guiaDao().insert(new Guia(descripcion));
            } else {
                Guia guia = db.guiaDao().findById(guiaId);
                guia.setDescripcion(descripcion);
                db.guiaDao().update(guia);
            }

            runOnUiThread(() -> {
                setResult(RESULT_OK); // Indicamos que la operación fue exitosa
                finish();  // Finaliza la actividad
            });
        });
    }


}
