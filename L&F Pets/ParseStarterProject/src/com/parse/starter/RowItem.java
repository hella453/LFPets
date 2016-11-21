package com.parse.starter;

/**
 * Created by Helena on 4/23/2015.
 */


public class RowItem {
    private String imageId;
    private String pasmina;
    private String ime;
    private String mjesto;
    private String vrijeme;

    public RowItem(String imageId, String ime, String pasmina, String mjesto, String vrijeme) {
        this.imageId = imageId;
        this.ime = ime;
        this.mjesto = mjesto;
        this.pasmina = pasmina;
        this.vrijeme = vrijeme;
    }
    public String getImageId() {
        return imageId;
    }
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
    public String getIme() {
        return ime;
    }
    public void setIme(String ime) {
        this.ime = ime;
    }
    public String getPasmina() {
        return pasmina;
    }
    public void setPasmina(String pasmina) {
        this.pasmina = pasmina;
    }
    public String getVrijeme() {
        return vrijeme;
    }
    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }
    public String getMjesto() {
        return mjesto;
    }
    public void setMjesto(String mjesto) {
        this.mjesto = mjesto;
    }
    @Override
    public String toString() {
        return ime + "\n" + pasmina + "\n" + vrijeme + "\n" + mjesto;
    }
}

