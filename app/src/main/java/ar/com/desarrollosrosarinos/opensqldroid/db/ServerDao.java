package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ServerDao {

    @Query("SELECT * FROM server ORDER BY name ASC")
    public abstract DataSource.Factory<Integer, Server> serversByName();

    @Query("SELECT * FROM server ORDER BY name")
    List<Server> getAll();

    @Query("SELECT * FROM server WHERE uid IN (:userIds)")
    List<Server> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM server WHERE uid =:uid")
    Server getByUid(int uid);

    @Insert
    void insertAll(Server... users);

    @Delete
    void delete(Server user);

    @Update
    void updateServers(Server... servers);
}
