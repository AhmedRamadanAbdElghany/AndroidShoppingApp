package com.ub.akshay.nitkart;


/*
******** normal users ********
when user choose item to buy
show item image and description and choose quantity
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IndividualProduct extends AppCompatActivity {

    private ArrayList<ShoppingItem> favItems;
    private ArrayList<Integer> arr;
    private final String TAG = IndividualProduct.class.getSimpleName();
    private int quantity = 1, qind = -1 , ex = 0 ;
    String ip;
    ShoppingItem item;
    Button add, sub;
    TextView name, description, quantityView;
    FloatingActionButton addToCart, shoppingCart;
    CheckBox infav;
    ImageView img;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private DataSnapshot dataSnapshot;

    private ArrayList<ShoppingItem> cartItems;
    private Boolean isItemAlreadyInCart = false;
    int indexOfAlreadyPresentItem = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_product);
        favItems = new ArrayList<>();
        progressBar = (ProgressBar) findViewById(R.id.individualProductPageProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        infav = (CheckBox) findViewById(R.id.favcheckbox);

        item = (ShoppingItem) getIntent().getSerializableExtra("product");
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    myRef = database.getReference("users/" + user.getUid());
                    // adding value event listener for myRef
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            progressBar.setVisibility(View.VISIBLE);
                            checkInFav(dataSnapshot.child("Favourites"));
                            IndividualProduct.this.dataSnapshot = dataSnapshot;
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                }
                else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
        getSupportActionBar().hide();
        ip = getResources().getString(R.string.ip);
        name = (TextView) findViewById(R.id.productNameIndividualProduct);
        name.setText(item.getTitle());
        description = (TextView) findViewById(R.id.productDescriptionIndividualProduct);
        description.setText(item.getDescription());
        quantityView = (TextView) findViewById(R.id.quantityProductPage);
        quantityView.setText(String.valueOf(quantity));
        ((TextView) findViewById(R.id.productPriceIndividualProduct)).setText(item.getPrice());
        img = (ImageView) findViewById(R.id.productImageIndividualProduct);
        img.setImageResource(R.drawable.shi);

        String dd = item.getProductID().toString();
        Toast.makeText(getApplicationContext() , dd ,Toast.LENGTH_SHORT ).show();

        if (dd.equals("sakr")) {
            img.setImageResource(R.drawable.sakr);
        }
        else if (dd.equals( "shi"))
            img.setImageResource(R.drawable.shi);
        else if (dd.equals("shata" ))
            img.setImageResource(R.drawable.shata);

        else if (dd.equals("malh"))
            img.setImageResource(R.drawable.malh);

        else if (dd.equals("enap"))
            img.setImageResource(R.drawable.enap);

        else if (dd.equals("balah"))
            img.setImageResource(R.drawable.balah);

        else if (dd.equals("ananas"))
            img.setImageResource(R.drawable.ananas);
        else if (dd.equals("tomato"))
            img.setImageResource(R.drawable.tomato);

        infav.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            // checkbox status is changed from uncheck to checked.
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                      int i = 0 ;
                    for (i =0 ; i < favItems.size();i++)
if (cmp(i)) {
    Toast.makeText(getApplicationContext(), "choosen before ", Toast.LENGTH_SHORT).show();
break;
}
                    if (i == favItems.size())
                         favItems.add(item);
                  }

                else {
                    arr.clear();
                   // Toast.makeText(getApplicationContext(), "  " + qind, Toast.LENGTH_LONG).show();
arr = new ArrayList<>();
                    for (int i = 0; i < favItems.size(); i++)
                        if (cmp(i)){
                            arr.add(i);
                        }

                        for (int i = 0 ; i < arr.size();i++)
                            favItems.remove(arr.get(i));

                }

                if (favItems.size() == 0 )
                    favItems.add(new ShoppingItem("", "", "", "", -1, -1));
                myRef.child("Favourites").setValue(favItems);
            }
        });

///  ********************  image load
   /*     Picasso.with(getApplicationContext())
                .load(ip + String.valueOf(item.getProductID()) + ".jpg")
                .fit()
                .into(productImage);
                */
        add = (Button) findViewById(R.id.incrementQuantity);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increment();
            }
        });

        sub = (Button) findViewById(R.id.decrementQuantity);
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrement();
            }
        });




        addToCart = (FloatingActionButton) findViewById(R.id.addToCartProductPage);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                cartItems = new ArrayList<>();
                if (dataSnapshot.getKey().equals(user.getUid())) {
                    //            Toast.makeText(getApplicationContext(), "ffff", Toast.LENGTH_LONG).show();
                    int tempIndex = 0;
                    for (DataSnapshot snap : dataSnapshot.child("cartItems").getChildren()) {
                        int itemPrice = -1;
                        try {
                            itemPrice = Integer.valueOf(NumberFormat.getCurrencyInstance()
                                    .parse(String.valueOf(snap.child("price").getValue()))
                                    .toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        String productID = snap.child("productID").getValue().toString();
                        if (productID == item.getProductID()) {
                            isItemAlreadyInCart = true;
                            indexOfAlreadyPresentItem = tempIndex;
                        }
                        cartItems.add(new ShoppingItem(
                                productID,
                                snap.child("title").getValue().toString(),
                                snap.child("type").getValue().toString(),
                                snap.child("description").getValue().toString(),
                                itemPrice,
                                Integer.valueOf(snap.child("quantity").getValue().toString())
                        ));
                        tempIndex++;
                    }
                }

                Snackbar.make(
                        findViewById(R.id.addToCartProductPage),
                        "Adding to cart " + quantity + " items.",
                        Snackbar.LENGTH_SHORT).show();

                if (isItemAlreadyInCart) {
                    cartItems.get(indexOfAlreadyPresentItem)
                            .setQuantity(cartItems.get(indexOfAlreadyPresentItem).getQuantity() + quantity);
                } else {
                    item.setQuantity(quantity);
                    cartItems.add(item);
                }

                myRef.child("cartItems").setValue(cartItems);
                progressBar.setVisibility(View.GONE);
            }
        });
        shoppingCart = (FloatingActionButton) findViewById(R.id.cartProductPage);
        shoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ShoppingCartWindow.class));
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    void increment() {
        if (quantity < 50) {
            quantity++;
            quantityView.setText(String.valueOf(quantity));
        } else {
            Toast.makeText(
                    getApplicationContext(),
                    "Limit of 5 products only",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    void decrement() {
        if (quantity > 1) {
            quantity--;
            quantityView.setText(String.valueOf(quantity));
        }
    }

    Boolean cmp(int idx) {
        return favItems.get(idx).getTitle().equals(item.getTitle()) &&
                favItems.get(idx).getProductID().equals(item.getProductID()) &&
                favItems.get(idx).getPrice().equals(item.getPrice()) &&
                favItems.get(idx).getDescription().equals(item.getDescription());

    }

    void checkInFav(DataSnapshot dataSnapshot1) {

        if (favItems != null) {
            favItems.clear();
        } else {
            favItems = new ArrayList<>();
        }
        int i =0, ex = 0 ;
        for (DataSnapshot snap : dataSnapshot1.getChildren()) {
            int itemPrice = -1, quantity = 0;
            try {
                itemPrice = Integer.valueOf(NumberFormat.getCurrencyInstance()
                        .parse(String.valueOf(snap.child("price").getValue()))
                        .toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            quantity = Integer.valueOf(snap.child("quantity").getValue().toString());

            favItems.add(new ShoppingItem(
                    snap.child("productID").getValue().toString(),
                    snap.child("title").getValue().toString(),
                    snap.child("type").getValue().toString(),
                    snap.child("description").getValue().toString(),
                    itemPrice,
                    quantity
            ));
            if (cmp(i)) {
                infav.setChecked(true);
            }
        i++;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
//        Toast.makeText(getApplicationContext() , " oo   "+favItems.size() ,Toast.LENGTH_LONG ).show();


    }
}