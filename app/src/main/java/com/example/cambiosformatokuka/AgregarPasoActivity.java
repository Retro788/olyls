package com.example.cambiosformatokuka;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AgregarPasoActivity extends AppCompatActivity {

    private EditText descripcionEditText;
    private ImageView imageViewPaso;
    private Button agregarButton, cancelarButton, seleccionarImagenButton, tomarFotoButton;
    private Uri imageUri; // URI para la imagen
    private String imagePath; // Ruta de la imagen
    private File photoFile; // Archivo temporal para la foto

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_paso);

        descripcionEditText = findViewById(R.id.editTextDescripcion);
        imageViewPaso = findViewById(R.id.imageViewPaso);
        agregarButton = findViewById(R.id.buttonAgregarPaso);
        cancelarButton = findViewById(R.id.buttonCancelar);
        seleccionarImagenButton = findViewById(R.id.buttonSeleccionarImagen);
        tomarFotoButton = findViewById(R.id.buttonTomarFoto);

        seleccionarImagenButton.setOnClickListener(view -> abrirGaleria());
        tomarFotoButton.setOnClickListener(view -> verificarPermisos());

        agregarButton.setOnClickListener(view -> {
            String descripcion = descripcionEditText.getText().toString();
            if (descripcion.isEmpty()) {
                Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show();
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("descripcion", descripcion);
                resultIntent.putExtra("imagenRuta", imagePath); // Ruta de la imagen
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        cancelarButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void abrirGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1); // Código 1 para galería
    }

    private void abrirCamara() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = crearArchivoDeImagen();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error al crear el archivo para la foto", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.example.cambiosformatokuka.fileprovider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, 2); // Código 2 para cámara
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) { // Galería
                imageUri = data.getData();
                imagePath = guardarImagenDesdeGaleria(imageUri); // Guardar imagen en el directorio común
                imageViewPaso.setImageURI(Uri.fromFile(new File(imagePath))); // Mostrar la copia
            } else if (requestCode == 2) { // Cámara
                imageViewPaso.setImageURI(imageUri);
                imagePath = photoFile.getAbsolutePath(); // La ruta ya apunta al directorio común
                Toast.makeText(this, "Foto tomada correctamente", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private File crearArchivoDeImagen() throws IOException {
        String nombreArchivo = "imagen_paso_" + System.currentTimeMillis() + ".jpg";
        File directorio = obtenerDirectorioDeImagenes(); // Usar el directorio común
        return File.createTempFile(nombreArchivo, ".jpg", directorio);
    }

    private File obtenerDirectorioDeImagenes() {
        File directorio = new File(getFilesDir(), "imagenes_pasos");
        if (!directorio.exists()) {
            directorio.mkdirs(); // Crear la carpeta si no existe
        }
        return directorio;
    }

    private String guardarImagenDesdeGaleria(Uri uri) {
        String nombreArchivo = "imagen_paso_" + System.currentTimeMillis() + ".jpg";
        File archivoDestino = new File(obtenerDirectorioDeImagenes(), nombreArchivo);

        try (InputStream inputStream = getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(archivoDestino)) {

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            return archivoDestino.getAbsolutePath(); // Retornar la nueva ruta de la imagen

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al guardar la imagen", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private void verificarPermisos() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, 100);
            } else {
                abrirCamara();
            }
        } else {
            abrirCamara();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            abrirCamara();
        } else {
            Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
        }
    }
}

