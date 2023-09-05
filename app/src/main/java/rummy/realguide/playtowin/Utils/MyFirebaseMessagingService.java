package rummy.realguide.playtowin.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Build;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import rummy.realguide.playtowin.Activity.SplashActivity;
import rummy.realguide.playtowin.R;
import rummy.realguide.playtowin.async.GeneratePictureStyleNotificationAsync;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Random;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private JSONObject jsonObject;
    private String image = "";
    private String message = "";
    private String title = "";
    private String contenttext = "";
    private int notificationid = 123;
    private Notification notification = null;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        JSONObject data = new JSONObject(remoteMessage.getData());
        if (!data.has("custom"))
        {
            if (remoteMessage.getData().size() > 0) {
                handleDataMessage(remoteMessage.getData());
            }
        }
    }

    private void handleDataMessage(Map<String, String> intent) {

        String message = intent.get("description");
        String title = intent.get("title");
        String image = intent.get("image");
        String screenNo = intent.get("screenNo");
        String matchId = intent.get("matchId");
        String url = intent.get("url");

        String notificationID = intent.get("Notification_Id");
        try {
            jsonObject = new JSONObject();
            if (image != null) {
                jsonObject.put("image", image);
            } else {
                jsonObject.put("image", "");
            }
            if (title != null) {
                jsonObject.put("title", title);
            } else {
                jsonObject.put("title", "");
            }
            if (matchId != null) {
                jsonObject.put("matchId", matchId);
            } else {
                jsonObject.put("matchId", "");
            }
            if (matchId != null) {
                jsonObject.put("url", url);
            } else {
                jsonObject.put("url", "");
            }
            if (title != null) {
                jsonObject.put("screenNo", screenNo);
            } else {
                jsonObject.put("screenNo", "");
            }
            if (message != null) {
                jsonObject.put("description", message);
            } else {
                jsonObject.put("description", "");
            }

            if (notificationID != null) {
                jsonObject.put("Notification_Id", notificationID);
            } else {
                jsonObject.put("Notification_Id", "");
            }
            generateNotification(getBaseContext(), jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void generateNotification(Context context, String data) {
        Intent notificationIntent = new Intent(context, SplashActivity.class);
        notificationIntent.putExtra("bundle", data);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent intent = null;
        try {
            if (new JSONObject(data).getString("Notification_Id") != null && new JSONObject(data).getString("Notification_Id").trim().length() > 0) {
                notificationid = Integer.parseInt(new JSONObject(data).getString("Notification_Id"));
            } else {
                Random r = new Random();
                notificationid = r.nextInt(500);
            }
            intent = PendingIntent.getActivity(context, notificationid, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            image = new JSONObject(data).getString("image").trim();
            title = new JSONObject(data).getString("title").trim();
            message = new JSONObject(data).getString("description").trim();
            if (title.trim().length() == 0) {
                title = Global_App.APPNAME;
            }
            if (image.equalsIgnoreCase("0") || image.equalsIgnoreCase("")) {
                if (message.trim().length() > 40) {
                    contenttext = message.substring(0, 40);
                } else {
                    contenttext = message;
                }
                Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(), R.mipmap.appicon);
                NotificationManager notificationmanager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("channel_01", "Playback Notification", NotificationManager.IMPORTANCE_HIGH);
                    channel.setSound(null, null);
                    AudioAttributes att = new AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build();
                    channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), att);
                    channel.enableVibration(true);
                    channel.enableLights(true);
                    channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                    channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000});
                    notificationmanager.createNotificationChannel(channel);
                    notification = new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.appicon)
                            .setContentText(contenttext)
                            .setContentIntent(intent)
                            .setAutoCancel(true)
                            .setOngoing(true)
                            .setLargeIcon(icon1)
                            .setBadgeIconType(R.mipmap.appicon)
                            .setChannelId("channel_01")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setStyle(new Notification.BigTextStyle().bigText(message))
                            .setContentTitle(title).build();
                } else {
                    notification = new Notification.Builder(getApplicationContext())
                            .setSmallIcon(R.mipmap.appicon)
                            .setContentText(contenttext)
                            .setContentIntent(intent)
                            .setAutoCancel(true)
                            .setOngoing(true)
                            .setLargeIcon(icon1)
                            .setVibrate(new long[]{1000, 1000, 1000, 1000})
                            .setLights(Color.RED, 3000, 3000)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setStyle(new Notification.BigTextStyle().bigText(message))
                            .setContentTitle(title).build();
                }
                if (notificationmanager != null) {
                    notificationmanager.notify(notificationid, notification);
                }
            } else {
                new GeneratePictureStyleNotificationAsync(context, title, message, image, data).execute();

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
