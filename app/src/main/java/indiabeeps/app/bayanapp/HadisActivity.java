package indiabeeps.app.bayanapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import indiabeeps.app.bayanapp.payment.PaymentActivity;

public class HadisActivity extends AppCompatActivity {

    WebView wb;
    Spinner hadisSpinner;
    EditText etHadisNo;
    LinearLayout layWhyAddon;
    Button btnContribute;
    ImageButton imgback, imgforward;
    SharedPreferences prefs;
    boolean isDarkMode;

    int HadisPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load theme from preferences
        prefs = getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("dark_mode", false);
        setTheme(isDarkMode ? R.style.DarkAppTheme : R.style.AppTheme);

        setContentView(R.layout.activity_hadis);
        hadisSpinner = findViewById(R.id.hadisSpinner);
        wb = findViewById(R.id.webViewHadis);
        etHadisNo = findViewById(R.id.etHadisNo);
        layWhyAddon = findViewById(R.id.layWhyAddon);
        imgback = findViewById(R.id.imgback);
        imgforward = findViewById(R.id.imgforward);
        btnContribute = findViewById(R.id.btnContribute);

        wb.setWebViewClient(new WebViewClient());

        imgback.setVisibility(View.INVISIBLE);
        imgforward.setVisibility(View.INVISIBLE);

        ShowSpinnerContent();
        hadisSpinner.setSelection(0);
        LoadContributeInWebview();
    }

    private void LoadContributeInWebview() {
        String data = getResources().getString(R.string.whyaddon);

        // Apply dark mode styling if enabled
        String style = "<style>" +
                "body { background-color: " + (isDarkMode ? "#121212" : "#FFFFFF") + "; color: " + (isDarkMode ? "#FFFFFF" : "#000000") + "; }" +
                "</style>";

        // Construct the full HTML content with styling
        String content = "<html><head>" + style + "</head><body>" + data + "</body></html>";

        wb.loadDataWithBaseURL(null, content, "text/html", "UTF-8", null);

        btnContribute.setVisibility(View.VISIBLE);
        layWhyAddon.setVisibility(View.INVISIBLE);
        wb.setVisibility(View.VISIBLE);
        imgback.setVisibility(View.INVISIBLE);
        imgforward.setVisibility(View.INVISIBLE);
    }

    private void ShowSpinnerContent() {
        List<String> values = new ArrayList<>();

        values.add("புஹாரி");
        values.add("முஸ்லிம்");
        values.add("திர்மிதி");
        values.add("நஸாயி");
        values.add("மற்றவை"); // Nil, index = 4

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hadisSpinner.setAdapter(adapter);

        hadisSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                HadisPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    public void OpenInBrowser(View view) {
        hideKeyboardFarook();

        List<String> Web = new ArrayList<>();
        Web.add("bukhari");
        Web.add("muslim");
        Web.add("tirmidhi");
        Web.add("nasaayi");
        Web.add("nil");  // index = 4

        String url = "https://quranandhadis.com/ta/";

        if (HadisPosition == 4) {
            url = "https://quranandhadis.com/ta/";
        } else {
            url = "https://quranandhadis.com/ta/" + Web.get(HadisPosition) + "-" + etHadisNo.getText();
        }

        btnContribute.setVisibility(View.INVISIBLE);
        wb.setVisibility(View.VISIBLE);
        layWhyAddon.setVisibility(View.INVISIBLE);
        imgback.setVisibility(View.VISIBLE);
        imgforward.setVisibility(View.VISIBLE);

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
    }

    public void OpenHadis(View view) {
        hideKeyboardFarook();

        List<String> Web = new ArrayList<>();
        Web.add("bukhari");
        Web.add("muslim");
        Web.add("tirmidhi");
        Web.add("nasaayi");
        Web.add("nil");  // index = 4

        if (HadisPosition == 4) {
            wb.loadUrl("https://quranandhadis.com/ta/");
        } else {
            wb.loadUrl("https://quranandhadis.com/ta/" + Web.get(HadisPosition) + "-" + etHadisNo.getText());
        }

        btnContribute.setVisibility(View.INVISIBLE);
        wb.setVisibility(View.VISIBLE);
        layWhyAddon.setVisibility(View.INVISIBLE);
        imgback.setVisibility(View.VISIBLE);
        imgforward.setVisibility(View.VISIBLE);
    }

    void hideKeyboardFarook() {
        try {
            InputMethodManager inputManager;
            inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

            if (inputManager != null) {
                // Check if no view has focus:
                View view = this.getCurrentFocus();
                if (view != null) {
                    if (view.getWindowToken() != null) {
                        inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenSupport(View view) {
        startActivity(new Intent(this, PaymentActivity.class));
    }

    public void GoBack(View view) {
        if (wb.canGoBack()) wb.goBack();
    }

    public void GoForward(View view) {
        if (wb.canGoForward()) wb.goForward();
    }
}
