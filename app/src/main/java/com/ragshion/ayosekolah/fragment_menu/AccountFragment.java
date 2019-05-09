package com.ragshion.ayosekolah.fragment_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.activities.DaftarActivity;
import com.ragshion.ayosekolah.activities.DatazAkunActivity;
import com.ragshion.ayosekolah.activities.DatazAkunApdActivity;
import com.ragshion.ayosekolah.activities.LoginActivity;
import com.ragshion.ayosekolah.activities.MenuUtamaActivity;
import com.ragshion.ayosekolah.activities.TambahApdActivity;
import com.ragshion.ayosekolah.activities.TambahAtsActivity;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.utilities.SharedPrefManager;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    TextView ac_nik, ac_nama, ac_email, ac_no_hp, total_ats, total_apd;
    SharedPrefManager sharedPrefManager;
    LinearLayout editprofile;
    RoundedImageView iv_account;

    CardView cardKeluar, cardATS, cardAPD, cardTambahATS, cardTambahAPD;
    int request = 1;

    SwipeRefreshLayout swipeRefreshLayout;

    GoogleSignInClient mGoogleSignInClient;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_account, container, false);

        sharedPrefManager = new SharedPrefManager(view.getContext());





        ac_nik = view.findViewById(R.id.ac_nik);
        ac_nama = view.findViewById(R.id.ac_nama);
        ac_email = view.findViewById(R.id.ac_email);
        ac_no_hp = view.findViewById(R.id.ac_no_hp);
        editprofile = view.findViewById(R.id.edit_profile);
        iv_account = view.findViewById(R.id.iv_account);
        cardKeluar = view.findViewById(R.id.cardKeluar);
        cardATS = view.findViewById(R.id.cardATS);
        cardAPD = view.findViewById(R.id.cardAPD);
        cardTambahATS = view.findViewById(R.id.cardTambahATS);
        cardTambahAPD = view.findViewById(R.id.cardTambahAPD);
        total_ats = view.findViewById(R.id.tv_total_ats);
        total_apd = view.findViewById(R.id.tv_total_apd);


        ac_nik.setText(sharedPrefManager.getSPNik());
        ac_nama.setText(sharedPrefManager.getSPNama());
        ac_email.setText(sharedPrefManager.getSPEmail());
        ac_no_hp.setText(sharedPrefManager.getSpNoHp());

        if (!sharedPrefManager.getSpFotoUrl().equals("default")){
            Glide
                .with(view.getContext())
                .load(sharedPrefManager.getSpFotoUrl())
                .into(iv_account);
        }

        editprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Edit Profile", Toast.LENGTH_SHORT).show();
            }
        });

        cardKeluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, false);

                if (sharedPrefManager.getSpOauth().equals("g")){
                    signoutgoogle();
                }else if(sharedPrefManager.getSpOauth().equals("fb")){
                    signoutfb();
                }

                Intent intent = new Intent(v.getContext(), MenuUtamaActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });

        cardTambahATS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TambahAtsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        cardTambahAPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TambahApdActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        cardATS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatazAkunActivity.class);
                startActivity(intent);
            }
        });

        cardAPD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DatazAkunApdActivity.class);
                startActivity(intent);
            }
        });

        swipeRefreshLayout = view.findViewById(R.id.swlayout);
        swipeRefreshLayout.setColorSchemeResources(R.color.accent,R.color.primary);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cek_usulan();
            }
        });

        cek_usulan();

        return view;
    }

    void signoutgoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);
        mGoogleSignInClient.signOut();
    }

    void signoutfb(){
        LoginManager.getInstance().logOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == request){
            cek_usulan();
        }
    }

    public void cek_usulan(){
        String user = sharedPrefManager.getSpUser();
        Service serviceApi = Client.getClient();
        Call<ResponseBody> cek_usulan = serviceApi.cek_usulan(user);
        cek_usulan.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("status").equals("true")){
                            total_ats.setText(jsonRESULTS.getString("total_ats"));
                            total_apd.setText(jsonRESULTS.getString("total_apd"));
                            swipeRefreshLayout.setRefreshing(false);
                        }else{
                            Toast.makeText(getView().getContext(), jsonRESULTS.getString("respon"), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){

                    }

                }else{

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getView().getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }
}