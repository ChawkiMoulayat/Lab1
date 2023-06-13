package algonquin.cst2335.moul0084;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import algonquin.cst2335.moul0084.databinding.ActivityMainBinding;

/**
 * MainActivity class represents the main activity of a login with password complexity check.
 *
 * @Author : Chawki Moulayat
 * Version : 1.0
 */
public class MainActivity extends AppCompatActivity {
    /**
     * The binding object for the activity's layout.
     */
    private ActivityMainBinding binding;

    /**
     * The TextView for displaying text.
     */
    private TextView tv = null ;

    /**
     * The EditText for user input.
     */
    private EditText et = null ;

    /**
     * The Button for triggering the password complexity check.
     */
    private Button btn = null ;
    /**
     * Called when the activity is starting.
     *
     * @param savedInstanceState Bundle object.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views
        tv = binding.textView4;
        et = binding.editText1;
        btn = binding.button;

        // Set click listener for the button
        btn.setOnClickListener(clk -> {
            String password = et.getText().toString();
            passwordComplexity(password);
        });
    }

    /**
     * Checks the complexity of the provided password and displays corresponding Toast messages.
     *
     * @param password The password to check.
     */
    void passwordComplexity(String password) {
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;
        boolean hasSpecialSymbol = false;
        boolean missingRequirement = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasNumber = true;
            } else if ("#$%^&*!@?".contains(String.valueOf(ch))) {
                hasSpecialSymbol = true;
            } else {
                missingRequirement = true;
            }
        }

        // Display Toast messages based on password complexity
        if (!hasUppercase) {
            Toast.makeText(MainActivity.this, "Your password does not have an uppercase letter", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass");
        } else if (!hasLowercase) {
            Toast.makeText(this, "Your password does not have a lowercase letter", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass");
        } else if (!hasNumber) {
            Toast.makeText(this, "Your password does not have a number", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass");
        } else if (!hasSpecialSymbol) {
            Toast.makeText(this, "Your password does not have a special symbol", Toast.LENGTH_SHORT).show();
            tv.setText("You shall not pass");
        } else if (!missingRequirement) {
            Toast.makeText(this, "Your password meets all requirements", Toast.LENGTH_SHORT).show();
            tv.setText("Your password is complex enough");
        }
    }
}
