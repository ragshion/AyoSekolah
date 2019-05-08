package com.ragshion.ayosekolah.objek;

/**
 * Created by abdalla on 1/12/18.
 */

public class MenuData {

    private String flowerName;
    private String flowerDescription;
    private String flowerDescription2;
    private int flowerImage;

    public MenuData(String flowerName, String flowerDescription, String flowerDescription2, int flowerImage) {
        this.flowerName = flowerName;
        this.flowerDescription = flowerDescription;
        this.flowerDescription2 = flowerDescription2;
        this.flowerImage = flowerImage;
    }

    public String getFlowerDescription2() {
        return flowerDescription2;
    }

    public String getFlowerName() {
        return flowerName;
    }

    public String getFlowerDescription() {
        return flowerDescription;
    }

    public int getFlowerImage() {
        return flowerImage;
    }
}
