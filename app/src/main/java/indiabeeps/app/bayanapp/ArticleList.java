package indiabeeps.app.bayanapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import indiabeeps.app.bayanapp.db.ArticlesDB;
import indiabeeps.app.bayanapp.unusedClasses.mySync;
//import android.app.ActionBar;

public class ArticleList extends Activity {

    ListView lv;
    ArticlesAdaptor ca;
    DatabaseHandler db;
    TextView myTV;
    public static ArticleList AL;
    //int fontSize=16;
    int fontSize;

    boolean fromFav = false;

    List<getAllArticles> arts = new ArrayList<>();

    public static List<getAllArticles> searchedfines = new ArrayList<>();
    // Search EditText
    EditText inputSearch;

    RelativeLayout sl;
    ImageButton slh;
    ImageButton searhit;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    private void showDeleteDialog(final getAllArticles article) {
        new AlertDialog.Builder(ArticleList.this)
                .setTitle("Remove article")
                .setMessage("Are you sure you want to remove this article from favourites?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (db.deleteFav(article.id)) {
                            arts.remove(article);
                            ca.notifyDataSetChanged();
                            Toast.makeText(ArticleList.this, "Removed from favourites!", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_article_list);

        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //        WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //===============================================
        prefs = getSharedPreferences(Splash.sMyAppOptions, ArticleList.MODE_PRIVATE);
        editor = prefs.edit();
        db = new DatabaseHandler(ArticleList.this);
        final ArticlesDB getArticles = new ArticlesDB(this);

        GeneralFunction.bAlignCenterInArticles = prefs.getBoolean(Splash.sAlignCenterInArticles, false);
        GeneralFunction.bBoldInArticles = prefs.getBoolean(Splash.sBoldInArticles, false);
        //===============================================

        fontSize = GeneralFunction.fontsizeOfListGlobal;
        //===============================================
        //SharedPreferences prefs = getSharedPreferences(Splash.sMyAppOptions,Splash.MODE_PRIVATE);
        fontSize = prefs.getInt(Splash.FontList, 16);
        //===============================================

        AL = this;
        myTV = findViewById(R.id.textView2);
/*
        if(getActionBar() != null){
            ActionBar actionBar =  getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
*/

        inputSearch = findViewById(R.id.editText2);
        slh = findViewById(R.id.imageButton4);
        sl = findViewById(R.id.sl);
        searhit = findViewById(R.id.imageButton5);

        slh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl.setVisibility(View.GONE);
                ca = new ArticlesAdaptor(ArticleList.this, arts, fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
            }
        });

        searhit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputSearch.length() > 0) {
                    searchedfines = new ArrayList<>();
                    for (int i = 0; i < arts.size(); i++) {
                        if (arts.get(i).name.toLowerCase().contains(inputSearch.getText().toString())) {
                            searchedfines.add(arts.get(i));
                        }
                    }
                    if (searchedfines.size() > 0) {
                        ca = new ArticlesAdaptor(ArticleList.this, searchedfines, fontSize);
                        ca.notifyDataSetChanged();
                        lv.setAdapter(ca);
                    } else {
                        Toast.makeText(getApplicationContext(), "Nothing Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Something to Search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String myStringId = prefs.getString("CATID", "");
        String myString = prefs.getString("CATName", "");
        String mySlug = prefs.getString("Slug", "a");

        // String s1 = "CATID: " + myStringId + ",     " +  "CATName: " + myString;
        // Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();


        editor.putString("isFav", "No").apply();

        myTV.setText(Html.fromHtml(myString));

        lv = findViewById(R.id.listView);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("fav")) {
                fromFav = true;
                new GetFavsAsyncTask().execute();
                return;
            }
        }

        if (myStringId != null && myStringId.equals("99999")) {
            editor.putString("isFav", "Yes").apply();
            arts = getArticles.getFavArticles(myStringId);
        } else {
            editor.putString("isFav", "No").apply();
            arts = getArticles.getSelectedArticles(myStringId, mySlug);
            // Toast.makeText(this, "mySlug: " + mySlug, Toast.LENGTH_LONG).show();
            GeneralFunction.Gen_itemList = getArticles.getSelectedArticles(myStringId, mySlug);
        }

        //Toast.makeText(this, String.valueOf(arts.size()), Toast.LENGTH_LONG).show();

        if (arts.size() > 0) {
            //Toast.makeText(this, String.valueOf(arts.size()), Toast.LENGTH_LONG).show();
            ca = new ArticlesAdaptor(this, arts, fontSize);
            ca.notifyDataSetChanged();
            lv.setAdapter(ca);
        }
    }

    private class GetFavsAsyncTask extends AsyncTask<Integer, Void, Boolean> {
        private ProgressDialog Dialog;

        @Override
        protected void onPreExecute() {
            Dialog = new ProgressDialog(ArticleList.this);
            Dialog.setMessage("Getting favourite articles, please wait...");
            Dialog.show();
            Dialog.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            ArticlesDB getArticles = new ArticlesDB(ArticleList.this);
            //   List<getAllArticles> list=getArticles.getAll(); SELECT * FROM Articles WHERE artid IN (3536, 213, 1078)
            List<String> favlist = db.getAllFavs();
            String query = "SELECT * FROM Articles WHERE artid IN (";
            for (int a = 0; a <= favlist.size(); a++) {
                if (a < favlist.size()) {
                    query += favlist.get(a);
                    if (a < favlist.size() - 1) {
                        query += ", ";
                    }
                } else {
                    query += ")";
                }
            }

            //favlist.in
            List<getAllArticles> arts2 = new ArrayList<>();
            arts = getArticles.getAll2(query);
            arts2.addAll(arts);

            for (int a = 0; a < favlist.size(); a++) {
                int index = favlist.indexOf(arts.get(a).id);
                arts2.remove(index);
                arts2.add(index, arts.get(a));
            }

            arts.clear();
            arts.addAll(arts2);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (Dialog != null) {
                Dialog.dismiss();
            }
            if (arts.size() > 0) {
                ca = new ArticlesAdaptor(ArticleList.this, arts, fontSize, true);
                ca.setCustomObjectListener(new ArticlesAdaptor.MyTestInterface() {
                    @Override
                    public void testFunctionOne() {
                        new GetFavsAsyncTask().execute();
                    }
                });

                myTV.setText("Bookmarks");
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
                lv.setVisibility(View.VISIBLE);
            } else {
                lv.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No favourite articles found!", Toast.LENGTH_SHORT).show();
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
        ShowPrevArticle();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click

        prefs = getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
        editor = prefs.edit();

        switch (item.getItemId()) {
            case R.id.action_minus:
                fontSize = fontSize - 2;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfListGlobal = fontSize;
                //===============================================
                editor.putInt(Splash.FontList, fontSize);
                editor.apply();
                //===============================================

                sl.setVisibility(View.GONE);
                ca = new ArticlesAdaptor(ArticleList.this, arts, fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
                return true;

            case R.id.action_plus:
                fontSize = fontSize + 2;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfListGlobal = fontSize;
                //===============================================
                editor.putInt(Splash.FontList, fontSize);
                editor.apply();
                //===============================================

                sl.setVisibility(View.GONE);
                ca = new ArticlesAdaptor(ArticleList.this, arts, fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_Share:
                // refresh
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //String shareBody = "Get Latest Tamil Islamic Information . Its Brilliant\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString();
                String shareBody = "இஸ்லாமிய பேச்சாளர்களுக்கான, பயான் குறிப்புகள் ஆன்ட்ராய்டு அப்: " + "\n " + "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Bayan App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;

            case R.id.BookmarkArticle:
                mySync getCat = new mySync();
                getCat.getCategoties(ArticleList.this);
                return true;

            case R.id.action_myalign:
                GeneralFunction.bAlignCenterInArticles = !GeneralFunction.bAlignCenterInArticles;
                editor.putBoolean(Splash.sAlignCenterInArticles, GeneralFunction.bAlignCenterInArticles);
                editor.apply();
                RestartActivity();
                return true;

            case R.id.action_Bold:
                GeneralFunction.bBoldInArticles = !GeneralFunction.bBoldInArticles;
                editor.putBoolean(Splash.sBoldInArticles, GeneralFunction.bBoldInArticles);
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
                Intent intent = new Intent(ArticleList.this, ArticleList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("fav", true);
                startActivityForResult(intent, 0);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void RestartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void ShowPrevArticle() {
        if (GeneralFunction.bShowPreviousArticle) {
            //GeneralFunction.Prev_Pos_List = prefs.getInt(Splash.sPrev_Pos_List, 1);
            //Toast.makeText(this, "Get List Pos: " + GeneralFunction.Prev_Pos_List, Toast.LENGTH_SHORT).show();
            ca.myPath.performClick();
        }
    }

    @Override
    public void onBackPressed() {
        GeneralFunction.bShowPreviousArticle = false;
        super.onBackPressed();
        finish();
        return;
    }
}
