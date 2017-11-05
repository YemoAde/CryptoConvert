package com.example.android.currencyconverter.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.android.currencyconverter.Data.CardContract.CardEntry;
import com.example.android.currencyconverter.Dialog.NoInternet;
import com.example.android.currencyconverter.Model.ApiResponse;
import com.example.android.currencyconverter.Model.Card;
import com.example.android.currencyconverter.rest.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by adeyemogabriel on 10/24/17.
 */

public class CardDbHelper extends SQLiteOpenHelper {

    private static  final String DATABASE_NAME = "CurrencyConverter.db";
    private static final int DATABASE_VERSION = 2;

    Context mContext;
    public CardDbHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_CARD_TABLE = "CREATE TABLE " +
                CardEntry.TABLE_NAME + "(" +
                CardEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CardEntry.COLUMN_BASE_CURRENCY + " TEXT," +
                CardEntry.COLUMN_LOCAL_CURRENCY + " TEXT," +
                CardEntry.COLUMN_EXCHANGE_RATE + " REAL DEFAULT 0" +
                ")";
        db.execSQL(SQL_CREATE_CARD_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CardEntry.TABLE_NAME );
        onCreate(db);
    }

//    function to insert data into SQLite database
    public boolean insert_data (ContentValues value) {

        SQLiteDatabase db = this.getWritableDatabase();
        if (db.insert(CardEntry.TABLE_NAME, null, value) > 0){
            db.close();
            return true;
        }
        else {
            db.close();
            return false;
        }
    }
//    function to read data from database
    public List<Card> read_data () {
        List<Card> card_list = new ArrayList<Card>();

        String[] projection = {CardEntry._ID, CardEntry.COLUMN_BASE_CURRENCY, CardEntry.COLUMN_LOCAL_CURRENCY, CardEntry.COLUMN_EXCHANGE_RATE };

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CardEntry.TABLE_NAME,projection, null, null, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                int card_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CardEntry._ID)));
                String base_currency = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_BASE_CURRENCY));
                String local_currency = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_LOCAL_CURRENCY));
                double exchangeRate = cursor.getDouble(cursor.getColumnIndex(CardEntry.COLUMN_EXCHANGE_RATE));

                Card mCard = new Card(card_id, base_currency, local_currency, exchangeRate);
                card_list.add(mCard);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return card_list;
    }
//    overload of the read_data function which accepts a String argument
    public List<Card> read_data (String crypto) {
        List<Card> card_list = new ArrayList<Card>();

        String[] projection = {CardEntry._ID, CardEntry.COLUMN_BASE_CURRENCY, CardEntry.COLUMN_LOCAL_CURRENCY, CardEntry.COLUMN_EXCHANGE_RATE };

        String selection = CardEntry.COLUMN_BASE_CURRENCY + " = ?";
        String[] selectionArgs = { crypto };
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(CardEntry.TABLE_NAME,projection, selection, selectionArgs, null, null, null);

        if(cursor.moveToFirst()) {
            do {
                int card_id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(CardEntry._ID)));
                String base_currency = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_BASE_CURRENCY));
                String local_currency = cursor.getString(cursor.getColumnIndex(CardEntry.COLUMN_LOCAL_CURRENCY));
                double exchangeRate = cursor.getDouble(cursor.getColumnIndex(CardEntry.COLUMN_EXCHANGE_RATE));

                Card mCard = new Card(card_id, base_currency, local_currency, exchangeRate);
                card_list.add(mCard);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return card_list;
    }

//    function to delete data form the SQLite database
    public void delete_data (int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(CardEntry.TABLE_NAME, CardEntry._ID + " = ?", new String[] { Integer.toString(id) });
        db.close();
    }

    /**
     * Function makes request to get all the exchange rates from the API
     * and saves all values of added cards(exchange currencies) in the sqlite DB
     *
     * It checks for availability of internet
     * if there is performs request
     * else Displays a dialog (NoInternet)
     */
    public void refresh (final String base_currency) {
        ApiClient apiClient = new ApiClient();
        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connMgr.getActiveNetworkInfo();

        if(activeNetwork !=null && activeNetwork.isConnectedOrConnecting()){
            Call<ApiResponse> call = apiClient.getApiService().getConversionData(base_currency);
            call.enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    ApiResponse rate_list = response.body();

                    SQLiteDatabase db = getWritableDatabase();
                    for(Card tmp: read_data(base_currency)) {

                        ContentValues values = new ContentValues();
                        values.put(CardEntry.COLUMN_EXCHANGE_RATE, getSpecificRate(tmp.getmLocalCurrency(), rate_list));

                        String selection = CardEntry._ID + " = ?" ;
                        String[] selectionArgs = {String.valueOf(tmp.getmID())};
                        db.update(CardEntry.TABLE_NAME,values,selection,selectionArgs);
                    }

                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    NoInternet.showNoInternet(mContext, "Could Not Get List");
                }
            });
        }else {
            NoInternet.showNoInternet(mContext);
        }

    }

    /**
     * Function separates exchange rates from the ApiResponse object returned by API
     *
     */
    public double getSpecificRate (String local_currency, ApiResponse apiResponse) {
        double rate;
        switch (local_currency) {
            case "NGN":
                rate = apiResponse.getNGN();
                break;
            case "USD":
                rate = apiResponse.getUSD();
                break;
            case "GBP":
                rate = apiResponse.getGBP();
                break;
            case "EUR":
                rate = apiResponse.getEUR();
                break;
            case "ZAR":
                rate = apiResponse.getZAR();
                break;
            case "RUB":
                rate = apiResponse.getRUB();
                break;
            case "CNY":
                rate = apiResponse.getCNY();
                break;
            case "JPY":
                rate = apiResponse.getJPY();
                break;
            case "CFP":
                rate = apiResponse.getCFP();
                break;
            case "CHF":
                rate = apiResponse.getCHF();
                break;
            case "GHS":
                rate = apiResponse.getGHS();
                break;
            case "AUD":
                rate = apiResponse.getAUD();
                break;
            case "ARS":
                rate = apiResponse.getARS();
                break;
            case "BRL":
                rate = apiResponse.getBRL();
                break;
            case "INR":
                rate = apiResponse.getINR();
                break;
            case "CAD":
                rate = apiResponse.getCAD();
                break;
            case "AED":
                rate = apiResponse.getAED();
                break;
            case "BND":
                rate = apiResponse.getBND();
                break;
            case "DKK":
                rate = apiResponse.getDKK();
                break;
            case "SAR":
                rate = apiResponse.getSAR();
                break;
            case "SGD":
                rate = apiResponse.getSGD();
                break;
            default:
                //pass
                rate = 0.0;
        }
        return rate;
    }
}
