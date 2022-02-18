package indiabeeps.app.bayanapp;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import indiabeeps.app.bayanapp.db.ArticlesDB;
import indiabeeps.app.bayanapp.payment.PaymentActivity;

public class AfterSplash extends Activity {

    private Context ctx;
    private Menu mMenu;
    int fontSize; //=16;
    Button btn1, btn2, btn3, btn4, btn5, btnhadis;
    PendingIntent pendingIntent;

    private Handler mHandler;
    private Runnable mRunnable;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        /*Intent myIntent = new Intent(this , MyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 17);
        calendar.set(Calendar.MINUTE, 10);
        calendar.set(Calendar.SECOND, 00);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000 , pendingIntent);  //set repeating every 24 ho
        */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_splash);

        prefs = getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
        editor = prefs.edit();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Intent service = new Intent(this, NotificationService.class);
        this.startService(service);

        prefs = getSharedPreferences(Splash.sMyAppOptions, Categories.MODE_PRIVATE);
        GeneralFunction.Prev_Pos_Btn = prefs.getInt(Splash.sPrev_Pos_Btn, 1);
        GeneralFunction.Prev_Pos_Category = prefs.getInt(Splash.sPrev_Pos_Category, 1);
        GeneralFunction.Prev_Pos_List = prefs.getInt(Splash.sPrev_Pos_List, 1);
        GeneralFunction.Prev_Pos_Scroll = prefs.getInt(Splash.sPrev_Pos_Scroll, 1);

        //===============================================
        try {
            fontSize = 20;
            fontSize = GeneralFunction.fontsizeOfAfterSplash;
            fontSize = prefs.getInt(Splash.sFontAfterSplah, 20);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //===============================================

        btn1 = findViewById(R.id.cmd1);
        btn2 = findViewById(R.id.cmd2);
        btn3 = findViewById(R.id.cmd3);
        btn4 = findViewById(R.id.cmd4);
        btn5 = findViewById(R.id.cmd5);
        btnhadis = findViewById(R.id.cmdhadis);

        TellFriends();
        SetFontSize(fontSize);
        // OpenArticleById();
    }

    private void OpenArticleById() {
        final ArticlesDB ThisArticleById = new ArticlesDB(this);
        List<getAllArticles> ParticularList = ThisArticleById.getParticularArticle("628");
        ctx = this;

        editor.putString("CATCON", GeneralFunction.sParticularContent).apply();
        editor.putString("ARTID", GeneralFunction.sParticularId).apply();
        editor.putString("ARTT", GeneralFunction.sParticularName).apply();

        Intent intent = new Intent(ctx, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        Bundle b = new Bundle();
        b.putString("SearchTerm", "");
        intent.putExtras(b);
        //Toast.makeText(v.getContext(), "String", Toast.LENGTH_LONG).show();
        ctx.startActivity(intent);
        //  ctx.startActivityForResult(intent,0);
    }

    private void SetFontSize(int yFontSize) {
        try {
            btn1.setTextSize(TypedValue.COMPLEX_UNIT_SP, yFontSize);
            btn2.setTextSize(TypedValue.COMPLEX_UNIT_SP, yFontSize);
            btn3.setTextSize(TypedValue.COMPLEX_UNIT_SP, yFontSize);
            btn4.setTextSize(TypedValue.COMPLEX_UNIT_SP, yFontSize);
            btn5.setTextSize(TypedValue.COMPLEX_UNIT_SP, yFontSize);
            //  Do database operation
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void TellFriends() {
        int opening, ModValue;
        opening = prefs.getInt(Splash.sOpening, 1);
        //opening=0;
        editor.putInt(Splash.sOpening, opening + 1);
        editor.apply();
        //this.finish();

        if (opening < 2) {
            ModValue = 1;
        } else if ((opening > 1) && (opening < 10)) {
            ModValue = 3;
        } else if ((opening > 9) && (opening < 40)) {
            ModValue = 8;
        } else if (opening > 39 && opening < 100) {
            ModValue = 10;
        } else {
            ModValue = 30;
        }

        int Res;
        Res = opening % ModValue;
        //Button b1 = (Button) findViewById(R.id.cmd1);
        //b1.setText(String.valueOf(opening) + " " + String.valueOf(Res) );
        // Toast.makeText(this,String.valueOf(opening) + " " + String.valueOf(Res)  ,Toast.LENGTH_SHORT).show();

        if (Res == 0) {
            Toast myToast = Toast.makeText(this, R.string.recommendApp, Toast.LENGTH_LONG);
            myToast.getView().setBackgroundColor(Color.parseColor("#1747c1"));
            myToast.getView().setPadding(20, 20, 20, 20);
            myToast.show();
        }
        if (opening == 5 | opening == 11 | opening == 21 | opening == 36 | opening == 52 | opening == 72 | opening == 92 | opening == 250) {
            Intent intentBU = new Intent(this, BeforeRating.class);
            startActivity(intentBU);
        }
    }

    public void Cat1(View v) {
        GeneralFunction.Prev_Pos_Btn_ToSave = 1;
        Intent i = new Intent(this, Categories.class);
        i.putExtra("cat", "a");
        startActivity(i);
    }

    public void Cat2(View v) {
        GeneralFunction.Prev_Pos_Btn_ToSave = 2;
        Intent i = new Intent(this, Categories.class);
        i.putExtra("cat", "b");
        startActivity(i);
    }

    public void Cat3(View v) {
        GeneralFunction.Prev_Pos_Btn_ToSave = 3;
        Intent i = new Intent(this, Categories.class);
        i.putExtra("cat", "c");
        startActivity(i);
    }

    public void Cat4(View v) {
        GeneralFunction.Prev_Pos_Btn_ToSave = 4;
        Intent i = new Intent(this, Categories.class);
        i.putExtra("cat", "d");
        startActivity(i);
    }

    public void Cat5(View v) {
        Intent intent = new Intent(AfterSplash.this, ArticleList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("fav", true);
        startActivityForResult(intent, 0);
    }

    public void CatHadis(View v) {
        Intent intent = new Intent(AfterSplash.this, HadisActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("fav", true);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_after_splash, menu);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_after_splash2, menu);
        mMenu = menu;
        //menu.findItem(R.id.miSmallScreen).setChecked(GeneralFunction.bSmallScreen);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        prefs = getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
        editor = prefs.edit();

        // Take appropriate action for each action item click
        switch (item.getItemId()) {

            case R.id.action_minus:
                fontSize = fontSize - 2;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfAfterSplash = fontSize;
                //===============================================
                editor.putInt(Splash.sFontAfterSplah, fontSize);
                editor.apply();
                //===============================================
                SetFontSize(fontSize);
                //RestartActivity();
                return true;

            case R.id.action_plus:
                fontSize = fontSize + 2;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfAfterSplash = fontSize;
                //===============================================
                editor.putInt(Splash.sFontAfterSplah, fontSize);
                editor.apply();
                //===============================================
                SetFontSize(fontSize);
                //RestartActivity();
                return true;

            case R.id.action_Share:
                // refresh
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //String shareBody = "Get Latest Tamil Islamic Information . Its Brilliant\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString();
                String shareBody = "இஸ்லாமிய பேச்சாளர்களுக்கான, பயான் குறிப்புகள் ஆன்ட்ராய்டு அப்: " + "\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bayan App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;

            case R.id.action_Contribute:
                startActivity( new Intent(this, PaymentActivity.class));
                return true;

            case R.id.action_Search:
                Intent intentS = new Intent(this, SearchList.class);
                startActivity(intentS);
                return true;

            case R.id.action_LastSeen:
                GeneralFunction.bShowPreviousArticle = true;
                //Toast.makeText(getApplicationContext(), "Opening last read article...", Toast.LENGTH_SHORT).show();
                if (GeneralFunction.Prev_Pos_Btn == 1) {
                    btn1.setPressed(true);
                } else if (GeneralFunction.Prev_Pos_Btn == 2) {
                    btn2.setPressed(true);
                } else if (GeneralFunction.Prev_Pos_Btn == 3) {
                    btn3.setPressed(true);
                } else if (GeneralFunction.Prev_Pos_Btn == 4) {
                    btn4.setPressed(true);
                }
                GotoLastRead();
                return true;

            case R.id.action_RateApp:
            case R.id.action_Update:
                Intent intentBU = new Intent(AfterSplash.this, BeforeRating.class);
                startActivity(intentBU);
                return true;

            case R.id.action_ShowBookmarks:
                //mySync getCat = new mySync();
                //getCat.getCategoties(AfterSplash.this);
                Cat5(btn5);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void GotoLastRead() {
        mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 2 seconds
                if (GeneralFunction.Prev_Pos_Btn == 1) {
                    //btn1.setPressed(true);
                    btn1.performClick();
                } else if (GeneralFunction.Prev_Pos_Btn == 2) {
                    //btn2.setPressed(true);
                    btn2.performClick();
                } else if (GeneralFunction.Prev_Pos_Btn == 3) {
                    //btn3.setPressed(true);
                    btn3.performClick();
                } else if (GeneralFunction.Prev_Pos_Btn == 4) {
                    //btn4.setPressed(true);
                    btn4.performClick();
                }
                //btn2.performClick();
            }
        }, 800);
    }

}
