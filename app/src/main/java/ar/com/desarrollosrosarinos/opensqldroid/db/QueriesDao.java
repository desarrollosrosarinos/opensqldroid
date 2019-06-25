package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface QueriesDao {

    @Query("SELECT * FROM queries WHERE uid = :serverUid ORDER BY fecha")
    public abstract DataSource.Factory<Integer, Queries>  loadAllByServer(int serverUid);

    @Query("SELECT * FROM queries WHERE uid = :uid AND fecha = :fecha")
    Queries getQuery(int uid,String fecha);

    @Insert
    void insertAll(Queries... users);

    @Delete
    void delete(Queries user);

    @Update
    void updateServers(Queries... servers);
}
