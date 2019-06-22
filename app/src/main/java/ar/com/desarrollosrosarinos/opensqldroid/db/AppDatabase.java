package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Server.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ServerDao serverDao();
}
