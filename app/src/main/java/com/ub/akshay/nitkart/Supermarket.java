package com.ub.akshay.nitkart;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

public class Supermarket extends Activity {
    Toolbar mToolbar ;
    ListView mListView;

    String[] countryNames = {"shop1", "shop2", "shop3", "shop4", "shop5", "shop6", "shop7", "shop8"};
    int[] countryFlags = {
            R.drawable.shop0,
            R.drawable.shop1,
            R.drawable.shop2,
            R.drawable.shop3,
            R.drawable.shop4,
            R.drawable.shop6,
            R.drawable.shop5,
            R.drawable.shop7,
            };
    int rate[]={1,2,3,4,5,6,7,8};
    String places[]={"القاهره" , "بنها" , "بني سويف" , "اسيوط" , "بحيره" , "ميت بره","اسطنها " , "ميت حلاوه" };
    int[] Flags = {
            R.drawable.star,
            R.drawable.star,
            R.drawable.star,
            R.drawable.star,
            R.drawable.star,
            R.drawable.star,
            R.drawable.star,
            R.drawable.star};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supermarket);

        SupermarketAdapter items = new SupermarketAdapter(this ,countryNames,places,countryFlags,Flags);
        ListView list = (ListView) findViewById(R.id.listview);
        list.setAdapter(items);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), Superm.class);
                intent.putExtra("id" , i);
                startActivity(intent);
            }
        });
    }
}
/*
read data from other database
action on items
 */