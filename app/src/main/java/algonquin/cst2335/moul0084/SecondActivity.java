package algonquin.cst2335.moul0084;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class SecondActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView profileImage;
    private ActivityResultLauncher<Intent> cameraResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String phoneNumber = prefs.getString("PhoneNumber", "");
        EditText editTextPhone = findViewById(R.id.editTextPhone);
        editTextPhone.setText(phoneNumber);
        TextView emailTextView = findViewById(R.id.textView);
        Intent fromPrevious = getIntent();
        String emailAddress = fromPrevious.getStringExtra("EmailAddress");
        emailTextView.setText("Welcome back " + emailAddress);

        profileImage = findViewById(R.id.profileImage);

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
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

        EditText editTextPhone = findViewById(R.id.editTextPhone);
        String phoneNumber = editTextPhone.getText().toString();

        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("PhoneNumber", phoneNumber);
        editor.apply();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            cameraResult.launch(takePictureIntent);
        }
    }
}
