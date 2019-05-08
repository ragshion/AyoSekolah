package com.ragshion.ayosekolah.fragment_menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.activities.DatazActivity;
import com.ragshion.ayosekolah.activities.LivesearchAcvitiy;
import com.ragshion.ayosekolah.adapter.DatazAdapter;
import com.ragshion.ayosekolah.adapter.FeedsAdapter;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.objek.Dataz;
import com.ragshion.ayosekolah.utilities.VerticalLineDecorator;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedsFragment extends Fragment {

    RecyclerView recyclerView;
    List<Dataz> movies;
    private List<Dataz> faskes = new ArrayList<>();
    FeedsAdapter adapter;
    String kategori = "kategori", data_kategori = "ALL";
    Service api;
    String TAG = "Feeds Activity - ";
    Context context;
    Toolbar dataztoolbar;
    ImageView ic_livesearch;
    GifImageView loading;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_list_feeds);
        movies = new ArrayList<>();
        loading = view.findViewById(R.id.loading);

        dataztoolbar = view.findViewById(R.id.dataztoolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(dataztoolbar);


        ic_livesearch = view.findViewById(R.id.ic_livesearch);
        ic_livesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent livesearch = new Intent(v.getContext(), LivesearchAcvitiy.class);
                startActivity(livesearch);
            }
        });

        adapter = new FeedsAdapter(view.getContext(), movies);
        adapter.setLoadMoreListener(new FeedsAdapter.OnLoadMoreListener() {
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
        load(0);
        countdata();

        return view;
    }



    void countdata(){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> count = serviceApi.count(kategori, data_kategori);
        count.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        dataztoolbar.setTitle(jsonRESULTS.getString("total"));
                        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Total Data ATS : "+jsonRESULTS.getString("total"));


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
        Call<List<Dataz>> call = api.getData("data_ats",kategori,data_kategori,index);
        call.enqueue(new Callback<List<Dataz>>() {
            @Override
            public void onResponse(Call<List<Dataz>> call, Response<List<Dataz>> response) {
                loading.setVisibility(View.GONE);
                if(response.isSuccessful()){
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

        Call<List<Dataz>> call = api.getData("data_ats",kategori,data_kategori,index);
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

    public static boolean isValid(String json) {
        try {
            new JsonParser().parse(json);
            return true;
        } catch (JsonSyntaxException jse) {
            return false;
        }
    }

}