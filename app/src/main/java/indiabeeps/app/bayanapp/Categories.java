package indiabeeps.app.bayanapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import indiabeeps.app.bayanapp.db.ArticlesDB;
import indiabeeps.app.bayanapp.payment.PaymentActivity;

public class Categories extends Activity {

    ListView lv;
    CategoriesAdaptor ca;
    String check;
    //int fontSize=16;
    int fontSize; //=16;

    List<getAllCategory> cats = new ArrayList<>();
    public static List<getAllCategory> searchedfines = new ArrayList<>();
    // Search EditText
    EditText inputSearch;

    RelativeLayout sl;
    ImageButton slh;
    ImageButton searhit;
    TextView tv;
    private Menu mMenu;


    public static Categories caa;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private boolean isDarkMode;

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the dark mode preference has changed
        boolean currentDarkMode = prefs.getBoolean("dark_mode", false);
        if (currentDarkMode != isDarkMode) {
            // Update the theme and recreate the activity only if the theme has changed
            isDarkMode = currentDarkMode;
            recreate();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // Retrieve the dark mode preference
        prefs = getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("dark_mode", false);

        // Apply the appropriate theme
        setTheme(isDarkMode ? R.style.DarkAppTheme : R.style.AppTheme);
        editor = prefs.edit();


        GeneralFunction.bAlignCenterInCategories = prefs.getBoolean(Splash.sAlignCenterInCategories, false);
        GeneralFunction.bBoldInCategories = prefs.getBoolean(Splash.sBoldInCategories, false);
        //===============================================

        fontSize = GeneralFunction.fontsizeOfCatGlobal;
        //===============================================
        //SharedPreferences prefs = getSharedPreferences(Splash.sMyAppOptions,Splash.MODE_PRIVATE);
        fontSize = prefs.getInt(Splash.FontCat, 16);
        //===============================================

        //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        //   this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        tv = findViewById(R.id.textView3);
        Intent i = getIntent();
        check = i.getStringExtra("cat");
        Log.i("check", check);
        caa = this;
        super.onCreate(savedInstanceState);
        //setTheme(R.style.DarkAppTheme);
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);


        setContentView(R.layout.activity_categories);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        ActionBar actionBar = getActionBar();
        //        actionBar.hide();
        // More stuff here...
        //        actionBar.show();
        // set the icon

          /*     try {
                    File sd = Environment.getExternalStorageDirectory();
                    File data = Environment.getDataDirectory();

                    if (sd.canWrite()) {
                        String currentDBPath = "/data/data/" + getPackageName() + "/databases/Articles";
                        String backupDBPath = "Articles.db";
                        File currentDB = new File(currentDBPath);
                        File backupDB = new File(sd, backupDBPath);

                        if (currentDB.exists()) {
                            FileChannel src = new FileInputStream(currentDB).getChannel();
                            FileChannel dst = new FileOutputStream(backupDB).getChannel();
                            dst.transferFrom(src, 0, src.size());
                            src.close();
                            dst.close();
                        }

                        Log.d("DONE","DONE!!!");
                    }
                } catch (Exception e) {
                    Log.d("DONE","DONE!!!" + e.toString());
                }*/

        inputSearch = findViewById(R.id.etSearch);
        slh = findViewById(R.id.imageButton3);
        sl = findViewById(R.id.slyout);
        searhit = findViewById(R.id.imageButton2);

/*
        slh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl.setVisibility(View.GONE);
                ca = new CategoriesAdaptor(Categories.this, cats,fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
            }
        });

lv.performItemClick(
                lv.getAdapter().getView(mActivePosition, null, null),
                mActivePosition,
                lv.getAdapter().getItemId(mActivePosition));

        searhit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (inputSearch.length() >0) {
                    searchedfines = new ArrayList<getAllCategory>();
                    for (int i = 0; i < cats.size(); i++) {
                        if (cats.get(i).name.toLowerCase().contains(inputSearch.getText().toString())) {
                            searchedfines.add(cats.get(i));
                        }
                    }
                    if (searchedfines.size() > 0) {
                        ca = new CategoriesAdaptor(Categories.this, searchedfines,fontSize);
                        ca.notifyDataSetChanged();
                        lv.setAdapter(ca);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Enter Something to Search", Toast.LENGTH_SHORT).show();
                }
            }
        });
*/

        ArticlesDB createDB = new ArticlesDB(this);
        if (prefs.getBoolean("firstRun", true)) {
            //  mySync getCat = new mySync();
            //  getCat.getCategoties(this);
//            try {
//                createDB.createDataBase();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            editor.putInt("version_code", BuildConfig.VERSION_CODE);
        }

        if (check.contains("a")) {
            cats = createDB.getCategoriesA();
        } else if (check.contains("b")) {
            cats = createDB.getCategoriesB();
        } else if (check.contains("c")) {
            cats = createDB.getCategoriesC();
        } else if (check.contains("d")) {
            cats = createDB.getCategoriesD();
        }

        lv = findViewById(R.id.listView);
        //lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setBackgroundColor(Color.BLACK);

        if (cats.size() > 0) {
            ca = new CategoriesAdaptor(this, cats, fontSize);
            ca.notifyDataSetChanged();
            lv.setAdapter(ca);
        }
        editor.apply();

        if (mMenu != null) {
            MenuItem darkModeItem = mMenu.findItem(R.id.miNightmode);
            if (darkModeItem != null) {
                CheckBox darkModeCheckbox = (CheckBox) darkModeItem.getActionView();
                darkModeCheckbox.setChecked(isDarkMode);
                darkModeCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // Save the dark mode state
                    editor.putBoolean("dark_mode", isChecked);
                    editor.apply();

                    // Restart activity to apply theme changes
                    recreate();
                });
            }
        }
    }

    public void myReload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_categories_page, menu);

        // Set checkbox state based on current theme
        boolean isDarkMode = prefs.getBoolean("dark_mode", false);
        MenuItem item = menu.findItem(R.id.miNightmode);
        item.setChecked(isDarkMode);
        ShowPrevArticle();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_minus:
                fontSize = fontSize - 2;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfCatGlobal = fontSize;
                //===============================================
                editor.putInt(Splash.FontCat, fontSize);
                editor.apply();
                //===============================================

                sl.setVisibility(View.GONE);
                ca = new CategoriesAdaptor(Categories.this, cats, fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
                return true;

            case R.id.action_plus:
                fontSize = fontSize + 2;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfCatGlobal = fontSize;
                //===============================================
                editor.putInt(Splash.FontCat, fontSize);
                editor.apply();
                //===============================================

                sl.setVisibility(View.GONE);
                ca = new CategoriesAdaptor(Categories.this, cats, fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
                return true;

            case R.id.action_Share:
                // refresh
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //String shareBody = "Get Latest Tamil Islamic Information . Its Brilliant\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString();
                String shareBody = "இஸ்லாமிய பேச்சாளர்களுக்கான, பயான் குறிப்புகள் ஆன்ட்ராய்டு அப்: " + "\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Bayan App");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;

            case R.id.action_myalign:
                GeneralFunction.bAlignCenterInCategories = !GeneralFunction.bAlignCenterInCategories;
                editor.putBoolean(Splash.sAlignCenterInCategories, GeneralFunction.bAlignCenterInCategories);
                editor.apply();
                RestartActivity();
                return true;

            case R.id.action_Bold:
                GeneralFunction.bBoldInCategories = !GeneralFunction.bBoldInCategories;
                editor.putBoolean(Splash.sBoldInCategories, GeneralFunction.bBoldInCategories);
                editor.apply();
                RestartActivity();
                return true;

            case R.id.action_RateApp:
            case R.id.action_Update:
                Intent intentBU = new Intent(this, BeforeRating.class);
                startActivity(intentBU);
                return true;

            case R.id.action_Search:
                Intent intentS = new Intent(this, SearchList.class);
                startActivity(intentS);
                return true;

            case R.id.action_ShowBookmarks:
                Intent intent = new Intent(Categories.this, ArticleList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("fav", true);
                startActivityForResult(intent, 0);
                return true;

            case R.id.action_Contribute:
                startActivity(new Intent(this, PaymentActivity.class));
                return true;


            case R.id.miNightmode:
                // Toggle dark mode
                boolean isDarkMode = item.isChecked();
                editor.putBoolean("dark_mode", !isDarkMode);
                editor.apply();
                // Restart activity to apply theme change
                Intent i = getIntent();
                finish();
                startActivity(i);
                return true;
/*
            case R.id.action_reload:
                mySync getCat = new mySync();
                getCat.getCategoties(Categories.this);
                return true;

       case R.id.action_search:
                // help action
                sl.setVisibility(View.VISIBLE);
                return true;*/

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GeneralFunction.bShowPreviousArticle = false;
        finish();
        return;
    }

    /*
    if (doubleBackToExitPressedOnce) {
        super.onBackPressed();
        return;
    }
    this.doubleBackToExitPressedOnce = true;
    Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

    new Handler().postDelayed(new Runnable() {

        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    }, 2000);
    */
    private void RestartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void ShowPrevArticle() {
        //Toast.makeText(this, "Prev_Pos : " + GeneralFunction.Prev_Pos_Category, Toast.LENGTH_SHORT).show();
        if (GeneralFunction.bShowPreviousArticle) {
            //GeneralFunction.Prev_Pos_Category = prefs.getInt(Splash.sPrev_Pos_Category, 1);
            //Toast.makeText(this, "Get Cat Pos: " + GeneralFunction.Prev_Pos_Category, Toast.LENGTH_SHORT).show();
            ca.myPath.performClick();
        }
    }
}

/*
    int mActivePosition;
    mActivePosition=3;

    long itemId = lv.getAdapter().getItemId(mActivePosition);
    View view = lv.getChildAt(mActivePosition);
    lv.performItemClick(view, mActivePosition, lv.getId());

    lv.performItemClick(
            lv.getAdapter().getView(mActivePosition, null, null),
            mActivePosition,
            lv.getAdapter().getItemId(mActivePosition));

    lv.performItemClick(
            lv.getChildAt(mActivePosition),
            mActivePosition,
            lv.getAdapter().getItemId(mActivePosition));


    lv.performItemClick(
            lv.getChildAt(R.id.textView3),
            mActivePosition,
            lv.getAdapter().getItemId(mActivePosition));

            Toast.makeText(this, "mActivePosition6:" + mActivePosition, Toast.LENGTH_SHORT).show();

//mList.getChildAt(mActivePosition)
//lv.getAdapter().getView(mActivePosition, null, null),
// lv.getAdapter().getView(mActivePosition, null, lv)


// lv.getAdapter().getView(mActivePosition,view,lv).performClick();


//ca.getView(0,lv,lv);

//ca.myPath.performClick();


//lv.performItemClick(ca.myPath, 1, mActivePosition);


//  lv.getAdapter().getView(mActivePosition, null, null).performClick();
//   lv.performItemClick(lv, 1, 1);
//     lv.performItemClick(lv.getChildAt(mActivePosition), mActivePosition, lv.getItemIdAtPosition(mActivePosition));


                long itemId = lv.getAdapter().getItemId(mActivePosition);
                View view = lv.getChildAt(mActivePosition);
                lv.performItemClick(view, mActivePosition, itemId);

                lv.performItemClick(
                        lv.getAdapter().getView(mActivePosition, null, null),
                        mActivePosition,
                        lv.getAdapter().getItemId(mActivePosition));

    lv.performItemClick(lv.getAdapter().getView(4, null, null), 0, lv.getAdapter().getItemId(4));

                lv.performItemClick(
                        lv.getAdapter().getView(mActivePosition, null, null),
                        mActivePosition,
                        lv.getAdapter().getItemId(mActivePosition));


 lv.getAdapter().getView(mActivePosition, null, null)

 lv.performItemClick(
                        lv.getChildAt(mActivePosition),
                        mActivePosition,
                        lv.getAdapter().getItemId(mActivePosition));
 */

//ShowPrevArticle();

            /*
            if(GeneralFunction.bShowPreviousArticle==true) {
                GeneralFunction.Prev_Pos_Category = prefs.getInt(Splash.sPrev_Pos_Category, 1);
                Toast.makeText(this, "Prev_Pos : " + GeneralFunction.Prev_Pos_Category, Toast.LENGTH_SHORT).show();
                ca.myPath.performClick();
            }
*/

//lv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
// lv.setMinimumHeight(300);
// lv.item
// get content height
/*
           // int contentHeight = lv.getChildAt(1).getHeight();

            // set listview height
            ViewGroup.LayoutParams lp = lv.getLayoutParams();
            lp.height = 3000 ;
            lv.setLayoutParams(lp);
            */