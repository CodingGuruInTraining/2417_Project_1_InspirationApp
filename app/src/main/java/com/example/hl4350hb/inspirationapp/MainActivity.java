package com.example.hl4350hb.inspirationapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
//     ############################################################

//       ##################     GLOBALS     ######################

//     ############################################################

    // Logging tag to identify custom messages.
    private static final String TAG = "thisIsMyTag";
    // Key value used to maintain image during rotations.
    private static final String IMAGE_FILEPATH_KEY = "aKeyForFilepath";


    // Creates global references to widgets.
    Button mPicButton;
    ImageView mNewPicture;
    ListView mListView;
    EditText mNoteEntry;


    // Identifier code for the camera returning a result.
    private static final int CAMERA_ACCESS_REQUEST_CODE = 0;
    // Identifier code for returning results.
    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1001;


    // Will contain the location of the file on device.
    private String mImagePath;
    // Holds the image in variable.
    private Bitmap mImage;



    protected CustomList adapter;
    private long currTime;


    // Will contain the notes for each picture.
    ArrayList<String> notesArray = new ArrayList<String>();
    // Will contain the image ID for each picture.
    ArrayList<String> imageIdArray = new ArrayList<String>();



//     ############################################################

//       ##############     onCreate Magic     #################

//     ############################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Loads image when rotated so it is not completely lost.
        if (savedInstanceState != null) {
            mImagePath = savedInstanceState.getString(IMAGE_FILEPATH_KEY);
        }

        // Defines widget variables for global use.
        mPicButton = (Button) findViewById(R.id.picButton);
        mNewPicture = (ImageView) findViewById(R.id.newPicture);
        mNoteEntry = (EditText) findViewById(R.id.noteEntry);

        // Defines click event for button.
        mPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
            }
        });




        mListView = (ListView) findViewById(R.id.picList);
// TODO update ArrayLists from database and then pass to CustomList here
        adapter = new CustomList(MainActivity.this, notesArray, imageIdArray);

        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
// TODO change to an event or something
                Toast.makeText(MainActivity.this, "You Clicked " + notesArray.get(+ position), Toast.LENGTH_SHORT).show();
            }
        });

        // Forces widgets to stay where they are when the keyboard appears.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);


        mNoteEntry.setFocusableInTouchMode(true);
        mNoteEntry.requestFocus();
        mNoteEntry.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {


                    String newNote = mNoteEntry.getText().toString();
                    adapter.addNewEntry(newNote, mImagePath);
                    mNoteEntry.getText().clear();
                    mNoteEntry.setVisibility(View.GONE);

                    mNewPicture.setImageResource(android.R.color.transparent);

                    Toast.makeText(MainActivity.this, "note saved!", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

    }





//     ############################################################

//       #############     OTHER OVERRIDES     #################

//     ############################################################

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CAMERA_ACCESS_REQUEST_CODE) {
            saveImage();
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // Fits picture to ImageView widget.
        if (hasFocus && mImagePath != null) {
            scalePicture();
            mNewPicture.setImageBitmap(mImage);

// TODO add notes here?
            mNoteEntry.setVisibility(View.VISIBLE);

        }
    }


    @Override
    public void onSaveInstanceState(Bundle outBundle) {
        outBundle.putString(IMAGE_FILEPATH_KEY, mImagePath);
    }


    @Override   // Callback for adding an image to the device's MediaStore.
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == SAVE_IMAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Save image
                MediaStore.Images.Media.insertImage(getContentResolver(), mImage, "InspirationApp", "Photo take by InspirationApp");
            } else {
                Toast.makeText(this, "All pictures will saved..NOT!", Toast.LENGTH_SHORT).show();
            }
        }
    }






//     ############################################################

//        ############     CUSTOM FUNCTIONS     ################

//     ############################################################

    private void takePhoto() {
        // Creates a new Intent object.
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Check if the current device has a camera before continuing.
        if (pictureIntent.resolveActivity(getPackageManager()) == null) {
            Toast.makeText(MainActivity.this, "A camera is needed to take pictures", Toast.LENGTH_LONG).show();
        } else {
            currTime = new Date().getTime();
            // Designate the filename for picture using the current time.
            String imageFilename = "inspiration_from_" + currTime; // new Date().getTime();

            // Designate temporary location of new file.
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = null;
            Uri imageFileUri = null;

            // Tries to save image.
            try {
                // Creates temporary file using name created above and stores in
                // designated storage location.
                imageFile = File.createTempFile(imageFilename, ".jpg", storageDirectory);
                // Save the image's location to be used later.
                mImagePath = imageFile.getAbsolutePath();

                Log.d(TAG, "the image id is " + R.drawable.image2);
                // Defines the image location and how to access it for the camera.
                imageFileUri = FileProvider.getUriForFile(MainActivity.this, "com.example.hl4350hb.inspirationapp", imageFile);
            } catch (IOException err) {
                // Can't go any further at this point.
                Log.e(TAG, "ERROR: There was a problem creating file for storing on device", err);
                return;
            }
            // Adds the Uri to the Intent so it can be transported in app.
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);

            // Request the device's camera starts.
            startActivityForResult(pictureIntent, CAMERA_ACCESS_REQUEST_CODE);
        }
    }


    private void scalePicture() {
        // Figures out the picture's dimensions.
        int imageViewHeight = mNewPicture.getHeight();
        int imageViewWidth = mNewPicture.getWidth();

        // Checks whether either value is 0 and stops progress.
        if (imageViewHeight == 0 || imageViewWidth == 0) {
            Log.w(TAG, "ERROR: The picture size is not scalable");
            return;
        }

        // Creates BitmapFactory object to store picture as pixels.
        BitmapFactory.Options bOptions = new BitmapFactory.Options();
        bOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(mImagePath, bOptions);

        // Retrieves dimensions.
        int pictureHeight = bOptions.outHeight;
        int pictureWidth = bOptions.outWidth;

        // Determines the scaling factor.
        int scaleFactor = Math.min(pictureHeight / imageViewHeight, pictureWidth / imageViewWidth);

        // Decodes the picture into a new image file while also scaling its size.
        bOptions.inJustDecodeBounds = false;
        bOptions.inSampleSize = scaleFactor;

        Bitmap bitmap = BitmapFactory.decodeFile(mImagePath, bOptions);
        mImage = bitmap;

    }


    private void saveImage() {
        // Adds image to MediaStore so it can be accessed by the gallery app and others.

        // Check if app has correct permissions before continuing.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            MediaStore.Images.Media.insertImage(getContentResolver(), mImage, "InspirationApp", "Image taken by InspirationApp");
        } else {
            // Prompts user to accept the permission request.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, SAVE_IMAGE_PERMISSION_REQUEST_CODE);
        }
    }
}





// References:
    // picture list setup - https://www.learn2crack.com/2013/10/android-custom-listview-images-text-example.html
    // adjust nothing when keyboard active - https://stackoverflow.com/questions/4207880/android-how-do-i-prevent-the-soft-keyboard-from-pushing-my-view-up
    // ENTER key event - https://stackoverflow.com/questions/8233586/android-execute-function-after-pressing-enter-for-edittext
    // loading picture from string - https://stackoverflow.com/questions/3004713/get-content-uri-from-file-path-in-android
    // "hiding" imageview - https://stackoverflow.com/questions/2859212/how-to-clear-an-imageview-in-android

