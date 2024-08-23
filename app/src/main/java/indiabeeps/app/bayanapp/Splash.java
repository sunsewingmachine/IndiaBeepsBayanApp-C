package indiabeeps.app.bayanapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.encoders.json.BuildConfig;

import indiabeeps.app.bayanapp.db.ArticlesDB;

public class Splash extends Activity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    ArticlesDB articlesDB;

    TextView txtVer;
    TextView ttc;
    ImageView img;
    public Handler mHandler;

    public static final String sMyAppOptions = "MyAppOptions";
    public static final String sSavedText = "SavedText";
    public static final String FontCat = "Category_FontSize";
    public static final String FontList = "ArticleList_FontSize";
    public static final String sFontAfterSplah = "AfterSplah_FontSize";
    public static final String ZoomWebview = "Webview_ZoomSize";
    public static final String sSCROLL_SPEED = "SCROLL_SPEED";
    public static final String sSmallScreen = "SmallScreen";
    public static final String sNightMode = "NightMode";
    public static final String sJummaMode = "JummaMode";
    public static final String sWebColor = "WebColor";
    public static final String sOpening = "Opening";
    public static final String sControlBox = "ControlBox";

    public static final String sPrev_Pos_Btn = "Prev_Pos_Btn";
    public static final String sPrev_Pos_Category = "Prev_Pos_Category";
    public static final String sPrev_Pos_List = "Prev_Pos_List";
    public static final String sPrev_Pos_Scroll = "Prev_Pos_Scroll";

    public static final String sAlignCenterInCategories = "AlignCenterInCategories";
    public static final String sAlignCenterInArticles = "AlignCenterInArticles";
    public static final String sBoldInArticles = "BoldInArticles";
    public static final String sBoldInCategories = "BoldInCategories";

    public static final String sWhatPurchased = "WhatPurchased";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        prefs = getSharedPreferences(sMyAppOptions, MODE_PRIVATE);
        editor = prefs.edit();
        try {
            if (prefs.getBoolean("firstRun", true)
                    || BuildConfig.VERSION_CODE > prefs.getInt("version_code", BuildConfig.VERSION_CODE)) {
                editor.putInt("version_code", BuildConfig.VERSION_CODE).apply();
                editor.putBoolean("firstRun", false).apply();
                articlesDB = new ArticlesDB(this, true);
            } else {
                articlesDB = new ArticlesDB(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//            newdb.createDataBase();

        GeneralFunction.fontsizeGlobal = 100;
        GeneralFunction.fontsizeOfCatGlobal = 16;
        GeneralFunction.fontsizeOfListGlobal = 16;
        GeneralFunction.fontsizeOfAfterSplash = 20;
        GeneralFunction.SCROLL_SPEED = 30;
        GeneralFunction.FullScreen = false;
        GeneralFunction.FullScreenRun = false;
        GeneralFunction.bSmallScreen = false;
        GeneralFunction.bNightMode = false;
        GeneralFunction.bJummaMode = true;
        GeneralFunction.bAlignCenterInArticles = false;
        GeneralFunction.bAlignCenterInCategories = false;
        GeneralFunction.bBoldInCategories = false;
        GeneralFunction.bBoldInArticles = false;
        GeneralFunction.ScrollX = 0;
        GeneralFunction.ScrollY = 0;
        GeneralFunction.Prev_Pos_Btn = 1;
        GeneralFunction.Prev_Pos_Category = 1;
        GeneralFunction.Prev_Pos_List = 1;
        GeneralFunction.Prev_Pos_Scroll = 1;
        GeneralFunction.bShowPreviousArticle = false;
        GeneralFunction.bControlBox = true;

        String restoredText = prefs.getString(sSavedText, null);

        try {
            String ss = String.valueOf(BuildConfig.VERSION_CODE);
            txtVer = findViewById(R.id.txtversion);
            txtVer.setText("Version " + ss);
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (restoredText != null) {
            //String name = prefs.getInt(Font1, 17);//17 is the default value.
            //int idName = prefs.getInt("idName", 0); //0 is the default value.
            //Toast.makeText(getApplicationContext(), "Now not Saving", Toast.LENGTH_SHORT).show();
        } else {
            //SharedPreferences.Editor prefsEdit = prefs.edit();
            editor.putString(Splash.sSavedText, Splash.sSavedText);
            editor.putInt(Splash.FontCat, 18);
            editor.putInt(Splash.sFontAfterSplah, 20);
            editor.putInt(Splash.FontList, 16);
            editor.putInt(Splash.ZoomWebview, 100);
            editor.putInt(Splash.sSCROLL_SPEED, 30);
            editor.putString(Splash.sWebColor, "#ffffff");
            editor.putBoolean(Splash.sSmallScreen, false);
            editor.putBoolean(Splash.sControlBox, true);
            editor.putBoolean(Splash.sNightMode, false);
            editor.putBoolean(Splash.sJummaMode, true);
            editor.putInt(Splash.sOpening, 1);
            editor.putBoolean(Splash.sAlignCenterInCategories, false);
            editor.putBoolean(Splash.sAlignCenterInArticles, false);
            editor.putBoolean(Splash.sBoldInCategories, false);
            editor.putBoolean(Splash.sBoldInArticles, false);
            editor.putInt(Splash.sPrev_Pos_Btn, 1);
            editor.putInt(Splash.sPrev_Pos_Category, 1);
            editor.putInt(Splash.sPrev_Pos_List, 1);
            editor.putInt(Splash.sPrev_Pos_Scroll, 1);
            editor.apply();
        }

       /* private static int SaveOptions(int n1, int n2, String s1) {
            SharedPreferences prefs = GeneralFunction.getSharedPreferences("MyAppOptions", Context.MODE_PRIVATE);
            prefs.Editor editor = prefs.edit();
            editor.putString(Splash.FirstTime, "FirstTime");
            editor.putInt(Splash.Font1, 18);
            editor.putInt(Splash.Font2, 16) ;
            editor.putInt(Splash.Font3, 100) ;
            editor.commit();
        }*/

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent intent = new Intent(Splash.this, AfterSplash.class);
                startActivityForResult(intent, 0);
                overridePendingTransition(R.transition.activity_fade, R.transition.activity_slide);
                finish();
            }
        }, 1800);
    }
}
