package com.ragshion.ayosekolah.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.adapter.DatazAdapter;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.objek.Dataz;
import com.ragshion.ayosekolah.objek.Lokasi;
import com.ragshion.ayosekolah.utilities.VerticalLineDecorator;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DatazDesaActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Dataz> movies;
    private List<Dataz> faskes = new ArrayList<>();
    DatazAdapter adapter;
    String kategori = "", data_kategori = "";
    Service api;
    String TAG = "MainActivity - ";
    //    Double latitude,longitude;
    Context context;
    Toolbar dataztoolbar;
    ImageView ic_livesearch;
    GifImageView loading;

    private MaterialEditText kecamatan, desa;
    private List<Lokasi> list_kecamatan = new ArrayList<>();
    ArrayList<String> array_kecamatan=new ArrayList<>();
    private List<Lokasi> list_desa = new ArrayList<>();
    ArrayList<String> array_desa =new ArrayList<>();
    SpinnerDialog spinnerDialog, spinnerDesa;

    CardView card_kec;



    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_desa);
        context = this;
        recyclerView = findViewById(R.id.recyclerview_list_data);
        movies = new ArrayList<>();
        loading = findViewById(R.id.loading);

        kecamatan = findViewById(R.id.kecamatan);
        desa = findViewById(R.id.desa);

        dataztoolbar = findViewById(R.id.dataztoolbar);
        setSupportActionBar(dataztoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Desa");

        ic_livesearch = findViewById(R.id.ic_livesearch);
        ic_livesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent livesearch = new Intent(DatazDesaActivity.this, LivesearchAcvitiy.class);
                startActivity(livesearch);
            }
        });

        kategori = getIntent().getStringExtra("kategori");
        data_kategori = getIntent().getStringExtra("data_kategori");

        adapter = new DatazAdapter(this, movies);
        adapter.setLoadMoreListener(new DatazAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        int index = movies.size() - 1;
                        loadMore(index);
                    }
                });
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new VerticalLineDecorator(2));
        recyclerView.setAdapter(adapter);
        api = Client.getClient();
        loading.setVisibility(View.GONE);
        loadKecamatan();
        kecamatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDialog.showSpinerDialog();
            }
        });

        desa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDesa.showSpinerDialog();
            }
        });

    }

    public void loadKecamatan(){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.loadKecamatan();

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String kecamatanString = response.body().toString();

                Type listType = new TypeToken<List<Lokasi>>() {}.getType();
                list_kecamatan = getTeamListFromJson(kecamatanString, listType);

                for (int i=0; i<list_kecamatan.size(); i++){
                    array_kecamatan.add(list_kecamatan.get(i).getName());
                }

//                spinnerDialog=new SpinnerDialog(DatazKecamatanActivity.this,array_kecamatan,"Select or Search City","Close Button Text");// With No Animation
                spinnerDialog=new SpinnerDialog(DatazDesaActivity.this,array_kecamatan,"Kecamatan",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation


                spinnerDialog.setCancellable(true); // for cancellable
                spinnerDialog.setShowKeyboard(false);// for open keyboard by default


                spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        kecamatan.setText(item);
                        array_desa.clear();
                        loadDesa(item);
                        desa.setEnabled(true);
                    }
                });

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void loadDesa(String kec){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.loadDesa(kec);

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String kecamatanString = response.body().toString();

                Type listType = new TypeToken<List<Lokasi>>() {}.getType();
                list_desa = getTeamListFromJson(kecamatanString, listType);

                for (int i=0; i<list_desa.size(); i++){
                    array_desa.add(list_desa.get(i).getName());
                }

//                spinnerDialog=new SpinnerDialog(DatazKecamatanActivity.this,array_kecamatan,"Select or Search City","Close Button Text");// With No Animation
                spinnerDesa=new SpinnerDialog(DatazDesaActivity.this,array_desa,"Desa",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation


                spinnerDesa.setCancellable(true); // for cancellable
                spinnerDesa.setShowKeyboard(false);// for open keyboard by default


                spinnerDesa.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        desa.setText(item);
                        movies.clear();
                        adapter.notifyDataChanged();
                        load(0);
                        countdata();
                    }
                });

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void countdata(){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> count = serviceApi.count_by("DESA", kecamatan.getText().toString()+";"+desa.getText().toString());
        count.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        dataztoolbar.setTitle(jsonRESULTS.getString("total"));

                        String total = desa.getText()+" : ";

                        getSupportActionBar().setTitle(total+jsonRESULTS.getString("total"));

                    }catch (Exception e){
                    }

                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void load(int index){
        Call<List<Dataz>> call = api.getDataLokasi("data_ats",kategori,kecamatan.getText().toString()+";"+desa.getText().toString(),data_kategori,index);
        call.enqueue(new Callback<List<Dataz>>() {
            @Override
            public void onResponse(Call<List<Dataz>> call, Response<List<Dataz>> response) {
                if(response.isSuccessful()){
                    loading.setVisibility(View.GONE);
                    List<Dataz> result = response.body();
                    if(result.size()<=0){
                        Toast.makeText(context,"Maaf, Saat Ini Belum ada Data pada Kategori Tersebut", Toast.LENGTH_SHORT).show();
                    }else{
                        movies.addAll(response.body());
                        adapter.notifyDataChanged();
                    }
                }else{
                    Log.e(TAG," Response Error "+ String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Dataz>> call, Throwable t) {
                Log.e(TAG,"Response Error "+t.getMessage());
                t.printStackTrace();
            }
        });
    }

    private void loadMore(int index){
        movies.add(new Dataz("load"));
        adapter.notifyItemInserted(movies.size()-1);

        Call<List<Dataz>> call = api.getDataLokasi("data_ats",kategori,kecamatan.getText().toString()+";"+desa.getText().toString(),data_kategori,index);
        call.enqueue(new Callback<List<Dataz>>() {
            @Override
            public void onResponse(Call<List<Dataz>> call, Response<List<Dataz>> response) {
                if(response.isSuccessful()){

                    //remove loading view
                    movies.remove(movies.size()-1);

                    /*if(cek_load==1){
                        movies.remove(movies.size()-1);
                        cek_load = cek_load+1;
                    }*/


                    List<Dataz> result = response.body();
                    //Collections.sort(movies,Faskes.BY_JARAK);

                    if(result.size()>1){
                        //add loaded data

                        /*if(cek_load==1){
                            movies.remove(movies.size()-1);
                            cek_load = cek_load+1;
                        }*/
                        //movies.remove(movies.size()-1);
                        movies.remove(movies.size()-1);
                        movies.addAll(result);
                    }else{//result size 0 means there is no more data available at server

                        adapter.setMoreDataAvailable(false);
                        //telling adapter to stop calling load more as no more server data available
                        //Toast.makeText(context,"Semua Data Sudah Dimuat",Toast.LENGTH_LONG).show();
                    }
                    //Collections.sort(movies,Faskes.BY_JARAK);

                    adapter.notifyDataChanged();
                    //should call the custom method adapter.notifyDataChanged here to get the correct loading status
                }else{
                    Log.e(TAG," Load More Response Error "+ String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<Dataz>> call, Throwable t) {
                Log.e(TAG," Load More Response Error "+t.getMessage());
            }
        });
    }

    public static <T> List<T> getTeamListFromJson(String jsonString, Type type) {
        if (!isValid(jsonString)) {
            return null;
        }
        return new Gson().fromJson(jsonString, type);
    }

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

}
