package ar.com.desarrollosrosarinos.opensqldroid.threads;

import android.os.AsyncTask;

import java.sql.ResultSet;
import java.sql.SQLException;

import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Query;


public class QueriesLoad extends AsyncTask<Query, Float, String> {
    public static final int QUERY_LOAD_CODE=3;

    private QueriesRunnerInterface listener;
    public void setListener(QueriesRunnerInterface listener){
        this.listener = listener;
    }

    private AppDatabase db;
    public void setAppDatabase(AppDatabase db){
        this.db = db;
    }

    @Override
    protected String doInBackground(Query... query) {
        String strQuery = db.queriesDao().getQuery(query[0].uid,query[0].timestamp).query;
        return strQuery;
    }

    @Override
    protected void onProgressUpdate(Float... progress)
    {
        if(listener != null)
        {
            listener.onProgressUpdate(progress[0]);
        }
    }

    @Override
    protected void onPostExecute(String result)
    {
        if(listener != null)
        {
            //we are loading a query, there is no error code
            listener.onQueryFinished(QUERY_LOAD_CODE, result);
        }
    }
}
