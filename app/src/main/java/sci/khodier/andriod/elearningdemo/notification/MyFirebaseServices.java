package sci.khodier.andriod.elearningdemo.notification;


import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Toast;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import sci.khodier.andriod.elearningdemo.MainActivity;
import sci.khodier.andriod.elearningdemo.R;


public class MyFirebaseServices extends FirebaseMessagingService {

    private static final String TAG = "PushNotification";
    private static final String CHANNEL_ID = "101";

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        try {
            Badges.setBadge(getBaseContext(), 1);
        } catch (BadgesNotSupportedException error) {
            Toast.makeText(getBaseContext(), "That was a error!", Toast.LENGTH_SHORT).show();

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(1, builder.build());
        }
    }
}
