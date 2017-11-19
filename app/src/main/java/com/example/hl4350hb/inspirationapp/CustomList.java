package com.example.hl4350hb.inspirationapp;


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static android.text.format.DateFormat.format;

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> notesArray;
    private final ArrayList<String> imageIdArray;
    private final ArrayList<Long> dateArray;
    private final SimpleDateFormat dateFormatter;
    private final DateFormat dateFormatter2;
    private final Calendar cal;

    // Constructor.
    public CustomList(Activity context, ArrayList<String> notesArray, ArrayList<String> imageIdArray, ArrayList<Long> dateArray) {
        // Extends an ArrayAdapter.
        super(context, R.layout.list_item, notesArray);
        this.context = context;
        this.notesArray = notesArray;
        this.imageIdArray = imageIdArray;
        this.dateArray = dateArray;
        this.dateFormatter = new SimpleDateFormat("M-dd-yyyy hh:mm:ss");
        this.dateFormatter2 = DateFormat.getDateInstance(DateFormat.LONG);
        this.cal = Calendar.getInstance(Locale.ENGLISH);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context. getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_item, null, true);
        final TextView noteTextView = (TextView) rowView.findViewById(R.id.txt);
        noteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteTextView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(1000) {
                }});
            }
        });

        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        noteTextView.setText(notesArray.get(position));

        TextView dateTextView = (TextView) rowView.findViewById(R.id.datetxt);
        dateTextView.setText(dateFormatter.format(dateArray.get(position)));
//        cal.setTimeInMillis(dateArray.get(position));
//        dateTextView.setText(DateFormat.format("M-dd-yyyy hh:mm:ss", cal).toString());
//        dateTextView.setText(dateFormatter2.format(dateArray.get(position)));

        imageView.setImageURI(Uri.fromFile(new File(imageIdArray.get(position))));
//        imageView.setImageResource(imageIdArray.get(position));
        return rowView;
    }

//TODO maybe pass a safe-word and validate
    public void addNewEntry(String note, String imageId, long currTime) {
        notesArray.add(note);
        imageIdArray.add(imageId);
        dateArray.add(currTime);
        notifyDataSetChanged();

    }
}
