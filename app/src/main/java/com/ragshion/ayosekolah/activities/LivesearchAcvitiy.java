package com.ragshion.ayosekolah.activities;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.adapter.LivesearchAdapter;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.objek.Dataz;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivesearchAcvitiy extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Dataz> contacts;
    private LivesearchAdapter adapter;
    private Service api;

    GifImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livesearch);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loading = findViewById(R.id.loading);
        loading.setVisibility(View.GONE);
        loading.bringToFront();

        recyclerView = findViewById(R.id.recyclerview_livesearch);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
//        fetchContact("dummy_ats", "");

    }

    public void fetchContact(String type, String key){
        loading.setVisibility(View.VISIBLE);

        api = Client.getClient();

//        apiInterface = Client.getClient().create(ApiInterface.class);

        Call<List<Dataz>> call = api.getLivesearch(type, key);
        call.enqueue(new Callback<List<Dataz>>() {
            @Override
            public void onResponse(Call<List<Dataz>> call, Response<List<Dataz>> response) {
                loading.setVisibility(View.GONE);
                contacts = response.body();
                adapter = new LivesearchAdapter(contacts, LivesearchAcvitiy.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Dataz>> call, Throwable t) {
                loading.setVisibility(View.GONE);
                Toast.makeText(LivesearchAcvitiy.this, "Error\n"+t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_atas, menu);

        getMenuInflater().inflate(R.menu.menu_atas, menu);
        MenuItem cari = menu.findItem(R.id.btnSearch);
        cari.setVisible(false);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) search.getActionView();

        search.expandActionView();
        search.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return false;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                View view = LivesearchAcvitiy.this.getCurrentFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(LivesearchAcvitiy.this.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                finish();
                return true;
            }
        });

//        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                fetchContact("data_ats", query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                fetchContact("data_ats", newText);
                return false;
            }
        });


        return true;
    }

}
