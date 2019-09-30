package ar.com.desarrollosrosarinos.opensqldroid.threads;

import android.os.AsyncTask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class QueriesRunner extends AsyncTask<String, Float, String> {
    public static final int ERROR_CODE = 1;
    public static final int OK_CODE = 2;

    private int code;

    private Statement stmt;
    private QueriesRunnerInterface listener;

    public void setStatement(Statement stmt){
        this.stmt = stmt;
    }

    public void setListener(QueriesRunnerInterface listener){
        this.listener = listener;
    }


    @Override
    protected String doInBackground(String... strings) {
        String output = "";
        try {
            ResultSet rs = stmt.executeQuery(strings[0]);
            //lets put the header
            int colCount = rs.getMetaData().getColumnCount();
            output = "<table border=1><tr>";
            for (int index = 1; index <= colCount; index++){
                output +="<td>"+rs.getMetaData().getColumnLabel(index)+"</td>";
            }
            output +="</tr>";
            while (rs.next()){
                code = OK_CODE;
                output += "<tr>";
                for (int index = 1; index <= colCount; index++){
                    output +="<td>"+rs.getString(index)+"</td>";
                }
                output += "</tr>";
            }
            output +="</table>";
        } catch (SQLException e) {
            code = ERROR_CODE;
            output = e.getMessage();
        }
        return output;
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
            listener.onQueryFinished(code, result);
        }
    }
}
