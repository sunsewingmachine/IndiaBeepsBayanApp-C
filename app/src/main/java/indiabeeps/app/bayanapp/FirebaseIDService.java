package indiabeeps.app.bayanapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by kaxhiftaj on 8/7/17.
 */

public class FirebaseIDService extends FirebaseMessagingService {
    private static final String TAG = "FirebaseIDService";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        sendRegistrationToServer(s);

    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }
}