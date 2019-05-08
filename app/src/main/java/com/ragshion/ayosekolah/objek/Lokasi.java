package com.ragshion.ayosekolah.objek;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lokasi {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("regency_id")
    @Expose
    private String regencyId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("district_id")
    @Expose
    private String districtId;

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegencyId() {
        return regencyId;
    }

    public void setRegencyId(String regencyId) {
        this.regencyId = regencyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
