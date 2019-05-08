package com.ragshion.ayosekolah.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Sekolah {

    @SerializedName("npsn")
    @Expose
    private String npsn;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("bp")
    @Expose
    private String bp;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("last_sync")
    @Expose
    private String lastSync;
    @SerializedName("pd")
    @Expose
    private String pd;
    @SerializedName("rombel")
    @Expose
    private String rombel;
    @SerializedName("guru")
    @Expose
    private String guru;
    @SerializedName("pegawai")
    @Expose
    private String pegawai;
    @SerializedName("r_kelas")
    @Expose
    private String rKelas;
    @SerializedName("r_lab")
    @Expose
    private String rLab;
    @SerializedName("r_perpus")
    @Expose
    private String rPerpus;
    @SerializedName("id_kecamatan")
    @Expose
    private String idKecamatan;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_kec")
    @Expose
    private String namaKec;

    public String getNpsn() {
        return npsn;
    }

    public void setNpsn(String npsn) {
        this.npsn = npsn;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getBp() {
        return bp;
    }

    public void setBp(String bp) {
        this.bp = bp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastSync() {
        return lastSync;
    }

    public void setLastSync(String lastSync) {
        this.lastSync = lastSync;
    }

    public String getPd() {
        return pd;
    }

    public void setPd(String pd) {
        this.pd = pd;
    }

    public String getRombel() {
        return rombel;
    }

    public void setRombel(String rombel) {
        this.rombel = rombel;
    }

    public String getGuru() {
        return guru;
    }

    public void setGuru(String guru) {
        this.guru = guru;
    }

    public String getPegawai() {
        return pegawai;
    }

    public void setPegawai(String pegawai) {
        this.pegawai = pegawai;
    }

    public String getRKelas() {
        return rKelas;
    }

    public void setRKelas(String rKelas) {
        this.rKelas = rKelas;
    }

    public String getRLab() {
        return rLab;
    }

    public void setRLab(String rLab) {
        this.rLab = rLab;
    }

    public String getRPerpus() {
        return rPerpus;
    }

    public void setRPerpus(String rPerpus) {
        this.rPerpus = rPerpus;
    }

    public String getIdKecamatan() {
        return idKecamatan;
    }

    public void setIdKecamatan(String idKecamatan) {
        this.idKecamatan = idKecamatan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaKec() {
        return namaKec;
    }

    public void setNamaKec(String namaKec) {
        this.namaKec = namaKec;
    }

}
