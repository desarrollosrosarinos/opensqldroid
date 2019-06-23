package ar.com.desarrollosrosarinos.opensqldroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import ar.com.desarrollosrosarinos.opensqldroid.activities.ServerConnectionNew;
import ar.com.desarrollosrosarinos.opensqldroid.activities.SqlQueriesList;
import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Server;
import ar.com.desarrollosrosarinos.opensqldroid.db.ServerDao;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this,ServerConnectionNew.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        /**
         * The list view of servers
         */
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BuildConfig.APPLICATION_ID).build();
        MyViewModel viewModel = new MyViewModel(db.serverDao());//ViewModelProviders.of(this).get(MyViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.server_list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(llm);
        ServerAdapter adapter = new ServerAdapter();
        viewModel.serversList.observe(this, pagedList -> adapter.submitList(pagedList));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Creating the view for the servers list
     */

    class MyViewModel extends ViewModel {
        public final LiveData<PagedList<Server>> serversList;
        public MyViewModel(ServerDao serverDao) {
            serversList = new LivePagedListBuilder<>(
                    serverDao.serversByName(), /* page size */ 20).build();
        }
    }

    public static DiffUtil.ItemCallback<Server> DIFF_CALLBACK = new DiffUtil.ItemCallback<Server>() {
        @Override
        public boolean areItemsTheSame(@NonNull Server oldItem, @NonNull Server newItem) {
            Log.e("areITems",""+(oldItem.uid == newItem.uid));
            return oldItem.uid == newItem.uid;
        }
        @Override
        public boolean areContentsTheSame(@NonNull Server oldItem, @NonNull Server newItem) {
            Log.e("areITems",""+(oldItem.uid == newItem.uid));
            return oldItem.uid == newItem.uid;
        }
    };

    public class ServerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;
        public TextView description;

        public ServerViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.server_list_title);
            description = itemView.findViewById(R.id.server_list_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent(MainActivity.this, SqlQueriesList.class);
            startActivity(intent);
        }
    }

    class ServerAdapter extends PagedListAdapter<Server, ServerViewHolder> {
        public ServerAdapter() {
            super(DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public ServerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_list_item, parent, false);
            ServerViewHolder userViewHolder = new ServerViewHolder(view);
            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(ServerViewHolder holder, int position) {
            Server user = getItem(position);
            holder.title.setText(user.name);
            holder.description.setText(user.address+":"+user.port);
        }
    }

}
