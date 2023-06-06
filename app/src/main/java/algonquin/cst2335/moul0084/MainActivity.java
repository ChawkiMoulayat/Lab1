package algonquin.cst2335.moul0084;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    Switch sw;

    @Override
    protected void onResume() {
        super.onResume();
        Log.w( "MainActivity", "In onCreate() - The application is now responding to user input" );
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.w( "MainActivity", "In onCreate() -  The application no longer responds to user input." );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.w( "MainActivity", "In onCreate() - The application is no longer visible." );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.w( "MainActivity", "In onCreate() - Any memory used by the application is freed." );
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.w( "MainActivity", "In onCreate() - The application is now visible on screen." );

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.w( "MainActivity", "In onCreate() - Loading Widgets" );

        Log.w( "MainActivity", "In onCreate() - is the first function that gets created when an application is launched." );
        Button button = findViewById(R.id.loginButton);
        EditText emailEditText = findViewById(R.id.emailEditText);
        button.setOnClickListener(clk -> {
            // Perform the action when the loginButton is clicked
            // Launch the second activity
            Intent secondIntent = new Intent(MainActivity.this, SecondActivity.class);
            secondIntent.putExtra( "EmailAddress", emailEditText.getText().toString() );
            startActivity(secondIntent);
        });
    }
}