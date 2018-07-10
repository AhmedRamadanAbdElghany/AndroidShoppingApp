package com.ub.akshay.nitkart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class Superm extends AppCompatActivity {



    private ArrayList<ShoppingItem> favItems;
    ListView view1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopitem);
        view1 = (ListView)findViewById(R.id.shoplistview);
        int id =(int) getIntent().getSerializableExtra("id");
        favItems= new ArrayList<>();
        if (id == 0 ) {
            favItems.add(new ShoppingItem("sakr", "سكر", "المائده", "فاخر", 10, 20));
            favItems.add(new ShoppingItem("shi", "شاي", "فاخر", "العروسه", 101, 200));
        }
        else if (id == 1 ) {
            favItems.add(new ShoppingItem("enap", "عنب", "salama", "abnm", 20, 2000));
            favItems.add(new ShoppingItem("balah", "بلح", "asd", "abhf", 30, 20000));
        }
        else if (id == 2 ) {
            favItems.add(new ShoppingItem("tomato", "طماطم", "sfsdf", "awqe", 40, 2500));
            favItems.add(new ShoppingItem("ananas", "اناناس", "dfgg", "jlklk", 50, 2055));
        }
        else if (id == 3) {
            favItems.add(new ShoppingItem("shata", "شطه", "ette", "yuii", 60, 2066));
            favItems.add(new ShoppingItem("malh", "ملح", "ewrw", "jhkhjk", 70, 2077));
        }
        view1.setAdapter(new FavouritesListAdapter(getApplicationContext(), favItems));


        try {
            view1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent productIntent = new Intent(getApplicationContext(), IndividualProduct.class);
                    productIntent.putExtra("product",favItems.get(i));
                    startActivity(productIntent);
                }
            });
        }
        catch (Exception e ){
            ;//Log.d(TAG , "rrrrr "+e.getMessage());
        }
    }
}
