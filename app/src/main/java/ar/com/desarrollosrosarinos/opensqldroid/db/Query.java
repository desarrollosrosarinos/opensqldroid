package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(primaryKeys = {"uid", "timestamp"})
public class Query {
    @NonNull
    @ColumnInfo(name = "uid")
    public int uid;
    @NonNull
    @ColumnInfo(name = "timestamp")
    public long timestamp;
    @ColumnInfo(name = "query")
    public String query;
}
