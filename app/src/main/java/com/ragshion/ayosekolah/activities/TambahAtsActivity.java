package com.ragshion.ayosekolah.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.ragshion.ayosekolah.adapter.SimpleArrayListAdapter;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.objek.Lokasi;
import com.ragshion.ayosekolah.objek.Sekolah;
import com.ragshion.ayosekolah.utilities.GPSTracker;
import com.ragshion.ayosekolah.utilities.SharedPrefManager;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
//
//import com.rtchagas.pingplacepicker.PingPlacePicker;


public class TambahAtsActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
//    GoogleApiClient googleApiClient;

    MaterialDialog materialDialog;

    CardView cardNik;

    TextView nik, nama, tgl_lahir, alamat, alasan, nama_ayah, nama_ibu, jk, agama, latitude, longitude, rt, rw;

    private SearchableSpinner kecamatan, pekerjaan, stat_kawin, desa, pddk_akhir, kategori, pekerjaan_ayah, pekerjaan_ibu, sekolah;
    private SimpleArrayListAdapter mSimpleArrayListAdapter;

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

    SpinnerDialog spinnerKecamatan, spinnerDesa, spinnerPekerjaan, spinnerPddk_akh, spinnerKategori, spinnerStat, spinnerSekolah;

    private ArrayList<Image> images = new ArrayList<>();


    FlatButton btnSimpan, btnLokasi;
    ImageView iv_foto_ats;

    SharedPrefManager sharedPrefManager;

    String tempat_lahir, foto_string;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ats_tambah);

        sharedPrefManager = new SharedPrefManager(this);
        Mapbox.getInstance(this, getResources().getString(R.string.access_token));


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tambah Data ATS");

        btnSimpan = findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog.Builder(TambahAtsActivity.this)
                        .content("Sedang Menyimpan...")
                        .progress(true, 0)
                        .cancelable(false)
                        .progressIndeterminateStyle(true)
                        .show();

                iv_foto_ats.buildDrawingCache();
                Bitmap bitmap = iv_foto_ats.getDrawingCache();

                ByteArrayOutputStream stream=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
                byte[] image=stream.toByteArray();
                System.out.println("byte array:"+image);

                foto_string = Base64.encodeToString(image, 0);
                Toast.makeText(TambahAtsActivity.this, foto_string, Toast.LENGTH_SHORT).show();

                simpan_ats();
            }
        });

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
        alasan = findViewById(R.id.tv_alasan);
        nama_ayah = findViewById(R.id.tv_ayah);
        nama_ibu = findViewById(R.id.tv_ibu);

        pekerjaan = findViewById(R.id.pekerjaan);
        pekerjaan_ayah = findViewById(R.id.pekerjaanAyah);
        pekerjaan_ibu = findViewById(R.id.pekerjaanIbu);

        stat_kawin = findViewById(R.id.stat_kwn);
        array_stat.add("Kawin");
        array_stat.add("Tidak Kawin");
        mSimpleArrayListAdapter = new SimpleArrayListAdapter(TambahAtsActivity.this, array_stat);
        stat_kawin.setAdapter(mSimpleArrayListAdapter);

        kecamatan = findViewById(R.id.kecamatan);

        desa = findViewById(R.id.desa);
        sekolah = findViewById(R.id.sekolah);

        pddk_akhir = findViewById(R.id.pddk_akh);

        array_pddk_akh.add("TIDAK PERNAH SEKOLAH");
        array_pddk_akh.add("PUTUS KELAS 1 (SD/MI/SDLB)");
        array_pddk_akh.add("PUTUS KELAS 2 (SD/MI/SDLB)");
        array_pddk_akh.add("PUTUS KELAS 3 (SD/MI/SDLB)");
        array_pddk_akh.add("PUTUS KELAS 4 (SD/MI/SDLB)");
        array_pddk_akh.add("PUTUS KELAS 5 (SD/MI/SDLB)");
        array_pddk_akh.add("PUTUS KELAS 6 (SD/MI/SDLB)");
        array_pddk_akh.add("TAMAT SD/MI");
        array_pddk_akh.add("PUTUS KELAS 7 (SMP/MTS/SMPLB)");
        array_pddk_akh.add("PUTUS KELAS 8 (SMP/MTS/SMPLB)");
        array_pddk_akh.add("PUTUS KELAS 9 (SMP/MTS/SMPLB)");
        array_pddk_akh.add("TAMAT SMP/MTS/SMPLB");
        array_pddk_akh.add("PUTUS KELAS 10 (SMA/SMK/MA/SMALB)");
        array_pddk_akh.add("PUTUS KELAS 11 (SMA/SMK/MA/SMALB)");
        array_pddk_akh.add("PUTUS KELAS 12 (SMA/SMK/MA/SMALB)");
        array_pddk_akh.add("TAMAT SMA/SMK/MA/SMALB");
        array_pddk_akh.add("TIDAK MENYELESAIKAN KEJAR PAKET A");
        array_pddk_akh.add("TIDAK MENYELESAIKAN KEJAR PAKET B");
        array_pddk_akh.add("TIDAK MENYELESAIKAN KEJAR PAKET C");
        mSimpleArrayListAdapter = new SimpleArrayListAdapter(TambahAtsActivity.this, array_pddk_akh);
        pddk_akhir.setAdapter(mSimpleArrayListAdapter);
        pddk_akhir.setOnItemSelectedListener(mOnItemPendidikan);



        kategori = findViewById(R.id.kategori);
        array_kategori.add("Drop Out");
        array_kategori.add("Tidak Melanjutkan");
        array_kategori.add("Tidak Pernah Sekolah");
        mSimpleArrayListAdapter = new SimpleArrayListAdapter(TambahAtsActivity.this, array_kategori);
        kategori.setAdapter(mSimpleArrayListAdapter);


        cardNik = findViewById(R.id.card_nik);
        cardNik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog = new MaterialDialog.Builder(TambahAtsActivity.this)
                        .content("Sedang Memuat...")
                        .progress(true, 0)
                        .cancelable(false)
                        .progressIndeterminateStyle(true)
                        .show();
                cek_nik(nik.getText().toString());
            }
        });

        loadPekerjaan();
        loadKecamatan();

        btnLokasi = findViewById(R.id.btnTitikLokasi);
        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToPickerActivity();

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

    private OnItemSelectedListener mOnItemKecamatan = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {
            materialDialog = new MaterialDialog.Builder(TambahAtsActivity.this)
                    .content("Sedang Memuat...")
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(true)
                    .show();
            loadDesa(kecamatan.getSelectedItem().toString());

        }

        @Override
        public void onNothingSelected() {
            Toast.makeText(TambahAtsActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
    };

    private OnItemSelectedListener mOnItemPendidikan = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {
            materialDialog = new MaterialDialog.Builder(TambahAtsActivity.this)
                .content("Sedang Memuat...")
                .progress(true, 0)
                .cancelable(false)
                .progressIndeterminateStyle(true)
                .show();
            loadSekolah(pddk_akhir.getSelectedItem().toString());

        }

        @Override
        public void onNothingSelected() {
            Toast.makeText(TambahAtsActivity.this, "Nothing Selected", Toast.LENGTH_SHORT).show();
        }
    };

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

                mSimpleArrayListAdapter = new SimpleArrayListAdapter(TambahAtsActivity.this, array_pekerjaan);

                pekerjaan.setAdapter(mSimpleArrayListAdapter);
                pekerjaan_ayah.setAdapter(mSimpleArrayListAdapter);
                pekerjaan_ibu.setAdapter(mSimpleArrayListAdapter);

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

                mSimpleArrayListAdapter = new SimpleArrayListAdapter(TambahAtsActivity.this, array_kecamatan);

                kecamatan.setAdapter(mSimpleArrayListAdapter);
                kecamatan.setOnItemSelectedListener(mOnItemKecamatan);

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

                mSimpleArrayListAdapter = new SimpleArrayListAdapter(TambahAtsActivity.this, array_desa);
                desa.setAdapter(mSimpleArrayListAdapter);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahAtsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadSekolah(String pddk_akh){
        Service serviceAPI = Client.getClient();
        Call<JsonArray> loadLokasi = serviceAPI.loadSekolah(pddk_akh);

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

                mSimpleArrayListAdapter = new SimpleArrayListAdapter(TambahAtsActivity.this, array_sekolah);
                sekolah.setAdapter(mSimpleArrayListAdapter);

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahAtsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void simpan_ats(){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> simpanats = serviceApi.simpanats(
                nik.getText().toString(),
                nama.getText().toString(),
                tempat_lahir,
                tgl_lahir.getText().toString(),
                jk.getText().toString(),
                kategori.getSelectedItem().toString(),
                nama_ayah.getText().toString(),
                nama_ibu.getText().toString(),
                pddk_akhir.getSelectedItem().toString(),
                alamat.getText().toString(),
                rt.getText().toString(),
                rw.getText().toString(),
                desa.getSelectedItem().toString(),
                kecamatan.getSelectedItem().toString(),
                agama.getText().toString(),
                stat_kawin.getSelectedItem().toString(),
                pekerjaan.getSelectedItem().toString(),
                pekerjaan_ayah.getSelectedItem().toString(),
                pekerjaan_ibu.getSelectedItem().toString(),
                alasan.getText().toString(),
                sekolah.getSelectedItem().toString(),
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
                        Toast.makeText(TambahAtsActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_LONG).show();
                        finish();

                    }catch (Exception e){
                        Toast.makeText(TambahAtsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(TambahAtsActivity.this, "not found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahAtsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
                            tempat_lahir = jsonRESULTS.getJSONObject("respon").getString("NO_PROP")+jsonRESULTS.getJSONObject("respon").getString("NO_KAB");



                        }else{
                            Toast.makeText(TambahAtsActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toast.makeText(TambahAtsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(TambahAtsActivity.this, "gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(TambahAtsActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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

//    private void showPlacePicker() {
//        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
//        builder.setAndroidApiKey("AIzaSyC82mYNAX0K8Kw07vzjhAmJrR6q0ZggF34")
//                .setGeolocationApiKey("AIzaSyC82mYNAX0K8Kw07vzjhAmJrR6q0ZggF34");
//        try {
//            Intent placeIntent = builder.build(TambahAtsActivity.this);
//            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST);
//        }
//        catch (Exception ex) {
//            // Google Play services is not available...
//        }
//    }

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
