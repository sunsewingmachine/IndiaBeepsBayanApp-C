package indiabeeps.app.bayanapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

public class CategoriesAdaptor extends BaseAdapter implements View.OnClickListener {

    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private long delayTime;
    TextView myPath;
    private int PositionNew;
    private int fontSize;
    private Context ctx;
    private List<getAllCategory> itemList;

    CategoriesAdaptor(Context ctx, List<getAllCategory> itemList, int fontSize) {
        this.ctx = ctx;
        this.itemList = itemList;
        this.fontSize = fontSize;
        prefs = ctx.getSharedPreferences(Splash.sMyAppOptions, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater lInf = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // Get the SharedPreferences instance
            SharedPreferences prefs = ctx.getSharedPreferences(Splash.sMyAppOptions, Splash.MODE_PRIVATE);
            boolean isDarkMode = prefs.getBoolean("dark_mode", false);

            // Determine the layout resource based on dark mode setting
            int layoutRes = isDarkMode ? R.layout.category_item_dark : R.layout.category_item;
            v = lInf.inflate(layoutRes, null);

            // Initialize view components
            myPath = v.findViewById(R.id.textView3);
            myPath.setText(Html.fromHtml(itemList.get(position).name));
            myPath.setTextSize(fontSize);

            SetAlignment();
            SetBold();
            SetPressed_LastSeen(position);

            ImageButton myrfv = v.findViewById(R.id.imageButton);
            myrfv.setVisibility(View.GONE);

            myPath.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PositionNew = GetNewPosition(position);

                    editor.putString("CATID", itemList.get(PositionNew).id).apply();
                    editor.putString("CATName", itemList.get(PositionNew).name).apply();
                    editor.putString("Slug", itemList.get(PositionNew).slug).apply();

                    ShowArticle_Activity();
                }
            });
        }

        return v;
    }


    //======================================================================

    //======================================================================
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

    private void ShowArticle_Activity() {
        //Small delay for visibility, if bShowPreviousArticle==True
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //================================================
                Intent intent = new Intent(ctx, ArticleList.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                Categories.caa.startActivityForResult(intent, 0);
                //================================================
            }
        }, delayTime);
        //delayTime will be set if Last seen article is shown
    }

    private void SetPressed_LastSeen(int iPosition) {
        //This code is used to Auto-Select last Category list row if user wants!
        //Also sets Delay for visibility if bShowPreviousArticle==True
        if (GeneralFunction.bShowPreviousArticle) {
            if (iPosition == GeneralFunction.Prev_Pos_Category) {
                //myPath.setBackgroundColor(Color.parseColor("#cd9a3b"));
                myPath.setPressed(true);
                delayTime = 800;
            } else {
                delayTime = 1;
            }
        }
    }

    private void SetBold() {
        //myPath.setTypeface(Typeface.DEFAULT_BOLD);
        if (!GeneralFunction.bBoldInCategories) {
            myPath.setTypeface(myPath.getTypeface(), Typeface.NORMAL);
        } else {
            myPath.setTypeface(myPath.getTypeface(), Typeface.BOLD);
        }
    }

    private void SetAlignment() {
        if (GeneralFunction.bAlignCenterInCategories) {
            myPath.setGravity(Gravity.CENTER);
            myPath.setPadding(25, 17, 25, 17);
        } else {
            myPath.setGravity(Gravity.LEFT);
            myPath.setPadding(30, 17, 20, 17);
        }
    }

    private int GetNewPosition(int CurrentPositon) {
        //This code is used to Auto-Click last read Article if user wants!
        int Position_New;

        if (GeneralFunction.bShowPreviousArticle) {
            Position_New = GeneralFunction.Prev_Pos_Category;
        } else {
            Position_New = CurrentPositon;
        }
        GeneralFunction.Prev_Pos_Category_ToSave = Position_New;
        return Position_New;
    }
    //============================================================

    @Override
    public void onClick(View v) {
    }
}



/*
    //This code is used to Auto-Click last read Article if user wants!
    //=============================================
    if(GeneralFunction.bShowPreviousArticle==true){
        PositionNew = GeneralFunction.Prev_Pos_Category;
    }else{
        PositionNew = position;
    }
    GeneralFunction.Prev_Pos_Category_ToSave = PositionNew;
    //=============================================
 */