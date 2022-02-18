package indiabeeps.app.bayanapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import androidx.core.app.NotificationCompat;

import java.util.Calendar;

public class NotificationService extends Service {
    public static final int NOTIFICATION_ID = 1;
    public NotificationService() {

        Calendar calendar = Calendar.getInstance();
        Calendar timefornotification = Calendar.getInstance();
        timefornotification.set(Calendar.HOUR_OF_DAY,11);
        timefornotification.set(Calendar.MINUTE,00);
        timefornotification.set(Calendar.SECOND,00);

        Long difference = calendar.getTimeInMillis() - timefornotification.getTimeInMillis();

        if(difference<0)

        {
            notificationTimer.postDelayed(notificationCaller, -(difference));
        }

        else

        {
            notificationTimer.postDelayed(notificationCaller, difference);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private Handler notificationTimer = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                sendNotifications();
            }
            super.handleMessage(msg);
        }
    };
    private Runnable notificationCaller = new Runnable() {

        @Override
        public void run() {
            Message msg = notificationTimer.obtainMessage();
            msg.what = 0;
            notificationTimer.sendMessage(msg);
        }
    };

    public void sendNotifications() {

        Intent intent = new Intent(this, Splash.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.logo);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo));
        builder.setContentTitle("குர்ஆன், ஹதீஸ்");
        builder.setContentText("படிக்க நேரம் ஒதுக்கலாமே!");
        //builder.setSubText("Tap to view documentation about notifications.\nHow much text can I put here I wonder...");
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, builder.build());

        notificationTimer.removeCallbacksAndMessages(null);
        notificationTimer.postDelayed(notificationCaller, 86400000);

    }
}