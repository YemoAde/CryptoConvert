package com.example.android.currencyconverter.Model;

/**
 * Created by adeyemogabriel on 10/25/17.
 */

/**
 * Model/Blueprint of a card
 */
public class Card {

    private int mID;

    private String mBaseCurrency;

    private String mLocalCurrency;

    private double mExchangeRate;

    public Card (int ID, String baseCurrency, String localCurrency, double exchangeRate) {
        this.mID = ID;
        this.mBaseCurrency = baseCurrency;
        this.mLocalCurrency = localCurrency;
        this.mExchangeRate = exchangeRate;
    }

    public void setiD (){

    }
    public int getmID () {
        return mID;
    }
    public void setmBaseCurrency () {

    }
    public String getmBaseCurrency () {
        return mBaseCurrency;
    }

    public String getmLocalCurrency() {
        return mLocalCurrency;
    }

    public void setmLocalCurrency(String mLocalCurrency) {
        this.mLocalCurrency = mLocalCurrency;
    }

    public double getmExchangeRate() {
        return mExchangeRate;
    }

    public void setmExchangeRate(double mExchangeRate) {
        this.mExchangeRate = mExchangeRate;
    }
}
