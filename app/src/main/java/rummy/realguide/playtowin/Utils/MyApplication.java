package rummy.realguide.playtowin.Utils;

import android.app.Application;
import android.content.Context;

import com.onesignal.OneSignal;


public class MyApplication extends Application {

    public static final String ONESIGNAL_APP_ID = "7b8f5846-3b08-461c-984f-915839140252";

    public static final String TAG = MyApplication.class
            .getSimpleName();

    private static MyApplication mInstance;
    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = getApplicationContext();

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
//        OneSignal.promptForPushNotifications();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public static synchronized MyApplication getInstance() {
        return mInstance;
    }

}
