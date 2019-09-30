package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

@Dao
public interface QueriesDao {

    @androidx.room.Query("SELECT * FROM `Query` WHERE uid = :serverUid ORDER BY timestamp")
    public abstract DataSource.Factory<Integer, Query>  loadAllByServer(int serverUid);

    @androidx.room.Query("SELECT * FROM `Query` WHERE uid = :uid AND timestamp = :timestamp")
    Query getQuery(int uid, long timestamp);

    @Insert
    void insertAll(Query... users);

    @Delete
    void delete(Query user);

    @Update
    void updateServers(Query... servers);
}
