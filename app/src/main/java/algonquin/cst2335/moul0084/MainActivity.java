package algonquin.cst2335.moul0084;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;

import algonquin.cst2335.moul0084.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    Switch sw;

    private ActivityMainBinding variableBinding;
    @Override
    protected void onResume() {
        super.onResume();
        Log.w("MainActivity", "In onResume() - The application is now responding to user input");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w("MainActivity", "In onPause() - The application no longer responds to user input.");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w("MainActivity", "In onStop() - The application is no longer visible.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w("MainActivity", "In onDestroy() - Any memory used by the application is freed.");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w("MainActivity", "In onStart() - The application is now visible on screen.");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        SharedPreferences sharedPreferences = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        // String emailAddress = sharedPreferences.getString("LoginName", "");
        SharedPreferences prefs = getSharedPreferences("MyData", Context.MODE_PRIVATE);
        String emailAddress = prefs.getString("EmailAddress", "");
        TextView emailEditText = variableBinding.emailEditText;
        // Set the email address to the TextView
        emailEditText.setText(emailAddress);
        Log.w("MainActivity", "In onCreate() - Loading Widgets");

        Log.w("MainActivity", "In onCreate() - This is the first function that gets created when an application is launched.");
        Button button = variableBinding.loginButton;

        button.setOnClickListener(clk -> {
            // Retrieve the stored email address from SharedPreferences
            String storedEmailAddress = prefs.getString("email", "");

            // Get the entered email address from the EditText
            String email = emailEditText.getText().toString();

            // Check if the login email address is different from the stored email address
            if (!storedEmailAddress.equals(email)) {
                // Reset the phone number and image
                SharedPreferences.Editor resetEditor = prefs.edit();
                resetEditor.remove("PhoneNumber");
                resetEditor.remove("ImageUri");
                resetEditor.apply();
            }

            // Store the current email address in SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.apply();

            // Launch the second activity
            Intent secondIntent = new Intent(MainActivity.this, SecondActivity.class);
            secondIntent.putExtra("email", email);
            startActivity(secondIntent);
        });
    }
}
