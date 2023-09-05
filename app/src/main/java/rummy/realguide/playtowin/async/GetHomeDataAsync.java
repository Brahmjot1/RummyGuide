package rummy.realguide.playtowin.async;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.Gson;
import rummy.realguide.playtowin.Activity.SplashActivity;
import rummy.realguide.playtowin.MainActivity;
import rummy.realguide.playtowin.Model.ResponseModel;
import rummy.realguide.playtowin.Utils.ApiClient;
import rummy.realguide.playtowin.Utils.ApiInterface;
import rummy.realguide.playtowin.Utils.Global_App;
import rummy.realguide.playtowin.Utils.Utility;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GetHomeDataAsync {
    private Activity activity;

    private JSONObject jObject;

    public GetHomeDataAsync(final Activity activity) {
        this.activity = activity;

        try {
            jObject = new JSONObject();
            jObject.put("device_id", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            jObject.put("FCMID", Utility.getFCMRegId(activity));
            jObject.put("adId", Utility.getAdID(activity));
            if (Utility.getReferData(activity).length() > 0) {
                jObject.put("deplinkdata", new JSONObject(Utility.getReferData(activity)));
            } else {
                jObject.put("deplinkdata", "");
            }
            jObject.put("todayOpen", String.valueOf(Utility.getTodayOpen(activity)));
            jObject.put("totalOpen", String.valueOf(Utility.getTotalOpen(activity)));
            jObject.put("deviceName", Build.MODEL);
            jObject.put("deviceVersion", Build.VERSION.RELEASE);
            jObject.put("appVersion", Utility.getAppVersion(activity));
            jObject.put("verifyInstallerId", Utility.verifyInstallerId(activity));
            jObject.put("deviseId", Settings.Secure.getString(activity.getContentResolver(), Settings.Secure.ANDROID_ID));
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<ResponseModel> call = apiService.getHomeData(jObject.toString());
            call.enqueue(new Callback<ResponseModel>() {
                @Override
                public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                    onPostExecute(response.body());
                }

                @Override
                public void onFailure(Call<ResponseModel> call, Throwable t) {

                    Log.e("Error--)", "" + t.getMessage());
                    if (!call.isCanceled()) {
                        Utility.NotifyFinish(activity, Global_App.APPNAME, Global_App.msg_Service_Error);
                    }
                }
            });
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void onPostExecute(ResponseModel responseModel) {
        try {

            Utility.setReferData(activity, "");
            if (responseModel.getStatus().equals(Global_App.STATUS_SUCCESS)) {
                Utility.setAsync(activity, "HomeData", new Gson().toJson(responseModel));
                ((SplashActivity)activity).setData(activity,responseModel);

            } else if (responseModel.getStatus().equals(Global_App.STATUS_ERROR)) {
                Utility.Notify(activity, Global_App.APPNAME, responseModel.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

}
