package sg.edu.np.madgroupyassignment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.format.DateUtils;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private int id = 1;

    @SuppressLint("NewApi")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        String title = message.getNotification().getTitle();
        String text = message.getNotification().getBody();
        String CHANNEL_ID = "Message";
        CharSequence name;
        NotificationChannel channel = new NotificationChannel(
                CHANNEL_ID,
                "Message Notification",
                NotificationManager.IMPORTANCE_HIGH);
        getSystemService(NotificationManager.class).createNotificationChannel(channel);

        RemoteViews collapsedView = new RemoteViews(getPackageName(), R.layout.notification_layout);
        collapsedView.setImageViewResource(R.id.PushNotification_Img, R.drawable.fush);
        collapsedView.setTextViewText(R.id.PushNotification_Title, title);
        collapsedView.setTextViewText(R.id.PushNotification_Body, text);
        collapsedView.setTextViewText(R.id.PushNotification_Timestamp, DateUtils.formatDateTime(this, System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME));

        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Notification.Builder notification = new Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.drawable.fush)
                .setContentIntent(contentIntent)
                .setCustomContentView(collapsedView)
                .setStyle(new Notification.DecoratedCustomViewStyle())
                .setAutoCancel(true);
        NotificationManagerCompat.from(this).notify(id, notification.build());
        id += 1;

        super.onMessageReceived(message);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Device Registration Tokens").child(mAuth.getUid());
        databaseReference.setValue(token);
    }
}
