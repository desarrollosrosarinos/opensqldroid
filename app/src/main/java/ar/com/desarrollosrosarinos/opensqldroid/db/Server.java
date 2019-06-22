package ar.com.desarrollosrosarinos.opensqldroid.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Server {
    @PrimaryKey
    public int uid;

    /**
     * types: 1 - postgresql,2 - mysql,3 - microsoft
     */
    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "address")
    public String address;

    @ColumnInfo(name = "port")
    public int port;

    @ColumnInfo(name = "user_name")
    public String userName;

    @ColumnInfo(name = "user_password")
    public String userPassword;


}
