package ar.com.desarrollosrosarinos.opensqldroid.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ar.com.desarrollosrosarinos.opensqldroid.BuildConfig;
import ar.com.desarrollosrosarinos.opensqldroid.R;
import ar.com.desarrollosrosarinos.opensqldroid.db.AppDatabase;
import ar.com.desarrollosrosarinos.opensqldroid.db.Queries;
import ar.com.desarrollosrosarinos.opensqldroid.db.QueriesDao;

public class SqlQueriesList extends AppCompatActivity {
    private int serverUid;
    public static final String SERVER_UID = "SqlQueriesList.ServerUid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.queries_list);

        Bundle bundle = this.getIntent().getExtras();

        if (bundle != null && bundle.containsKey(SERVER_UID)) {//if there is no extra something is wrong
            serverUid = bundle.getInt(SERVER_UID);
            /**
             * The list view of servers
             */
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, BuildConfig.APPLICATION_ID).build();
            MyViewModel viewModel = new MyViewModel(db.queriesDao());//ViewModelProviders.of(this).get(MyViewModel.class);
            RecyclerView recyclerView = findViewById(R.id.queries_list);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(llm);
            QueriesAdapter adapter = new QueriesAdapter();
            viewModel.serversList.observe(this, pagedList -> adapter.submitList(pagedList));
            recyclerView.setAdapter(adapter);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addQuery);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                    Intent intent = new Intent(SqlQueriesList.this,QueriesNew.class);
                    intent.putExtra(SqlQueriesList.SERVER_UID,serverUid);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * Creating the view for the servers list
     */

    class MyViewModel extends ViewModel {
        public final LiveData<PagedList<Queries>> serversList;
        public MyViewModel(QueriesDao queriesDao) {
            serversList = new LivePagedListBuilder<>(
                    queriesDao.loadAllByServer(serverUid), /* page size */ 20).build();
        }
    }

    public static DiffUtil.ItemCallback<Queries> DIFF_CALLBACK = new DiffUtil.ItemCallback<Queries>() {
        @Override
        public boolean areItemsTheSame(@NonNull Queries oldItem, @NonNull Queries newItem) {
            Log.e("areITems",""+(oldItem.uid == newItem.uid));
            return oldItem.uid == newItem.uid && oldItem.fecha.equals(newItem.fecha);
        }
        @Override
        public boolean areContentsTheSame(@NonNull Queries oldItem, @NonNull Queries newItem) {
            Log.e("areITems",""+(oldItem.uid == newItem.uid));
            return oldItem.uid == newItem.uid && oldItem.fecha.equals(newItem.fecha);
        }
    };

    public class QueriesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;
        public TextView description;

        public QueriesViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.server_list_title);
            description = itemView.findViewById(R.id.server_list_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Intent intent = new Intent(SqlQueriesList.this, SqlQueriesList.class);
            startActivity(intent);
        }
    }

    class QueriesAdapter extends PagedListAdapter<Queries, QueriesViewHolder> {
        public QueriesAdapter() {
            super(DIFF_CALLBACK);
        }

        @NonNull
        @Override
        public QueriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.server_list_item, parent, false);
            QueriesViewHolder userViewHolder = new QueriesViewHolder(view);
            return userViewHolder;
        }

        @Override
        public void onBindViewHolder(QueriesViewHolder holder, int position) {
            Queries user = getItem(position);
            holder.title.setText(user.fecha);
            holder.description.setText(user.query);
        }
    }
}
