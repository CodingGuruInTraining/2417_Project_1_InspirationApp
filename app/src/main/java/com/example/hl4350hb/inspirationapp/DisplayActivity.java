package com.example.hl4350hb.inspirationapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.io.File;

/**
 *
 */

public class DisplayActivity extends AppCompatActivity {

    // Variables for widgets.
    EditText mDisplayNoteEntry;
    ImageView mDisplayNewPicture;
    EditText mDisplaySearchField;
    Button mDisplaySearchButton;
    TableLayout mDisplayTable;

//    private final Activity context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayer);

        mDisplayNoteEntry = (EditText) findViewById(R.id.display_noteEntry);
        mDisplayNewPicture = (ImageView) findViewById(R.id.display_newPicture);
        mDisplaySearchField = (EditText) findViewById(R.id.display_searchField);
        mDisplaySearchButton = (Button) findViewById(R.id.display_searchButton);
        mDisplayTable = (TableLayout) findViewById(R.id.display_table);

//        Intent intent = getIntent();
//        int whichOption = intent.getIntExtra(MainActivity.OPT_KEY, 0);
//        String text = intent.getStringExtra(MainActivity.TEXT_KEY);
//
//        displayOption(whichOption, text);
    }




    private void displayOption(Bundle bundle) {
        int whichOption = bundle.getInt(MainActivity.OPT_KEY,0);
        String text = bundle.getString(MainActivity.TEXT_KEY);
        switch (whichOption) {
            case 1:
                mDisplayNoteEntry.setVisibility(View.VISIBLE);
                mDisplayNoteEntry.setText(text);
            case 2:
                mDisplayNewPicture.setVisibility(View.VISIBLE);
                mDisplayNewPicture.setImageURI(Uri.fromFile(new File(text)));
            case 3:
                mDisplaySearchField.setVisibility(View.VISIBLE);
                mDisplaySearchButton.setVisibility(View.VISIBLE);
                mDisplayTable.setVisibility(View.VISIBLE);
            default:

        }
    }
}
