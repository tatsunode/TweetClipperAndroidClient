package dev.tatsuno.tweetclipperserver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class ClippingActivity extends AppCompatActivity {

    private static final String TAG = "ClippingActivity";
    private String altURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clipping);

        SharedPreferences sp = getSharedPreferences("settings", Context.MODE_PRIVATE);
        altURL = sp.getString("alt_url", "https://twitter.com/x/");

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action == null || !action.equals(Intent.ACTION_SEND)) {
            // Not Target
            finish();
        }

        intent.removeExtra("org.chromium.chrome.extra.TASK_ID");
        intent.removeExtra("share_screenshot_as_stream");

        Bundle extras = intent.getExtras();

        ClipData clipData = intent.getClipData();
        ClipData.Item item = clipData.getItemAt(0);
        String clipItemText = (String) item.getText();

        Log.d(TAG, clipData.toString());
        Log.d(TAG, item.toString());

        for (final String key: extras.keySet()) {
            Log.d(TAG, "");
            Log.d(TAG, "KEY: " + key);
            Object extra = extras.get(key);
            Log.d(TAG, "VALUE: " + extra.toString());
        }

        String tweetId = "";
        if(extras != null) {
            CharSequence textChar = extras.getCharSequence(Intent.EXTRA_TEXT);
            String text = String.valueOf(textChar);
            if(text != null) {
                Log.d(TAG, "------");
                Log.d(TAG, text);
                Log.d(TAG, "------");
            }
            Object tweetIdObj = extras.get("tweet_id");
            tweetId = tweetIdObj.toString();
        }

        Uri newUri = Uri.parse(altURL + tweetId);

        // Construct Intent to Evernote Clipper
        // String[] mimeType = new String[1];
        // mimeType[0] = ClipDescription.MIMETYPE_TEXT_PLAIN;
        // ClipData newClipData = new ClipData(new ClipDescription("text_data", mimeType), new ClipData.Item(newUri));
        // intent.setClipData(newClipData);
        // intent.setComponent(new ComponentName("com.evernote", "com.evernote.clipper.ClipActivity"));
        // startActivity(intent);

        Intent evernote = new Intent();
        evernote.setComponent(new ComponentName("com.evernote", "com.evernote.clipper.ClipActivity"));
        // evernote.setAction(Intent.ACTION_SEND);
        // evernote.setData(Uri.parse(newUri.toString()));
        // evernote.setType("text/plain");
        evernote.putExtra(Intent.EXTRA_TEXT, newUri.toString());
        evernote.putExtra(Intent.EXTRA_STREAM, newUri.toString());
        startActivity(evernote);

        finish();
    }
}
