package indiabeeps.app.bayanapp;
import static com.google.android.material.internal.ViewUtils.hideKeyboard;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class HadisActivity extends AppCompatActivity {

    private Spinner bookSpinner;
    private EditText etHadisNo;
    private Button btnLoadHadis, btnOpenBrowser, btnOpenBrowseSearch;
    private WebView webView;
    private int selectedBookPosition = 0;
    private String currentUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hadis);

        bookSpinner = findViewById(R.id.bookSpinner);
        etHadisNo = findViewById(R.id.etHadisNo);
        btnLoadHadis = findViewById(R.id.btnLoadHadis);
        btnOpenBrowser = findViewById(R.id.btnOpenBrowser);
        btnOpenBrowseSearch = findViewById(R.id.btnOpenBrowseSearch);
        webView = findViewById(R.id.webView);

        // Spinner Data
        List<Pair<String, String>> bookData = new ArrayList<>();
        bookData.add(new Pair<>("புகாரி", "Bukhari"));
        bookData.add(new Pair<>("முஸ்லிம்", "Muslim"));
        bookData.add(new Pair<>("இப்னு மாஜா", "Ibn-Majah"));
        bookData.add(new Pair<>("அபூதாவூத்", "Abu-Dawood"));
        bookData.add(new Pair<>("திர்மிதீ", "Tirmidhi"));
        bookData.add(new Pair<>("நஸாயி", "Nasaayi"));
        bookData.add(new Pair<>("மாலிக்", "muwatta-malik"));
        bookData.add(new Pair<>("அஹ்மத்", "Musnad-Ahmad"));
        bookData.add(new Pair<>("ஹாகிம்", "Hakim"));
        bookData.add(new Pair<>("தாரகுத்னீ", "Daraqutni"));
        bookData.add(new Pair<>("தாரிமீ", "Darimi"));
        bookData.add(new Pair<>("இப்னு குஸைமா", "Ibn-Khuzaymah"));
        bookData.add(new Pair<>("இப்னு ஹிப்பான்", "Ibn-Hibban"));

        bookData.add(new Pair<>("அக்பாரு அஸ்பஹான்", "akhbar-asbahan"));
        bookData.add(new Pair<>("அல்அதபுல் முஃப்ரத்", "Al-Adabul-Mufrad"));
        bookData.add(new Pair<>("அல்ஆதாப் பைஹகீ", "al-aaadab-lil-bayhaqi"));
        bookData.add(new Pair<>("அல்முஃஜமுல் அவ்ஸத்", "almujam-alawsat"));
        bookData.add(new Pair<>("அல்முஃஜமுல் கபீர்", "Almujam-Alkabir"));
        bookData.add(new Pair<>("அல்முஃஜமுஸ் ஸகீர்", "Almujam-Assaghir"));
        bookData.add(new Pair<>("இலல் இப்னு அபீஹாதிம்", "ibn-abi-hatim"));
        bookData.add(new Pair<>("குப்ரா நஸாயி", "Kubra-Nasaayi"));
        bookData.add(new Pair<>("குப்ரா நஸாயீ", "Kubra-Nasaayi"));
        bookData.add(new Pair<>("குப்ரா பைஹகீ", "kubra-bayhaqi"));
        bookData.add(new Pair<>("தபகாத்துல் குப்ரா", "Tabaqatul-Kubra-Ibn-Sahd"));
        bookData.add(new Pair<>("தலாஇலுன் நுபுவ்வஹ் பைஹகீ", "Dalail-Annubuwwah-Bayhaqi"));
        bookData.add(new Pair<>("தாரீகு பஃக்தாத்", "Tarikh-Baghdad"));
        bookData.add(new Pair<>("திப்புன் நபவீ அபூநுஐம்", "Thibbun-Nabawi-Abu-Nuaym"));
        bookData.add(new Pair<>("முஅத்தா மாலிக்", "Muwatta-Malik"));
        bookData.add(new Pair<>("முஸன்னஃப் அப்துர் ரஸ்ஸாக்", "Musannaf-Abdur-Razzaq"));
        bookData.add(new Pair<>("முஸன்னஃப் இப்னு அபீ ஷைபா", "Musannaf-Ibn-Abi-Shaybah"));
        bookData.add(new Pair<>("முஸ்னத் அபீ யஃலா", "Abi-Yala"));
        bookData.add(new Pair<>("முஸ்னத் இஸ்ஹாக்", "Musnad-Ishaq-Ibn-Rahawayh"));
        bookData.add(new Pair<>("முஸ்னத் தயாலிஸீ", "Tayalisi"));
        bookData.add(new Pair<>("முஸ்னத் பஸ்ஸார்", "Bazzar"));
        bookData.add(new Pair<>("ஷரஹ் மஆனில் ஆஸார்", "Sharh-Maanil-Aasaar"));
        bookData.add(new Pair<>("ஷரஹ் முஷ்கிலில் ஆஸார்", "Sharh-Mushkil-Al-Athar"));
        bookData.add(new Pair<>("ஷுஅபுல் ஈமான்", "Shuabul-Iman"));
        bookData.add(new Pair<>("ஸகீர் பைஹகீ", "Assunan-Assaghir-Bayhaqi"));
        bookData.add(new Pair<>("ஸுனன் குப்ரா பைஹகீ", "Kubra-Bayhaqi"));


        List<String> books = new ArrayList<>();
        for (Pair<String, String> pair : bookData) {
            books.add(pair.first); // Extract book names for the Spinner
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, books);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bookSpinner.setAdapter(adapter);

        bookSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBookPosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedBookPosition = 0;
            }
        });

        // WebView Settings
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        btnLoadHadis.setOnClickListener(v -> {
            hideKeyboard(v);
            String baseUrl = "https://quranandhadis.com/ta/";
            String selectedBookUrl = bookData.get(selectedBookPosition).second.toLowerCase();
            String hadisNumber = etHadisNo.getText().toString();

            if (selectedBookUrl.equals("nil")) {
                currentUrl = baseUrl;
            } else {
                currentUrl = baseUrl + selectedBookUrl + "-" + (hadisNumber.isEmpty() ? "1" : hadisNumber);
            }

            // Load URL in WebView
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(currentUrl);
        });

        btnOpenBrowser.setOnClickListener(v -> {
            if (!currentUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl));
                startActivity(browserIntent);
            }
        });

        btnOpenBrowser.setOnClickListener(v -> {
            String baseUrl = "https://quranandhadis.com/ta/";
            String selectedBookUrl = bookData.get(selectedBookPosition).second.toLowerCase();
            String hadisNumber = etHadisNo.getText().toString();

            if (selectedBookUrl.equals("nil")) {
                currentUrl = baseUrl;
            } else {
                currentUrl = baseUrl + selectedBookUrl + "-" + (hadisNumber.isEmpty() ? "1" : hadisNumber);
            }

            if (!currentUrl.isEmpty()) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl));
                startActivity(browserIntent);
            }
        });

        etHadisNo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {
                hideKeyboard(etHadisNo); // Hide keyboard
                btnLoadHadis.performClick(); // Trigger the "Show Hadith" button action
                return true;
            }
            return false;
        });



    }


    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
