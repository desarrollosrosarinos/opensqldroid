package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Server.class,Queries.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ServerDao serverDao();
    public abstract QueriesDao queriesDao();
}
