package indiabeeps.app.bayanapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import java.util.*;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import indiabeeps.app.bayanapp.GeneralFunction;
import indiabeeps.app.bayanapp.getAllArticles;
import indiabeeps.app.bayanapp.getAllCategory;

public class ArticlesDB extends SQLiteOpenHelper {

    private SQLiteDatabase myDatabase;
    private Context mContext;

    //The Android's default system path of your application database.
    private static String DB_PATH;// = "/data/data/"+mContext.getPackageName()+"/databases/";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Articles.db";

    public ArticlesDB(Context context, boolean isDbDeleted) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;

        if (isDbDeleted) {
            try {
                mContext.deleteDatabase(DATABASE_NAME);
                createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public ArticlesDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    private void createDataBase() throws IOException {

        /*if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = "/data/data/" + mContext.getPackageName() + "/databases/";
        } else {
            DB_PATH = mContext.getDatabasePath(DATABASE_NAME).getPath();//+"/databases/";
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            DB_PATH = mContext.getDatabasePath(DATABASE_NAME).getParentFile().getPath() + "/";
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DB_PATH = mContext.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + mContext.getPackageName() + "/databases/";
        }

        boolean dbExist = checkDataBase();

        if (!dbExist) {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            this.close();
        }// else {
        /*
         * do nothing - database already exist
         * The DB doesn't need to be processed
         */
        //}
        try {
            copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath;
            if (DB_PATH != null) {
                myPath = DB_PATH + DATABASE_NAME;
            } else {
                myPath = mContext.getDatabasePath(DATABASE_NAME).getPath();
            }
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
            e.printStackTrace();
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    private void copyDataBase() throws IOException {
        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DATABASE_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DATABASE_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        //Open the database
        String myPath = DB_PATH + DATABASE_NAME;
        myDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    @Override
    public synchronized void close() {
        if (myDatabase != null)
            myDatabase.close();

        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    /*    String CREATE_Articles_TABLE = "CREATE TABLE Articles ( " +
                "id INTEGER AUTO INCREMENT PRIMARY KEY, " +
                "artid TEXT, "+
                "description TEXT, "+
                "category TEXT, "+
                "catid TEXT, "+
                "name TEXT, "+
                "modified timestamp, "+
                "fav TEXT)";

        String CREATE_Categories_TABLE = "CREATE TABLE Categories ( " +
                "id INTEGER AUTO INCREMENT PRIMARY KEY, " +
                "modified timestamp, "+
                "catid TEXT, "+
                "category TEXT, "+
                "slug TEXT)";

        db.execSQL(CREATE_Categories_TABLE);
        db.execSQL(CREATE_Articles_TABLE);
        */
        /*try {
            if (PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean("firstRun", true)) {
                createDataBase();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public void deleteAllCategories() {
        myDatabase = this.getWritableDatabase();
        myDatabase.execSQL("DROP TABLE IF EXISTS Categories");

        String CREATE_Categories_TABLE = "CREATE TABLE Categories ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "modified timestamp, " +
                "catid TEXT, " +
                "category TEXT, " +
                "slug TEXT)";
        myDatabase.execSQL(CREATE_Categories_TABLE);
    }

    public void deleteAllArticles() {
        myDatabase = this.getWritableDatabase();
        myDatabase.execSQL("DROP TABLE IF EXISTS Articles");
        String CREATE_Articles_TABLE = "CREATE TABLE Articles ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "artid TEXT, " +
                "description TEXT, " +
                "category TEXT, " +
                "catid TEXT, " +
                "name TEXT, " +
                "modified timestamp, " +
                "fav TEXT)";
        myDatabase.execSQL(CREATE_Articles_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < newVersion) {
//                db.execSQL("DROP TABLE IF EXISTS Articles");
//                db.execSQL("DROP TABLE IF EXISTS Categories");
//            this.onCreate(db);
//        }
    }
    //---------------------------------------------------------------------

    private static final String TABLE_Articles = "Articles";

    private static final String KEY_Name = "name";
    private static final String KEY_Description = "description";
    private static final String KEY_Modified = "modified";
    private static final String KEY_Fav = "fav";
    private static final String KEY_Category = "category";

    public void addArticles(String aID, String ID, String sKEY_Name, String sKEY_Fav, String sKEY_Description, String sKEY_Category, String sKEY_Mofified) {
        myDatabase = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("catid", ID);
        values.put("artid", aID);
        values.put(KEY_Description, sKEY_Description); // get title
        values.put(KEY_Name, sKEY_Name); // get author
        values.put(KEY_Modified, sKEY_Mofified); // get title
        values.put(KEY_Category, sKEY_Category); // get authort title
        values.put(KEY_Fav, sKEY_Fav); // get author

        // 3. insert
        myDatabase.insert(TABLE_Articles, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        close();
    }

    public void addCategories(String ID, String sKEY_Category, String sKEY_Modified, String Slug) {
        myDatabase = this.getWritableDatabase();

        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("catid", ID);
        values.put(KEY_Category, sKEY_Category);
        values.put(KEY_Modified, sKEY_Modified);
        values.put("slug", Slug);

        // 3. insert
        myDatabase.insert("Categories", // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values

        // 4. close
        close();
    }

    public List<getAllArticles> getAll() {
        List<getAllArticles> Articles = new LinkedList<>();
        try {
            myDatabase = this.getWritableDatabase();

            // 1. build the query
            String query = "SELECT  * FROM " + TABLE_Articles;

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllArticles Article;
            if (cursor.moveToFirst()) {
                do {
                    Article = new getAllArticles();

                    Article.id = (cursor.getString(1));
                    Article.catid = (cursor.getString(4));
                    Article.name = (cursor.getString(5));
                    Article.description = (cursor.getString(2));
                    Article.modified = (cursor.getString(6));
                    Article.category = (cursor.getString(3));
                    Article.fav = (cursor.getString(7));

                    Articles.add(Article);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        } finally {
            close();
        }
        return Articles;
    }

    public List<getAllArticles> getAll2(String query) {
        List<getAllArticles> Articles = new LinkedList<>();
        try {
            myDatabase = this.getWritableDatabase();

            // 1. build the query
            //  String query = "SELECT  * FROM " + TABLE_Articles ;

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllArticles Article;
            if (cursor.moveToFirst()) {
                do {
                    Article = new getAllArticles();

                    Article.id = (cursor.getString(1));
                    Article.catid = (cursor.getString(4));
                    Article.name = (cursor.getString(5));
                    Article.description = (cursor.getString(2));
                    Article.modified = (cursor.getString(6));
                    Article.category = (cursor.getString(3));
                    Article.fav = (cursor.getString(7));

                    Articles.add(Article);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        } finally {
            close();
        }
        myDatabase.close();
        return Articles;
    }

    public List<getAllArticles> getSelectedArticles(String ID, String mySlug) {
        List<getAllArticles> Articles = new LinkedList<>();
        try {
            myDatabase = this.getWritableDatabase();

            // 1. build the query
            String query = "SELECT * FROM " + TABLE_Articles + " where catid = " + ID + " ORDER BY Modified DESC";

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllArticles Article;
            if (cursor.moveToFirst()) {
                do {
                    Article = new getAllArticles();

                    Article.id = (cursor.getString(1));
                    Article.name = (cursor.getString(5));
                    Article.catid = (cursor.getString(4));
                    Article.description = (cursor.getString(2));
                    Article.modified = (cursor.getString(6));
                    Article.category = (cursor.getString(3));
                    Article.fav = (cursor.getString(7));

                    Articles.add(Article);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        } finally {
            close();
        }

        // if ((mySlug.startsWith("u")  || ID == "83") && ID != "u461")

        if (mySlug.startsWith("u")  || ID == "83")
        {
            Articles = sortArticlesByNumber(Articles);
        }
        return Articles;
    }


    public List<getAllArticles> sortArticlesByNumber(List<getAllArticles> articles) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Collections.sort(articles, Comparator.comparing(getAllArticles::getArticleName));
        }

        return articles;
    }

    public List<getAllArticles> getFavArticles(String ID) {
        List<getAllArticles> Articles = new LinkedList<>();
        try {
            // 1. build the query
            String query = "SELECT * FROM " + TABLE_Articles + " where fav = 'Yes' ORDER BY Modified DESC";

            // 2. get reference to writable DB
            myDatabase = this.getWritableDatabase();
            Cursor cursor = myDatabase.rawQuery(query, null);

            getAllArticles Article;
            if (cursor.moveToFirst()) {
                do {
                    Article = new getAllArticles();

                    Article.id = (cursor.getString(1));
                    Article.name = (cursor.getString(5));
                    Article.catid = (cursor.getString(4));
                    Article.description = (cursor.getString(2));
                    Article.modified = (cursor.getString(6));
                    Article.category = (cursor.getString(3));
                    Article.fav = (cursor.getString(7));

                    Articles.add(Article);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        } finally {
            close();
        }
        return Articles;
    }

    public List<getAllCategory> getCategories() {
        List<getAllCategory> Categories = new LinkedList<>();
        try {
            myDatabase = this.getWritableDatabase();

            String query = "SELECT * FROM Categories";

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllCategory Category;
            if (cursor.moveToFirst()) {
                do {
                    Category = new getAllCategory();

                    Category.id = (cursor.getString(2));
                    Category.modified = (cursor.getString(1));
                    Category.name = (cursor.getString(3));
                    Category.slug = (cursor.getString(4));

                    Categories.add(Category);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            myDatabase.close();
        } finally {
            myDatabase.close();
        }
        return Categories;
    }

    public List<getAllCategory> getCategoriesA() {
        List<getAllCategory> Categories = new LinkedList<>();
        try {
            myDatabase = this.getReadableDatabase();

            /*
             * Removed the following line to reduce the variables
             */
            /*String stat = "a";
            if (stat.length() != 0) {
                stat = "%" + stat + "%";
            }*/
            String query = "SELECT * FROM Categories where slug like '%a%' Order by slug";

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllCategory Category;
            if (cursor.moveToFirst()) {
                do {
                    Category = new getAllCategory();

                    Category.id = (cursor.getString(2));
                    Category.modified = (cursor.getString(1));
                    Category.name = (cursor.getString(3));
                    Category.slug = (cursor.getString(4));

                    Categories.add(Category);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            this.close();
        } finally {
            this.close();
        }
        return Categories;
    }

    public List<getAllCategory> getCategoriesB() {
        List<getAllCategory> Categories = new LinkedList<>();
        try {
            myDatabase = this.getWritableDatabase();

            /*
             * Removed the following line to reduce the variables
             */
//            String stat = "b";
            String query = "SELECT * FROM Categories where slug like '%b%' Order by slug";

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllCategory Category;
            if (cursor.moveToFirst()) {
                do {
                    Category = new getAllCategory();

                    Category.id = (cursor.getString(2));
                    Category.modified = (cursor.getString(1));
                    Category.name = (cursor.getString(3));
                    Category.slug = (cursor.getString(4));

                    Categories.add(Category);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            close();
        } finally {
            close();
        }
        return Categories;
    }

    public List<getAllCategory> getCategoriesC() {
        List<getAllCategory> Categories = new LinkedList<>();
        try {
            myDatabase = this.getWritableDatabase();

            /*
             * Removed the following line to reduce the variables
             */
            /*String stat = "q";
            if (stat.length() != 0) {
                stat = "%" + stat + "%";
            }*/
            String query = "SELECT * FROM Categories WHERE slug LIKE '%q%' Order by slug";

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllCategory Category;
            if (cursor.moveToFirst()) {
                do {
                    Category = new getAllCategory();

                    Category.id = (cursor.getString(2));
                    Category.modified = (cursor.getString(1));
                    Category.name = (cursor.getString(3));
                    Category.slug = (cursor.getString(4));

                    Categories.add(Category);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            myDatabase.close();
        } finally {
            myDatabase.close();
        }
        return Categories;
    }

    public List<getAllCategory> getCategoriesD() {
        List<getAllCategory> Categories = new LinkedList<>();
        try {
            myDatabase = this.getWritableDatabase();

            /*
             * Removed the following line to reduce the variables
             */
            /*String stat = "u";
            if (stat.length() != 0) {
                stat = "%" + stat + "%";
            }*/
            String query = "SELECT * FROM Categories where slug like '%u%' Order by slug";

            Cursor cursor = myDatabase.rawQuery(query, null);
            getAllCategory Category;
            if (cursor.moveToFirst()) {
                do {
                    Category = new getAllCategory();

                    Category.id = (cursor.getString(2));
                    Category.modified = (cursor.getString(1));
                    Category.name = (cursor.getString(3));
                    Category.slug = (cursor.getString(4));

                    Categories.add(Category);
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            myDatabase.close();
        } finally {
            myDatabase.close();
        }
        return Categories;
    }

    public List<getAllCategory> getDistCategories() {
        List<getAllCategory> Categories = new LinkedList<>();

        String query = "SELECT * FROM Categories group by category";

        // 2. get reference to writable DB
        myDatabase = this.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery(query, null);

        getAllCategory Category;

        if (cursor.moveToFirst()) {
            do {
                Category = new getAllCategory();

                Category.id = (cursor.getString(2));
                Category.name = (cursor.getString(3));
                Category.modified = (cursor.getString(1));
                Category.slug = (cursor.getString(4));
                Categories.add(Category);
            } while (cursor.moveToNext());
        }

        cursor.close();
        myDatabase.close();
        return Categories;
    }

    public void updateNote(String sKEY_Id) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_Fav, "Yes"); //These Fields should be your String values of actual column names

        myDatabase = this.getWritableDatabase();
        myDatabase.update(TABLE_Articles, cv, "artid = " + sKEY_Id, null);
    }

    public void updateNoter(String sKEY_Id) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_Fav, "No"); //These Fields should be your String values of actual column names

        myDatabase = this.getWritableDatabase();
        myDatabase.update(TABLE_Articles, cv, "artid = " + sKEY_Id, null);
    }

    public List<getAllArticles> getParticularArticle(String ID) {
        List<getAllArticles> Articles = new LinkedList<>();

        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_Articles + " where artid = " + ID + " ORDER BY Modified DESC";

        // 2. get reference to writable DB
        myDatabase = this.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery(query, null);

        getAllArticles Article;
        if (cursor.moveToFirst()) {
            do {
                Article = new getAllArticles();

                Article.id = (cursor.getString(1));
                Article.name = (cursor.getString(5));
                Article.catid = (cursor.getString(4));
                Article.description = (cursor.getString(2));
                Article.modified = (cursor.getString(6));
                Article.category = (cursor.getString(3));
                Article.fav = (cursor.getString(7));
                Articles.add(Article);

                GeneralFunction.sParticularId = (cursor.getString(1));
                GeneralFunction.sParticularName = (cursor.getString(5));
                GeneralFunction.sParticularContent = (cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();
        myDatabase.close();
        return Articles;
    }
}