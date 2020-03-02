package com.coverteam.junkcraft.model;

public class DataTutorialSlide {

    String judultutor, fototutor, kettutor;

    public DataTutorialSlide(){}

    public DataTutorialSlide(String judultutor, String fototutor, String kettutor) {
        this.judultutor = judultutor;
        this.fototutor = fototutor;
        this.kettutor = kettutor;
    }

    public String getJudultutor() {
        return judultutor;
    }

    public void setJudultutor(String judultutor) {
        this.judultutor = judultutor;
    }

    public String getFototutor() {
        return fototutor;
    }

    public void setFototutor(String fototutor) {
        this.fototutor = fototutor;
    }

    public String getKettutor() {
        return kettutor;
    }

    public void setKettutor(String kettutor) {
        this.kettutor = kettutor;
    }
}
