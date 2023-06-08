package algonquin.cst2335.moul0084;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import android.net.Uri;
import android.content.pm.PackageManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import algonquin.cst2335.moul0084.databinding.ActivityMainBinding;
import algonquin.cst2335.moul0084.databinding.ActivitySecondBinding;

public class SecondActivity extends AppCompatActivity {
    private ActivitySecondBinding variableBinding;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView profileImage;
    private ActivityResultLauncher<Intent> cameraResult;
    private Uri imageUri;
    private String emailAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("PhoneNumber", "");
        EditText editTextPhone = variableBinding.editTextPhone;
        editTextPhone.setText(phoneNumber);
        TextView emailTextView = variableBinding.textView;
        Intent fromPrevious = getIntent();
        emailAddress = fromPrevious.getStringExtra("email");
        emailTextView.setText("Welcome back " + emailAddress);
        profileImage = variableBinding.imageView;
        String imageUriString = prefs.getString("ImageUri", null);

        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap imageBitmap = BitmapFactory.decodeStream(imageStream);
                profileImage.setImageBitmap(imageBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        Button buttonPicture = variableBinding.buttonPicture;
        buttonPicture.setOnClickListener( v-> {

                dispatchTakePictureIntent();

        });
        Button callButton = variableBinding.button;
        callButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Get the phone number from the EditText
                EditText editTextPhone = variableBinding.editTextPhone;
                String phoneNumber = editTextPhone.getText().toString();

                // Create an intent to initiate the phone call
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));

               // SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
                String previousEmail = prefs.getString("EmailAddress", "");

                if (!previousEmail.equals(emailAddress)) {
                    profileImage.setImageResource(0); // Clear the image
                    editTextPhone.setText(""); // Clear the phone number
                }

                // Save the current email address in SharedPreferences
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("EmailAddress", emailAddress);
                editor.apply();

                // Check if the device supports making phone calls
                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    // Start the phone call activity
                    startActivity(callIntent);
                } else {
                    Toast.makeText(SecondActivity.this, "Phone call not supported", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cameraResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            Bundle extras = data.getExtras();
                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            profileImage.setImageBitmap(imageBitmap);
                        }
                    }
                });
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Save the phone number in SharedPreferences
        EditText editTextPhone = variableBinding.editTextPhone;
        String phoneNumber = editTextPhone.getText().toString();

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneNumber);
        String previousEmail = prefs.getString("EmailAddress", "");
        if (!previousEmail.equals(emailAddress)) {
            // Reset the phone number and image
            profileImage.setImageResource(0); // Clear the image
            editTextPhone.setText(""); // Clear the phone number
        }

        // Save the current email address in SharedPreferences
        editor.putString("EmailAddress", emailAddress);
        editor.apply();

        // Save the image URI in SharedPreferences
        Uri currentImageUri = null;
        Drawable drawable = profileImage.getDrawable();
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            currentImageUri = saveImageToStorage(bitmap);
        }

        if (currentImageUri != null) {
            editor.putString("ImageUri", currentImageUri.toString());
        }

        editor.apply();
    }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraResult.launch(takePictureIntent);
        }
    }
    private Uri saveImageToStorage(Bitmap imageBitmap) {
        // Create a file to save the image
        File imagesDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(imagesDir, "profile_image_" + emailAddress + ".jpg");

        try {
            // Compress the image and write it to the file
            FileOutputStream outputStream = new FileOutputStream(imageFile);
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            imageUri = Uri.fromFile(imageFile);
            return imageUri;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null ;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    if (imageBitmap != null) {
                        saveImageToStorage(imageBitmap);
                    }
                }
            }
        }
    }
}
