package com.ragshion.ayosekolah.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.fragment_menu.AccountFragment;
import com.ragshion.ayosekolah.fragment_menu.FeedsFragment;
import com.ragshion.ayosekolah.fragment_menu.HomeFragment;
import com.ragshion.ayosekolah.utilities.SharedPrefManager;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MenuUtamaActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    SharedPrefManager sharedPrefManager;
    private boolean doubleBackToExitPressedOnce = false;
    BottomNavigationView bottomNavigationView;
    MaterialDialog materialDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utama);
        sharedPrefManager = new SharedPrefManager(this);

        reqPermitGPS();

        // kita set default nya Home Fragment
        loadFragment(new HomeFragment());
        // inisialisasi BottomNavigaionView
        bottomNavigationView = findViewById(R.id.bn_main);
        // beri listener pada saat item/menu bottomnavigation terpilih
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        pengumuman();
    }

    void pengumuman(){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> login = serviceApi.pengumuman();
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());

                        if (jsonRESULTS.getString("prioritas").equalsIgnoreCase("1")){
                            materialDialog = new MaterialDialog.Builder(MenuUtamaActivity.this)
                                    .content(jsonRESULTS.getString("pengumuman"))
                                    .title(jsonRESULTS.getString("jenis"))
                                    .positiveText("OK")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            materialDialog.dismiss();
                                        }
                                    })
                                    .show();
                        }else{
                            materialDialog = new MaterialDialog.Builder(MenuUtamaActivity.this)
                                    .content(jsonRESULTS.getString("pengumuman"))
                                    .title(jsonRESULTS.getString("jenis"))
                                    .positiveText("UPDATE")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                                            try {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                            } catch (android.content.ActivityNotFoundException anfe) {
                                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                            }

                                        }
                                    })
                                    .cancelable(false)
                                    .show();
                        }

//                        Toast.makeText(MenuUtamaActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
//                        Toast.makeText(MenuUtamaActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else{
//                    Toast.makeText(MenuUtamaActivity.this, "not found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Toast.makeText(MenuUtamaActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean reqPermitGPS(){
        String permission = Manifest.permission.ACCESS_FINE_LOCATION;
        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)){
                requestPermissions(new String[] {permission}, 101);
            }
        }
        return true;
    }

    private boolean loadFragment(Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        confirmQuit();
    }

    private void confirmQuit() {
        if (doubleBackToExitPressedOnce) {
            finish();
            return;
        }

        doubleBackToExitPressedOnce = true;
        Toast.makeText(this,"Tekan kembali sekali lagi untuk Keluar",Toast.LENGTH_SHORT).show();
        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fl_container);
        switch (menuItem.getItemId()){
            case R.id.home_menu:
//                if (!(currentFragment instanceof HomeFragment)) {
//                    fragment = HomeFragment.newInstance();
//                }
//                break;
                fragment = new HomeFragment();
                break;
            case R.id.favorite_menu:
                fragment = new FeedsFragment();
                break;
            case R.id.account_menu:
                if(!sharedPrefManager.getSPSudahLogin()){
                    Intent login = new Intent(this, LoginActivity.class);
//                    startActivity(login);
                    startActivityForResult(login, 1);
                }else{
                    fragment = new AccountFragment();
                }
                break;
        }
        return loadFragment(fragment);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            loadFragment(new HomeFragment());
            bottomNavigationView.setSelectedItemId(R.id.account_menu);
        }
    }
}

