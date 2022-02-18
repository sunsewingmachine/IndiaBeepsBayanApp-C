package indiabeeps.app.bayanapp;

import android.content.SharedPreferences;

import java.util.List;

/**
 * Created by PC2 on 21/09/2016.
 */
public class GeneralFunction   {

    public static int LoadDataMethod = 2;

    public static List<getAllArticles> Gen_itemList;
    public static int Gen_PositionNew; //1;


    public static int Prev_Pos_Btn; //1;
    public static int Prev_Pos_Category; //1;
    public static int Prev_Pos_List; //1;
    public static int Prev_Pos_Btn_ToSave; //1;
    public static int Prev_Pos_Category_ToSave; //1;
    public static int Prev_Pos_List_ToSave; //1;
    public static int Prev_Pos_Scroll; //1;

    public static int fontsizeGlobal; //=100;
    public static int fontsizeOfCatGlobal; //=100;
    public static int fontsizeOfAfterSplash; //=100;
    public static int fontsizeOfListGlobal; //=100;
    public static int SCROLL_SPEED;// = 30;
    public static int ScrollX; //=100;
    public static int ScrollY; //=100;

    public static boolean isScrollingNow; //=100;
    public static boolean FullScreen; //=100;
    public static boolean FullScreenRun; //=100;
    public static boolean bSmallScreen; //true
    public static boolean bNightMode; //false
    public static boolean bJummaMode; //true
    public static boolean bAlignCenterInCategories; //=100;
    public static boolean bAlignCenterInArticles; //=100;
    public static boolean bBoldInArticles;
    public static boolean bBoldInCategories;
    public static boolean bShowPreviousArticle;
    public static boolean bControlBox;

    public static String sParticularContent="";
    public static String sParticularName="";
    public static String sParticularId="";


    public static int ivar1,ivar2;
    public static String svar1,svar2;
    public static int[] myarray1=new int[10];

    public static String wcolor1="#feffe0";
    public static String wcolor2="#ffffff";
    public static String wcolor3="#e0e0e0";
    public static String wcolor4="#c4c5c6";
    public static String wcolor5="#e7ffe0";
    public static String wcolor6="#eff8ff"; //Lightblue
    public static String wcolor7="#fcefff"; //Light Rose
    public static String wcolor8="#ffeedd";
    //samsung duos GT-S7562 model of Syed Rahim


    public static int minFunction(int n1, int n2) {
        int min;
        if (n1 > n2)
            min = n2;
        else
            min = n1;

        return min;
    }


}
