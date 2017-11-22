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
import android.widget.ListView;
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
    ListView mDisplayTable;
    Button mSubmitButton;

    protected static final String EXTRA_FROM_DISPLAYER = "extra from the displayer";
//    private final Activity context;

    private int whichOption;
    private int currRowId;

//    DisplayScreenListener mDisplayScreenListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.displayer);

        mDisplayNoteEntry = (EditText) findViewById(R.id.display_noteEntry);
        mDisplayNewPicture = (ImageView) findViewById(R.id.display_newPicture);
        mDisplaySearchField = (EditText) findViewById(R.id.display_searchField);
        mDisplaySearchButton = (Button) findViewById(R.id.display_searchButton);
        mDisplayTable = (ListView) findViewById(R.id.display_table);
        mSubmitButton = (Button) findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean noteChanged = hideOption();
                Bundle bundle = new Bundle();
                if (noteChanged) {
                    String newNote = mDisplayNoteEntry.getText().toString();
                    bundle.putInt(MainActivity.ID_KEY, currRowId);
                    bundle.putString(MainActivity.NOTE_KEY, newNote);
                }
                returnIntent(bundle);
            }
        });

        Intent intent = getIntent();
        int whichOption = intent.getIntExtra(MainActivity.OPT_KEY, 0);
        String text = intent.getStringExtra(MainActivity.TEXT_KEY);
        int rowId = intent.getIntExtra(MainActivity.ID_KEY, 0);

        displayOption(rowId, text, whichOption);
    }




    private void displayOption(int rowId, String text, int which) {
//        int whichOption = bundle.getInt(MainActivity.OPT_KEY,0);
//        String text = bundle.getString(MainActivity.TEXT_KEY);
        whichOption = which;
        switch (which) {
            case 1:
                mDisplayNoteEntry.setVisibility(View.VISIBLE);
                mDisplayNoteEntry.setText(text);
                currRowId = rowId;
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

    private boolean hideOption() {
        switch (whichOption) {
            case 1:
                mDisplayNoteEntry.setVisibility(View.INVISIBLE);
                return true;
            case 2:
                mDisplayNewPicture.setVisibility(View.INVISIBLE);
                break;
            case 3:
                mDisplaySearchField.setVisibility(View.INVISIBLE);
                mDisplaySearchButton.setVisibility(View.INVISIBLE);
                mDisplayTable.setVisibility(View.INVISIBLE);
            default:

        }
        return false;
    }

    private void returnIntent(Bundle bundle) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_FROM_DISPLAYER, bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

//    interface DisplayScreenListener {
//        void
//    }
}
