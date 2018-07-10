package com.ub.akshay.nitkart;
/*
user search for product
*/

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

public class Search extends AppCompatActivity {

    public final String TAG = Search.class.getSimpleName();
    ListView shoppingItemView;
    ShoppingListAdapter adapter;
    ProgressBar progressBar;
    EditText searchbar;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private final int REQ_CODE_SCANNER = 200;
    TextView ifSellerListEmpty;
    private Boolean exit = false;
    private ArrayList<ShoppingItem> shoppingItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search);
           // Toast.makeText(getApplicationContext() , "user " , Toast.LENGTH_LONG).show();

           // Toolbar toolbar = (Toolbar) findViewById(R.id.Stoolbar);
            //setSupportActionBar(toolbar);



        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       // getSupportActionBar().setIcon(R.drawable.launcher); //also displays wide logo
        getSupportActionBar().setDisplayShowTitleEnabled(false); //optional

        searchbar = (EditText)findViewById(R.id.searchBar);
            // want to see my shooping cart to cheekout or see the cost
            FloatingActionButton shoppingCart = (FloatingActionButton) findViewById(R.id.cartMainPage);
            shoppingCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), ShoppingCartWindow.class));
                }
            });

            progressBar = (ProgressBar) findViewById(R.id.mainPageProgressBar);
            progressBar.setVisibility(View.GONE);
            shoppingItemView = (ListView) findViewById(R.id.shoppingList);
// ------------ change items with supermarket
            DatabaseReference myRef = database.getReference("items");
            myRef.addValueEventListener(new ValueEventListener() {
                // This listener is only for database with reference of key "items"
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.

                    // Now the Shopping List gets updated whenever the data changes in the server
                    shoppingItems = getAllItems(dataSnapshot);
                    adapter = new ShoppingListAdapter(getApplicationContext(), shoppingItems);
                    shoppingItemView.setAdapter(adapter);
                    shoppingItemView.setTextFilterEnabled(true);

                    searchbar.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }
// make list of all matches of items.
                        /*
                        remain :
                        if items exist in many shopes it will choose best

                        do better in making match of similar items use trie data structure as if number of item is large
                        linaer sol  will be slow.
                         */

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            //Toast.makeText(getApplicationContext() ,"  " +charSequence ,Toast.LENGTH_LONG).show();
                            int textlength = charSequence.length();
                            ArrayList<ShoppingItem> tempShoppingItems = new ArrayList<>();
                            for(ShoppingItem x: shoppingItems){
                                if (textlength <= x.getTitle().length()) {
                                    if (x.getTitle().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                        tempShoppingItems.add(x);
                                    }
                                }
                            }
                            shoppingItemView.setAdapter(new ShoppingListAdapter(getApplicationContext(), tempShoppingItems));
                        }
                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
// when choose one item show it's info and choose quantity
            shoppingItemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent productIntent = new Intent(Search.this, IndividualProduct.class);
                    productIntent.putExtra("product", shoppingItems.get(i));
                    startActivity(productIntent);
                }
            });
        }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_app_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.




        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

         if (id == R.id.scun ){
            Intent intent = new Intent(Search.this, Capture.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivityForResult(intent, REQ_CODE_SCANNER);
        }

        else {
            promptSpeechInput();
        }
        return super.onOptionsItemSelected(item);
    }
    // code for google talk
    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }
// read data from items

    public static ArrayList<ShoppingItem> getAllItems(DataSnapshot dataSnapshot){
        ArrayList<ShoppingItem> items  = new ArrayList<ShoppingItem>();
        /*for (DataSnapshot item : dataSnapshot.getChildren()) {
            items.add(new ShoppingItem(
                    item.child("productID").getValue().toString(),
                    item.child("name").getValue().toString(),
                    item.child("type").getValue().toString(),
                    item.child("description").getValue().toString(),
                    Integer.valueOf(item.child("price").getValue().toString()),
                    Integer.valueOf(item.child("quantity").getValue().toString())
            ));
        }
*/
        items.add(new ShoppingItem("sakr", "سكر", "المائده", "فاخر", 10, 20));
        items.add(new ShoppingItem("shi", "شاي", "فاخر", "العروسه", 101, 200));
        items.add(new ShoppingItem("enap", "عنب", "salama", "abnm", 20, 2000));
        items.add(new ShoppingItem("balah", "بلح", "asd", "abhf", 30, 20000));
        items.add(new ShoppingItem("tomato", "طماطم", "sfsdf", "awqe", 40, 2500));
        items.add(new ShoppingItem("ananas", "اناناس", "dfgg", "jlklk", 50, 2055));
        items.add(new ShoppingItem("shata", "شطه", "ette", "yuii", 60, 2066));
        items.add(new ShoppingItem("malh", "ملح", "ewrw", "jhkhjk", 70, 2077));
        return items;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // google talk code
        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    searchbar.setText(result.get(0));
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
                break;
            }
            // callback from scanner and add the resoult to the item
            case REQ_CODE_SCANNER: {
                if (resultCode == RESULT_OK) {
                    if (data.hasExtra("position")) {
                        String type = data.getStringExtra("type");
                        String code = data.getStringExtra("code");
                        int position = data.getIntExtra("position", 0);
                        Toast.makeText(getApplicationContext() , type + "  " + code +"  " + position,Toast.LENGTH_LONG ).show();
                        //+ search in data for barcode

                        //  callback from scanner and add new item in the list with code in the item
                    } else {
                        String type = data.getStringExtra("type");
                        String code = data.getStringExtra("code");
                        Toast.makeText(getApplicationContext(), type + "  " + code + "  fff", Toast.LENGTH_LONG).show();
                        //+ search in data for barcode
                    }
                }
                break;
            }
        }
    }
}
