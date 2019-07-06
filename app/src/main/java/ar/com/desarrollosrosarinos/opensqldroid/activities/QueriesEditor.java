package ar.com.desarrollosrosarinos.opensqldroid.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import androidx.room.Room;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import ar.com.desarrollosrosarinos.opensqldroid.BuildConfig;
import ar.com.desarrollosrosarinos.opensqldroid.R;
import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Server;
import ar.com.desarrollosrosarinos.opensqldroid.db.ServerDao;
import ar.com.desarrollosrosarinos.opensqldroid.threads.QueriesRunner;
import ar.com.desarrollosrosarinos.opensqldroid.threads.QueriesRunnerInterface;

public class QueriesEditor extends Activity implements QueriesRunnerInterface {
    private Statement stmt;

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

        FloatingActionButton fab = findViewById(R.id.runQuery);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText = findViewById(R.id.queries_editor_sql);
                QueriesRunner query = new QueriesRunner();
                query.setStatement(stmt);
                query.setListener(QueriesEditor.this);
                query.execute(editText.getText().toString());
            }
        });
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
            stmt = connection.createStatement();

        }catch(Exception e){
            Log.wtf("QueriesEditor.oncreate",e);
        }
    }

    @Override
    public void onProgressUpdate(Float progress) {

    }

    @Override
    public void onQueryFinished(int code, String result) {
        WebView textView = findViewById(R.id.queryResponse);
        if (code == QueriesRunner.ERROR_CODE){
            textView.loadData(result, "text/html", "utf-8");
        }else{
            textView.loadData(result, "text/html", "utf-8");
        }
    }
}
