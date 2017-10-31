package com.example.hl4350hb.inspirationapp;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> notesArray;
    private final ArrayList<Integer> imageIdArray;

    // Constructor.
    public CustomList(Activity context, ArrayList<String> notesArray, ArrayList<Integer> imageIdArray) {
        // Extends an ArrayAdapter.
        super(context, R.layout.list_item, notesArray);
        this.context = context;
        this.notesArray = notesArray;
        this.imageIdArray = imageIdArray;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context. getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);
        TextView noteTextView = (TextView) rowView.findViewById(R.id.txt);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        noteTextView.setText(notesArray.get(position));

        imageView.setImageResource(imageIdArray.get(position));
        return rowView;
    }

//TODO maybe pass a safe-word and validate
    public void addNewEntry(String note, int imageId) {
        notesArray.add(note);
        imageIdArray.add(imageId);
        notifyDataSetChanged();

    }
}
