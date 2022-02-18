package indiabeeps.app.bayanapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "ArticlesFavdb";
    private static final int DATABASE_VERSION = 1;

    private static String TABLE_FAVOURITE = "favourites";

    private static final String FAV_ID = "fav_id";
    private static final String ARTICLE_ID = "article_id";

    private Context ctx;

    DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        ctx = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE '"
                + TABLE_FAVOURITE
                + "' (" + FAV_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                "" + ARTICLE_ID + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
//		db.execSQL("DROP TABLE  IF EXISTS '" + TABLE_FAVOURITE + "'");
//		onCreate(db);
    }

    public long addFav(String articleid) {
        if (!getArticleById(articleid)) {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(ARTICLE_ID, articleid);
            long i = db.insert(TABLE_FAVOURITE, null, cv);
            db.close();

            return i;
        }
        return -1;
    }

    private boolean getArticleById(String articleid) {
        List<String> favs = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT 1"
                + " FROM " + TABLE_FAVOURITE + " WHERE " + ARTICLE_ID + " = " + articleid + " ;";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null || cursor.getCount() == 0) {
            cursor.close();
            db.close();
            return false;
        }

        db.close();
        cursor.close();
        return true;
    }

    public void clearFavs() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_FAVOURITE, null, null);
        db.close();
    }

    public boolean deleteFav(String articleid) {
        SQLiteDatabase db = getWritableDatabase();
        boolean res = db.delete(TABLE_FAVOURITE, ARTICLE_ID + " = " + articleid, null) > 0;
        db.close();
        return res;
    }

    public List<String> getAllFavs() {
        List<String> favs = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_FAVOURITE + " ORDER BY " + FAV_ID + " DESC;";

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor == null || cursor.getCount() == 0) {
            db.close();
            return favs;
        }
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String articleid = cursor.getString(cursor.getColumnIndex(ARTICLE_ID));

                    favs.add(articleid);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();
        return favs;
    }
}
