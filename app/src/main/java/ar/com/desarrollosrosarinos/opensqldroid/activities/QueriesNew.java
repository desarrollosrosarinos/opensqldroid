package ar.com.desarrollosrosarinos.opensqldroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.room.Room;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import ar.com.desarrollosrosarinos.opensqldroid.BuildConfig;
import ar.com.desarrollosrosarinos.opensqldroid.R;
import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Server;
import ar.com.desarrollosrosarinos.opensqldroid.db.ServerDao;

public class QueriesNew extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queries_editor);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(SqlQueriesList.SERVER_UID)){
            int serverUid = bundle.getInt(SqlQueriesList.SERVER_UID);
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BuildConfig.APPLICATION_ID).build();
            ServerDao srvDao = db.serverDao();
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Server srv = srvDao.getByUid(serverUid);
                    connectToServer(srv);
                }
            });

        }
    }

    private void connectToServer(Server srv){
        String proveedor="org.postgresql.Driver";
        //in.close();
        //out.close();
        Connection connection=null;
        try {
            Class.forName(proveedor);
            String serverType = "postgresql";
            if (srv.type == Server.TYPE_POSTGRES){
                serverType = "postgresql";
            }
            connection = DriverManager.getConnection(
                    "jdbc:"+serverType+"://"+srv.address+":"+srv.port+"/"+srv.database
                    ,srv.userName, srv.userPassword);
            Statement stmt = connection.createStatement();

        }catch(Exception e){
            Log.wtf("QueriesNew.oncreate",e);
        }
    }
}
