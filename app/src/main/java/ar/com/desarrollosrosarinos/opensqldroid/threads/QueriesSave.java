package ar.com.desarrollosrosarinos.opensqldroid.threads;

import android.os.AsyncTask;

import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Query;

public class QueriesSave extends AsyncTask<Query, Float, Boolean> {
    private AppDatabase db;
    private boolean update = false;

    public void setAppDatabase(AppDatabase db){
        this.db = db;
    }

    public void setUpdate(boolean update){
        this.update = update;
    }

    @Override
    protected Boolean doInBackground(Query... queries) {
        if (update) {
            db.queriesDao().updateQuery(queries);
        }else{
            db.queriesDao().insertAll(queries);
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Float... progress)
    {

    }

    @Override
    protected void onPostExecute(Boolean result)
    {

    }
}
