package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"uid", "fecha"})
public class Queries {
    @NonNull
    @ColumnInfo(name = "uid")
    public int uid;
    @NonNull
    @ColumnInfo(name = "fecha")
    public String fecha;
    @ColumnInfo(name = "query")
    public String query;
}
