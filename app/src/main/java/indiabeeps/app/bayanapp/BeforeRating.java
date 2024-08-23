package indiabeeps.app.bayanapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

public class BeforeRating extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_rating);
        //gotoPlayStore();
    }


    public void gotoPlayStore(View v) {

        //Toast.makeText(getApplicationContext(), "Enter Something to Search", Toast.LENGTH_SHORT).show();

            try {
                Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                startActivity(goToMarket);

            } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
                //startActivity(new Intent(Intent.ACTION_VIEW,
                    //         Uri.parse("https://play.google.com/store/apps/details?id=vnmsmgroup.com.audiostoriesforkids")));


        }
        /*
        return true;
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }*/
    }


    public void ShareThisApp(View v) {
        //Toast.makeText(getApplicationContext(), "Enter Something to Search", Toast.LENGTH_SHORT).show();
        try {
            // refresh
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            //String shareBody = "Get Latest Tamil Islamic Information . Its Brilliant\n\"http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString();
            String shareBody = "இஸ்லாமிய பேச்சாளர்களுக்கான, பயான் குறிப்புகள் ஆன்ட்ராய்டு அப்:\n " + "http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName().toString();

            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Bayan App");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        } catch (ActivityNotFoundException e) {
            //nothing
        }
    }



    /*

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

    */

}