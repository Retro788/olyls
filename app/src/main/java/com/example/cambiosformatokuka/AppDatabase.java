package com.example.cambiosformatokuka;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.cambiosformatokuka.dao.GuiaDao;
import com.example.cambiosformatokuka.dao.ManualDao;
import com.example.cambiosformatokuka.dao.PasoDao;
import com.example.cambiosformatokuka.dao.UsuarioDao;
import com.example.cambiosformatokuka.models.Guia;
import com.example.cambiosformatokuka.models.Manual;
import com.example.cambiosformatokuka.models.Paso;
import com.example.cambiosformatokuka.models.Usuario;

@Database(entities = {Usuario.class, Guia.class, Manual.class, Paso.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase instance;

    // Métodos abstractos para acceder a los DAO
    public abstract UsuarioDao usuarioDao();
    public abstract GuiaDao guiaDao();
    public abstract ManualDao manualDao();
    public abstract PasoDao pasoDao();  // Método para acceder al DAO de 'Paso'

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "cambios_formato_kuka_db")
                            .fallbackToDestructiveMigration()  // Si cambia el esquema, se destruye y se crea de nuevo
                            .build();
                }
            }
        }
        return instance;
    }
}


