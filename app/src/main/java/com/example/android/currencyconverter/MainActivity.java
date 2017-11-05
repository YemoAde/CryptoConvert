package com.example.android.currencyconverter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.currencyconverter.Adapter.CardListAdapter;
import com.example.android.currencyconverter.Data.CardContract;
import com.example.android.currencyconverter.Data.CardDbHelper;
import com.example.android.currencyconverter.Dialog.ConversionDialog;
import com.example.android.currencyconverter.Dialog.NoInternet;
import com.example.android.currencyconverter.Model.Card;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements  View.OnClickListener{

    /**
     * Base API URL
     */
    public static final String BASE_URL = "https://min-api.cryptocompare.com/";

    CardDbHelper mDbHelper = new CardDbHelper(this);
    private GridView cardListView;
//    private RecyclerView cardListView;
    TextView placeholder;

    private Boolean isFabOpen = false;
    private FloatingActionButton fab,fab1,fab2,fab3;
    private Animation fab_open,fab_close,rotate_forward,rotate_backward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //getting or refreshing the exchange values in sqlite database
        sendDataRequest();

        //initializing FABs
        initFabs();

        //Loading Data from the SQLite database into adapter then view
        final ArrayList<Card> card_list = (ArrayList<Card>) mDbHelper.read_data();
        placeholder = (TextView) findViewById(R.id.empty);
        placeholder.setVisibility(View.INVISIBLE);

        cardListView = (GridView) findViewById(R.id.card_view);
        final CardListAdapter cardListAdapter = new CardListAdapter(this, card_list);
        cardListView.setAdapter(cardListAdapter);
        if (card_list.isEmpty())
            isEmpty();
        else
            isNotEmpty()

//        final CarrdListAdapter carrdListAdapter = new CarrdListAdapter(card_list);
//        cardListView = (RecyclerView) findViewById(R.id.card_view);
//        cardListView.setLayoutManager(new GridLayoutManager(this,2));
//        cardListView.setHasFixedSize(true);
//        cardListView.setRecycledViewPool(new RecyclerView.RecycledViewPool());
//        cardListView.setAdapter(cardListAdapter);
        ;

//        actionButton = (FloatingActionButton) findViewById(R.id.fabd);
//        actionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getBaseContext(), ConversionActivity.class);
//                startActivity(intent);
//            }
//        });

        /**
         * To Perform data refresh from server at certain intervals
         */

        final Handler handler = new Handler();
        final int delay = 90000; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                sendDataRequest();
                getData();
                handler.postDelayed(this, delay);
            }
        }, delay);

        //To delete item on Long Press
        cardListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Delete Card ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK button
                        Card current_card = mDbHelper.read_data().get(pos);
                        mDbHelper.delete_data(current_card.getmID());
                        getData();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                return true;
            }
        });

//        To Show Conversion Dialog on Item Press
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Card selected_card = mDbHelper.read_data().get(position);
                ConversionDialog.createConversionDialog(selected_card, MainActivity.this);
                if(selected_card.getmExchangeRate() == 0.0){
                    NoInternet.showNoInternet(MainActivity.this);
                }
            }
        });
    }

//    refreshes the the adapter on data change
    public void getData () {
        ArrayList<Card> card_list = (ArrayList<Card>) mDbHelper.read_data();
        CardListAdapter cardListAdapter = new CardListAdapter(this, card_list);
        cardListView.setAdapter(cardListAdapter);
        if (card_list.isEmpty())
            isEmpty();
        else
            isNotEmpty();
    }

    //Shows PlaceHolder if List is Empty
    public void isEmpty (){
        cardListView.setVisibility(View.INVISIBLE);
        placeholder.setVisibility(View.VISIBLE);
    }

//    Reveals populated List
    public void isNotEmpty() {
        cardListView.setVisibility(View.VISIBLE);
        placeholder.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        getData();
    }

//    function to animate FABs
    public void animateFAB(){

        if(isFabOpen){

            fab.startAnimation(rotate_backward);
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab3.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            isFabOpen = false;

        } else {

            fab.startAnimation(rotate_forward);
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab3.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isFabOpen = true;

        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fab:
                animateFAB();
                break;
            case R.id.fab1:

                sendDataRequest();
                getData();
                break;
            case R.id.fab2:

                add_card();
                break;
            case R.id.fab3:

                Toast.makeText(this, "Click and Hold the card to Delete", Toast.LENGTH_LONG).show();
                break;
        }
    }

//    initialize FABs
    public void initFabs (){
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab1 = (FloatingActionButton)findViewById(R.id.fab1);
        fab2 = (FloatingActionButton)findViewById(R.id.fab2);
        fab3 = (FloatingActionButton)findViewById(R.id.fab3);
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fab_close);
        rotate_forward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_backward);
        fab.setOnClickListener(this);
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);
    }

    public void sendDataRequest () {
        mDbHelper.refresh("BTC");
        mDbHelper.refresh("ETH");
    }

    /**
     * Function handles the add_card Dialog
     * for adding new conversion cards
     */
    public void add_card () {
        final Dialog addDialog = new Dialog(this);
        addDialog.setContentView(R.layout.add_card);

        Spinner spinner = (Spinner) addDialog.findViewById(R.id.spinner);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.local_currency_list, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        final CardDbHelper mDbHelper = new CardDbHelper(this);

        final TextView select_local = (TextView) addDialog.findViewById(R.id.select_local);
        select_local.setText("?");
        final TextView select_base = (TextView) addDialog.findViewById(R.id.select_base);
        select_base.setText("?");


        Button select_btc = (Button) addDialog.findViewById(R.id.select_btc);
        select_base.isEnabled();
        select_btc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_base.setText("BTC");
            }
        });
        Button select_eth = (Button) addDialog.findViewById(R.id.select_eth);
        select_eth.isEnabled();
        select_eth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_base.setText("ETH");
            }
        });

        ImageButton submit = (ImageButton) addDialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select_local.getText() != "?" && select_base.getText() != "?") {
                    ContentValues values = new ContentValues();
                    values.put(CardContract.CardEntry.COLUMN_BASE_CURRENCY, select_base.getText().toString());
                    values.put(CardContract.CardEntry.COLUMN_LOCAL_CURRENCY, select_local.getText().toString());
                    mDbHelper.insert_data(values);
                    sendDataRequest();
                    getData();


                }
                else {
                    Toast.makeText(MainActivity.this, "Please Select Card details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select_local.setText(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        addDialog.show();
    }


}
