package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Server {
    public static final int TYPE_POSTGRES = 1,TYPE_MYSQL = 2, TYPE_MICROFOST = 3;
    @PrimaryKey
    public int uid;

    /**
     * types: 1 - postgresql,2 - mysql,3 - microsoft
     */
    @ColumnInfo(name = "type")
    public int type;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "port")
    public int port;

    @ColumnInfo(name = "database")
    public String database;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "user_password")
    public String userPassword;


}
