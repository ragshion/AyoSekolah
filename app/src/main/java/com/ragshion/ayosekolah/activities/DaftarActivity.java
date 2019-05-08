package com.ragshion.ayosekolah.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.objek.Lokasi;
import com.ragshion.ayosekolah.utilities.ProgressGenerator;
import com.ragshion.ayosekolah.utilities.SharedPrefManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DaftarActivity extends AppCompatActivity implements ProgressGenerator.OnCompleteListener {

    FlatButton btnDaftar;

    MaterialSpinner kategori, kecamatan, desa;
    MaterialEditText nik, nama, tgl_lahir, no_hp, instansi, alamat, jabatan, email, username, password, konfirm_password;
    TextView textkecamatan, textdesa;

    MaterialDialog materialDialog;

    CardView cardNik;

    String foto_url = "default", oauth = "";

    private List<Lokasi> list_kecamatan = new ArrayList<>();
    private List<Lokasi> list_desa= new ArrayList<>();

    ArrayList<String> array_kecamatan=new ArrayList<>();
    ArrayList<String> array_desa=new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_daftar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        nik = findViewById(R.id.tv_nik);
        nama = findViewById(R.id.tv_nama);
        nama.setEnabled(false);
        tgl_lahir = findViewById(R.id.tv_tanggal_lahir);
        tgl_lahir.setEnabled(false);
        no_hp = findViewById(R.id.tv_nomor);
        instansi = findViewById(R.id.tv_instansi);
        alamat = findViewById(R.id.tv_alamat);
        jabatan = findViewById(R.id.tv_jabatan);
        email = findViewById(R.id.tv_email);
        username = findViewById(R.id.tv_username);
        password = findViewById(R.id.tv_password);
        konfirm_password = findViewById(R.id.tv_konfirm_password);
        textkecamatan = findViewById(R.id.text_kecamatan);
        textdesa = findViewById(R.id.text_desa);
        cardNik = findViewById(R.id.card_nik);

        kategori = findViewById(R.id.kategori);
        kecamatan = findViewById(R.id.kecamatan);
        desa = findViewById(R.id.desa);

        if (getIntent().hasExtra("email")){
            email.setText(getIntent().getStringExtra("email"));
            email.setEnabled(false);
        }

        btnDaftar = findViewById(R.id.btnDaftar);

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog.Builder(DaftarActivity.this)
                        .content("Sedang Memuat...")
                        .progress(true, 0)
                        .cancelable(false)
                        .progressIndeterminateStyle(true)
                        .show();
                daftar();
            }
        });

        cardNik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog.Builder(DaftarActivity.this)
                        .content("Sedang Memuat...")
                        .progress(true, 0)
                        .cancelable(false)
                        .progressIndeterminateStyle(true)
                        .show();

                if (nik.getText().equals("")){

                }else{
                    cek_nik(nik.getText().toString());
                }
            }
        });

        kategori.setItems("Pilih Kategori. . .", "Desa", "Kecamatan", "Pemkab", "Instansi Vertikal", "Swasta", "Masyarakat", "CSR");
        kategori.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                LinearLayout layout_gone = findViewById(R.id.layout_gone);

                if (item.equals("Desa")){
                    if (layout_gone.getVisibility() == View.VISIBLE){

                    }else{
//                        layout_gone.setVisibility(View.VISIBLE);

                    }

                    Toast.makeText(DaftarActivity.this, "Mohon Maaf, Untuk saat ini sistem hanya dapat melayani pendaftaran Masyarakat", Toast.LENGTH_SHORT).show();

//                    loadKecamatan();
//
//                    instansi.setVisibility(View.GONE);
//                    textdesa.setVisibility(View.VISIBLE);
//                    textkecamatan.setVisibility(View.VISIBLE);
//                    desa.setVisibility(View.VISIBLE);
//                    kecamatan.setVisibility(View.VISIBLE);

                }else if (item.equals("Kecamatan")){
                    if (layout_gone.getVisibility() == View.VISIBLE){

                    }else{
//                        layout_gone.setVisibility(View.VISIBLE);

                    }

                    Toast.makeText(DaftarActivity.this, "Mohon Maaf, Untuk saat ini sistem hanya dapat melayani pendaftaran Masyarakat", Toast.LENGTH_SHORT).show();


//                    loadKecamatan();
//
//                    instansi.setVisibility(View.GONE);
//                    desa.setVisibility(View.GONE);
//                    textdesa.setVisibility(View.GONE);
//                    kecamatan.setVisibility(View.VISIBLE);
//                    textkecamatan.setVisibility(View.VISIBLE);

                }else if (item.equals("Pemkab")){
                    if (layout_gone.getVisibility() == View.VISIBLE){

                    }else{
//                        layout_gone.setVisibility(View.VISIBLE);

                    }

                    Toast.makeText(DaftarActivity.this, "Mohon Maaf, Untuk saat ini sistem hanya dapat melayani pendaftaran Masyarakat", Toast.LENGTH_SHORT).show();


//                    desa.setVisibility(View.GONE);
//                    kecamatan.setVisibility(View.GONE);
//                    instansi.setVisibility(View.VISIBLE);
//                    textdesa.setVisibility(View.GONE);
//                    textkecamatan.setVisibility(View.GONE);

                }else if (item.equals("Instansi Vertikal")){
                    if (layout_gone.getVisibility() == View.VISIBLE){

                    }else{
//                        layout_gone.setVisibility(View.VISIBLE);

                    }

                    Toast.makeText(DaftarActivity.this, "Mohon Maaf, Untuk saat ini sistem hanya dapat melayani pendaftaran Masyarakat", Toast.LENGTH_SHORT).show();


//                    desa.setVisibility(View.GONE);
//                    kecamatan.setVisibility(View.GONE);
//                    instansi.setVisibility(View.VISIBLE);
//                    textdesa.setVisibility(View.GONE);
//                    textkecamatan.setVisibility(View.GONE);

                }else if (item.equals("Swasta")){
                    if (layout_gone.getVisibility() == View.VISIBLE){

                    }else{
//                        layout_gone.setVisibility(View.VISIBLE);

                    }

                    Toast.makeText(DaftarActivity.this, "Mohon Maaf, Untuk saat ini sistem hanya dapat melayani pendaftaran Masyarakat", Toast.LENGTH_SHORT).show();


//                    desa.setVisibility(View.GONE);
//                    kecamatan.setVisibility(View.GONE);
//                    instansi.setVisibility(View.VISIBLE);
//                    textdesa.setVisibility(View.GONE);
//                    textkecamatan.setVisibility(View.GONE);

                }else if (item.equals("Masyarakat")){
                    if (layout_gone.getVisibility() == View.VISIBLE){

                    }else{
                        layout_gone.setVisibility(View.VISIBLE);

                    }


                    desa.setVisibility(View.GONE);
                    kecamatan.setVisibility(View.GONE);
                    instansi.setVisibility(View.GONE);
                    jabatan.setVisibility(View.GONE);
                    textdesa.setVisibility(View.GONE);
                    textkecamatan.setVisibility(View.GONE);

                }else if (item.equals("CSR")){
                    if (layout_gone.getVisibility() == View.VISIBLE){

                    }else{
//                        layout_gone.setVisibility(View.VISIBLE);

                    }

                    Toast.makeText(DaftarActivity.this, "Mohon Maaf, Untuk saat ini sistem hanya dapat melayani pendaftaran Masyarakat", Toast.LENGTH_SHORT).show();


//                    desa.setVisibility(View.GONE);
//                    kecamatan.setVisibility(View.GONE);
//                    instansi.setVisibility(View.VISIBLE);
//                    textdesa.setVisibility(View.GONE);
//                    textkecamatan.setVisibility(View.GONE);

                }else{

                }
            }
        });

        kecamatan.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (kategori.getText().toString().equals("Desa")){
                    loadDesa(kategori.getText().toString());
                }
            }
        });


    }

    public void daftar(){
        if(nik.getText().toString().equals("") |
                nama.getText().toString().equals("") |
                no_hp.getText().toString().equals("") |
                alamat.getText().toString().equals("") |
                email.getText().toString().equals("") |
                username.getText().toString().equals("") |
                password.getText().toString().equals("") |
                konfirm_password.getText().equals("") |
                !password.getText().toString().equals(konfirm_password.getText().toString())
        ){
            materialDialog.dismiss();
            if (!password.getText().toString().equals(konfirm_password.getText().toString())){
                Toast.makeText(this, "Password dan Konfirmasi Password Tidak Sama", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Harap Lengkapi Terlebih Dahulu Semua Field", Toast.LENGTH_SHORT).show();
            }
        }else{
            Service serviceApi = Client.getClient();
            Call<ResponseBody> daftar = serviceApi.daftar(nik.getText().toString(),
                                            nama.getText().toString(),
                                            tgl_lahir.getText().toString(),
                                            alamat.getText().toString(),
                                            jabatan.getText().toString(),
                                            email.getText().toString(),
                                            kategori.getText().toString(),
                                            instansi.getText().toString(),
                                            no_hp.getText().toString(),
                                            kecamatan.getText().toString(),
                                            desa.getText().toString(),
                                            foto_url,
                                            username.getText().toString(),
                                            password.getText().toString(),
                                            oauth
                    );
            daftar.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    materialDialog.dismiss();
                    if(response.isSuccessful()){
                        try{
                            JSONObject jsonRESULTS = new JSONObject(response.body().string());

                            Toast.makeText(DaftarActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_LONG).show();
                            finish();

                        }catch (Exception e){
                            Toast.makeText(DaftarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }else{
                        Toast.makeText(DaftarActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    materialDialog.dismiss();
                    Toast.makeText(DaftarActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    t.printStackTrace();
                }
            });
        }

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

                array_kecamatan.add("Pilih Kecamatan...");
                for (int i=0; i<list_kecamatan.size(); i++){
                    array_kecamatan.add(list_kecamatan.get(i).getName());
                }

                kecamatan.setItems(array_kecamatan);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void loadDesa(String nama_kecamatan){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.loadDesa(nama_kecamatan);

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String desaString = response.body().toString();

                Type listType = new TypeToken<List<Lokasi>>() {}.getType();
                list_desa = getTeamListFromJson(desaString, listType);

                array_desa.add("Pilih Desa...");
                for (int i=0; i<list_desa.size(); i++){
                    array_desa.add(list_desa.get(i).getName());
                }

                desa.setItems(array_desa);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
    }

    public void cek_nik(String nik){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> cekNik = serviceApi.cek_nik(nik, "user");
        cekNik.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                materialDialog.dismiss();
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("verifikasi").equals("berhasil")){
                            nama.setText(jsonRESULTS.getJSONObject("respon").getString("NAMA_LGKP"));
                            tgl_lahir.setText(jsonRESULTS.getJSONObject("respon").getString("tgl_lahir"));

                        }else{
                            Toast.makeText(DaftarActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(DaftarActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(DaftarActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


    @Override
    public void onComplete() {
        Toast.makeText(this, "Selesai", Toast.LENGTH_LONG).show();
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
