package rummy.realguide.playtowin.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import rummy.realguide.playtowin.Activity.WebActivity;
import rummy.realguide.playtowin.Model.CategoryModel;
import rummy.realguide.playtowin.R;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class Utility {

    public static String interID = "ca-app-pub-3940256099942544/1033173712", rewardID = "ca-app-pub-3940256099942544/5224354917", bannerID = "ca-app-pub-3940256099942544/6300978111";
    ProgressDialog dialogwait;

    public void showWaitDialog(Activity activity) {
        dialogwait = ProgressDialog.show(activity, "",
                "Ad loading. Please wait...", true);
        dialogwait.show();
    }

    public void dismissWaitDialog() {
        if (dialogwait != null) {
            dialogwait.dismiss();
        }
    }

    public static boolean checkInternetConnection(Activity context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null && cm.getActiveNetworkInfo() != null) {
                return cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected();
            }
        }
        return false;
    }

    public static String changeDateFormat(String currentFormat, String requiredFormat, String dateString) {
        String result = "";
        if (isStringNullOrEmpty(dateString)) {
            return result;
        }
        SimpleDateFormat formatterOld = new SimpleDateFormat(currentFormat, Locale.getDefault());
        SimpleDateFormat formatterNew = new SimpleDateFormat(requiredFormat, Locale.getDefault());
        Date date = null;
        try {
            date = formatterOld.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (date != null) {
            result = formatterNew.format(date);
        }
        return result;
    }

    public static void setFCMRegId(Context activity, String regId) {
        SharedPreferences pref = activity.getSharedPreferences("DEVICETOKEN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (regId != null) {
            editor.putString("FCMregId", regId);
        } else {
            editor.putString("FCMregId", "");
        }
        editor.apply();
    }

    public static void setNotificationData(Context activity, String NDATA) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(activity).edit();
        editor.putString("NDATA", NDATA);
        editor.commit();
    }

    public static boolean AppInstalledOrNot(Activity activity, String uri) {
        PackageManager pm = activity.getPackageManager();
        try {
            pm.getPackageInfo(uri, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static CategoryModel getNotificationData(Context activity) {
        try {
            String data = PreferenceManager.getDefaultSharedPreferences(activity).getString("NDATA", "");
            return new Gson().fromJson(data, CategoryModel.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void LoadBannerAd(Activity activity, LinearLayout banner, CategoryModel model) {
        banner.setVisibility(View.VISIBLE);
        View layout2 = LayoutInflater.from(activity).inflate(R.layout.ad_buttome, banner, false);

        ImageView imgTopBanner = (ImageView) layout2.findViewById(R.id.imgTopBanner);

        if (model != null) {
            if (!Utility.isStringNullOrEmpty(model.getImage())) {
                imgTopBanner.setVisibility(View.VISIBLE);
                if (model.getImage().contains(".gif")) {
                    Glide.with(activity)
                            .load(model.getImage())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(imgTopBanner);
                } else {
                    Picasso.get().load(model.getImage()).into(imgTopBanner);
                }
                imgTopBanner.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!Utility.isStringNullOrEmpty(model.getUrl())) {
                            Uri uri = Uri.parse(model.getUrl());
                            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                            try {
                                activity.startActivity(goToMarket);
                            } catch (ActivityNotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
            } else {
                imgTopBanner.setVisibility(View.GONE);
            }
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        banner.addView(layout2, params);
        banner.setBackgroundColor(Color.WHITE);
    }

    public static void setIsFromnotification(Activity activity, boolean value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("IsFromnotification", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
        prefsEditor.putBoolean("IsFromnotification", value);
        prefsEditor.commit();
    }

    public static void setIDArray(Activity activity, ArrayList<String> IDArray) {
        SharedPreferences pref = activity.getSharedPreferences("dialogIDArray", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(IDArray);
        editor.putString("dialogIDArray", json);
        editor.apply();
    }

    public static ArrayList<String> getIDArray(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("dialogIDArray", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = pref.getString("dialogIDArray", null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        ArrayList<String> arrayList = gson.fromJson(json, type);
        return arrayList;
    }

    public static void cleanDialogID(Activity activity) {
        SharedPreferences preferences = activity.getSharedPreferences("dialogIDArray", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static boolean verifyInstallerId(Activity context) {
        // A list with valid installers package name
        List<String> validInstallers = new ArrayList<>(Arrays.asList("com.android.vending", "com.google.android.feedback"));

        // The package name of the app that has installed your app
        final String installer = context.getPackageManager().getInstallerPackageName(context.getPackageName());

        // true if your app has been downloaded from Play Store
        return installer != null && validInstallers.contains(installer);
    }

    public void showImageLotteGIF(Activity mActivity, String imgURL, ImageView ivBanner, ImageView ivGIF, ProgressBar progressBar) {
        if (imgURL != null && imgURL.length() > 0) {

            if (imgURL.contains(".gif")) {
                ivGIF.setVisibility(View.VISIBLE);
                ivBanner.setVisibility(View.GONE);
                Glide.with(mActivity)
                        .load(imgURL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .into(ivGIF);
            } else {

                ivGIF.setVisibility(View.GONE);
                ivBanner.setVisibility(View.VISIBLE);
                Glide.with(mActivity)
                        .load(imgURL)
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }
                                return false;
                            }
                        })
                        .into(ivBanner);
            }

        }


    }

    public static void ShomeHomePopup(Activity activity, CategoryModel popData) {
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.setContentView(R.layout.dialogue_homepopup);
            dialog1.setCancelable(false);
            TextView btnGetNow = dialog1.findViewById(R.id.btnGetNow);
            ImageView btnCancel = dialog1.findViewById(R.id.ic_close);
            ProgressBar probrBanner = dialog1.findViewById(R.id.probrBanner);
            ImageView imgBanner = dialog1.findViewById(R.id.imgBanner);

            if (!isStringNullOrEmpty(popData.getIsForce()) && popData.getIsForce().equals("1")) {
                btnCancel.setVisibility(View.GONE);
            } else {
                btnCancel.setVisibility(View.VISIBLE);
            }

            if (!isStringNullOrEmpty(popData.getBtnName())) {
                btnGetNow.setText(popData.getBtnName());
            }
            if (!Utility.isStringNullOrEmpty(popData.getImage())) {
                if (popData.getImage().contains(".gif")) {
                    imgBanner.setVisibility(View.VISIBLE);
                    probrBanner.setVisibility(View.GONE);
                    Glide.with(activity)
                            .load(popData.getImage())
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                            .into(imgBanner);
                } else {
                    probrBanner.setVisibility(View.VISIBLE);
                    imgBanner.setVisibility(View.VISIBLE);
                    Picasso.get().load(popData.getImage()).into(imgBanner, new Callback() {
                        @Override
                        public void onSuccess() {
                            probrBanner.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            probrBanner.setVisibility(View.GONE);
                        }
                    });
                }

            } else {
                imgBanner.setVisibility(View.GONE);
                probrBanner.setVisibility(View.GONE);
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!activity.isFinishing()) {
                        dialog1.dismiss();
                    }
                }
            });

            btnGetNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!activity.isFinishing()) {
                        dialog1.dismiss();
                    }
                    Utility.Redirect(activity, popData);
                }
            });

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            WindowManager.LayoutParams lp;
            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog1.getWindow().getAttributes());
            lp.width = (int) (width);
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialog1.getWindow().setAttributes(lp);
            if (!activity.isFinishing() && !dialog1.isShowing()) {
                dialog1.show();
            }
        }
    }

    public static void setDate(Activity activity, String CurrDate) {
        SharedPreferences pref = activity.getSharedPreferences("CurrDate", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("CurrDate", CurrDate);
        editor.apply();
    }

    public static String getDate(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("CurrDate", Context.MODE_PRIVATE);
        return pref.getString("CurrDate", "0");
    }

    public static void setTodayOpen(Activity activity, int spin) {
        SharedPreferences pref = activity.getSharedPreferences("todayOpen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("todayOpen", spin);
        editor.apply();
    }

    public static int getTodayOpen(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("todayOpen", Context.MODE_PRIVATE);
        return pref.getInt("todayOpen", 0);
    }

    public static void setTotalOpen(Activity activity, int spin) {
        SharedPreferences pref = activity.getSharedPreferences("TotalOpen", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("TotalOpen", spin);
        editor.apply();
    }

    public static int getTotalOpen(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("TotalOpen", Context.MODE_PRIVATE);
        return pref.getInt("TotalOpen", 1);
    }

    public static void setHomePopID(Activity activity, String regId) {
        SharedPreferences pref = activity.getSharedPreferences("HomePopID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("HomePopID", regId);
        editor.apply();
    }


    public static String getHomePopID(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("HomePopID", Context.MODE_PRIVATE);
        return pref.getString("HomePopID", "");
    }

    public static void setReferData(Activity activity, String regId) {
        SharedPreferences pref = activity.getSharedPreferences("ReferData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("ReferData", regId);
        editor.apply();
    }


    public static String getReferData(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("ReferData", Context.MODE_PRIVATE);
        return pref.getString("ReferData", "");
    }

    public static void setAdID(Activity activity, String regId) {
        SharedPreferences pref = activity.getSharedPreferences("AdID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("AdID", regId);
        editor.apply();
    }


    public static String getAdID(Activity activity) {
        SharedPreferences pref = activity.getSharedPreferences("AdID", Context.MODE_PRIVATE);
        return pref.getString("AdID", "");
    }


    public static boolean getIsFromnotification(Activity activity) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("IsFromnotification", Context.MODE_PRIVATE);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean("IsFromnotification", false);
        }
        return false;
    }

    public static String getFCMRegId(Context activity) {
        String regId = "";
        if (activity != null) {
            SharedPreferences pref = activity.getSharedPreferences("DEVICETOKEN", Context.MODE_PRIVATE);
            if (pref != null) {
                regId = pref.getString("FCMregId", "");
            }
        }
        return regId;
    }

    public static void setAppVersion(Context activity, String regId) {
        SharedPreferences pref = activity.getSharedPreferences("AppVersion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("AppVersion", regId);
        editor.apply();
    }

    public static String getAppVersion(Context activity) {
        String regId = "";
        if (activity != null) {
            SharedPreferences pref = activity.getSharedPreferences("AppVersion", Context.MODE_PRIVATE);
            if (pref != null) {
                regId = pref.getString("AppVersion", "");
            }
        }
        return regId;
    }

    public static void setAsync(Context activity, String Key, String regId) {
        SharedPreferences pref = activity.getSharedPreferences(Key, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Key, regId);
        editor.apply();
    }

    public static boolean isStringNullOrEmpty(String text) {
        return (text == null || text.trim().equals("null") || text.trim()
                .length() <= 0);
    }


    public static String getAsync(Context activity, String Key) {
        String regId = "";
        if (activity != null) {
            SharedPreferences pref = activity.getSharedPreferences(Key, Context.MODE_PRIVATE);
            if (pref != null) {
                regId = pref.getString(Key, "");
            }
        }
        return regId;
    }

    public static void setEnableDiseble(final Activity activity, final View v) {
        v.setEnabled(false);
        Timer buttonTimer = new Timer();
        buttonTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                });
            }
        }, 2000);
    }

    public static String updateTimeRemaining(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            int hours = (int) ((timeDiff / (1000 * 60 * 60)) % 24);
            int days = (int) (timeDiff / (1000 * 60 * 60 * 24));
            if (days > 0) {
                return String.format(Locale.getDefault(), "%02d days left", days);
            } else {
                return String.format(Locale.getDefault(), "%02d:%02d:%02d", hours + (days * 24), minutes, seconds);
            }
        } else {
            return "Time's up!!";
        }
    }

    public static String MinitTimeRemaining(long timeDiff) {
        if (timeDiff > 0) {
            int seconds = (int) (timeDiff / 1000) % 60;
            int minutes = (int) ((timeDiff / (1000 * 60)) % 60);
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        } else {
            return "Time's up!!";
        }
    }


    public static long convertTimeInMillis(String dateTimeFormat, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            Date mDate = sdf.parse(date);
            return mDate.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void Notify(final Activity activity, String title, String message) {
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.setContentView(R.layout.dialogue_notify);
            dialog1.setCancelable(false);
            Button btnOk = dialog1.findViewById(R.id.btnSubmit);
            TextView txtTitle = dialog1.findViewById(R.id.txtTitle);
            txtTitle.setText(title);
            TextView txtMessage = dialog1.findViewById(R.id.txtMessage);
            txtMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                if (!activity.isFinishing()) {
                    dialog1.dismiss();
                }
            });
            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            WindowManager.LayoutParams lp;
            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog1.getWindow().getAttributes());
            lp.width = (int) (width - (width * 0.07));
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog1.getWindow().setAttributes(lp);
            if (!activity.isFinishing()) {
                dialog1.show();
            }
        }
    }

    public static void UpdateApp(final Activity activity, String isForupdate, final String appurl, String msg) {
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.setContentView(R.layout.dialogue_updateapp);
            Button btnUpdate = dialog1.findViewById(R.id.btnUpdate);
            Button btnCancel = dialog1.findViewById(R.id.btnCancel);
            TextView txtMessage = dialog1.findViewById(R.id.txtMessage);
            txtMessage.setText(msg);
            View viewDivider = dialog1.findViewById(R.id.viewDivider);
            if (isForupdate.equals("1")) {
                dialog1.setCancelable(false);
                btnUpdate.setVisibility(View.VISIBLE);
                viewDivider.setVisibility(View.GONE);
                btnCancel.setVisibility(View.GONE);
                btnUpdate.setBackgroundResource(R.drawable.btn_bg_diloage);
            } else {
                dialog1.setCancelable(true);
                btnUpdate.setVisibility(View.VISIBLE);
                viewDivider.setVisibility(View.VISIBLE);
                btnCancel.setVisibility(View.VISIBLE);
            }
            btnUpdate.setOnClickListener(v -> {
                if (!activity.isFinishing() && !isForupdate.equals("1")) {
                    dialog1.dismiss();
                }
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appurl));
                    activity.startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(activity, "No application can handle this request."
                            + " Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            });
            btnCancel.setOnClickListener(view -> {
                if (!activity.isFinishing()) {
                    dialog1.dismiss();
                }
            });
            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int hight = display.getHeight();
            WindowManager.LayoutParams lp;
            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog1.getWindow().getAttributes());
            lp.width = (int) (width - (width * 0.07));
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog1.getWindow().setAttributes(lp);
            if (!activity.isFinishing()) {
                dialog1.show();
            }
        }
    }

    public static void NotifyFinish(final Activity activity, String title, String message) {
        if (activity != null) {
            final Dialog dialog1 = new Dialog(activity);
            dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.setContentView(R.layout.dialogue_notify);
            dialog1.setCancelable(false);
            Button btnOk = dialog1.findViewById(R.id.btnSubmit);
            TextView txtTitle = dialog1.findViewById(R.id.txtTitle);
            txtTitle.setText(title);
            TextView txtMessage = dialog1.findViewById(R.id.txtMessage);
            txtMessage.setText(message);
            btnOk.setOnClickListener(v -> {
                if (!activity.isFinishing()) {
                    dialog1.dismiss();
                    activity.finish();
                }
            });
            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            WindowManager.LayoutParams lp;
            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog1.getWindow().getAttributes());
            lp.width = (int) (width - (width * 0.07));
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog1.getWindow().setAttributes(lp);
            if (!activity.isFinishing()) {
                dialog1.show();
            }
        }
    }

    public static void Redirect(Activity context, CategoryModel model) {
        Log.e("ScreeNo--)",""+model.getScreenNo());
        Log.e("ScreeNo--)",""+model.getUrl());
        if (model.getScreenNo() != null && model.getScreenNo().length() > 0) {
            switch (model.getScreenNo()) {
                case "2":
                    Intent in = new Intent(context, WebActivity.class);
                    in.putExtra("title", model.getTitle());
                    in.putExtra("url", model.getUrl());
                    context.startActivity(in);
                    break;
                case "0":
                    Uri uri = Uri.parse(model.getUrl());
                    Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_NEW_DOCUMENT | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    try {
                        context.startActivity(goToMarket);
                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }

    public int getRandomNumber() {
        int random = new Random().nextInt((3 - 1) + 1) + 1;
        return random;
    }
}
