package com.ragshion.ayosekolah.utilities;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String SP_MAHASISWA_APP = "spMahasiswaApp";

    public static final String SP_NAMA = "spNama";
    public static final String SP_USER = "SP_USER";
    public static final String SP_ID = "spID";
    public static final String SP_IMAGE = "spImage";
    public static final String SP_EMAIL = "spEmail";
    public static final String SP_STATUS = "spStatus";
    public static final String SP_HISTORI = "spHistori";
    public static final String SP_SUDAH_LOGIN = "spSudahLogin";

    public static final String SP_NIK = "spNIK";
    public static final String SP_KATEGORI_LOGIN = "spKatLogin";
    public static final String SP_FOTO_URL = "SP_FOTO_URL";
    public static final String SP_NO_HP = "SP_NO_HP";

    public static final String SP_LATITUDE = "X";
    public static final String SP_LONGITUDE = "X";
    public static final String SP_OAUTH = "x";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_MAHASISWA_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public String getSpImage(){ return sp.getString(SP_IMAGE, "");}

    public String getSPNama(){
        return sp.getString(SP_NAMA, "");
    }

    public String getSpOauth(){
        return sp.getString(SP_OAUTH, "");
    }

    public String getSpLatitude(){
        return sp.getString(SP_LATITUDE, "");
    }

    public String getSpLongitude(){
        return sp.getString(SP_LONGITUDE, "");
    }

    public String getSPNik(){
        return sp.getString(SP_NIK, "");
    }

    public String getSpFotoUrl(){
        return sp.getString(SP_FOTO_URL, "");
    }

    public String getSpNoHp(){
        return sp.getString(SP_NO_HP, "");
    }

    public String getSpKategoriLogin(){
        return sp.getString(SP_KATEGORI_LOGIN, "");
    }

    public String getSPID(){
        return sp.getString(SP_ID, "");
    }

    public String getSpHistori(){
        return sp.getString(SP_HISTORI, "");
    }

    public String getSpStatus(){return sp.getString(SP_STATUS,"");}

    public String getSpUser(){return sp.getString(SP_USER,"");}

    public String getSPEmail(){
        return sp.getString(SP_EMAIL, "");
    }

    public Boolean getSPSudahLogin(){
        return sp.getBoolean(SP_SUDAH_LOGIN, false);
    }




}
