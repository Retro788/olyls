package com.example.cambiosformatokuka;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cambiosformatokuka.models.Paso;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PasoActivity extends AppCompatActivity {

    private TextView descripcionPasoTextView;
    private ImageView imagenPasoImageView;
    private Button siguientePasoButton, anteriorPasoButton, agregarPasoButton;
    private AppDatabase db;
    private int guiaId, pasoIndex;
    private List<Paso> pasos;
    private Paso pasoActual; // Declarar pasoActual como variable de instancia

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paso);

        // Inicializamos la base de datos y los componentes de la interfaz
        db = AppDatabase.getInstance(this);
        TextView tituloGuiaTextView = findViewById(R.id.textViewTituloGuia); // TextView para el título
        descripcionPasoTextView = findViewById(R.id.textViewDescripcionPaso);
        imagenPasoImageView = findViewById(R.id.imageViewPaso);
        siguientePasoButton = findViewById(R.id.buttonSiguientePaso);
        anteriorPasoButton = findViewById(R.id.buttonAnteriorPaso);
        agregarPasoButton = findViewById(R.id.buttonAgregarPaso);

        // Obtener ID de la guía
        guiaId = getIntent().getIntExtra("guiaId", -1);

        // Cargar el título de la guía
        cargarTituloGuia(tituloGuiaTextView);

        // Llamar a cargar pasos
        cargarPasos();

        // Configuración de botones
        siguientePasoButton.setOnClickListener(view -> {
            if (pasoIndex < pasos.size() - 1) {
                pasoIndex++;
                mostrarPaso();
            }
        });

        anteriorPasoButton.setOnClickListener(view -> {
            if (pasoIndex > 0) {
                pasoIndex--;
                mostrarPaso();
            }
        });

        agregarPasoButton.setOnClickListener(view -> {
            Intent intent = new Intent(PasoActivity.this, AgregarPasoActivity.class);
            startActivityForResult(intent, 1);  // Código de solicitud 1
        });

        imagenPasoImageView.setOnClickListener(view -> {
            if (pasoActual != null && pasoActual.getImagenRuta() != null) {
                Intent intent = new Intent(this, ImagenCompletaActivity.class);
                intent.putExtra("imagenRuta", pasoActual.getImagenRuta()); // Usar pasoActual
                startActivity(intent);
            } else {
                Toast.makeText(this, "No hay imagen para mostrar", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para cargar los pasos en un hilo en segundo plano
    private void cargarPasos() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            pasos = db.pasoDao().getPasosByGuiaId(guiaId);
            runOnUiThread(this::mostrarPaso);  // Actualizar la UI en el hilo principal
        });
    }

    // Método para mostrar el paso actual
    private void mostrarPaso() {
        if (pasos == null || pasos.isEmpty()) {
            Toast.makeText(this, "No hay pasos para mostrar", Toast.LENGTH_SHORT).show();
            return;
        }

        // Actualizar pasoActual con el paso actual
        pasoActual = pasos.get(pasoIndex);
        descripcionPasoTextView.setText(pasoActual.getDescripcion());

        if (pasoActual.getImagenRuta() != null) {
            // Usamos Glide para cargar la imagen desde la ruta
            Glide.with(this).load(pasoActual.getImagenRuta()).into(imagenPasoImageView);
        } else {
            // Si no hay imagen, mostramos un placeholder
            imagenPasoImageView.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Recibir la descripción y la ruta de la imagen
            String descripcion = data.getStringExtra("descripcion");
            String imagenRuta = data.getStringExtra("imagenRuta");

            // Agregar el paso con la descripción y la ruta de la imagen
            agregarPaso(descripcion, imagenRuta);
        }
    }

    private void agregarPaso(String descripcion, String imagenRuta) {
        int guiaId = this.guiaId; // Obtener el guiaId desde la actividad actual

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Crear un nuevo objeto Paso con la descripción y la ruta de la imagen
            Paso nuevoPaso = new Paso(guiaId, descripcion, imagenRuta);
            db.pasoDao().insert(nuevoPaso);

            // Actualizar la UI con el nuevo paso
            runOnUiThread(() -> {
                pasos.add(nuevoPaso);
                pasoIndex = pasos.size() - 1;
                mostrarPaso();
            });
        });
    }

    // Método para cargar el título de la guía
    private void cargarTituloGuia(TextView tituloGuiaTextView) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            String tituloGuia = db.guiaDao().getTituloGuiaById(guiaId); // Consulta para obtener el título
            runOnUiThread(() -> tituloGuiaTextView.setText(tituloGuia)); // Actualiza la interfaz en el hilo principal
        });
    }
}

