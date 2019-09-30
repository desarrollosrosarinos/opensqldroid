package ar.com.desarrollosrosarinos.opensqldroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Calendar;

import ar.com.desarrollosrosarinos.opensqldroid.BuildConfig;
import ar.com.desarrollosrosarinos.opensqldroid.R;
import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Query;
import ar.com.desarrollosrosarinos.opensqldroid.db.Server;
import ar.com.desarrollosrosarinos.opensqldroid.db.ServerDao;
import ar.com.desarrollosrosarinos.opensqldroid.threads.QueriesLoad;
import ar.com.desarrollosrosarinos.opensqldroid.threads.QueriesRunner;
import ar.com.desarrollosrosarinos.opensqldroid.threads.QueriesRunnerInterface;
import ar.com.desarrollosrosarinos.opensqldroid.threads.QueriesSave;

public class QueriesEditor extends Activity implements QueriesRunnerInterface {
    private Statement stmt;
    int screenHeight = 0;
    private int serverUid;
    private long queryTimestamp;

    public static final String QUERY_TIMESTAMP = "QueriesEditor.QUERY_TIMESTAMP";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queries_editor);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(SqlQueriesList.SERVER_UID)){
            serverUid = bundle.getInt(SqlQueriesList.SERVER_UID);
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BuildConfig.APPLICATION_ID).build();
            ServerDao srvDao = db.serverDao();
            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    Server srv = srvDao.getByUid(serverUid);
                    connectToServer(srv);
                }
            });

            //we are editing a query
            if (bundle.containsKey(QUERY_TIMESTAMP)){
                queryTimestamp = bundle.getLong(QUERY_TIMESTAMP);
                Query qry = new Query();
                qry.timestamp = queryTimestamp;
                qry.uid = serverUid;
                QueriesLoad qryl = new QueriesLoad();
                qryl.setAppDatabase(db);
                qryl.setListener(this);
                qryl.execute(qry);
            }
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

        ImageButton btnMove = findViewById(R.id.moveLayout);
        btnMove.setOnTouchListener(onMoveTouchListener());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
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
        if (code == QueriesRunner.ERROR_CODE) {
            textView.loadData(result, "text/html", "utf-8");
        }else if (code == QueriesLoad.QUERY_LOAD_CODE){
            EditText editText = findViewById(R.id.queries_editor_sql);
            editText.setText(result);
        }else{
            textView.loadData(result, "text/html", "utf-8");
            QueriesSave qrySave = new QueriesSave();
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BuildConfig.APPLICATION_ID).build();
            qrySave.setAppDatabase(db);
            Query qry = new Query();
            qry.uid = serverUid;
            qry.timestamp = Calendar.getInstance().getTimeInMillis();
            EditText editText = findViewById(R.id.queries_editor_sql);
            qry.query = editText.getText().toString();
            qrySave.execute(qry);
        }
    }

    private View.OnTouchListener onMoveTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                LinearLayout topLayer = findViewById(R.id.qrytTopLayout);
                LinearLayout bottomLayer = findViewById(R.id.qryBottomLayout);
                final int Y = (int) event.getRawY();

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Apply the percent to bottom layer an the difference to top layer
                        int percentBottom = (Y * 100) / screenHeight;
                        int percentTop = 100 - percentBottom;

                        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                percentTop
                        );
                        LinearLayout.LayoutParams param2d = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                percentBottom
                        );
                        topLayer.setLayoutParams(param2);
                        bottomLayer.setLayoutParams(param2d);

                        return true;
                    case MotionEvent.ACTION_UP:
                        return true;
                }

                return false;
            }

        };
    }
}
