package indiabeeps.app.bayanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import indiabeeps.app.bayanapp.payment.PaymentActivity;

public class HadisActivity extends AppCompatActivity {

    WebView wb;
    ListView listView;
    EditText etHadisNo;
    LinearLayout layWhyAddon;
    Button btnContribute;
    ImageButton imgback, imgforward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hadis);
        listView = findViewById(R.id.hadisListView);
        wb = findViewById(R.id.webViewHadis);
        etHadisNo = findViewById(R.id.etHadisNo);
        layWhyAddon = findViewById(R.id.layWhyAddon);
        imgback = findViewById(R.id.imgback);
        imgforward = findViewById(R.id.imgforward);
        btnContribute = findViewById(R.id.btnContribute);

        wb.setWebViewClient(new WebViewClient());

        imgback.setVisibility(View.INVISIBLE);
        imgforward.setVisibility(View.INVISIBLE);

        ShowLstContent();
        listView.setSelection(0);
        LoadContributeInWebview();
    }

    private void  LoadContributeInWebview(){
        //data == html data which you want to load
        //String data = "Your data which you want to load";
        String data = getResources().getString(R.string.whyaddon);

        //WebView webview = (WebView)this.findViewById(R.id.webView);
        //webview.getSettings().setJavaScriptEnabled(true);
        wb.loadData(data, "text/html; charset=utf-8", "UTF-8");
        wb.loadUrl("file:///android_asset/whyaddon.html");

        btnContribute.setVisibility(View.VISIBLE);
        layWhyAddon.setVisibility(View.INVISIBLE);
        wb.setVisibility(View.VISIBLE);
        imgback.setVisibility(View.INVISIBLE);
        imgforward.setVisibility(View.INVISIBLE);

    }

    private void ShowLstContent(){
        List<String> values = new ArrayList<>();

        values.add("புஹாரி");
        values.add("முஸ்லிம்");
        values.add("திர்மிதி");
        values.add("நஸாயி");
        values.add("மற்றவை"); //Nil, index = 4

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.hadislistitem, values);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(
        new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick (AdapterView < ? > adapter, View view, int position, long arg){
                    // TODO Auto-generated method stub
                    TextView v = (TextView) view.findViewById(R.id.txtHadisName);
                    listView.setSelection(position);
                    listView.setItemChecked( position, true);

                    for (int j = 0; j < adapter.getChildCount(); j++)
                        adapter.getChildAt(j).setBackgroundColor(Color.rgb(245, 245, 245)); //48, 176, 255

                        //adapter.getChildAt(j).setBackgroundColor(Color.rgb(48, 176, 255));

                    // change the background color of the selected element //99, 196, 255 //48, 176, 255
                    view.setBackgroundColor(Color.rgb(99, 196, 255));

                    //view.setBackgroundColor(Color.argb(100, 255, 128, 0));
                    HadisPosition = position;

                    //Toast.makeText(getApplicationContext(), "selected Item Name is " + v.getText(), Toast.LENGTH_LONG).show();
                }
            }
        );

        // listView.setSelection(0);
        // listView.getSelectedView().setSelected(true);
        // listView.getAdapter().getView(0, null, null).performClick();
        // listView.setSelection(0);
    }

    int HadisPosition = 0;


    public void OpenHadis(View view) {

        //InputMethodManager inputManager = (InputMethodManager)  getSystemService(Context.INPUT_METHOD_SERVICE);

        //inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS);
        hideKeyboardFarook();

        List<String> Web = new ArrayList<>();
        Web.add("bukhari");
        Web.add("muslim");
        Web.add("tirmidhi");
        Web.add("nasaayi");
        Web.add("nil");  // index = 4

        if(HadisPosition == 4)
        {
            wb.loadUrl("https://quranandhadis.com/ta/");
        }
        else {
            wb.loadUrl("https://quranandhadis.com/ta/" + Web.get(HadisPosition) + "-" + etHadisNo.getText() );
        }

        btnContribute.setVisibility(View.INVISIBLE);
        wb.setVisibility(View.VISIBLE);
        layWhyAddon.setVisibility(View.INVISIBLE);
        imgback.setVisibility(View.VISIBLE);
        imgforward.setVisibility(View.VISIBLE);

        //Toast.makeText(getApplicationContext(), "Sel Index:  " + HadisPosition, Toast.LENGTH_LONG).show();
    }

    void hideKeyboardFarook() {
        try {
                    InputMethodManager inputManager;
                    inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                    if(inputManager != null)
                    {
                        // Check if no view has focus:
                        View view = this.getCurrentFocus();
                        if(view != null)
                        {
                            if(view.getWindowToken() != null)
                            {
                                inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                        }
                    }
            }
        catch (Exception e)
        {
                e.printStackTrace();
        }
    }

    public void OpenSupport(View view) {
        startActivity( new Intent(this, PaymentActivity.class));
    }

    public void GoBack(View view) {
        if(wb.canGoBack()) wb.goBack();
    }

    public void GoForward(View view) {
        if(wb.canGoForward()) wb.goForward();
    }

}