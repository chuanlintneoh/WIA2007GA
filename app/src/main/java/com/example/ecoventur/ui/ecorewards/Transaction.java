package com.example.ecoventur.ui.ecorewards;

public class Transaction {
    private String voucherTitle;
    private int ecoCoins;

    public Transaction() {
        this.voucherTitle = "Unspecified Voucher Title";
        this.ecoCoins = 0;
    }

    public Transaction(String voucherTitle, int ecoCoins) {
        this.voucherTitle = voucherTitle;
        this.ecoCoins = ecoCoins;
    }

    public String getVoucherTitle() {
        return voucherTitle;
    }

    public void setVoucherTitle(String voucherTitle) {
        this.voucherTitle = voucherTitle;
    }

    public int getEcoCoins() {
        return ecoCoins;
    }

    public void setEcoCoins(int ecoCoins) {
        this.ecoCoins = ecoCoins;
    }
}

