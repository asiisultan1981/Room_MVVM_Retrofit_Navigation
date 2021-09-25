package com.asii.room_mvvm_retrofit.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Dog.class}, version = 1, exportSchema = false)
public abstract class DogDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "dog_database";
    private static volatile DogDatabase INSTANCE;
    public static final int NUM_OF_THREADS = 4;
    public static final ExecutorService databaseWriterExecutor
            = Executors.newFixedThreadPool(NUM_OF_THREADS);
    public static final RoomDatabase.Callback sRoomDatabaseCallback =
            new Callback() {

                @Override
                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                    super.onCreate(db);
                    databaseWriterExecutor.execute(() -> {
                        // invoke dao
                        DogDao dogDao = INSTANCE.dogDao();
                        dogDao.deleteALlDogs();

                    });
                }

            };

    public static DogDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (DogDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DogDatabase.class, DATABASE_NAME)
//                            .allowMainThreadQueries()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract DogDao dogDao();
}
