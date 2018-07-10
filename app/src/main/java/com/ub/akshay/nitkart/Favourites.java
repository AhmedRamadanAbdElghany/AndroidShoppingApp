package com.ub.akshay.nitkart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Favourites extends AppCompatActivity {
    ListView view1;
    private ArrayList<ShoppingItem> favItems;
    private final String TAG = ShoppingCartWindow.class.getSimpleName();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef , Ref;
    int d = 0;
    ProgressBar progressBar;

//    Boolean isFavEmpty = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        view1 = (ListView) findViewById(R.id.favlistview);
        progressBar = (ProgressBar) findViewById(R.id.favprogressBar);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    myRef = database.getReference("users/" + user.getUid());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.getKey().equals(user.getUid())) {
                                progressBar.setVisibility(View.VISIBLE);
                                setUpFavCart(dataSnapshot.child("Favourites"));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            Log.w(TAG, "Failed to read value.", error.toException());
                        }
                    });
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };



try {
    view1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent productIntent = new Intent(Favourites.this, IndividualProduct.class);
                productIntent.putExtra("product",favItems.get(i));
                startActivity(productIntent);
        }
    });
}
catch (Exception e ){
    Log.d(TAG , "rrrrr "+e.getMessage());
}

final Context context = this;
try {
    view1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        public boolean onItemLongClick(AdapterView<?> arg0, View v, final int index, long arg3) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);
            // set title
            alertDialogBuilder.setTitle(" delete ");
            // set dialog message
            alertDialogBuilder
                    .setMessage("Do you want to Remove from Favourites!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, close
                            // current activity
                            try {
           favItems.remove(index);
           view1.setAdapter(new FavouritesListAdapter(getApplicationContext(), favItems));
                                Toast.makeText(getApplicationContext() ,"kkk" ,Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext() , " "+favItems.get(index).getTitle()+"has remove ",Toast.LENGTH_LONG).show();
                                favItems.add(new ShoppingItem("", "", "", "", -1, -1));
                                myRef.child("Favourites").setValue(favItems);
                                Toast.makeText(getApplicationContext() ,"ooo" ,Toast.LENGTH_SHORT).show();
favItems.remove(favItems.size()-1);
                                progressBar.setVisibility(View.GONE);
                            }catch (Exception e ){
                            Log.d(TAG , "pppppppp "+e.getMessage());
                        }
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();

    //           Toast.makeText(getApplicationContext() , " qqqq " +index ,Toast.LENGTH_LONG ).show();
            return true;
        }
    });
}catch (Exception e ){
    Log.d(TAG , "yyyyy "+e.getMessage());
}

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

        private void setUpFavCart(DataSnapshot dataSnapshot1) {

        if (favItems != null) {
            favItems.clear();
        } else {
            favItems = new ArrayList<>();
        }
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

            if (quantity != -1 )
            favItems.add(new ShoppingItem(
                    snap.child("productID").getValue().toString(),
                    snap.child("title").getValue().toString(),
                    snap.child("type").getValue().toString(),
                    snap.child("description").getValue().toString(),
                    itemPrice,
                    quantity
            ));
        }

            progressBar.setVisibility(View.GONE);

    /*        if(favItems.size()==0){
                favItems.add(new ShoppingItem("sakr", "سكر", "المائده", "فاخر", 10, 20));
                favItems.add(new ShoppingItem("shi", "شاي", "فاخر", "العروسه", 101, 200));
                favItems.add(new ShoppingItem("enap", "عنب", "salama", "abnm", 20, 2000));
                favItems.add(new ShoppingItem("balah", "بلح", "asd", "abhf", 30, 20000));
                favItems.add(new ShoppingItem("tomato", "طماطم", "sfsdf", "awqe", 40, 2500));
                favItems.add(new ShoppingItem("ananas", "اناناس", "dfgg", "jlklk", 50, 2055));
                favItems.add(new ShoppingItem("shata", "شطه", "ette", "yuii", 60, 2066));
                favItems.add(new ShoppingItem("malh", "ملح", "ewrw", "jhkhjk", 70, 2077));

            }
*/
        // Now the Cart gets updated whenever the data changes in the server
          view1.setAdapter(new FavouritesListAdapter(getApplicationContext(), favItems));
    }
}

