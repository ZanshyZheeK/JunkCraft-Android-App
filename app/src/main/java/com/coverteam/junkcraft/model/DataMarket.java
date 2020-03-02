package com.coverteam.junkcraft.model;

public class DataMarket {
    String IklanID, IklanJudul, IklanDeskripsi, IklanBahan, IklanFoto, IklanKategori,IklanPenjual,IklanPenjualID,IklanAlamat;
    Double IklanHarga;

    public DataMarket(){}

    public DataMarket(String iklanID, String iklanJudul, String iklanDeskripsi, String iklanBahan, String iklanFoto, String iklanKategori, String iklanPenjual, String iklanPenjualID, String iklanAlamat, Double iklanHarga) {
        IklanID = iklanID;
        IklanJudul = iklanJudul;
        IklanDeskripsi = iklanDeskripsi;
        IklanBahan = iklanBahan;
        IklanFoto = iklanFoto;
        IklanKategori = iklanKategori;
        IklanPenjual = iklanPenjual;
        IklanPenjualID = iklanPenjualID;
        IklanAlamat = iklanAlamat;
        IklanHarga = iklanHarga;
    }

    public String getIklanID() {
        return IklanID;
    }

    public void setIklanID(String iklanID) {
        IklanID = iklanID;
    }

    public String getIklanJudul() {
        return IklanJudul;
    }

    public void setIklanJudul(String iklanJudul) {
        IklanJudul = iklanJudul;
    }

    public String getIklanDeskripsi() {
        return IklanDeskripsi;
    }

    public void setIklanDeskripsi(String iklanDeskripsi) {
        IklanDeskripsi = iklanDeskripsi;
    }

    public String getIklanBahan() {
        return IklanBahan;
    }

    public void setIklanBahan(String iklanBahan) {
        IklanBahan = iklanBahan;
    }

    public String getIklanFoto() {
        return IklanFoto;
    }

    public void setIklanFoto(String iklanFoto) {
        IklanFoto = iklanFoto;
    }

    public String getIklanKategori() {
        return IklanKategori;
    }

    public void setIklanKategori(String iklanKategori) {
        IklanKategori = iklanKategori;
    }

    public String getIklanPenjual() {
        return IklanPenjual;
    }

    public void setIklanPenjual(String iklanPenjual) {
        IklanPenjual = iklanPenjual;
    }

    public String getIklanPenjualID() {
        return IklanPenjualID;
    }

    public void setIklanPenjualID(String iklanPenjualID) {
        IklanPenjualID = iklanPenjualID;
    }

    public String getIklanAlamat() {
        return IklanAlamat;
    }

    public void setIklanAlamat(String iklanAlamat) {
        IklanAlamat = iklanAlamat;
    }

    public Double getIklanHarga() {
        return IklanHarga;
    }

    public void setIklanHarga(Double iklanHarga) {
        IklanHarga = iklanHarga;
    }
}
