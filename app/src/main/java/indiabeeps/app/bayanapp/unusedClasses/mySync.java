package indiabeeps.app.bayanapp.unusedClasses;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import indiabeeps.app.bayanapp.Categories;
import indiabeeps.app.bayanapp.Splash;
import indiabeeps.app.bayanapp.db.ArticlesDB;
import indiabeeps.app.bayanapp.getAllArticles;
import indiabeeps.app.bayanapp.getAllCategory;

/**
 * Created by baber on 6/7/2016.
 */
public class mySync extends Application {

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public static Context mContext;
    public ProgressDialog pDialog;
    String jsonResult;
    String jsonResultTwo;
    //  String url = "http://www.indiabeeps.com/myarticles/getcat.php";
    // String url = "http://www.bayanapp.indiabeeps.com/?json=get_recent_posts()";
    String url = "http://www.bayanapp.bayanapp.com/?json=get_posts()";
    String urlArticles = "http://www.bayanapp.com/myarticles/myjson.php";

    public void getCategoties(Context ctx) {
        mContext = ctx;
        prefs = mContext.getSharedPreferences(Splash.sMyAppOptions, MODE_PRIVATE);
        editor = prefs.edit();

        if (isNetworkAvailable()) {
            accessWebService(); //get Categories
        } else {
            //Toast.makeText(ctx,"Please connect to internet to download all the latest articles",Toast.LENGTH_SHORT).show();
            Toast.makeText(ctx, "\n" + "புதிக கட்டுரைகளை அப்டேட் செய்வதற்கு, இணைய இணைப்பு தேவை. உங்கள் இணைய இணைப்பை சரிபார்க்கவும்." + "\n", Toast.LENGTH_LONG).show();
        }
    }

    private class JsonReadTask extends AsyncTask<String, Void, String> {

        protected void onPreExecute() {
            pDialog = new ProgressDialog(mContext);
            pDialog.setCancelable(false);
            pDialog.setMessage("ஸுப்ஹானல்லாஹ் என்று நீங்கள் 100 முறை சொல்லி முடிப்பதற்குள், டவுன்லோடு ஆகிவிடும்." + "\n \n" + "இணைய இணைப்பு கட் ஆனால் மீண்டும் முயற்சிக்கவும்…..");
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL ulrn = new URL(url);

                HttpURLConnection con = (HttpURLConnection) ulrn.openConnection();
                //    con.setRequestMethod("GET");
                //   con.setConnectTimeout(10000);
                InputStream response = con.getInputStream();
                jsonResult = inputStreamToString(response).toString();
                ListDrwaer();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private StringBuilder inputStreamToString(InputStream is) {
            String rLine;
            StringBuilder answer = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            try {
                while ((rLine = rd.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                //e.printStackTrace();
                Log.d("Error", "Error" + e.toString());
            }

            return answer;
        }

        @Override
        protected void onPostExecute(String result) {
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }
            Categories.caa.myReload();
            //   accessWebServiceTwo(); // get Article Details
        }
    }

    public void accessWebService() {
        JsonReadTask task = new JsonReadTask();
        //passesvaluesfortheurlsstringarray
        task.execute(url);
    }

    public void ListDrwaer() {
        List<getAllCategory> Categoriess = new ArrayList<>();
        List<getAllArticles> Articles = new ArrayList<>();

        List<getAllCategory> CategoriesTemp = new ArrayList<>();
        List<getAllArticles> ArticlesTemp;

        ArticlesDB mdb = new ArticlesDB(mContext);

        CategoriesTemp = mdb.getCategories();
        ArticlesTemp = mdb.getAll();

        mdb.deleteAllCategories();
        mdb.deleteAllArticles();

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = df.format(c.getTime());

        try {
            getAllCategory Category;
            getAllArticles Article;

            //  JSONArray myArray = new JSONArray(jsonResult);
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("posts");
            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                Article = new getAllArticles();

                Article.id = "" + jsonChildNode.optInt("id");
                Article.description = jsonChildNode.optString("content");
                Article.modified = jsonChildNode.optString("modified");
                Article.name = jsonChildNode.optString("title");
                JSONArray myArray = new JSONArray(jsonChildNode.optString("categories"));
                JSONObject myCat = myArray.getJSONObject(0);
                Article.category = myCat.optString("id");

                Category = new getAllCategory();
                Category.id = myCat.optString("id");
                Category.name = myCat.optString("title");
                Category.slug = myCat.optString("slug");
                Category.modified = myCat.optString(formattedDate);

                Article.fav = "No";
                Articles.add(Article);
                Categoriess.add(Category);
            }

            for (int x = 0; x < Articles.size(); x++) {
                Log.d("" + x + " ", Articles.get(x).name);
                mdb.addArticles(Articles.get(x).id, Articles.get(x).category, Articles.get(x).name, Articles.get(x).fav, Articles.get(x).description, Articles.get(x).category, Articles.get(x).modified);
            }

            for (int y = 0; y < ArticlesTemp.size(); y++) {
                if (ArticlesTemp.get(y).fav.equals("Yes")) {
                    mdb.updateNote(ArticlesTemp.get(y).id);
                }
            }

            for (int x = 0; x < Categoriess.size(); x++) {
                Log.d("" + x + " ", Categoriess.get(x).name);
                mdb.addCategories(Categoriess.get(x).id, Categoriess.get(x).name, Categoriess.get(x).modified, Categoriess.get(x).slug);
            }

            Categoriess = mdb.getDistCategories();
            mdb.deleteAllCategories();

            for (int x = 0; x < Categoriess.size(); x++) {
                Log.d("" + x + " ", Categoriess.get(x).name);
                mdb.addCategories(Categoriess.get(x).id, Categoriess.get(x).name, Categoriess.get(x).modified, Categoriess.get(x).slug);
            }

            mdb.addCategories("99999", "Favorite", formattedDate, "a123");
            if (prefs == null || editor == null) {
                prefs = mContext.getSharedPreferences(Splash.sMyAppOptions, MODE_PRIVATE);
                editor = prefs.edit();
                editor.putBoolean("firstRun", false).apply();
            } else {
                editor.putBoolean("firstRun", false).apply();
            }
        } catch (JSONException e) {
            Log.d("Error", "Error" + e.toString());
//            Toast.makeText(getApplicationContext(),"Error" +e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
