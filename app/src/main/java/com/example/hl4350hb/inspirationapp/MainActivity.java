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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
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


    // Identifier code for the camera returning a result.
    private static final int CAMERA_ACCESS_REQUEST_CODE = 0;
    // Identifier code for returning results.
    private static final int SAVE_IMAGE_PERMISSION_REQUEST_CODE = 1001;


    // Will contain the location of the file on device.
    private String mImagePath;
    // Holds the image in variable.
    private Bitmap mImage;




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

        // Defines click event for button.
        mPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takePhoto();
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
                Toast.makeText(this, "All pictures will not be saved", Toast.LENGTH_SHORT).show();
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
            // Designate the filename for picture using the current time.
            String imageFilename = "inspiration_from_" + new Date().getTime();

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
