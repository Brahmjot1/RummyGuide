package rummy.realguide.playtowin;

import static rummy.realguide.playtowin.Activity.SplashActivity.responseMain;
import static rummy.realguide.playtowin.Utils.Utility.isStringNullOrEmpty;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.navigation.NavigationView;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import rummy.realguide.playtowin.Activity.WebActivity;
import rummy.realguide.playtowin.Adapter.HomeDataAdapter;
import rummy.realguide.playtowin.Model.CategoryModel;
import rummy.realguide.playtowin.Model.ResponseModel;
import rummy.realguide.playtowin.Utils.Global_App;
import rummy.realguide.playtowin.Utils.Utility;
import rummy.realguide.playtowin.Utils.font.BoldTextView;
import rummy.realguide.playtowin.Utils.font.RegularTextView;
import rummy.realguide.playtowin.Utils.recyclerviewpager.PagerAdapter;
import rummy.realguide.playtowin.Utils.recyclerviewpager.PagerModel;
import rummy.realguide.playtowin.Utils.recyclerviewpager.RecyclerViewPager;
import rummy.realguide.playtowin.async.DownloadImageShareAsync;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private Toolbar toolbar;

    private RelativeLayout loutSlider;
    private RecyclerViewPager pagerSlider;
    private BoldTextView txtUpcoming;
    public static String homeWebUrl = "";
    private LinearLayout bannerContainer, banner_container_top;
    private ImageView menuAdBanner;
    private WebView webNote;
    private NavigationView navView;
    private LinearLayout MenuShareApp;
    private LinearLayout menuContactUs;
    private LinearLayout menuPrivacyPolicy;
    private LinearLayout menuRateApp;
    private LinearLayout MenuAboutUs;
    Utility utility;
    private LinearLayout txtMenuFooter;
    private RegularTextView txtAppVersion;
    Dialog dialogExit;
    String emailID, privacyPolicy,aboutUS;
    private LinearLayout loutInflate;
    RecyclerView rvHomeData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    public void setHeaderView(String title) {
        View v = LayoutInflater.from(this).inflate(R.layout.actionbar_home, null);
        TextView action_bar_title = v.findViewById(R.id.action_bar_title);
        action_bar_title.setText(title);
        ImageView imgNavigation = v.findViewById(R.id.imgNavigation);
        imgNavigation.setImageResource(R.drawable.icon_menu);
        getSupportActionBar().setCustomView(v);
        imgNavigation.setOnClickListener(view -> {
            try {
                drawerLayout.openDrawer(Gravity.LEFT);
            } catch (Exception r) {
                r.printStackTrace();
            }
        });
    }

    private void initView() {
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHeaderView(getResources().getString(R.string.app_name));
        utility = new Utility();
        loutSlider = findViewById(R.id.loutSlider);
        rvHomeData = findViewById(R.id.rvHomeData);
        loutInflate = findViewById(R.id.loutInflate);
        pagerSlider = findViewById(R.id.pager);
        txtUpcoming = findViewById(R.id.txtUpcoming);
        bannerContainer = findViewById(R.id.banner_container);
        banner_container_top = findViewById(R.id.banner_container_top);
        menuAdBanner = findViewById(R.id.menuAdBanner);
        webNote = findViewById(R.id.webNote);
        navView = findViewById(R.id.nav_view);
        MenuShareApp = findViewById(R.id.MenuShareApp);
        menuContactUs = findViewById(R.id.menuContactUs);
        menuPrivacyPolicy = findViewById(R.id.menuPrivacyPolicy);
        menuRateApp = findViewById(R.id.menuRateApp);
        MenuAboutUs = findViewById(R.id.MenuAboutUs);
        txtMenuFooter = findViewById(R.id.txtMenuFooter);
        txtAppVersion = findViewById(R.id.txtAppVersion);

        menuContactUs.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.setPackage("com.google.android.gm");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{emailID});
            i.putExtra(Intent.EXTRA_SUBJECT, "");
            i.putExtra(Intent.EXTRA_TEXT, "");
            try {
                startActivity(Intent.createChooser(i, "Send mail..."));
            } catch (ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawers();
                }
            }, 1000);
        });

        MenuAboutUs.setOnClickListener(v -> {
            Intent in = new Intent(MainActivity.this, WebActivity.class);
            in.putExtra("title", "About Us");
            in.putExtra("urlWeb", aboutUS);
            startActivity(in);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawers();
                }
            }, 1000);
        });


        menuRateApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String appPackageName = getPackageName(); // package name of the app
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawers();
                    }
                }, 1000);
            }
        });



        menuPrivacyPolicy.setOnClickListener(v -> {
            Intent in = new Intent(MainActivity.this, WebActivity.class);
            in.putExtra("title", "Privacy Policy");
            in.putExtra("urlWeb", privacyPolicy);
            startActivity(in);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    drawerLayout.closeDrawers();
                }
            }, 1000);
        });

        OneSignal.promptForPushNotifications();
        setHomeData(MainActivity.this, responseMain);
    }

    @Override
    public void onBackPressed() {
        if (dialogExit != null) {
            if (!dialogExit.isShowing()) {
                dialogExit.show();
            }
        }
    }

    public void setHomeData(Activity activity, ResponseModel responseMain) {

        if (responseMain != null) {
            if (responseMain.getHomeData()!=null)
            {
                HomeDataAdapter homeDataAdapter=new HomeDataAdapter(MainActivity.this,responseMain.getHomeData());
                rvHomeData.setAdapter(homeDataAdapter);
                rvHomeData.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            }
            if (responseMain.getHomeSlider() != null && responseMain.getHomeSlider().size() > 0) {
                pagerSlider.setVisibility(View.VISIBLE);
                loutSlider.setVisibility(View.VISIBLE);
                pagerSlider.setClear();
                ArrayList<CategoryModel> dataSlider = responseMain.getHomeSlider();
                for (int i = 0; i < dataSlider.size(); i++) {
                    pagerSlider.addItem(new PagerModel(dataSlider.get(i).getImage(), "", getApplicationContext()));
                }
                pagerSlider.start();

                pagerSlider.setOnItemClickListener(new PagerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Utility.Redirect(activity, responseMain.getHomeSlider().get(position));
                    }
                });
            } else {
                pagerSlider.setVisibility(View.GONE);
                loutSlider.setVisibility(View.GONE);
            }

            if (responseMain.getAppVersion() != null) {
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;

                    if (!responseMain.getAppVersion().equals(version)) {
                        Utility.UpdateApp(MainActivity.this, responseMain.getIsForupdate(), responseMain.getAppUrl(), responseMain.getUpdateMessage());
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (Utility.getIsFromnotification(MainActivity.this)) {
                    Utility.setIsFromnotification(MainActivity.this, false);
                    Utility.Redirect(MainActivity.this, Utility.getNotificationData(MainActivity.this));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (responseMain.getAdGInd() != null) {
                Utility.interID = responseMain.getAdGInd();
            }

            if (responseMain.getAdGBanner() != null) {
                Utility.bannerID = responseMain.getAdGBanner();
            }
            if (responseMain.getAdGReward() != null) {
                Utility.rewardID = responseMain.getAdGReward();
            }

            if (responseMain.getPrivacyPolicy() != null) {
                privacyPolicy = responseMain.getPrivacyPolicy();
            }

            if (responseMain.getMailId() != null) {
                emailID = responseMain.getMailId();
            }

            if (responseMain.getAboutus() != null) {
                aboutUS = responseMain.getAboutus();
            }

            if (responseMain.getExitDialoge() != null) {
                loadHomeExit(MainActivity.this, responseMain.getExitDialoge());
            } else {
                loadHomeExit(MainActivity.this, null);
            }

            if (responseMain.getHomeDiloage() != null) {
                CategoryModel popData = responseMain.getHomeDiloage();
                Utility.ShomeHomePopup(activity, popData);
            }

            if (!isStringNullOrEmpty(responseMain.getHomeNote())) {
                webNote.setVisibility(View.VISIBLE);
                webNote.loadDataWithBaseURL(null, responseMain.getHomeNote(), "text/html", "UTF-8", null);
            }

            if (responseMain.getMenuBanner() != null) {
                if (!isStringNullOrEmpty(responseMain.getMenuBanner().getImage())) {
                    menuAdBanner.setVisibility(View.VISIBLE);
                    if (responseMain.getMenuBanner().getImage().contains(".gif")) {
                        Glide.with(activity)
                                .load(responseMain.getMenuBanner().getImage())
                                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                                .into(menuAdBanner);
                    } else {
                        Picasso.get().load(responseMain.getMenuBanner().getImage()).into(menuAdBanner);
                    }
                    menuAdBanner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!isStringNullOrEmpty(responseMain.getMenuBanner().getUrl())) {
                                Uri uri = Uri.parse(responseMain.getMenuBanner().getUrl());
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
                    menuAdBanner.setVisibility(View.GONE);
                }
            }
            if (responseMain.getAppVersion() != null) {
                try {
                    PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                    String version = pInfo.versionName;
                    txtAppVersion.setText("Version: " + version);
                    if (!responseMain.getAppVersion().equals(version)) {
                        Utility.UpdateApp(MainActivity.this, responseMain.getIsForupdate(), responseMain.getAppUrl(), responseMain.getUpdateMessage());
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (Utility.getIsFromnotification(MainActivity.this)) {
                    Utility.setIsFromnotification(MainActivity.this, false);
                    Utility.Redirect(MainActivity.this, Utility.getNotificationData(MainActivity.this));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (responseMain.getButtomeAds() != null) {
                if (responseMain.getButtomeAds().getType() != null) {
                    utility.LoadBannerAd(MainActivity.this, bannerContainer, responseMain.getButtomeAds());
                }
            }

            if (responseMain.getTopAds() != null) {
                if (responseMain.getTopAds().getType() != null) {
                    utility.LoadBannerAd(MainActivity.this, banner_container_top, responseMain.getTopAds());
                }
            }

            MenuShareApp.setOnClickListener(v -> {
                if (responseMain.getShareUrl() != null && !responseMain.getShareUrl().isEmpty()) {
                    if (Build.VERSION.SDK_INT >= 23) {
                        int hasWriteContactsPermission = checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 74);
                        } else {
                            shareImageData1(MainActivity.this, responseMain.getShareUrl(), "", responseMain.getShareMessage());
                        }
                    } else {
                        shareImageData1(MainActivity.this, responseMain.getShareUrl(), "", responseMain.getShareMessage());
                    }
                } else {
                    shareImageData1(MainActivity.this, "", "", responseMain.getShareMessage());
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawers();
                    }
                }, 1000);
            });
        }
    }

    public void loadHomeExit(Activity activity, CategoryModel popData) {
        if (activity != null) {
            dialogExit = new Dialog(activity);
            dialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogExit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialogExit.setContentView(R.layout.dialogue_home_exit);
            dialogExit.setCancelable(false);
            Button btnYesExit = dialogExit.findViewById(R.id.btnYesExit);
            LinearLayout lWatch = dialogExit.findViewById(R.id.lWatch);
            TextView txtWatch = dialogExit.findViewById(R.id.txtWatch);
            TextView txtTitle = dialogExit.findViewById(R.id.txtTitle);
            TextView btnNoExit = dialogExit.findViewById(R.id.btnNoExit);
            LinearLayout lDataExit = dialogExit.findViewById(R.id.lDataExit);
            ProgressBar probrBanner = dialogExit.findViewById(R.id.probrBanner);
            RelativeLayout relPopup = dialogExit.findViewById(R.id.relPopup);
            ImageView imgBanner = dialogExit.findViewById(R.id.imgBanner);

            if (popData != null) {
                lDataExit.setVisibility(View.VISIBLE);
                txtTitle.setText(popData.getTitle());
                TextView txtMessage = dialogExit.findViewById(R.id.txtMessage);
                txtMessage.setText(popData.getDescription());
                if (!isStringNullOrEmpty(popData.getBtnName())) {
                    txtWatch.setText(popData.getBtnName());
                }

                if (!isStringNullOrEmpty(popData.getImage())) {
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

                        Glide.with(activity)
                                .load(popData.getImage())
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                                        probrBanner.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(imgBanner);
                    }

                } else {

                    imgBanner.setVisibility(View.GONE);
                    probrBanner.setVisibility(View.GONE);
                }


                relPopup.setOnClickListener(v -> Utility.Redirect(activity, popData));

                lWatch.setOnClickListener(v -> {
                    if (!activity.isFinishing()) {
                        dialogExit.dismiss();
                    }
                    Utility.Redirect(activity, popData);
                });
            } else {
                lDataExit.setVisibility(View.GONE);
            }

            btnNoExit.setOnClickListener(v -> {
                if (!activity.isFinishing()) {
                    dialogExit.dismiss();
                }

            });

            btnYesExit.setOnClickListener(view -> {
                if (!activity.isFinishing()) {
                    dialogExit.dismiss();
                }
                finish();
            });

            Display display = activity.getWindowManager().getDefaultDisplay();
            int width = display.getWidth() - 18;
            WindowManager.LayoutParams lp;
            lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialogExit.getWindow().getAttributes());
            lp.width = (int) (width);
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            dialogExit.getWindow().setAttributes(lp);
        }
    }

    public void shareImageData1(Activity activity, String img, String pkg, String msg) {
        Intent share;
        if (img.trim().length() > 0) {
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/QuantumAI/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String[] str = img.trim().split("/");
            String extension = "";
            if (str[str.length - 1].contains(".")) {
                extension = str[str.length - 1].substring(str[str.length - 1].lastIndexOf("."));
            }
            if (extension.equals(".png") || extension.equals(".jpg") || extension.equals(".gif")) {
                extension = "";
            } else {
                extension = ".png";
            }
            File file = new File(dir, str[str.length - 1] + extension);
            if (file.exists()) {
                try {
                    share = new Intent(Intent.ACTION_SEND);
                    Uri uri = null;
                    if (Build.VERSION.SDK_INT >= 24) {
                        share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        uri = FileProvider.getUriForFile(activity.getApplicationContext(), activity.getPackageName(), file);
                    } else {
                        uri = Uri.fromFile(file);
                    }
                    share.setType("image/*");
                    if (img.contains(".gif")) {
                        share.setType("image/gif");
                    } else {
                        share.setType("image/*");
                    }
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    share.putExtra(Intent.EXTRA_SUBJECT, Global_App.APPNAME);
                    share.putExtra(Intent.EXTRA_TEXT, msg);
                    activity.startActivity(Intent.createChooser(share, "Share"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Utility.checkInternetConnection(activity)) {
                new DownloadImageShareAsync(activity, file, img, "", msg).execute();
            }
        } else {
            try {
                share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_SUBJECT, "");
                share.putExtra(Intent.EXTRA_TEXT, msg);
                share.setType("text/plain");
                activity.startActivity(Intent.createChooser(share, "Share"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 74) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareImageData1(MainActivity.this, responseMain.getShareUrl(), "", responseMain.getShareMessage());
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}