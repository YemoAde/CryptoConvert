package com.example.android.currencyconverter.Adapter;

import android.app.Activity;
import android.icu.util.Currency;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.currencyconverter.Model.Card;
import com.example.android.currencyconverter.R;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;

/**
 * Created by adeyemogabriel on 10/22/17.
 */

public class CardListAdapter extends ArrayAdapter<Card> {

    public CardListAdapter(Activity context, ArrayList<Card> base_currency) {
        super(context, 0, base_currency);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;


        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.card_item, parent, false);
        }


        final Card current_base_currency = getItem(position);
        Currency c = Currency.getInstance(current_base_currency.getmLocalCurrency());

//        Check if Base Currency is BTC or ETH and load appropriately
        if (current_base_currency.getmBaseCurrency().toString().equals("BTC")){
            ImageView imageView = (ImageView) listItemView.findViewById(R.id.logo);
            imageView.setImageResource(R.drawable.bitcoin);
        }
        else {
            ImageView imageView = (ImageView) listItemView.findViewById(R.id.logo);
            imageView.setImageResource(R.drawable.eth);
        }

//        Base Currency TextView
        TextView base_currency_value_TextView = (TextView) listItemView.findViewById(R.id.base_currency);
        base_currency_value_TextView.setText(String.valueOf(current_base_currency.getmBaseCurrency()));

//        Exchange rate TextView display
        TextView exchangeRate_TextView = (TextView) listItemView.findViewById(R.id.exchange_rate);
        exchangeRate_TextView.setText(c.getSymbol() + String.valueOf(current_base_currency.getmExchangeRate()));


//        Exchange rate TextView display
        TextView local_currency_TextView = (TextView) listItemView.findViewById(R.id.local_currency);
        local_currency_TextView.setText(String.valueOf(current_base_currency.getmLocalCurrency()));
//
        return listItemView;

    }


}

