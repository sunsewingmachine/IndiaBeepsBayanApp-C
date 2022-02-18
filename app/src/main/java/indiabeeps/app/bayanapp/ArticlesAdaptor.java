package indiabeeps.app.bayanapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import indiabeeps.app.bayanapp.db.ArticlesDB;

public class ArticlesAdaptor extends BaseAdapter implements View.OnClickListener {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private Handler mHandler;
    private Runnable mRunnable;
    private long delayTime;
    private String sNumb;
    private boolean fromFav = false;
    private int PositionNew;
    TextView myPath;
    private Context ctx;
    private List<getAllArticles> itemList;
    private MyTestInterface listener;
    private int fontSize;
    private String searchedTerm = "";

    void setCustomObjectListener(MyTestInterface listener) {
        this.listener = listener;
    }

    private void showDeleteDialog(final getAllArticles article, final int pos) {
        new AlertDialog.Builder(ctx)
                .setTitle("Remove article")
                .setMessage("Are you sure you want to remove this article from favourites?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        DatabaseHandler db = new DatabaseHandler(ctx);
                        if (db.deleteFav(article.id)) {
                            if (listener != null)
                                listener.testFunctionOne(); // <---- fire listener here
//                            Intent intent = new Intent(ctx,ArticleList.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                            intent.putExtra("fav",true);
//                            ctx.startActivity(intent);
//
//                            ctx.finish();
//                            itemList.remove(pos);
//                            notifyDataSetChanged();
                            Toast.makeText(ctx, "Removed from favourites!", Toast.LENGTH_SHORT).show();
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

    ArticlesAdaptor(Context ctx, List<getAllArticles> itemList, int fontSize) {
        this.ctx = ctx;
        this.itemList = itemList;
        this.fontSize = fontSize;
        prefs = ctx.getSharedPreferences(Splash.sMyAppOptions, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    ArticlesAdaptor(Context ctx, List<getAllArticles> itemList, int fontSize, boolean fromFav) {
        this.ctx = ctx;
        this.itemList = itemList;
        this.fromFav = fromFav;
        this.fontSize = fontSize;
        prefs = ctx.getSharedPreferences(Splash.sMyAppOptions, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    ArticlesAdaptor(Context ctx, List<getAllArticles> itemList, int fontSize, String st) {
        this.ctx = ctx;
        this.itemList = itemList;
        this.fontSize = fontSize;
        this.searchedTerm = st;
        prefs = ctx.getSharedPreferences(Splash.sMyAppOptions, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int pos) {
        return itemList.get(pos);
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int pos) {
        return itemList.get(pos).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater lInf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = lInf.inflate(R.layout.category_item, null);

            myPath = v.findViewById(R.id.textView3);
            ImageButton myrfv = v.findViewById(R.id.imageButton);
            myPath.setTextSize(fontSize);
            myrfv.setVisibility(View.GONE);
            //==========================================================
            SetAlignment();
            SetBold();
            SetPressed_LastSeen(position);
            //==========================================================

            if (prefs.getString("isFav", "No").equals("Yes")) {
                myrfv.setVisibility(View.VISIBLE);
            }
            //==========================================================

            myrfv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ArticlesDB getCats = new ArticlesDB(ctx);
                    getCats.updateNoter(itemList.get(position).id);
                    ArticleList.AL.myReload();
                }
            });
            //==========================================================
            //sNumb = String.valueOf(position +1 ) + ")  ";
            sNumb = "";
            myPath.setText(sNumb + Html.fromHtml(itemList.get(position).name));

            myPath.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (fromFav) {
                        showDeleteDialog(itemList.get(position), position);
                        return true;
                    } else {
                        return false;
                    }
                }
            });

            myPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //This function is used to Auto-Click last read Article if user wants!
                    PositionNew = GetNewPosition(position);
                    GeneralFunction.Gen_PositionNew = PositionNew;

                    editor.putString("CATCON", itemList.get(PositionNew).description).apply();
                    editor.putString("ARTID", itemList.get(PositionNew).id).apply();
                    editor.putString("ARTT", itemList.get(PositionNew).name).apply();
                    //PreferenceManager.getDefaultSharedPreferences(ctx).edit().putInt("ListPosition", PositionNew).commit();

                    ShowArticle_Activity();
                }
            });
        }
        return v;
    }

    /*
    if (IsHeading(itemList.get(PositionNew).name)==false) {
        ShowArticle_Activity();
    }
    private boolean IsHeading(String word) {
        try{
            if (word.length() > 3) {
                if (word.substring(word.length()-3)=="..."){return true;}
            }
            else if (word.length() == 3) {
                if (word=="..."){return true;}
            }
            else {
                // whatever is appropriate in this case
                //throw new IllegalArgumentException("word has less than 3 characters!");
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
*/

    private void ShowArticle_Activity() {
        //Small delay for visibility if bShowPreviousArticle==True
        //========================================================
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                Bundle b = new Bundle();
                b.putString("SearchTerm", searchedTerm);
                intent.putExtras(b);
                ctx.startActivity(intent);
            }
        }, delayTime);
        //========================================================
    }

    //==========================================================
    private void SetPressed_LastSeen(int position) {
        //This block is used to Auto-Select last Article list row if user wants!
        //Also sets Delay for visibility if bShowPreviousArticle==True
        if (GeneralFunction.bShowPreviousArticle) {
            if (position == GeneralFunction.Prev_Pos_List) {
                //myPath.setBackgroundColor(Color.parseColor("#cd9a3b"));
                myPath.setPressed(true);
                delayTime = 800;
            } else {
                delayTime = 1;
            }
        }
    }
    //==========================================================

    private void SetBold() {
        //myPath.setTypeface(Typeface.DEFAULT_BOLD);
        if (!GeneralFunction.bBoldInArticles) {
            myPath.setTypeface(myPath.getTypeface(), Typeface.NORMAL);
        } else {
            myPath.setTypeface(myPath.getTypeface(), Typeface.BOLD);
        }
        //==========================================================
    }

    private void SetAlignment() {
        if (GeneralFunction.bAlignCenterInArticles) {
            myPath.setGravity(Gravity.CENTER);
            myPath.setPadding(25, 17, 25, 17);
        } else {
            myPath.setGravity(Gravity.LEFT);
            myPath.setPadding(30, 17, 20, 17);
        }
        //=========================================================
    }

    public interface MyTestInterface {
        void testFunctionOne();
    }

    private int GetNewPosition(int CurrentPositon) {
        //This code is used to Auto-Click last read Article if user wants!
        int Position_New;
        //=============================================
        if (GeneralFunction.bShowPreviousArticle) {
            Position_New = GeneralFunction.Prev_Pos_List;
        } else {
            Position_New = CurrentPositon;
        }
        GeneralFunction.Prev_Pos_List_ToSave = Position_New;
        return Position_New;
        //=============================================
    }

    @Override
    public void onClick(View v) {
    }
}


/*
//This code is used to Auto-Click last read Article if user wants!
//=============================================
if(GeneralFunction.bShowPreviousArticle==true){
    PositionNew = GeneralFunction.Prev_Pos_List;
}else{
    PositionNew = position;
}
GeneralFunction.Prev_Pos_List = PositionNew;
//=============================================
*/
