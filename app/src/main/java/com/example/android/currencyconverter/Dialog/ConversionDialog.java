package com.example.android.currencyconverter.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.currencyconverter.Model.Card;
import com.example.android.currencyconverter.R;

import java.util.function.Function;

/**
 * Created by adeyemogabriel on 11/2/17.
 */

//The conversion Dialog Displayed when each card is clicked
public class ConversionDialog {

//    Function is made static to enable calling without creating an object of the class
    public static void createConversionDialog (final Card card, Context context) {
//        Creating a dialog object
        final Dialog conversionDialog = new Dialog(context);
        conversionDialog.setContentView(R.layout.convert_dialog);

        TextView base_curr  = (TextView) conversionDialog.findViewById(R.id.text_from);
        base_curr.setText(card.getmBaseCurrency());

        TextView local_curr  = (TextView) conversionDialog.findViewById(R.id.text_to);
        local_curr.setText(card.getmLocalCurrency());

        final EditText input_from = (EditText) conversionDialog.findViewById(R.id.input_from);

        /**
         * Closes Dialog box if open
         */
        final TextView result_text = (TextView) conversionDialog.findViewById(R.id.conversion_result);
        ImageView close_btn = (ImageView) conversionDialog.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conversionDialog.isShowing()){
                    conversionDialog.cancel();
                }
            }
        });

        final Double rate = card.getmExchangeRate();

        /**
         * A text change listener to convert the values real time as user changes input
         */
        input_from.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(s.toString().isEmpty())){
                    Double from = Double.valueOf(s.toString());
                    result_text.setText(String.valueOf(from * rate));
                }else{
                    result_text.setText("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        /**
         * Shows the dialog box
         */
        conversionDialog.show();
    }
}
