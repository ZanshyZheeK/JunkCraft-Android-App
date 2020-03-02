package com.coverteam.junkcraft.model;

public class DataSaldo {
    private double saldoJumlah;
    private String saldoTanggal,saldoFotoTransfer,saldoStatus,saldoType,saldoIDTransaksi;

    public DataSaldo() {
    }

    public DataSaldo(double saldoJumlah, String saldoTanggal, String saldoFotoTransfer, String saldoStatus, String saldoType, String saldoIDTransaksi) {
        this.saldoJumlah = saldoJumlah;
        this.saldoTanggal = saldoTanggal;
        this.saldoFotoTransfer = saldoFotoTransfer;
        this.saldoStatus = saldoStatus;
        this.saldoType = saldoType;
        this.saldoIDTransaksi = saldoIDTransaksi;
    }

    public String getSaldoIDTransaksi() {
        return saldoIDTransaksi;
    }

    public void setSaldoIDTransaksi(String saldoIDTransaksi) {
        this.saldoIDTransaksi = saldoIDTransaksi;
    }

    public double getSaldoJumlah() {
        return saldoJumlah;
    }

    public void setSaldoJumlah(double saldoJumlah) {
        this.saldoJumlah = saldoJumlah;
    }

    public String getSaldoTanggal() {
        return saldoTanggal;
    }

    public void setSaldoTanggal(String saldoTanggal) {
        this.saldoTanggal = saldoTanggal;
    }

    public String getSaldoFotoTransfer() {
        return saldoFotoTransfer;
    }

    public void setSaldoFotoTransfer(String saldoFotoTransfer) {
        this.saldoFotoTransfer = saldoFotoTransfer;
    }

    public String getSaldoStatus() {
        return saldoStatus;
    }

    public void setSaldoStatus(String saldoStatus) {
        this.saldoStatus = saldoStatus;
    }

    public String getSaldoType() {
        return saldoType;
    }

    public void setSaldoType(String saldoType) {
        this.saldoType = saldoType;
    }
}
