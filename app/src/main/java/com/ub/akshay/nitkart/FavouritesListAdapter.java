package com.ub.akshay.nitkart;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.v4.content.ContextCompat.startActivity;

public class FavouritesListAdapter extends ArrayAdapter<ShoppingItem> {
    Context context;
    public FavouritesListAdapter(Context context, List<ShoppingItem> items){
        super(context, 0, items);
        this.context = context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.favouriteslist, parent, false
            );
        }
        final ShoppingItem currentItem = getItem(position);
        ImageView img = (ImageView) listItemView.findViewById(R.id.favpic);
        String dd = String.valueOf(currentItem.getProductID());
        if (dd == "sakr")
            img.setImageResource(R.drawable.sakr);
        else if (dd == "shi")
            img.setImageResource(R.drawable.shi);
        else if (dd == "shata" )
            img.setImageResource(R.drawable.shata);

        else if (dd == "malh")
            img.setImageResource(R.drawable.malh);

        else if (dd == "enap")
            img.setImageResource(R.drawable.enap);

        else if (dd == "balah")
            img.setImageResource(R.drawable.balah);

        else if (dd == "ananas")
            img.setImageResource(R.drawable.ananas);
else if (dd =="tomato")
            img.setImageResource(R.drawable.tomato);

       /* Picasso.with(getContext())
                .load(context.getApplicationContext().getString(R.string.ip)
                        + String.valueOf(currentItem.getProductID())
                        + ".jpg")
                .fit().centerCrop()
                .into(img);
       */



        TextView name = (TextView) listItemView.findViewById(R.id.favname);
        name.setText(currentItem.getTitle());
        TextView type = (TextView) listItemView.findViewById(R.id.favtype);
        type.setText(currentItem.getType());
        TextView description = (TextView) listItemView.findViewById(R.id.favdescribe);
        description.setText(currentItem.getDescription());
        TextView cost = (TextView) listItemView.findViewById(R.id.favprice);
        cost.setText(currentItem.getPrice());

        return listItemView;
    }
}
