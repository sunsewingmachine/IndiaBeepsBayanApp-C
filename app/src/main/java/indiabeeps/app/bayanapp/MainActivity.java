package indiabeeps.app.bayanapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import indiabeeps.app.bayanapp.payment.PaymentActivity;

import static indiabeeps.app.bayanapp.GeneralFunction.ScrollX;
import static indiabeeps.app.bayanapp.GeneralFunction.ScrollY;
import static indiabeeps.app.bayanapp.GeneralFunction.isScrollingNow;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

//@SuppressLint("NewApi")
public class MainActivity extends Activity implements View.OnClickListener {

    //private List<getAllArticles> itemList;

    DatabaseHandler db;
    String sColorRed = "#ff0000";
    String sColorBlack = "#000000";
    String sColorWhite = "#ffffff";
    String sColorGrey = "#9e9e9e";
    Boolean IsResultScreen;
    String sSearchedText;

    TextView ttc;
    WebView mywv;
    LinearLayout wLayout, LayColors, LayWeb;
    LinearLayout mywLay2, LayNext;
    Button btn1, btn2, btn3, btn4, btn5, btn6, btn7, btn8, btn10;
    TextView btnPrev, btnNext, btnMenu;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    EditText inputSearch;
    int fontSize;//=100;

    RelativeLayout sl;
    ImageButton slh;
    ImageButton searhit;
    // int fontSize=100;


//How to use other or many activity
    // GeneralFunction.ivar1=10;
//  int fontSize =   GeneralFunction.fontsizee;
//    fontSize = 100;
    // int i=Global.ivar1;

    public static String sHadisText;
    public static String myString = "";
    public static String myID = "";
    //private static int SCROLL_SPEED = 500;
    private Menu mMenu;
    private Handler mHandler;
    private Runnable mRunnable;
    private Bundle b;
    Boolean bIsFullScreenNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Toast.makeText(getApplicationContext(), "Opening....", Toast.LENGTH_LONG).show();
        db = new DatabaseHandler(MainActivity.this);

        prefs = getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
        editor = prefs.edit();

        b = getIntent().getExtras();

        GeneralFunction.bJummaMode = prefs.getBoolean(Splash.sJummaMode, true);
        GeneralFunction.bNightMode = prefs.getBoolean("dark_mode", false);
        GeneralFunction.bSmallScreen = prefs.getBoolean(Splash.sSmallScreen, false);
        GeneralFunction.bControlBox = prefs.getBoolean(Splash.sControlBox, false);
        // remove title bar, time bar
        //==============================================================================

        //Why taking this value in another variable?
        //We will change the value of these variables soon, so assigning in another variable!
        bIsFullScreenNow = GeneralFunction.FullScreen || GeneralFunction.FullScreenRun;
        //==============================================================================

        try {
            if (!GeneralFunction.bSmallScreen) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_IGNORE_CHEEK_PRESSES);
            }

            if (GeneralFunction.FullScreen) {
                GeneralFunction.FullScreen = false;
                requestWindowFeature(Window.FEATURE_NO_TITLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set the appropriate theme based on the retrieved value
        if (GeneralFunction.bNightMode) {
            setTheme(R.style.DarkAppTheme);  // Replace with your dark theme
        } else {
            setTheme(R.style.AppTheme);  // Replace with your light theme
        }

        setContentView(R.layout.activity_main);

        try {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mywv = findViewById(R.id.webView);
        //  mywv.getSettings().setBuiltInZoomControls(true);
        ttc = findViewById(R.id.textView4);
        mywLay2 = findViewById(R.id.web_layout);

        setTitle("");

        ttc.setText(Html.fromHtml(prefs.getString("ARTT", "")));
        myString = prefs.getString("CATCON", "");
        myID = prefs.getString("ARTID", "");
        WebSettings settings = mywv.getSettings();

        settings.setJavaScriptEnabled(true); // Enable JavaScript

        IsResultScreen = false;

        //Retrive the Global Value
        fontSize = GeneralFunction.fontsizeGlobal;
        //===============================================
        //SharedPreferences prefs = getSharedPreferences(Splash.sMyAppOptions,Splash.MODE_PRIVATE);
        fontSize = prefs.getInt(Splash.ZoomWebview, 100);
        //===============================================
        //WebView.BackgroundColor(Color.TRANSPARENT);

        isScrollingNow = false;
        GeneralFunction.SCROLL_SPEED = prefs.getInt(Splash.sSCROLL_SPEED, 30);
        //============================================================================

        settings.setTextZoom(fontSize);
        SetWebViewContent();

        //==========================================================
        try {
            if (IsResultScreen) {
                WebViewClient client = new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        if (Build.VERSION.SDK_INT >= 16) {
                            mywv.findAllAsync(sSearchedText);
                        } else {
                            mywv.findAll(sSearchedText);
                        }
                    }
                };
                mywv.setWebViewClient(client);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //==========================================================
        mywv.setBackgroundColor(Color.TRANSPARENT);

        LayNext = findViewById(R.id.LayNext);
        if (bIsFullScreenNow) {
            ShowHideLayNext(false);
        } else {
            ShowHideLayNext(GeneralFunction.bControlBox);
        }
        //==========================================================

        inputSearch = findViewById(R.id.editText);
        slh = findViewById(R.id.imageButton6);
        sl = findViewById(R.id.sl);
        searhit = findViewById(R.id.imageButton7);

        slh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl.setVisibility(View.GONE);
                myString = myString.replaceAll("(\\\\r)", "").replaceAll("(\\\\n)", "</br>");

                /*if(GeneralFunction.LoadDataMethod == 1)
                {
                    mywv.loadData(myString , "text/html; charset=UTF-8", null);
                }
                else if(GeneralFunction.LoadDataMethod == 2)
                {
                    String htmlContent = myString;
                    String encodedHtml = Base64.encodeToString(htmlContent.getBytes(), Base64.NO_PADDING);
                    mywv.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
                }*/

                LoadWebviewData(myString);
                mywv.setBackgroundColor(Color.TRANSPARENT);
            }
        });

        searhit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputSearch.length() > 0) {
                    String newString = myString;
                    newString = newString.replace(inputSearch.getText().toString(), "<mark>" + inputSearch.getText().toString() + "</mark>");
                    myString = myString.replaceAll("(\\\\r)", "").replaceAll("(\\\\n)", "</br>");

                    LoadWebviewData(newString);

                    /*
                    if(GeneralFunction.LoadDataMethod == 1)
                    {
                        mywv.loadData(newString , "text/html; charset=UTF-8", null);
                    }
                    else if(GeneralFunction.LoadDataMethod == 2)
                    {
                        String htmlContent = newString;
                        String encodedHtml = Base64.encodeToString(htmlContent.getBytes(), Base64.NO_PADDING);
                        mywv.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
                    }
                    */
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Something to Search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                mywv.scrollBy(0, 1);
                mHandler.postDelayed(this, GeneralFunction.SCROLL_SPEED);
            }
        };

        /*
        //@Override
        mywv.setWebViewClient(new WebViewClient() {

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(1==1) {
                sHadisText = getHadisText(url);
                Toast.makeText(MainActivity.this, sHadisText,Toast.LENGTH_LONG).show();
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
            }
        });
        */

        SetBtns();
        RunWeb();
        GetScrollPosition();
    }

    private void ShowHideLayNext(boolean visible) {
        if (visible) {
            LayNext.setVisibility(View.VISIBLE);
        } else {
            LayNext.setVisibility(View.GONE);
        }
    }

    private void SetWebViewContent() {
        String DivBefore;
        String DivAfter;

        String OldString1;
        String OldString2;
        String NewString;

        //   Toast.makeText(getApplicationContext(),b.getString("SearchTerm",""),Toast.LENGTH_SHORT).show();

        if (b.getString("SearchTerm", "").length() > 0) {
            IsResultScreen = true;
            sSearchedText = b.getString("SearchTerm", "");
            //DivBefore = "<div style=\"font-size: 20pt;\">";
            //DivAfter = "</div>";
            //newString = DivBefore + myString + DivBefore;
            if (!GeneralFunction.bJummaMode) {
                SetArabicTextSize("14");
            }
            /* NewString = myString;
            NewString = NewString.replace(b.getString("SearchTerm",""),"<mark>"+b.getString("SearchTerm","")+"</mark>");
            mywv.loadData(NewString , "text/html; charset=UTF-8", null); */


            if (GeneralFunction.bNightMode) {
                //Toast.makeText(this, "Night mode" + "/n" + "Set Cat Pos: " + GeneralFunction.bNightMode, Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Night mode", Toast.LENGTH_SHORT).show();
                myString = myString.replace(sColorRed, sColorBlack);
                myString = myString.replace(sColorBlack, sColorGrey);
                DivBefore = "<div style=\"color: " + sColorGrey + " !important;\">";
                DivAfter = "</div>";
                myString = DivBefore + myString + DivAfter;
            }


            myString = myString.replace(b.getString("SearchTerm", ""), "<mark>" + b.getString("SearchTerm", "") + "</mark>");
            myString = myString.replaceAll("(\\\\r)", "").replaceAll("(\\\\n)", "</br>");
            //mywv.loadData(myString , "text/html; charset=UTF-8", null);
            LoadWebviewData(myString);
        }

        else {

            if (GeneralFunction.bNightMode) {
                //Toast.makeText(this, "Night mode" + "/n" + "Set Cat Pos: " + GeneralFunction.bNightMode, Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Night mode", Toast.LENGTH_SHORT).show();
                myString = myString.replace(sColorRed, sColorBlack);
                myString = myString.replace(sColorBlack, sColorGrey);
                DivBefore = "<div style=\"color: " + sColorGrey + " !important;\">";
                DivAfter = "</div>";
                myString = DivBefore + myString + DivAfter;
            }

            if (!GeneralFunction.bJummaMode) {
                SetArabicTextSize("13");
            }

            myString = myString.replaceAll("(\\\\r)", "").replaceAll("(\\\\n)", "</br>");
            //mywv.loadData(myString , "text/html; charset=UTF-8", null);
            LoadWebviewData(myString);
        }
    }

    private void LoadWebviewData2Original(String stringToLoad) {
        if (GeneralFunction.LoadDataMethod == 1) {
            mywv.loadData(stringToLoad, "text/html; charset=UTF-8", null);
        } else if (GeneralFunction.LoadDataMethod == 2) {
            String encodedHtml = Base64.encodeToString(stringToLoad.getBytes(), Base64.NO_PADDING);
            mywv.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
        }
    }

    private String injectCssAndJs(String htmlContent, String cssContent, String jsContent) {

        String jquery = "<script src=\"https://code.jquery.com/jquery-3.6.0.min.js\"></script>";

        // Wrap the CSS content in <style> tag
        String cssTag = "<style>" + cssContent + "</style>";

        // Wrap the JS content in <script> tag
        String jsTag = "<script type=\"text/javascript\">" + jsContent + "</script>";

        // Check if <head> exists, inject inside it. Otherwise, create one.
        if (htmlContent.contains("<head>")) {
            htmlContent = htmlContent.replace("</head>", cssTag + jsTag + " ");
        } else {
            htmlContent = "<head>" + jquery + cssTag + jsTag + "</head>" + htmlContent;
        }

        return htmlContent;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void LoadWebviewData(String stringToLoad) {
        // Load CSS and JS from assets folder
        String cssContent = readAssetFileAsString("quran-popup.css");
        String jsContent = readAssetFileAsString("quran-popup.js");
        // Inject CSS and JS into the HTML content
        stringToLoad = injectCssAndJs(stringToLoad, cssContent, jsContent);

        WebSettings settings = mywv.getSettings();
        settings.setJavaScriptEnabled(true);  // Enable JavaScript
        mywv.setWebChromeClient(new WebChromeClient());

        // Call the method to write to internal storage
        // String fileName = "e3.txt";
        // writeToInternalStorage(fileName, stringToLoad);

        // Load the content into WebView
        if (GeneralFunction.LoadDataMethod == 1) {
            mywv.loadData(stringToLoad, "text/html; charset=UTF-8", null);
        } else if (GeneralFunction.LoadDataMethod == 2) {
            String encodedHtml = Base64.encodeToString(stringToLoad.getBytes(), Base64.NO_PADDING);
            mywv.loadData(encodedHtml, "text/html; charset=UTF-8", "base64");
        }
    }

    private void writeToInternalStorage(String fileName, String content) {
        FileOutputStream fos = null;
        try {
            // Open the file output stream
            fos = openFileOutput(fileName, MODE_PRIVATE);  // MODE_PRIVATE means it will overwrite any existing file with the same name
            fos.write(content.getBytes());  // Write content to the file
            fos.close();
            Log.d("FileWrite", "File written to internal storage successfully");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FileWrite", "Failed to write to file");
        }
    }

    private String readAssetFileAsString(String fileName) {
        try {
            InputStream inputStream = getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");  // Add new line for better formatting
            }
            reader.close();
            inputStream.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "";  // Return an empty string if file is not found
        }
    }


    private void GetScrollPosition() {
        if (GeneralFunction.bShowPreviousArticle) {
            //Toast.makeText(this, "Set as: " + Integer.toString(GeneralFunction.Prev_Pos_Scroll), Toast.LENGTH_SHORT).show();
            mywv.setScrollY(GeneralFunction.Prev_Pos_Scroll);
            //mywv.scrollTo( 0, GeneralFunction.Prev_Pos_Scroll);
        }
    }

    private void SaveCurrentPositions() {
        GeneralFunction.bShowPreviousArticle = false;
        editor.putInt(Splash.sPrev_Pos_Category, GeneralFunction.Prev_Pos_Category_ToSave);
        editor.putInt(Splash.sPrev_Pos_List, GeneralFunction.Prev_Pos_List_ToSave);
        editor.putInt(Splash.sPrev_Pos_Btn, GeneralFunction.Prev_Pos_Btn_ToSave);
        editor.putInt(Splash.sPrev_Pos_Scroll, mywv.getScrollY());
        editor.apply();
        //Toast.makeText(this, "Saved as: " + Integer.toString(mywv.getScrollY()), Toast.LENGTH_SHORT).show();
        //=============================================
    }


    private void SetArabicTextSize(String NewFontSize) {
        String OldString1, OldString2, NewString;
        OldString1 = "font-size: 18pt";
        OldString2 = "font-size:18pt";
        //NewString = "visibility: hidden";
        //NewString = "font-size: 0pt";
        NewString = "font-size: " + NewFontSize + "pt";
        myString = myString.replace(OldString1, NewString);
        myString = myString.replace(OldString2, NewString);
    }

    private String getHadisText(String url) {
        return url;
    }

    private String BooleanToString(boolean fullScreen) {
        if (fullScreen) {
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions_other, menu);
        mMenu = menu;

        //MenuItem mi1;
        //mi1 = (MenuItem) mMenu.getItem(R.id.miSmallScreen);
        //item.setChecked(!IsSmallScreen);
        //mi1.setChecked(true);

        //Also you can do this for sub menu
        //menu.getItem(firstItemIndex).getSubMenu().getItem(subItemIndex).setChecked(true);
        //return true;

        // GeneralFunction.bSmallScreen
        // Toast.makeText(getApplicationContext(), BooleanToString(GeneralFunction.FullScreen) + "  " + BooleanToString(GeneralFunction.FullScreenRun), Toast.LENGTH_SHORT).show();

        // RunWeb();

        menu.findItem(R.id.miNightmode).setChecked(GeneralFunction.bNightMode);
        menu.findItem(R.id.miJummamode).setChecked(GeneralFunction.bJummaMode);
        menu.findItem(R.id.miSmallScreen).setChecked(GeneralFunction.bSmallScreen);
        menu.findItem(R.id.miControlBox).setChecked(GeneralFunction.bControlBox);

        try {
            mMenu.findItem(R.id.action_speed_minus).setVisible(false);
            mMenu.findItem(R.id.action_speed_plus).setVisible(false);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return true;
    }

    public void RunWeb() {
        if (GeneralFunction.FullScreenRun) {
            // item.setVisible(false);
            // mMenu.findItem(R.id.action_pause).setVisible(true);
            //ScrollX = ScrollX -100;
            if (ScrollY > 100) {
                ScrollY = ScrollY - 100;
            } else {
                ScrollY = ScrollY - 10;
            }

            mywv.setScrollX(ScrollX);
            mywv.setScrollY(ScrollY);
            mHandler.postDelayed(mRunnable, GeneralFunction.SCROLL_SPEED);
            isScrollingNow = true;
            GeneralFunction.FullScreenRun = false;
            //onOptionsItemSelected(mMenu.findItem(R.id.action_play));
        }
    }

    public void onBackPressed() {
        SaveCurrentPositions();
        super.onBackPressed();
        return;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        //  Do database operation
        //try {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
            return true;
        }

        if (id == R.id.miControlBox) {
            if (item.isCheckable()) {
                Boolean IsControlBox = !item.isChecked();
                item.setChecked(IsControlBox);
                GeneralFunction.bControlBox = IsControlBox;
                editor.putBoolean(Splash.sControlBox, IsControlBox);
                editor.apply();
                RestartActivity();
            }
        }
        //=======================================================================
        if (id == R.id.miSmallScreen) {
            if (item.isCheckable()) {
                Boolean IsSmallScreen = !item.isChecked();
                item.setChecked(IsSmallScreen);
                GeneralFunction.bSmallScreen = IsSmallScreen;
                editor.putBoolean(Splash.sSmallScreen, IsSmallScreen);
                editor.apply();
                RestartActivity();
            }
        }
        //=======================================================================

        if (id == R.id.miJummamode) {
            if (item.isCheckable()) {
                Boolean IsJummaMode = !item.isChecked();
                item.setChecked(IsJummaMode);
                GeneralFunction.bJummaMode = IsJummaMode;
                editor.putBoolean(Splash.sJummaMode, IsJummaMode);
                editor.apply();
                RestartActivity();
                Toast.makeText(getApplicationContext(), R.string.sJummaMode, Toast.LENGTH_LONG).show();
            }
        }
        //=======================================================================

        if (id == R.id.miNightmode) {
            if (item.isCheckable()) {
                boolean isNightMode = !item.isChecked();
                item.setChecked(isNightMode);
                GeneralFunction.bNightMode = isNightMode;

                // Save the new dark_mode value in SharedPreferences
                prefs = getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
                boolean isDarkMode = prefs.getBoolean("dark_mode", false);
                editor.putBoolean("dark_mode", isNightMode);
                editor.apply();

                RestartActivity();  // Restart the activity to apply the new theme
            }
            return true;
        }
        //=======================================================================

        if (id == R.id.action_minus) {
            if (isScrollingNow) {
                if (GeneralFunction.SCROLL_SPEED < 90) {
                    GeneralFunction.SCROLL_SPEED = GeneralFunction.SCROLL_SPEED + 6;
                }
                editor.putInt(Splash.sSCROLL_SPEED, GeneralFunction.SCROLL_SPEED);
                editor.apply();
                //===============================================
                //  Toast.makeText(getApplicationContext(), "Speed " + Integer.toString(100-GeneralFunction.SCROLL_SPEED) , Toast.LENGTH_SHORT).show();
            } else {
                if (fontSize > 30) {
                    fontSize = fontSize - 5;
                } else {
                    fontSize = 30;
                }

                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeGlobal = fontSize;
                //===============================================
                editor.putInt(Splash.ZoomWebview, fontSize);
                editor.apply();
                //===============================================

                WebSettings settings = mywv.getSettings();
                settings.setTextZoom(fontSize);
                return true;
            }
        }

        //==============================================================================
        if (id == R.id.action_plus) {

            if (isScrollingNow) {
                if (GeneralFunction.SCROLL_SPEED > 7) {
                    GeneralFunction.SCROLL_SPEED = (int) (GeneralFunction.SCROLL_SPEED * 0.85);
                    editor.putInt(Splash.sSCROLL_SPEED, GeneralFunction.SCROLL_SPEED);
                    editor.apply();
                }
            } else {
                if (fontSize < 210) {
                    fontSize = fontSize + 5;
                } else {
                    fontSize = 210;
                }

                GeneralFunction.fontsizeGlobal = fontSize;
                editor.putInt(Splash.ZoomWebview, fontSize);
                editor.apply();

                WebSettings settings = mywv.getSettings();
                settings.setTextZoom(fontSize);
            }
            return true;
        }

        if (id == R.id.action_zoom_plus) {

            if (fontSize < 210) {
                fontSize = fontSize + 5;
            } else {
                fontSize = 210;
            }
            Toast.makeText(getApplicationContext(), "Zoom/Font +", Toast.LENGTH_SHORT).show();

            GeneralFunction.fontsizeGlobal = fontSize;
            editor.putInt(Splash.ZoomWebview, fontSize);
            editor.apply();
            WebSettings settings = mywv.getSettings();
            settings.setTextZoom(fontSize);
            return true;
        }
        if (id == R.id.action_zoom_minus) {

            if (fontSize > 30) {
                fontSize = fontSize - 5;
            } else {
                fontSize = 30;
            }
            Toast.makeText(getApplicationContext(), "Zoom/Font -", Toast.LENGTH_SHORT).show();
            GeneralFunction.fontsizeGlobal = fontSize;
            editor.putInt(Splash.ZoomWebview, fontSize);
            editor.apply();
            WebSettings settings = mywv.getSettings();
            settings.setTextZoom(fontSize);
            return true;
        }

        if (id == R.id.action_speed_plus) {
            if (isScrollingNow) {
                if (GeneralFunction.SCROLL_SPEED > 7) {
                    GeneralFunction.SCROLL_SPEED = (int) (GeneralFunction.SCROLL_SPEED * 0.85);
                    editor.putInt(Splash.sSCROLL_SPEED, GeneralFunction.SCROLL_SPEED);
                    editor.apply();
                }
            }
            return true;
        }

        if (id == R.id.action_speed_minus) {
            if (isScrollingNow) {
                if (GeneralFunction.SCROLL_SPEED < 90) {
                    GeneralFunction.SCROLL_SPEED = GeneralFunction.SCROLL_SPEED + 6;
                }
                editor.putInt(Splash.sSCROLL_SPEED, GeneralFunction.SCROLL_SPEED);
                editor.apply();
            }
            return true;
        }
        //==============================================================================

        if (id == R.id.action_refresh) {
            // refresh
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Get Latest Tamil Islamic Information . Its Brilliant\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "FX Currency");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            return true;
        }

        if (id == R.id.action_help) {
            // help action
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
            return true;
        }

        if (id == R.id.BookmarkArticle) {
            if (db.addFav(myID) == -1) {
                Toast.makeText(getApplicationContext(), "Already added to favourite!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
;
            return true;
        }

        if (id == R.id.action_search) {
            // help action
            sl.setVisibility(View.VISIBLE);
            return true;
        }

        if (id == R.id.Fscreen) {
            // help action
            // Activity.recreate();
            GeneralFunction.FullScreen = true;
            GeneralFunction.FullScreenRun = false;
            RestartActivity();
            // setContentView(R.layout.activity_main);
            return true;
        }

        if (id == R.id.FscreenRun) {
            // Go FullScreen an Auto-run
            ScrollX = mywv.getScrollX();
            ScrollY = mywv.getScrollY();
            GeneralFunction.FullScreen = true;
            GeneralFunction.FullScreenRun = true;
            RestartActivity();
            return true;
        }

        if (id == R.id.Theme) {
            LayColors.setVisibility(View.VISIBLE);
            return true;
        }

        if (id == R.id.action_play) {
            item.setVisible(false);
            mMenu.findItem(R.id.action_pause).setVisible(true);
            mMenu.findItem(R.id.action_speed_minus).setVisible(true);
            mMenu.findItem(R.id.action_speed_plus).setVisible(true);
            mHandler.postDelayed(mRunnable, GeneralFunction.SCROLL_SPEED);
            setTitle("Speed ->");
            isScrollingNow = true;
            return true;
        }

        if (id == R.id.action_pause) {
            item.setVisible(false);
            mMenu.findItem(R.id.action_speed_minus).setVisible(false);
            mMenu.findItem(R.id.action_speed_plus).setVisible(false);
            mMenu.findItem(R.id.action_play).setVisible(true);
            mHandler.removeCallbacks(mRunnable);
            setTitle(" ");
            isScrollingNow = false;
            return true;
        }

        if (id == R.id.action_Contribute){
            startActivity( new Intent(this, PaymentActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    private void RestartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button1:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor1));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor1).commit();
                break;
            case R.id.button2:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor2));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor2).commit();
                break;
            case R.id.button3:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor3));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor3).commit();
                break;
            case R.id.button4:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor4));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor4).commit();
                break;
            case R.id.button5:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor5));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor5).commit();
                break;
            case R.id.button6:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor6));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor6).commit();
                break;
            case R.id.button7:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor7));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor7).commit();
                break;
            case R.id.button8:
                mywLay2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor8));
                editor.putString(Splash.sWebColor, GeneralFunction.wcolor8).commit();
                break;
            case R.id.button10:
                LayColors.setVisibility(View.GONE);
                break;
            case R.id.buttonPrev:
                ShowArticle("Prev");
                break;
            case R.id.buttonNext:
                ShowArticle("Next");
                break;
            case R.id.buttonMenu:
                //ShowArticle("Next");
                break;
            default:
                break;
        }
    }

    private void MakeButtonOver(String ButtonState) {
        try {
            if (ButtonState == "Normal") {
                btnPrev.setText("Prev"); //Orange
                btnPrev.setTextColor(Color.parseColor("#717171")); //Grey
                btnNext.setText("Next"); //Orange
                btnNext.setTextColor(Color.parseColor("#717171")); //Grey
            } else if (ButtonState == "Prev") {
                btnPrev.setText("Start"); //Orange
                btnPrev.setTextColor(Color.parseColor("#cd9a3b")); //Orange
            } else {
                btnNext.setText("End"); //Orange
                btnNext.setTextColor(Color.parseColor("#cd9a3b")); //Orange
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String ShowArticle(String which) {
        //Toast.makeText(getApplicationContext(), Integer.toString(GeneralFunction.Gen_PositionNew), Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(), Integer.toString(GeneralFunction.Gen_itemList.size()), Toast.LENGTH_SHORT).show();
        //return null;
        try {
            MakeButtonOver("Normal");
            int ArticlesUbound;

            // npos = Integer.parseInt(myID) + 1;
            //String NmyID = Integer.toString(GeneralFunction.Gen_PositionNew +1);
            int npos = GeneralFunction.Gen_PositionNew + 1;

            if (which.equals("Next")) {
                GeneralFunction.Gen_PositionNew = GeneralFunction.Gen_PositionNew + 1;
                //Toast.makeText(getApplicationContext(), Integer.toString(GeneralFunction.Gen_PositionNew), Toast.LENGTH_SHORT).show();
                ArticlesUbound = GeneralFunction.Gen_itemList.size() - 1;
                if (GeneralFunction.Gen_PositionNew == ArticlesUbound) {
                    MakeButtonOver("Next");
                } else if (GeneralFunction.Gen_PositionNew > ArticlesUbound) {
                    GeneralFunction.Gen_PositionNew = ArticlesUbound;
                    MakeButtonOver("Next");
                    return null;
                    //btnNext.setTextColor(Color.parseColor("#cd9a3b") ); //Orange
                    //Toast.makeText(getApplicationContext(), "முடிந்தது", Toast.LENGTH_SHORT).show();
                }
            } else {
                GeneralFunction.Gen_PositionNew = GeneralFunction.Gen_PositionNew - 1;
                //Toast.makeText(getApplicationContext(), Integer.toString(GeneralFunction.Gen_PositionNew), Toast.LENGTH_SHORT).show();
                if (GeneralFunction.Gen_PositionNew == 0) {
                    MakeButtonOver("Prev");
                } else if (GeneralFunction.Gen_PositionNew < 0) {
                    GeneralFunction.Gen_PositionNew = 0;
                    MakeButtonOver("Prev");
                    return null;
                    //Toast.makeText(getApplicationContext(), "ஆரம்பம்", Toast.LENGTH_SHORT).show();
                    //((Button)v).setTextColor(Color.parseColor("#000000"));
                    //btnPrev.setTextColor(Color.parseColor("#cd9a3b") ); //Orange
                }
            }

            String NmyID = Integer.toString(GeneralFunction.Gen_PositionNew);
            String NmyString = GeneralFunction.Gen_itemList.get(GeneralFunction.Gen_PositionNew).description;
            //mywv.loadData(" " , "text/html; charset=UTF-8", null);
            //Toast.makeText(getApplicationContext(), NmyID + " " + NmyString, Toast.LENGTH_LONG).show();
            //mywv.reload();
            //mywv.clearCache(true);
            //mywv.clearHistory();
            //mywv.loadData("1" , "text/html; charset=UTF-8", null);
            //mywv.loadData("" , "text/html; charset=UTF-8", null);

            //String Nname = GeneralFunction.Gen_itemList.get(GeneralFunction.Gen_PositionNew+1).name;
            //String sp ="<span style=\"font-size: 8pt; color: #808080;\">";
            //NmyString = NmyString + "<hr /> " + sp + ">> " + Nname + "</span>"  ;
            //NmyString = NmyString + "<hr /> " + "<p>Next: " + Nname + "</p>"  ;

            GeneralFunction.Prev_Pos_List_ToSave = GeneralFunction.Gen_PositionNew;
            myString = NmyString;
            SetWebViewContent();

            myString = NmyString;
            SetWebViewContent();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //mywv.loadData(NmyString , "text/html; charset=UTF-8", null);
        //mywv.loadData(NmyString , "text/html; charset=UTF-8", null);
        return null;
    }


    private void SetBtns() {
        //======================================================

        //LayColors.setOnClickListener(this);
        LayColors = findViewById(R.id.LayColors);
        //LayColors.setVisibility(LayColors.GONE);
        //LayWeb.setOnClickListener(this);
        LayWeb = findViewById(R.id.web_layout);
        LayWeb.setBackgroundColor(Color.parseColor(prefs.getString(Splash.sWebColor, "#ffffff")));
        if (GeneralFunction.bNightMode) {
            LayWeb.setBackgroundColor(Color.parseColor("#262626"));
        }
        //===============================================

        btn1 = findViewById(R.id.button1);
        btn1.setOnClickListener(this);
        btn1.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor1));

        btn2 = findViewById(R.id.button2);
        btn2.setOnClickListener(this);
        btn2.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor2));

        btn3 = findViewById(R.id.button3);
        btn3.setOnClickListener(this);
        btn3.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor3));

        btn4 = findViewById(R.id.button4);
        btn4.setOnClickListener(this);
        btn4.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor4));

        btn5 = findViewById(R.id.button5);
        btn5.setOnClickListener(this);
        btn5.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor5));

        btn6 = findViewById(R.id.button6);
        btn6.setOnClickListener(this);
        btn6.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor6));

        btn7 = findViewById(R.id.button7);
        btn7.setOnClickListener(this);
        btn7.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor7));

        btn8 = findViewById(R.id.button8);
        btn8.setOnClickListener(this);
        btn8.setBackgroundColor(Color.parseColor(GeneralFunction.wcolor8));

        btn10 = findViewById(R.id.button10);
        btn10.setOnClickListener(this);


        btnPrev = findViewById(R.id.buttonPrev);
        btnPrev.setOnClickListener(this);

        btnNext = findViewById(R.id.buttonNext);
        btnNext.setOnClickListener(this);

        btnMenu = findViewById(R.id.buttonMenu);
        btnMenu.setOnClickListener(this);

        LinearLayout wLayout = findViewById(R.id.web_layout);
        //======================================================
    }
}
