package dev.tatsuno.tweetclipperserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private TextView textViewApplied;
    private EditText editTextAltURL;
    private Button applyButton;

    private String appliedAltURL;
    private String urlSuffix = "{tweet_id}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        applyButton = findViewById(R.id.button);
        editTextAltURL = findViewById(R.id.editTextAltURL);
        textViewApplied = findViewById(R.id.textViewAltURL);

        SharedPreferences sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
        appliedAltURL = sp.getString("alt_url", "https://twitter.com/x/") + urlSuffix;
        textViewApplied.setText(appliedAltURL);

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write new URL
                SharedPreferences sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
                String editedURL = editTextAltURL.getText().toString();
                sp.edit().putString("alt_url", editedURL).apply();

                // Read
                appliedAltURL = sp.getString("alt_url", "https://twitter.com/x/") + urlSuffix;
                textViewApplied.setText(appliedAltURL);
            }
        });
    }
}
