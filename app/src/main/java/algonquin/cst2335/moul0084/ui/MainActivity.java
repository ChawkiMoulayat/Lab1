package algonquin.cst2335.moul0084.ui;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import android.widget.Toast;
import algonquin.cst2335.moul0084.data.MainViewModel;
import algonquin.cst2335.moul0084.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding variableBinding;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        model = new ViewModelProvider(this).get(MainViewModel.class);
        variableBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(variableBinding.getRoot());
        variableBinding.mybutton.setOnClickListener(click -> {
            model.editString.postValue(variableBinding.myedittext.getText().toString());
        });
        model.editString.observe(this, s -> {
            String newText = "Your edit text has: " + s;
            variableBinding.textview.setText(newText);
        });
        model.isSelected.observe(this, selected -> {
            variableBinding.mycheckbox.setChecked(selected);
            variableBinding.myswitch.setChecked(selected);
            variableBinding.myradio.setChecked(selected);
        });
        variableBinding.mycheckbox.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("CheckBox clicked");
        });
        variableBinding.myswitch.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("Switch clicked");
        });
        variableBinding.myradio.setOnCheckedChangeListener((btn, isChecked) -> {
            model.isSelected.postValue(isChecked);
            showToast("Radio button clicked");
        });
        variableBinding.imageView.setOnClickListener( v -> {
               showToast("The width ="+ v.getWidth() + " and the height =" + v.getHeight());
        });

    }
        private void showToast(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        }

}
