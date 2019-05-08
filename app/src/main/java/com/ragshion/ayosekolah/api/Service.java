package com.ragshion.ayosekolah.api;

import com.google.gson.JsonArray;
import com.ragshion.ayosekolah.objek.BaseResponse;
import com.ragshion.ayosekolah.objek.Dataz;
import com.ragshion.ayosekolah.objek.Lokasi;

import java.util.List;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {


//
//    YANG DIGUNAKAN DI BACK TO SCHOOL
//    @GET("android/api_by/{table}/{kategori}/{data_kategori}")
//    Call<List<Dataz>> getData(@Path("table") String table,
//                                @Path("kategori") String kategori,
//                                @Path("data_kategori") String data_kategori,
//                                @Query("index") int index);

    @GET("android/api_by/")
    Call<List<Dataz>> getData(@Query("table") String table,
                              @Query("kategori") String kategori,
                              @Query("data_kategori") String data_kategori,
                              @Query("index") int index);

    @GET("android/api_by/")
    Call<List<Dataz>> getDataLokasi(@Query("table") String table,
                              @Query("kategori") String kategori,
                              @Query("data_kategori") String data_kategori,
                              @Query("lokasi") String lokasi,
                              @Query("index") int index);

    @GET("android/api_akun/")
    Call<List<Dataz>> getApiAkun(@Query("user") String user,
                              @Query("index") int index);


    @GET("android/livesearch")
    Call<List<Dataz>> getLivesearch(
        @Query("item_type") String type,
        @Query("key") String keyword
    );

    @GET("android/livesearch_akun")
    Call<List<Dataz>> getLivesearchAkun(
            @Query("item_type") String type,
            @Query("key") String keyword,
            @Query("user") String user
    );

    @GET("android/load_kecamatan")
    Call<JsonArray> loadKecamatan();

    @GET("android/load_pekerjaan")
    Call<JsonArray> loadPekerjaan();

    @FormUrlEncoded
    @POST("android/load_desa")
    Call<JsonArray> loadDesa(
            @Field("kecamatan") String kecamatan
    );


    @GET("android/load_sekolah")
    Call<JsonArray> loadSekolah(
            @Query("pddk_akh") String pddk_akh
    );

    @FormUrlEncoded
    @POST("android/cek_login_oauth")
    Call<ResponseBody> cek_login_oauth(
            @Field("email") String email,
            @Field("kategori") String kategori
    );


    @GET("android/count")
    Call<ResponseBody> count(
            @Query("kategori") String kategori,
            @Query("data_kategori") String data_kategori
    );

    @GET("android/count_by")
    Call<ResponseBody> count_by(
            @Query("kategori") String kategori,
            @Query("nama_lokasi") String nama_lokasi
    );

    @GET("android/count_akun")
    Call<ResponseBody> countAkun(
            @Query("user") String user
    );

    @FormUrlEncoded
    @POST("android/nik")
    Call<ResponseBody> cek_nik(
            @Field("nik") String nik,
            @Field("table") String table
    );

    @FormUrlEncoded
    @POST("android/simpan_ats")
    Call<ResponseBody> simpanats(
            @Field("nik") String nik,
            @Field("nama_lgkp") String nama_lgkp,
            @Field("tmpt_lhr") String tempat_lahir,
            @Field("tgl_lahir") String tgl_lahir,
            @Field("jk") String jk,
            @Field("kategori") String kategori,
            @Field("nama_ayah") String nama_ayah,
            @Field("nama_ibu") String nama_ibu,
            @Field("pddk_akh") String pddk_akh,
            @Field("alamat") String alamat,
            @Field("rt") String rt,
            @Field("rw") String rw,
            @Field("desa_kel") String desa_kel,
            @Field("kecamatan") String kecamatan,
            @Field("agama") String agama,
            @Field("stat_kwn") String stat_kwn,
            @Field("pekerjaan") String pekerjaan,
            @Field("pekerjaan_ayah") String pekerjaan_ayah,
            @Field("pekerjaan_ibu") String pekerjaan_ibu,
            @Field("alasan") String alasan,
            @Field("sekolah_sebelumnya") String sekolah_sebelumnya,
            @Field("user") String user,
            @Field("lat") String latitude,
            @Field("lng") String longitude,
            @Field("foto") String foto
    );

    @FormUrlEncoded
    @POST("android/cek_usulan")
    Call<ResponseBody> cek_usulan(
            @Field("user") String user
    );

    @FormUrlEncoded
    @POST("android/login")
    Call<ResponseBody> login(
            @Field("username") String username,
            @Field("password") String password
    );

    @GET("android/pengumuman")
    Call<ResponseBody> pengumuman();


    @GET("android/detail_ats/")
    Call<ResponseBody> detail_ats(
            @Query("no") String no
    );

    @FormUrlEncoded
    @POST("android/daftar")
    Call<ResponseBody> daftar(
            @Field("nik") String nik,
            @Field("nama_lgkp") String nama_lgkp,
            @Field("tgl_lhr") String tgl_lhr,
            @Field("alamat") String alamat,
            @Field("jabatan") String jabatan,
            @Field("email") String email,
            @Field("kategori") String kategori,
            @Field("instansi") String instansi,
            @Field("no_hp") String no_hp,
            @Field("kecamatan") String kecamatan,
            @Field("desa") String desa,
            @Field("foto_url") String foto_url,
            @Field("username") String username,
            @Field("password") String password,
            @Field("oauth") String oauth
    );



//


    @Multipart
    @POST(Config.API_UPLOAD)
    Call<BaseResponse> uploadPhotoMultipart(
            @Part("action") RequestBody action,
            @Part MultipartBody.Part photo);


    @GET("datafaskes/api/{kategori}/{latitude}/{longitude}/loadall")
    Call<JsonArray> readTeamArray(
            @Path("kategori") String kategori,
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );

    @GET("datafaskes/apispesialis/{latitude}/{longitude}")
    Call<JsonArray> getSpesialis(
            @Path("latitude") Double latitude,
            @Path("longitude") Double longitude
    );

//    @GET("datafaskes/apispesialis/{kategori}/{latitude}/{longitude}")
//    Call<List<Faskes>> spesialis(
//            @Path("kategori") String kategori,
//            @Path("latitude") Double latitude,
//            @Path("longitude") Double longitude
//    );
//
//    @GET("datafaskes/api/{kategori}/{latitude}/{longitude}/loadmore")
//    Call<List<Faskes>> getFaskes(@Path("kategori") String kategori,
//                                 @Path("latitude") Double latitude,
//                                 @Path("longitude") Double longitude,
//                                 @Query("index") int index);
//
//    @GET("nomorpenting/api/{kategori}")
//    Call<JsonArray> getNoPe(
//            @Path("kategori") String kategori
//    );
//



}
