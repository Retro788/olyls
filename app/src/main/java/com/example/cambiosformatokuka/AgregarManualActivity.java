package com.example.cambiosformatokuka;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cambiosformatokuka.models.Manual;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AgregarManualActivity extends AppCompatActivity {

    private EditText tituloEditText;
    private Button seleccionarPdfButton, agregarManualButton, cancelarButton;
    private Uri pdfUri; // URI del PDF seleccionado
    private String pdfPath; // Ruta donde se guardará el PDF internamente

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_manual);

        tituloEditText = findViewById(R.id.editTextTituloManual);
        seleccionarPdfButton = findViewById(R.id.buttonSeleccionarPdf);
        agregarManualButton = findViewById(R.id.buttonAgregarManual);
        cancelarButton = findViewById(R.id.buttonCancelar);

        // Acción para seleccionar un archivo PDF
        seleccionarPdfButton.setOnClickListener(view -> abrirSelectorDeArchivos());

        // Acción para agregar un manual
        agregarManualButton.setOnClickListener(view -> {
            String titulo = tituloEditText.getText().toString();

            if (titulo.isEmpty()) {
                Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pdfUri == null) {
                Toast.makeText(this, "Debe seleccionar un archivo PDF", Toast.LENGTH_SHORT).show();
                return;
            }

            guardarPdfEnDirectorioInterno();

            // Pasar los datos de regreso
            Intent resultIntent = new Intent();
            resultIntent.putExtra("titulo", titulo);
            resultIntent.putExtra("pdfRuta", pdfPath); // Ruta del PDF copiado
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Acción para cancelar
        cancelarButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    // Método para abrir el selector de archivos y filtrar solo PDFs
    private void abrirSelectorDeArchivos() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("application/pdf");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1); // Código 1 para seleccionar PDF
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1) {
            pdfUri = data.getData();

            if (pdfUri != null) {
                String nombreArchivo = obtenerNombreDeArchivo(pdfUri);
                Toast.makeText(this, "PDF seleccionado: " + nombreArchivo, Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Método para guardar el archivo PDF en el almacenamiento interno en una carpeta específica
    private void guardarPdfEnDirectorioInterno() {
        if (pdfUri == null) return;

        ContentResolver contentResolver = getContentResolver();
        try (InputStream inputStream = contentResolver.openInputStream(pdfUri)) {
            // Crear una carpeta específica dentro del almacenamiento interno
            File directory = new File(getFilesDir(), "manuales_pdfs"); // Carpeta específica
            if (!directory.exists()) {
                directory.mkdirs(); // Crear la carpeta si no existe
            }

            // Crear el archivo PDF dentro de esta carpeta
            File nuevoArchivo = new File(directory, "manual_" + System.currentTimeMillis() + ".pdf");
            pdfPath = nuevoArchivo.getAbsolutePath(); // Guardar la ruta del archivo

            try (FileOutputStream outputStream = new FileOutputStream(nuevoArchivo)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                // Insertar el manual en la base de datos
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    Manual nuevoManual = new Manual(tituloEditText.getText().toString(), pdfPath);
                    AppDatabase.getInstance(this).manualDao().insert(nuevoManual);

                    runOnUiThread(() -> {
                        Toast.makeText(this, "Manual agregado correctamente", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    });
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar el archivo PDF", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para obtener el nombre del archivo PDF seleccionado
    private String obtenerNombreDeArchivo(Uri uri) {
        String nombreArchivo = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME);
                    if (index != -1) {
                        nombreArchivo = cursor.getString(index);
                    }
                }
            }
        }
        if (nombreArchivo == null) {
            nombreArchivo = uri.getLastPathSegment();
        }
        return nombreArchivo;
    }
}


