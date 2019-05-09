package com.ragshion.ayosekolah.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.dd.processbutton.FlatButton;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.plugins.places.picker.PlacePicker;
import com.mapbox.mapboxsdk.plugins.places.picker.model.PlacePickerOptions;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.objek.Lokasi;
import com.ragshion.ayosekolah.objek.Sekolah;
import com.ragshion.ayosekolah.utilities.GPSTracker;
import com.ragshion.ayosekolah.utilities.SharedPrefManager;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//
//import com.rtchagas.pingplacepicker.PingPlacePicker;


public class TambahApdActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
//    GoogleApiClient googleApiClient;

    MaterialDialog materialDialog;

    CardView cardNik;


    private List<Lokasi> list_kecamatan = new ArrayList<>();
    private List<Lokasi> list_desa= new ArrayList<>();
    private List<Lokasi> list_pekerjaan= new ArrayList<>();
    private List<Sekolah> list_sekolah= new ArrayList<>();

    ArrayList<String> array_kecamatan=new ArrayList<>();
    ArrayList<String> array_desa=new ArrayList<>();
    ArrayList<String> array_pekerjaan=new ArrayList<>();
    ArrayList<String> array_pddk_akh=new ArrayList<>();
    ArrayList<String> array_kategori=new ArrayList<>();
    ArrayList<String> array_stat=new ArrayList<>();
    ArrayList<String> array_sekolah=new ArrayList<>();

    SpinnerDialog spinnerKecamatan, spinnerDesa, spinnerPekerjaan, spinnerPddk_akh, spinnerKategori, spinnerStat, spinnerSekolah, spinnerAyah, spinnerIbu;

    MaterialEditText nik, nama, tgl_lahir, alamat, nama_ayah, nama_ibu, jk, agama, latitude, longitude, rt, rw;
    MaterialEditText tv_kecamatan, tv_desa, tv_pekerjaan, tv_stat, tv_pddk_akh, tv_kategori, tv_sekolah, tv_pekerjaan_ayah, tv_pekerjaanibu;

    private ArrayList<Image> images = new ArrayList<>();


    FlatButton btnSimpan, btnLokasi;
    ImageView iv_foto_ats;

    SharedPrefManager sharedPrefManager;

    String tempat_lahir, foto_string;

    String adagambar = "0";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apd_tambah);

        sharedPrefManager = new SharedPrefManager(this);
        Mapbox.getInstance(this, getResources().getString(R.string.access_token));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Data APD/ABK");

//        NEW SPINNER DIALOG

        tv_kecamatan = findViewById(R.id.tv_kecamatan);
        tv_kecamatan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerKecamatan.showSpinerDialog();
            }
        });
        loadKecamatan();

        tv_desa = findViewById(R.id.tv_desa);
        tv_desa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDesa.showSpinerDialog();
            }
        });

        tv_pekerjaan = findViewById(R.id.tv_pekerjaan);
        tv_pekerjaan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPekerjaan.showSpinerDialog();
            }
        });

        tv_pekerjaan_ayah = findViewById(R.id.tv_pekerjaan_ayah);
        tv_pekerjaanibu = findViewById(R.id.tv_pekerjaan_ibu);

        tv_pekerjaan_ayah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerAyah.showSpinerDialog();
            }
        });

        tv_pekerjaanibu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerIbu.showSpinerDialog();
            }
        });

        tv_stat = findViewById(R.id.tv_stat);
        array_stat.add("KAWIN");
        array_stat.add("BELUM KAWIN");
        spinnerStat = new SpinnerDialog(TambahApdActivity.this,array_stat,"Status Perkawinan",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation
        spinnerStat.setCancellable(true); // for cancellable
        spinnerStat.setShowKeyboard(false);// for open keyboard by default
        spinnerStat.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                tv_stat.setText(item);
            }
        });

        tv_stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerStat.showSpinerDialog();
            }
        });

        tv_pddk_akh = findViewById(R.id.tv_pddk_akh);
        array_pddk_akh.add("SD/MI/SDLB/PAKET A");
        array_pddk_akh.add("SMP/MTS/SMPLB/PAKET B");
        array_pddk_akh.add("SMA/SMK/MA/SMALB/PAKET C");
        spinnerPddk_akh = new SpinnerDialog(TambahApdActivity.this,array_pddk_akh,"Pendidikan Saat Ini",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation
        spinnerPddk_akh.setCancellable(true); // for cancellable
        spinnerPddk_akh.setShowKeyboard(false);// for open keyboard by default
        spinnerPddk_akh.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                tv_pddk_akh.setText(item);
                materialDialog = new MaterialDialog.Builder(TambahApdActivity.this)
                        .content("Sedang Memuat...")
                        .progress(true, 0)
                        .cancelable(false)
                        .progressIndeterminateStyle(true)
                        .show();
                loadSekolah(tv_pddk_akh.getText().toString());
            }
        });

        tv_pddk_akh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerPddk_akh.showSpinerDialog();
            }
        });

        tv_kategori = findViewById(R.id.tv_kategori);
        array_kategori.add("Disabilitas Fisik");
        array_kategori.add("Disabilitas Intelektual");
        array_kategori.add("Disabilitas Mental");
        array_kategori.add("Disabilitas Sensorik");
        spinnerKategori = new SpinnerDialog(TambahApdActivity.this,array_kategori,"Jenis Disabilitas",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation
        spinnerKategori.setCancellable(true); // for cancellable
        spinnerKategori.setShowKeyboard(false);// for open keyboard by default
        spinnerKategori.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                tv_kategori.setText(item);
            }
        });

        tv_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerKategori.showSpinerDialog();
            }
        });

        tv_sekolah = findViewById(R.id.tv_sekolah);
        tv_sekolah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerSekolah.showSpinerDialog();
            }
        });


//        END OF SPINNER


        iv_foto_ats = findViewById(R.id.iv_foto_ats);
        iv_foto_ats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImagePicker().start();
            }
        });

        nik = findViewById(R.id.tv_nik);
        nama = findViewById(R.id.tv_nama);
        tgl_lahir = findViewById(R.id.tv_tanggal_lahir);
        jk = findViewById(R.id.tv_jk);
        agama = findViewById(R.id.tv_agama);
        latitude = findViewById(R.id.tv_latitude);
        longitude = findViewById(R.id.tv_longitude);
        rt = findViewById(R.id.tv_rt);
        rw = findViewById(R.id.tv_rw);

        latitude.setEnabled(false);
        longitude.setEnabled(false);
        nama.setEnabled(false);
        tgl_lahir.setEnabled(false);
        jk.setEnabled(false);
        agama.setEnabled(false);

        alamat = findViewById(R.id.tv_alamat);
        nama_ayah = findViewById(R.id.tv_ayah);
        nama_ibu = findViewById(R.id.tv_ibu);

        cardNik = findViewById(R.id.card_nik);
        cardNik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog.Builder(TambahApdActivity.this)
                        .content("Sedang Memuat...")
                        .progress(true, 0)
                        .cancelable(false)
                        .progressIndeterminateStyle(true)
                        .show();
                nik.setFocusableInTouchMode(false);
                nik.setFocusable(false);
                nik.setFocusableInTouchMode(true);
                nik.setFocusable(true);
                cek_nik(nik.getText().toString());

            }
        });

        loadPekerjaan();


        btnLokasi = findViewById(R.id.btnTitikLokasi);
        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToPickerActivity();

            }
        });

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nik.getText().toString().equals("") |
                        nama.getText().toString().equals("") |
                        tgl_lahir.getText().toString().equals("") |
                        alamat.getText().toString().equals("") |
                        nama_ayah.getText().toString().equals("") |
                        nama_ibu.getText().toString().equals("") |
                        jk.getText().toString().equals("") |
                        agama.getText().toString().equals("") |
                        rt.getText().toString().equals("") |
                        rw.getText().toString().equals("") |
                        tv_kecamatan.getText().toString().equals("") |
                        tv_desa.getText().toString().equals("") |
                        tv_pekerjaan.getText().toString().equals("") |
                        tv_stat.getText().toString().equals("") |
                        tv_pddk_akh.getText().toString().equals("") |
                        tv_kategori.getText().toString().equals("") |
                        tv_sekolah.getText().toString().equals("") |
                        tv_pekerjaan_ayah.getText().toString().equals("") |
                        tv_pekerjaanibu.getText().toString().equals("")
                ){
                    Toast.makeText(TambahApdActivity.this, "Harap Isi Semua Field Terlebih Dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    materialDialog = new MaterialDialog.Builder(TambahApdActivity.this)
                            .content("Sedang Menyimpan...")
                            .progress(true, 0)
                            .cancelable(false)
                            .progressIndeterminateStyle(true)
                            .show();

                    if (adagambar.equalsIgnoreCase("0")){
                        foto_string = "0";
                    }else{
                        iv_foto_ats.buildDrawingCache();
                        Bitmap bitmap = iv_foto_ats.getDrawingCache();

                        ByteArrayOutputStream stream=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                        byte[] image=stream.toByteArray();
                        System.out.println("byte array:"+image);
                        foto_string = Base64.encodeToString(image, 0);
                    }

                    simpan_ats();
                }


            }
        });

    }

    private void goToPickerActivity() {
        GPSTracker gpsTracker = new GPSTracker(this);
        startActivityForResult(
                new PlacePicker.IntentBuilder()
                        .accessToken(getString(R.string.access_token))
                        .placeOptions(PlacePickerOptions.builder()
                                .statingCameraPosition(new CameraPosition.Builder()
                                        .target(new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude())).zoom(16).build())
                                .build())
                        .build(this), PLACE_PICKER_REQUEST);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        googleApiClient.disconnect();
    }

    void loadPekerjaan(){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.loadPekerjaan();

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String kecamatanString = response.body().toString();

                Type listType = new TypeToken<List<Lokasi>>() {}.getType();
                list_pekerjaan = getTeamListFromJson(kecamatanString, listType);

                for (int i=0; i<list_pekerjaan.size(); i++){
                    array_pekerjaan.add(list_pekerjaan.get(i).getNama());
                }

                spinnerPekerjaan = new SpinnerDialog(TambahApdActivity.this,array_pekerjaan,"Pekerjaan",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation

                spinnerPekerjaan.setCancellable(true); // for cancellable
                spinnerPekerjaan.setShowKeyboard(false);// for open keyboard by default

                spinnerPekerjaan.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        tv_pekerjaan.setText(item);
                    }
                });

                spinnerAyah = new SpinnerDialog(TambahApdActivity.this,array_pekerjaan,"Pekerjaan Ayah",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation
                spinnerAyah.setCancellable(true); // for cancellable
                spinnerAyah.setShowKeyboard(false);// for open keyboard by default
                spinnerAyah.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        tv_pekerjaan_ayah.setText(item);
                    }
                });

                spinnerIbu = new SpinnerDialog(TambahApdActivity.this,array_pekerjaan,"Pekerjaan Ibu",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation
                spinnerIbu.setCancellable(true); // for cancellable
                spinnerIbu.setShowKeyboard(false);// for open keyboard by default
                spinnerIbu.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        tv_pekerjaanibu.setText(item);
                    }
                });

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

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

                spinnerKecamatan =new SpinnerDialog(TambahApdActivity.this,array_kecamatan,"Kecamatan",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation

                spinnerKecamatan.setCancellable(true); // for cancellable
                spinnerKecamatan.setShowKeyboard(false);// for open keyboard by default

                spinnerKecamatan.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        tv_kecamatan.setText(item);
                        materialDialog = new MaterialDialog.Builder(TambahApdActivity.this)
                                .content("Sedang Memuat...")
                                .progress(true, 0)
                                .cancelable(false)
                                .progressIndeterminateStyle(true)
                                .show();
                        array_desa.clear();
                        tv_desa.setText("");
                        loadDesa(tv_kecamatan.getText().toString());
                    }
                });

//                kecamatan.setItems(array_kecamatan);

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
                materialDialog.dismiss();
                String desaString = response.body().toString();

                Type listType = new TypeToken<List<Lokasi>>() {}.getType();
                list_desa = getTeamListFromJson(desaString, listType);

                for (int i=0; i<list_desa.size(); i++){
                    array_desa.add(list_desa.get(i).getName());
                }

                spinnerDesa =new SpinnerDialog(TambahApdActivity.this,array_desa,"Desa",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation

                spinnerDesa.setCancellable(true); // for cancellable
                spinnerDesa.setShowKeyboard(false);// for open keyboard by default

                spinnerDesa.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        tv_desa.setText(item);
                    }
                });

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahApdActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadSekolah(String pddk_akh){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.loadSekolahApd(pddk_akh);

        loadLokasi.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                materialDialog.dismiss();
                String desaString = response.body().toString();

                Type listType = new TypeToken<List<Sekolah>>() {}.getType();
                list_sekolah = getTeamListFromJson(desaString, listType);

                for (int i=0; i<list_sekolah.size(); i++){
                    array_sekolah.add(list_sekolah.get(i).getNama());
                }

                spinnerSekolah = new SpinnerDialog(TambahApdActivity.this,array_sekolah,"Sekolah Sebelumnya",R.style.DialogAnimations_SmileWindow,"Tutup");// With 	Animation

                spinnerSekolah.setCancellable(true); // for cancellable
                spinnerSekolah.setShowKeyboard(false);// for open keyboard by default

                spinnerSekolah.bindOnSpinerListener(new OnSpinerItemClick() {
                    @Override
                    public void onClick(String item, int position) {
                        tv_sekolah.setText(item);
                    }
                });

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahApdActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void simpan_ats(){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> simpanats = serviceApi.simpanapd(
                nik.getText().toString(),
                nama.getText().toString(),
                tempat_lahir,
                tgl_lahir.getText().toString(),
                jk.getText().toString(),
                tv_kategori.getText().toString(),
                nama_ayah.getText().toString(),
                nama_ibu.getText().toString(),
                tv_pddk_akh.getText().toString(),
                alamat.getText().toString(),
                rt.getText().toString(),
                rw.getText().toString(),
                tv_desa.getText().toString(),
                tv_kecamatan.getText().toString(),
                agama.getText().toString(),
                tv_stat.getText().toString(),
                tv_pekerjaan.getText().toString(),
                tv_pekerjaan_ayah.getText().toString(),
                tv_pekerjaanibu.getText().toString(),
                tv_sekolah.getText().toString(),
                sharedPrefManager.getSpUser(),
                latitude.getText().toString(),
                longitude.getText().toString(),
                foto_string
        );
        simpanats.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                materialDialog.dismiss();
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());

                        Log.d("myTag", jsonRESULTS.toString());
                        Toast.makeText(TambahApdActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_LONG).show();
                        finish();

                    }catch (Exception e){
                        Toast.makeText(TambahApdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(TambahApdActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahApdActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    public void cek_nik(String nik){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> cekNik = serviceApi.cek_nik(nik, "data_ats");
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
                            jk.setText(jsonRESULTS.getJSONObject("respon").getString("JENIS_KLMIN"));
                            agama.setText(jsonRESULTS.getJSONObject("respon").getString("AGAMA"));
                            alamat.setText(jsonRESULTS.getJSONObject("respon").getString("ALAMAT"));
                            rt.setText(jsonRESULTS.getJSONObject("respon").getString("NO_RT"));
                            rw.setText(jsonRESULTS.getJSONObject("respon").getString("NO_RW"));
                            tv_kecamatan.setText(jsonRESULTS.getJSONObject("respon").getString("KEC_NAME"));
                            tv_desa.setText(jsonRESULTS.getJSONObject("respon").getString("KEL_NAME"));
                            nama_ayah.setText(jsonRESULTS.getJSONObject("respon").getString("NAMA_LGKP_AYAH"));
                            nama_ibu.setText(jsonRESULTS.getJSONObject("respon").getString("NAMA_LGKP_IBU"));
                            tv_stat.setText(jsonRESULTS.getJSONObject("respon").getString("STATUS_KAWIN"));
                            tempat_lahir = jsonRESULTS.getJSONObject("respon").getString("NO_PROP")+jsonRESULTS.getJSONObject("respon").getString("NO_KAB");








                        }else{
                            Toast.makeText(TambahApdActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(TambahApdActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(TambahApdActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahApdActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = (ArrayList<Image>) ImagePicker.getImages(data);
            adagambar = "1";
            printImages(images);
            return;
        }

        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            // Retrieve the information from the selected location's CarmenFeature
            CarmenFeature carmenFeature = PlacePicker.getPlace(data);
//            Toast.makeText(this, carmenFeature.geometry().toJson(), Toast.LENGTH_SHORT).show();
//            selectedLocationTextView.setText(carmenFeature.geometry().toJson());

            try{
                JSONObject jsonRESULTS = new JSONObject(carmenFeature.geometry().toJson());
                String[] separated = jsonRESULTS.getString("coordinates").split(",");
                StringBuilder builder;
                builder = new StringBuilder( separated[0]);
                String lat = builder.deleteCharAt(0).toString();
                builder = new StringBuilder(separated[1]);
                String lng = builder.deleteCharAt(separated[1].length() - 1).toString();

                latitude.setText(lat);
                longitude.setText(lng);

//                selectedLocationTextView.setText("Latitude : "+lat+" Longitude : "+lng);

            }catch (Exception e){

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void printImages(List<Image> images) {
        if (images == null) return;

        StringBuilder stringBuffer = new StringBuilder();
        for (int i = 0, l = images.size(); i < l; i++) {
            stringBuffer.append(images.get(i).getPath());
        }

        File imgFile = new File(stringBuffer.toString());

        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv_foto_ats.setImageBitmap(myBitmap);
        }else{
            Toast.makeText(this, imgFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        }


    }

    private ImagePicker getImagePicker() {

        ImagePicker imagePicker = ImagePicker.create(this)
                .language("in") // Set image picker language
                .theme(R.style.ImagePickerTheme)
                .single()
                .returnMode(ReturnMode.ALL) // set whether pick action or camera action should return immediate result or not. Only works in single mode for image picker
                .folderMode(true) // set folder mode (false by default)
                .includeVideo(false) // include video (false by default)
                .toolbarArrowColor(Color.RED) // set toolbar arrow up color
                .toolbarFolderTitle("Folder") // folder selection title
                .toolbarImageTitle("Tap to select") // image selection title
                .toolbarDoneButtonText("DONE"); // done button text


        return imagePicker.limit(10) // max images can be selected (99 by default)
                .showCamera(true) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .imageFullDirectory(Environment.getExternalStorageDirectory().getPath()); // can be full path
    }

}
