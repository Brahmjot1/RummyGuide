package rummy.realguide.playtowin.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import rummy.realguide.playtowin.MainActivity;
import rummy.realguide.playtowin.Model.ResponseModel;
import rummy.realguide.playtowin.R;
import rummy.realguide.playtowin.Utils.Utility;
import rummy.realguide.playtowin.async.GetHomeDataAsync;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {
    public static ResponseModel responseMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);

        setContentView(R.layout.activity_splash);
        try {
            if (getIntent().getStringExtra("bundle") != null && getIntent().getStringExtra("bundle").trim().length() > 0) {
                Utility.setNotificationData(SplashActivity.this, getIntent().getExtras().getString("bundle"));
                Utility.setIsFromnotification(SplashActivity.this, true);
            } else {
                Utility.setIsFromnotification(SplashActivity.this, false);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Utility.setIsFromnotification(SplashActivity.this, false);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("global");

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            FirebaseMessaging.getInstance().subscribeToTopic("globalV" + version);
            Utility.setAppVersion(SplashActivity.this, version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        int todayOpen = Utility.getTodayOpen(SplashActivity.this) + 1;
        Utility.setTodayOpen(SplashActivity.this, todayOpen);

        if (Utility.getDate(SplashActivity.this).matches("0")) {
            Utility.setDate(SplashActivity.this, getCurrentDate());
        }

        if (!Utility.getDate(SplashActivity.this).matches(getCurrentDate())) {
            Utility.setTodayOpen(SplashActivity.this, 1);
            Utility.setTotalOpen(SplashActivity.this, Utility.getTotalOpen(SplashActivity.this) + 1);
            Utility.setDate(SplashActivity.this, getCurrentDate());
            Utility.cleanDialogID(SplashActivity.this);
        }

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            try {
                                if (deepLink != null) {
                                    String str = new Gson().toJson(splitQuery(new URL(deepLink.toString())));
                                    Utility.setReferData(SplashActivity.this, str.toString());

                                } else {
                                    Utility.setReferData(SplashActivity.this, "");
                                }

                            } catch (UnsupportedEncodingException | MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utility.setReferData(SplashActivity.this, "");
                    }
                });

        AsyncTask<Void, Void, String> task = new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                AdvertisingIdClient.Info idInfo = null;
                try {
                    idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String advertId = null;
                try {
                    advertId = idInfo.getId();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return advertId;
            }

            @Override
            protected void onPostExecute(String advertId) {
                Utility.setAdID(SplashActivity.this, advertId);
            }
        };
        task.execute();

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }
                        String token = task.getResult();
                        Utility.setFCMRegId(SplashActivity.this, token);
                    }
                });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new GetHomeDataAsync(SplashActivity.this);
            }
        }, 1500);

    }

    public String getCurrentDate() {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");

        return curFormater.format(c);
    }


    public static Map<String, String> splitQuery(URL url) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String query = url.getQuery();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
        }
        return query_pairs;
    }

    public void setData(Activity activity, ResponseModel responseModel) {

        responseMain = responseModel;
//                if (responseMain.getIsRedirectBrowser() != null) {
//                    if (responseMain.getIsRedirectBrowser().matches("1")) {
//                        String brawserUrl = responseMain.getIsRedirectUrl();
//                        if (brawserUrl != null && !brawserUrl.trim().isEmpty()) {
//                            Intent intent = new Intent(SplashActivity.this, WebActivity.class);
//                            intent.putExtra("urlWeb", brawserUrl);
//                            startActivity(intent);
//                            finish();
//                        }
//                    } else {
//                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
//                        finish();
//
//                    }
//                } else {
        startActivity(new Intent(SplashActivity.this, MainActivity.class));
        finish();

//                }


    }
}