package com.example.cambiosformatokuka;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cambiosformatokuka.dao.UsuarioDao;
import com.example.cambiosformatokuka.models.Usuario;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {
    private EditText userEditText, passwordEditText;
    private Button loginButton;
    private ImageButton togglePasswordButton;
    private UsuarioDao usuarioDao;
    private ExecutorService executorService;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEditText = findViewById(R.id.editTextUsername);
        passwordEditText = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.buttonLogin);
        togglePasswordButton = findViewById(R.id.buttonTogglePassword);

        usuarioDao = AppDatabase.getInstance(this).usuarioDao();
        executorService = Executors.newSingleThreadExecutor(); // Creamos un único hilo para operaciones en segundo plano

        insertDefaultUsers();

        // Funcionalidad de alternar visibilidad de la contraseña
        togglePasswordButton.setOnClickListener(view -> togglePasswordVisibility());

        loginButton.setOnClickListener(view -> {
            String username = userEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            authenticateUser(username, password);
        });
    }

    // Método para alternar la visibilidad de la contraseña
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Ocultar contraseña
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            togglePasswordButton.setImageResource(R.drawable.ic_visibility_off);
        } else {
            // Mostrar contraseña
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
            togglePasswordButton.setImageResource(R.drawable.ic_visibility);
        }
        // Mover el cursor al final del texto
        passwordEditText.setSelection(passwordEditText.getText().length());
        isPasswordVisible = !isPasswordVisible;
    }

    // Método para autenticar al usuario usando ExecutorService
    private void authenticateUser(String username, String password) {
        executorService.execute(() -> {
            Usuario user = usuarioDao.getUserById(username);

            runOnUiThread(() -> {
                if (user != null && user.getPasswordEncrypted().equals(password)) {
                    // Al iniciar sesión correctamente, guardar el estado de autenticación en SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("is_authenticated", true);  // Establecer como 'true' cuando el login sea exitoso
                    editor.apply();

                    // Redirigir a la actividad principal
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();  // Cierra la actividad de login
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    // Método para insertar los usuarios por defecto usando ExecutorService
    private void insertDefaultUsers() {
        executorService.execute(() -> {
            Usuario admin = usuarioDao.getUserById("admin");
            if (admin == null) {
                usuarioDao.insert(new Usuario("admin", "control.01#kuka"));
            }

            Usuario operador = usuarioDao.getUserById("operador");
            if (operador == null) {
                usuarioDao.insert(new Usuario("operador", "oper.kuka#02"));
            }
        });
    }
}


