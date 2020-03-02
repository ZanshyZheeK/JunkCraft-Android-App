package com.coverteam.junkcraft.model;

public class DataCheckout {
    String CheckoutID, CheckoutIDPembelian, CheckoutIDBarang, CheckoutUsernamePelanggan,CheckoutUsernamePenjual, CheckoutDateTime,
            CheckoutJudul, CheckoutDesc,CheckoutAlamat,CheckoutNoHP,CheckoutFotoBarang,CheckoutStatus,CheckoutNoResi;
    Integer CheckoutJumlah,CheckoutTotalHarga;

    public DataCheckout(){}

    public DataCheckout(String checkoutID, String checkoutIDPembelian, String checkoutIDBarang, String checkoutUsernamePelanggan, String checkoutUsernamePenjual, String checkoutDateTime, String checkoutJudul, String checkoutDesc, String checkoutAlamat, String checkoutNoHP, String checkoutFotoBarang, String checkoutStatus, String checkoutNoResi, Integer checkoutJumlah, Integer checkoutTotalHarga) {
        CheckoutID = checkoutID;
        CheckoutIDPembelian = checkoutIDPembelian;
        CheckoutIDBarang = checkoutIDBarang;
        CheckoutUsernamePelanggan = checkoutUsernamePelanggan;
        CheckoutUsernamePenjual = checkoutUsernamePenjual;
        CheckoutDateTime = checkoutDateTime;
        CheckoutJudul = checkoutJudul;
        CheckoutDesc = checkoutDesc;
        CheckoutAlamat = checkoutAlamat;
        CheckoutNoHP = checkoutNoHP;
        CheckoutFotoBarang = checkoutFotoBarang;
        CheckoutStatus = checkoutStatus;
        CheckoutNoResi = checkoutNoResi;
        CheckoutJumlah = checkoutJumlah;
        CheckoutTotalHarga = checkoutTotalHarga;
    }

    public String getCheckoutID() {
        return CheckoutID;
    }

    public void setCheckoutID(String checkoutID) {
        CheckoutID = checkoutID;
    }

    public String getCheckoutIDPembelian() {
        return CheckoutIDPembelian;
    }

    public void setCheckoutIDPembelian(String checkoutIDPembelian) {
        CheckoutIDPembelian = checkoutIDPembelian;
    }

    public String getCheckoutIDBarang() {
        return CheckoutIDBarang;
    }

    public void setCheckoutIDBarang(String checkoutIDBarang) {
        CheckoutIDBarang = checkoutIDBarang;
    }

    public String getCheckoutUsernamePelanggan() {
        return CheckoutUsernamePelanggan;
    }

    public void setCheckoutUsernamePelanggan(String checkoutUsernamePelanggan) {
        CheckoutUsernamePelanggan = checkoutUsernamePelanggan;
    }

    public String getCheckoutUsernamePenjual() {
        return CheckoutUsernamePenjual;
    }

    public void setCheckoutUsernamePenjual(String checkoutUsernamePenjual) {
        CheckoutUsernamePenjual = checkoutUsernamePenjual;
    }

    public String getCheckoutDateTime() {
        return CheckoutDateTime;
    }

    public void setCheckoutDateTime(String checkoutDateTime) {
        CheckoutDateTime = checkoutDateTime;
    }

    public String getCheckoutJudul() {
        return CheckoutJudul;
    }

    public void setCheckoutJudul(String checkoutJudul) {
        CheckoutJudul = checkoutJudul;
    }

    public String getCheckoutDesc() {
        return CheckoutDesc;
    }

    public void setCheckoutDesc(String checkoutDesc) {
        CheckoutDesc = checkoutDesc;
    }

    public String getCheckoutAlamat() {
        return CheckoutAlamat;
    }

    public void setCheckoutAlamat(String checkoutAlamat) {
        CheckoutAlamat = checkoutAlamat;
    }

    public String getCheckoutNoHP() {
        return CheckoutNoHP;
    }

    public void setCheckoutNoHP(String checkoutNoHP) {
        CheckoutNoHP = checkoutNoHP;
    }

    public String getCheckoutFotoBarang() {
        return CheckoutFotoBarang;
    }

    public void setCheckoutFotoBarang(String checkoutFotoBarang) {
        CheckoutFotoBarang = checkoutFotoBarang;
    }

    public String getCheckoutStatus() {
        return CheckoutStatus;
    }

    public void setCheckoutStatus(String checkoutStatus) {
        CheckoutStatus = checkoutStatus;
    }

    public String getCheckoutNoResi() {
        return CheckoutNoResi;
    }

    public void setCheckoutNoResi(String checkoutNoResi) {
        CheckoutNoResi = checkoutNoResi;
    }

    public Integer getCheckoutJumlah() {
        return CheckoutJumlah;
    }

    public void setCheckoutJumlah(Integer checkoutJumlah) {
        CheckoutJumlah = checkoutJumlah;
    }

    public Integer getCheckoutTotalHarga() {
        return CheckoutTotalHarga;
    }

    public void setCheckoutTotalHarga(Integer checkoutTotalHarga) {
        CheckoutTotalHarga = checkoutTotalHarga;
    }
}
