package com.example.android.currencyconverter.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.currencyconverter.R;


/**
 * Created by adeyemogabriel on 11/2/17.
 */

/**
 * Dialog Box to be displayed if network listener returns no active network
 */
public class NoInternet {

    public static void showNoInternet (Context context) {

        final Dialog NoInternetDialog = new Dialog(context);
        NoInternetDialog.setContentView(R.layout.no_internet);

        ImageView close_btn = (ImageView) NoInternetDialog.findViewById(R.id.close);
        close_btn.isEnabled();
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoInternetDialog.isShowing()){
                    NoInternetDialog.hide();
                }
            }
        });

        /**
         * Closes the Dialog Box after 5 seconds if user does not
         * click the close button
         */
        NoInternetDialog.show();
        Handler handler = new Handler();
        final int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if (NoInternetDialog.isShowing()){
                    NoInternetDialog.hide();
                }
            }
        }, delay);
    }
    /**
     * Overload of showInternet()
     * Handles When there is an active Network but Retrofit could not get
     * data from the API
     */
    public static void showNoInternet (Context context, String errorMessage) {

        final Dialog NoInternetDialog = new Dialog(context);
        NoInternetDialog.setContentView(R.layout.no_internet);

        TextView msg_view = (TextView) NoInternetDialog.findViewById(R.id.no_internet);
        msg_view.setText(errorMessage);

        ImageView close_btn = (ImageView) NoInternetDialog.findViewById(R.id.close);
        close_btn.isEnabled();
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NoInternetDialog.isShowing()){
                    NoInternetDialog.hide();
                }
            }
        });
        NoInternetDialog.show();
        Handler handler = new Handler();
        final int delay = 5000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                if (NoInternetDialog.isShowing()){
                    NoInternetDialog.hide();
                }

            }
        }, delay);
    }
}
