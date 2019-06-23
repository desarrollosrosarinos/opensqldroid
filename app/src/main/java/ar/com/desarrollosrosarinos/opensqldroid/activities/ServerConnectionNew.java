package ar.com.desarrollosrosarinos.opensqldroid.activities;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.room.Room;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.nio.file.Files;

import ar.com.desarrollosrosarinos.opensqldroid.BuildConfig;
import ar.com.desarrollosrosarinos.opensqldroid.R;
import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Server;

public class ServerConnectionNew extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.server_connection_new);

        MaterialButton btnSave = findViewById(R.id.server_connection_btn_save);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputEditText txtServerName = findViewById(R.id.server_connection_server_name);
                TextInputEditText txtServerAddress = findViewById(R.id.server_connection_server_address);
                TextInputEditText txtServerPort = findViewById(R.id.server_connection_server_port);
                TextInputEditText txtUserName = findViewById(R.id.server_connection_user_name);
                TextInputEditText txtUserPassword = findViewById(R.id.server_connection_user_password);

                AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BuildConfig.APPLICATION_ID).build();

                Server server = new Server();
                server.name = txtServerName.getText().toString();
                server.address = txtServerAddress.getText().toString();
                server.port = Integer.parseInt(txtServerPort.getText().toString());
                server.userName = txtUserName.getText().toString();
                server.userPassword = txtUserPassword.getText().toString();

                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        db.serverDao().insertAll(server);
                    }
                });

            }

        });
    }
}
