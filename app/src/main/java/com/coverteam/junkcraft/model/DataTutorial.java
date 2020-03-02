package com.coverteam.junkcraft.model;

public class DataTutorial {
    String judul, foto, keterangan,id;

    public DataTutorial(){}

    public DataTutorial(String judul, String foto, String keterangan, String id) {
        this.judul = judul;
        this.foto = foto;
        this.keterangan = keterangan;
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
