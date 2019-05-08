package com.ragshion.ayosekolah.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dd.processbutton.FlatButton;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.utilities.GPSTracker;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    TextView dt_nik, dt_nama, dt_jk, dt_tanggal, dt_usia, dt_kategori, dt_alasan, dt_alamat, dt_pddk, dt_sekolah, dt_perkawinan, dt_pekerjaan, dt_sumber_data, dt_status;
    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;

    private RecyclerView mRecyclerView;
//    private ArrayList<Komentar> mArrayList;
//    private KomentarAdapter mAdapter;

    ImageView mainBackdrop;

    FloatingActionButton btnTambah;
    FlatButton btnNavigasi;

    String latlng;

    ImageView imageTelepon;

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        imageTelepon = findViewById(R.id.imageTelepon);
        collapsingToolbarLayout = findViewById(R.id.maincollapsing);

        toolbar = findViewById(R.id.maintoolbar);
//        toolbar.setTitle(getIntent().getStringExtra("nama_opd"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        dt_nik = findViewById(R.id.dt_nik);
        dt_nama = findViewById(R.id.dt_nama);
        dt_jk = findViewById(R.id.dt_jk);
        dt_tanggal = findViewById(R.id.dt_tanggal);
        dt_usia = findViewById(R.id.dt_usia);
        dt_kategori = findViewById(R.id.dt_kategori);
        dt_alasan = findViewById(R.id.dt_alasan);
        dt_alamat = findViewById(R.id.dt_alamat);
        dt_pddk = findViewById(R.id.dt_pddk);
        dt_sekolah = findViewById(R.id.dt_sekolah);
        dt_perkawinan = findViewById(R.id.dt_perkawinan);
        dt_pekerjaan = findViewById(R.id.dt_pekerjaan);
        dt_sumber_data = findViewById(R.id.dt_sumber_data);
        dt_status = findViewById(R.id.dt_status);

        mainBackdrop = findViewById(R.id.mainbackdrop);

        String id = getIntent().getStringExtra("id");

        loadDetail(id);
//        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

//
//        if(getIntent().hasExtra("status")){
//            loadDetail(gps.getLatitude(), gps.getLongitude(), getIntent().getStringExtra("id"));
//        }else{
//            alamat.setText(getIntent().getStringExtra("alamat"));
//            no_telp.setText(getIntent().getStringExtra("no_tlp"));
//            email.setText(getIntent().getStringExtra("email"));
//            website.setText(getIntent().getStringExtra("website"));
//            hari.setText(getIntent().getStringExtra("hari"));
//            jam.setText(getIntent().getStringExtra("jam"));
//            keterangan.setText(getIntent().getStringExtra("keterangan"));
//            jarak.setText(getIntent().getStringExtra("jarak"));
//
//            latitude = getIntent().getStringExtra("latitude");
//            longitude = getIntent().getStringExtra("longitude");
//
//            Glide.with(this)
//                    .load(getIntent().getStringExtra("foto"))
//                    .thumbnail(Glide.with(this).load(R.drawable.ic_image_black_24dp))
//                    .into(mainBackdrop);
//        }

//        mRecyclerView = findViewById(R.id.komentar_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(layoutManager);

//        lihat_semua = findViewById(R.id.tv_lihat_semua);
//        lihat_semua.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent semuakomen = new Intent(DetailActivity.this, KomentarActivity.class);
//                semuakomen.putExtra("id",getIntent().getStringExtra("id"));
//                startActivity(semuakomen);
//            }
//        });

//        loadJson(id_opd);

//        btnTambah = findViewById(R.id.fabKomen);
//        btnTambah.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent tambah = new Intent(DetailActivity.this, TambahKomentarActivity.class);
//                tambah.putExtra("id",getIntent().getStringExtra("id"));
//                startActivity(tambah);
//            }
//        });

        btnNavigasi = findViewById(R.id.btnNavigasi);
        btnNavigasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+latlng);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                try {
                    v.getContext().startActivity(mapIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DetailActivity.this, "Smartphone anda tidak terdapat aplikasi google maps", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        imageTelepon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String nomor = "tel:"+no_telp.getText().toString().trim();
//                Intent manggilIntent = new Intent(Intent.ACTION_CALL);
//                manggilIntent.setData(Uri.parse(nomor));
//                if(ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
//                    return;
//                }
//                v.getContext().startActivity(manggilIntent);
//            }
//        });
//
//        website.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://"+website.getText().toString()));
//                startActivity(browserIntent);
//            }
//        });

    }

    void loadDetail(String id){
        Service serviceAPI = Client.getClient();
        serviceAPI.detail_ats(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try{
                    JSONObject jsonRESULTS = new JSONObject(response.body().string());
                    collapsingToolbarLayout.setTitle(jsonRESULTS.getString("nama_lgkp"));

                    dt_nik.setText(jsonRESULTS.getString("nik"));
                    dt_nama.setText(jsonRESULTS.getString("nama_lgkp"));
                    dt_jk.setText(jsonRESULTS.getString("jk"));
                    dt_tanggal.setText(jsonRESULTS.getString("tgl_lahir"));
                    dt_usia.setText(jsonRESULTS.getString("usia"));
                    dt_kategori.setText(jsonRESULTS.getString("kategori"));
                    dt_alasan.setText(jsonRESULTS.getString("alasan"));
                    dt_alamat.setText(jsonRESULTS.getString("alamat"));
                    dt_pddk.setText(jsonRESULTS.getString("pendidikan_akhir"));
                    dt_sekolah.setText(jsonRESULTS.getString("nama_sekolah"));
                    dt_perkawinan.setText(jsonRESULTS.getString("status_perkawinan"));
                    dt_pekerjaan.setText(jsonRESULTS.getString("pekerjaan"));
                    dt_sumber_data.setText(jsonRESULTS.getString("user"));
                    dt_status.setText(jsonRESULTS.getString("verifikasi"));
                    latlng = jsonRESULTS.getString("latlng");

                    if (jsonRESULTS.getString("foto").equalsIgnoreCase("default")){
                        mainBackdrop.setImageResource(R.drawable.ic_image_black_24dp);
                    }else{
                        Glide.with(DetailActivity.this)
                                .load(getResources().getString(R.string.pathfoto)+jsonRESULTS.getString("foto"))
                                .thumbnail(Glide.with(DetailActivity.this).load(R.drawable.loading_elips))
                                .into(mainBackdrop);
                    }

                    if (latlng.equalsIgnoreCase("0,0")){
                        btnNavigasi.setVisibility(View.GONE);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                    Log.v("ErrorGetData",e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                String error = "Anda Tidak Terhubung Ke Internet, Silahkan Periksa Koneksi Anda";
                Toast.makeText(DetailActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

//
//    private void loadJson(String id_opd){
//        Service serviceAPI = Client.getClient();
//        Call<JsonArray> loadNomor = serviceAPI.getKomenLimit(id_opd);
//        loadNomor.enqueue(new Callback<JsonArray>() {
//            @Override
//            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                String faskesString = response.body().toString();
//
//                Type listType = new TypeToken<ArrayList<Komentar>>() {}.getType();
//                mArrayList = getTeamListFromJson(faskesString, listType);
//
//                mAdapter = new KomentarAdapter(mArrayList);
//                mRecyclerView.setAdapter(mAdapter);
//            }
//
//            @Override
//            public void onFailure(Call<JsonArray> call, Throwable t) {
//
//            }
//        });
//    }

    public static <T> ArrayList<T> getTeamListFromJson(String jsonString, Type type) {
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
