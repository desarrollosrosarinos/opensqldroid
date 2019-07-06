package ar.com.desarrollosrosarinos.opensqldroid.threads;

public interface QueriesRunnerInterface {
    public void onProgressUpdate(Float progress);
    public void onQueryFinished(int code,String result);

}
