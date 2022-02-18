package indiabeeps.app.bayanapp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import indiabeeps.app.bayanapp.db.ArticlesDB;
import indiabeeps.app.bayanapp.unusedClasses.mySync;

public class SearchList extends Activity {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public ProgressDialog pDialog;

    String url = "http://www.bayanapp.bayanapp.com/?json=get_posts()";
    String sFind1, sHelp;
    ListView lv;
    ArticlesAdaptor ca;
    public static SearchList AL;
    //int fontSize=16;
    int fontSize;

    List<getAllArticles> arts = new ArrayList<>();

    public static List<getAllArticles> searchedfines = new ArrayList<>();
    // Search EditText
    EditText inputSearch;
    ImageButton searhit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_searched_article_list);

        //===============================================
        prefs = getSharedPreferences(Splash.sMyAppOptions, SearchList.MODE_PRIVATE);
        editor = prefs.edit();
        GeneralFunction.bAlignCenterInArticles = prefs.getBoolean(Splash.sAlignCenterInArticles, false);
        GeneralFunction.bBoldInArticles = prefs.getBoolean(Splash.sBoldInArticles, false);
        //===============================================

        fontSize = GeneralFunction.fontsizeOfListGlobal;
        //===============================================
        //SharedPreferences prefs = getSharedPreferences(Splash.sMyAppOptions,Splash.MODE_PRIVATE);
        fontSize = prefs.getInt(Splash.FontList, 16);
        //===============================================


        AL = this;
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        sHelp = getString(R.string.sHelp);

        inputSearch = findViewById(R.id.editText2);
        searhit = findViewById(R.id.imageButton5);

        searhit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int iHelp;
                lv.setVisibility(View.VISIBLE);
                sFind1 = inputSearch.getText().toString().trim();
                sFind1 = sFind1.trim();

                try {
                    sFind1 = sFind1.replaceAll("  ", " ");
                    sFind1 = sFind1.replaceAll("  ", " ");
                    sFind1 = sFind1.replaceAll("  ", " ");
                    sFind1 = sFind1.trim();
                    inputSearch.setText(sFind1);
                    inputSearch.setSelection(inputSearch.getText().length());
                    //lv.setVisibility(View.INVISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (inputSearch.length() > 0) {
                    searchedfines = new ArrayList<>();

                    for (int i = 0; i < arts.size(); i++) {
                        //if (arts.get(i).name.toLowerCase().contains(inputSearch.getText().toString()) || arts.get(i).description.toLowerCase().contains(inputSearch.getText().toString())) {
                        if (arts.get(i).name.contains(sFind1) || arts.get(i).description.contains(sFind1)) {
                            searchedfines.add(arts.get(i));
                        }
                    }

                    if (searchedfines.size() > 0) {
                        ca = new ArticlesAdaptor(SearchList.this, searchedfines, fontSize, inputSearch.getText().toString());
                        ca.notifyDataSetChanged();
                        lv.setAdapter(ca);
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(ArticleList.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(inputSearch.getWindowToken(), 0);
                        }
                        //lv.setVisibility(View.INVISIBLE);
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        searchedfines = new ArrayList<>();
                        //====================================================================
                        for (iHelp = 0; iHelp < arts.size(); iHelp++) {
                            if (arts.get(iHelp).name.contains(sHelp)) {
                                searchedfines.add(arts.get(iHelp));
                                break;
                            }
                        }
                        //====================================================================
                        //searchedfines.add(arts.get(iHelp));
                        ca = new ArticlesAdaptor(SearchList.this, searchedfines, fontSize, inputSearch.getText().toString());
                        ca.notifyDataSetChanged();
                        lv.setAdapter(ca);
                        /* lv.setVisibility(View.INVISIBLE); */
                        Toast.makeText(getApplicationContext(), R.string.nothing_found, Toast.LENGTH_SHORT).show();
                        try {
                            inputSearch.requestFocus();
                            InputMethodManager imm = (InputMethodManager) getSystemService(ArticleList.INPUT_METHOD_SERVICE);
                            imm.showSoftInput(inputSearch, InputMethodManager.SHOW_IMPLICIT);
                            inputSearch.requestFocus();
                        }
                        //lv.setVisibility(View.INVISIBLE);
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Enter Something to Search", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String myString = prefs.getString("CATName", "");
        editor.putString("isFav", "No").apply();

        /*
        StartSearchActivity task =new StartSearchActivity();
        //passesvaluesfortheurlsstringarray
        mContext=getApplicationContext();
        task.execute(new String[]{url});
        */
        //FillSearchList()

        try {
            StartSearchActivity task = new StartSearchActivity();
            task.execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    searhit.performClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void FillSearchList() {
        try {
            lv = findViewById(R.id.listView);
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            final ArticlesDB getArticles = new ArticlesDB(this);
            arts = getArticles.getAll();
            if (arts.size() > 0) {
                //Toast.makeText(this, String.valueOf(arts.size()), Toast.LENGTH_LONG).show();
                ca = new ArticlesAdaptor(this, arts, fontSize);
                ca.notifyDataSetChanged();
                //  lv.setAdapter(ca);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class StartSearchActivity extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            try {
                // mContext = this;
                pDialog = new ProgressDialog(SearchList.this);
                pDialog.setCancelable(false);
                pDialog.setMessage("Please wait...");
                pDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //@Override
        protected String doInBackground(String... params) {
            FillSearchList();
            return null;
        }

        //@Override
        protected void onPostExecute(String result) {
            try {
                lv.setAdapter(ca);
                if (pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_categories_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_minus:
                fontSize = fontSize - 1;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfListGlobal = fontSize;
                //===============================================
                editor.putInt(Splash.FontList, fontSize);
                editor.apply();
                //===============================================

                ca = new ArticlesAdaptor(SearchList.this, arts, fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
                return true;

            case R.id.action_plus:
                fontSize = fontSize + 1;
                //Save Font Size to Global Variable to preserve throughout the Application Lifecycle
                GeneralFunction.fontsizeOfListGlobal = fontSize;
                //===============================================
                editor.putInt(Splash.FontList, fontSize);
                editor.apply();
                //===============================================

                ca = new ArticlesAdaptor(SearchList.this, arts, fontSize);
                ca.notifyDataSetChanged();
                lv.setAdapter(ca);
                return true;

            case android.R.id.home:
                this.finish();
                return true;

            case R.id.action_Share:
                // refresh
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                //String shareBody = "Get Latest Tamil Islamic Information . Its Brilliant\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString();
                String shareBody = "இஸ்லாமிய பேச்சாளர்களுக்கான, பயான் குறிப்புகள் ஆன்ட்ராய்டு அப்: " + "\n " + "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();

                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Bayan App");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                return true;

            case R.id.BookmarkArticle:
                mySync getCat = new mySync();
                getCat.getCategoties(SearchList.this);
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

            /*
            case R.id.action_RateApp :
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

            case R.id.action_RateApp:
                Intent intentBU = new Intent (this, BeforeRating.class );
                startActivity(intentBU);
                return true;

            case R.id.action_Update :
                // help action
                Uri uri2 = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket2 = new Intent(Intent.ACTION_VIEW, uri2);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket2);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                }
                return true; */

            case R.id.action_RateApp:
            case R.id.action_Update:
                Intent intentBU = new Intent(this, BeforeRating.class);
                startActivity(intentBU);
                return true;

        /*    case R.id.action_search:
                // help action
                sl.setVisibility(View.VISIBLE);
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void RestartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
