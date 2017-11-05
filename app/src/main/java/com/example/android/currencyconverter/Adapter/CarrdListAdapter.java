package com.example.android.currencyconverter.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.currencyconverter.Data.CardDbHelper;
import com.example.android.currencyconverter.Dialog.ConversionDialog;
import com.example.android.currencyconverter.Dialog.NoInternet;
import com.example.android.currencyconverter.MainActivity;
import com.example.android.currencyconverter.Model.Card;
import com.example.android.currencyconverter.R;

import java.util.Currency;
import java.util.List;

/**
 * Created by adeyemogabriel on 11/2/17.
 */

public class CarrdListAdapter extends RecyclerView.Adapter<CarrdListAdapter.CarrdViewHolder> {
    private List<Card> cardList;
    CardDbHelper mDbHelper;
    Context mContext;



    public CarrdListAdapter (List<Card> cards, Context context) {
        this.cardList = cards;
        mContext = context;
        mDbHelper = new CardDbHelper(context);

    }

    @Override
    public CarrdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);
        return new CarrdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CarrdViewHolder  holder, int position) {
        //to set Text and Image
        final Card currentCard = cardList.get(position);
//        final Context context = holder.itemView.getContext();
        Currency c = Currency.getInstance(currentCard.getmLocalCurrency());

//        Check if Base Currency is BTC or ETH and load appropriately
        if (currentCard.getmBaseCurrency().toString().equals("BTC")){
            holder.imageView.setImageResource(R.drawable.bitcoin);
        }
        else {
            holder.imageView.setImageResource(R.drawable.eth);
        }
        holder.base_currency_value_TextView.setText(String.valueOf(currentCard.getmBaseCurrency()));
        holder.exchangeRate_TextView.setText(c.getSymbol() + String.valueOf(currentCard.getmExchangeRate()));
        holder.local_currency_TextView.setText(String.valueOf(currentCard.getmLocalCurrency()));
    }


    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class CarrdViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        private ImageView imageView;
        private TextView base_currency_value_TextView;
        private TextView exchangeRate_TextView;
        private TextView local_currency_TextView;
        private Card currentCard;

        public CarrdViewHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.logo);
            base_currency_value_TextView = (TextView) itemView.findViewById(R.id.base_currency);
            exchangeRate_TextView = (TextView) itemView.findViewById(R.id.exchange_rate);
            local_currency_TextView = (TextView) itemView.findViewById(R.id.local_currency);
        }
        public void bind(Card card) {
            currentCard = card;
        }

        @Override
        public void onClick(View v) {

        }

        @Override
        public boolean onLongClick(View v) {
            return true;
        }
    }
}

