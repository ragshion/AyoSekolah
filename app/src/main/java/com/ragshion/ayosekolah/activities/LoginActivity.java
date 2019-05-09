package com.ragshion.ayosekolah.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.dd.processbutton.FlatButton;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.api.Client;
import com.ragshion.ayosekolah.api.Service;
import com.ragshion.ayosekolah.utilities.SharedPrefManager;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.shobhitpuri.custombuttons.GoogleSignInButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

    int RC_SIGN_IN = 1;

    String TAG = "LoginActivity -/ ";
    String login_code;

    CallbackManager callbackManager;
    CardView fb_login_button;
    ImageView imageTest;

    TextView tv_daftar, tv_lupa;

    SharedPrefManager sharedPrefManager;

    FlatButton btnLogin;

    MaterialEditText username, password;
    MaterialDialog materialDialog;

    String email, url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        sharedPrefManager = new SharedPrefManager(this);

        tv_daftar = findViewById(R.id.tv_daftar);
        tv_lupa = findViewById(R.id.tv_lupa);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        tv_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent daftar = new Intent(LoginActivity.this, DaftarActivity.class);
                startActivity(daftar);
            }
        });

        tv_lupa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().equals("") | password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Username/ Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
                }else{
                    materialDialog = new MaterialDialog.Builder(LoginActivity.this)
                            .content("Sedang Memuat...")
                            .progress(true, 0)
                            .cancelable(false)
                            .progressIndeterminateStyle(true)
                            .show();
                    login();
                }

            }
        });


        imageTest = findViewById(R.id.imageTest);

    //FACEBOOK LOGIN
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("Success", "Login");
                        login_code = "1";

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        // Application code

                                        try{
                                            String email = object.getString("email");
//                                            String url = "https://graph.facebook.com/"+object.getString("id")+"/picture?type=large";

//                                            Glide
//                                                    .with(LoginActivity.this)
//                                                    .load(url)
//                                                    .into(imageTest);
//
                                            cek_login_oauth(email, url, "fb_mail");
                                        }catch (JSONException e){

                                        }

                                    }
                                });
                            Bundle parameters = new Bundle();
//                            parameters.putString("fields", "id,name,email,gender,birthday");
                            parameters.putString("fields", "id,name,email");
                            request.setParameters(parameters);
                            request.executeAsync();

//                        Toast.makeText(LoginActivity.this, "selamat datang", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(LoginActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });


        fb_login_button = findViewById(R.id.card_facebook);

        fb_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("email"));
            }
        });
    //END OF FACEBOOK LOGIN


    /// GOOGLE LOGIN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        CardView signInButton = findViewById(R.id.card_google);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /// END OF GOOGLE LOGIN

    @Override
    protected void onStart() {
        super.onStart();
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void signIn() {
        login_code = "2";
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void login(){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> login = serviceApi.login(username.getText().toString(), password.getText().toString());
        login.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                materialDialog.dismiss();
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());

                        if (jsonRESULTS.getString("status").equals("berhasil")){
                            
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, jsonRESULTS.getString("nik"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, jsonRESULTS.getString("nama_lengkap"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, jsonRESULTS.getString("email"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_FOTO_URL, jsonRESULTS.getString("foto_url"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NO_HP, jsonRESULTS.getString("no_hp"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_USER, jsonRESULTS.getString("username"));

                            finish();

                        }else{
                            Toast.makeText(LoginActivity.this, jsonRESULTS.getString("respon"), Toast.LENGTH_SHORT).show();
                        }


                    }catch (Exception e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }else{
                    Toast.makeText(LoginActivity.this, "not found", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                materialDialog.dismiss();
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            callbackManager.onActivityResult(requestCode, resultCode, data);

            if (requestCode == RC_SIGN_IN) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account){
        if (account != null){
            email = account.getEmail();
//            String url = account.getPhotoUrl().toString();
            cek_login_oauth(email, url, "g_mail");
        }
    }

    private void cek_login_oauth(final String email, final String url, String kategori){
        Service serviceApi = Client.getClient();
        Call<ResponseBody> cekLogin = serviceApi.cek_login_oauth(email, kategori);
        cekLogin.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{
                        JSONObject jsonRESULTS = new JSONObject(response.body().string());
                        if (jsonRESULTS.getString("status").equals("berhasil")){
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, jsonRESULTS.getString("nik"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_OAUTH, "g");
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NIK, jsonRESULTS.getString("nik"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NAMA, jsonRESULTS.getString("nama_lengkap"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_EMAIL, jsonRESULTS.getString("email"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_FOTO_URL, jsonRESULTS.getString("foto_url"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_NO_HP, jsonRESULTS.getString("no_hp"));
                            sharedPrefManager.saveSPString(SharedPrefManager.SP_USER, jsonRESULTS.getString("username"));
                            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_SUDAH_LOGIN, true);
                            finish();
                        }else{
                            Intent intent = new Intent(LoginActivity.this, DaftarActivity.class);
                            intent.putExtra("url", url);
                            intent.putExtra("email", email);
                            startActivity(intent);
                            Toast.makeText(LoginActivity.this, "Akun anda belum terdaftar, silahkan daftar terlebih dahulu", Toast.LENGTH_SHORT).show();

                        }
                    }catch (Exception e){
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    
                }else{
                    Toast.makeText(LoginActivity.this, response.body().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(LoginActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });
    }

    private void signOut() {

        LoginManager.getInstance().logOut();

        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

}
