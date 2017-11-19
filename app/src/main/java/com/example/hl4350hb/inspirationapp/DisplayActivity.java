package com.example.hl4350hb.inspirationapp;

import android.app.Activity;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;

import java.io.File;

/**
 *
 */

public class DisplayActivity {

    // Variables for widgets.
    EditText mDisplayNoteEntry;
    ImageView mDisplayNewPicture;
    EditText mDisplaySearchField;
    Button mDisplaySearchButton;
    TableLayout mDisplayTable;

    private final Activity context;

    public DisplayActivity(Activity context, int whichOption, String note, String image) {
        this.context = context;

        displayOption(whichOption, note, image);
    }




    private void displayOption(int whichOption, String note, String image) {
        switch (whichOption) {
            case 1:
                mDisplayNoteEntry.setVisibility(View.VISIBLE);
                mDisplayNoteEntry.setText(note);
            case 2:
                mDisplayNewPicture.setVisibility(View.VISIBLE);
                mDisplayNewPicture.setImageURI(Uri.fromFile(new File(image)));
            case 3:
                mDisplaySearchField.setVisibility(View.VISIBLE);
                mDisplaySearchButton.setVisibility(View.VISIBLE);
                mDisplayTable.setVisibility(View.VISIBLE);
            default:

        }
    }
}
