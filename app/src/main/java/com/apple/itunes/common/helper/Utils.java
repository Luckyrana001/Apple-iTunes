package com.apple.itunes.common.helper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.apple.itunes.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {
    Context context;

    public Utils(Context context) {
        this.context = context;
    }

    /**
     * Return date in specified format.
     *
     * @param milliSeconds Date in milliseconds
     * @param dateFormat   Date format
     * @return String representing date in specified format
     */
    public static String getDate(long milliSeconds, String dateFormat) {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public static boolean isNetworkConnected(Context context, boolean isShowMessage, int teamId) {
        return isNetworkConnected(context, isShowMessage, false, teamId);
    }

    public static boolean isNetworkConnected(Context context, boolean isShowMessage, boolean isClose, int teamId) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            String message = "";
            boolean airPlaneMode = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
            if (airPlaneMode) {
                message = "Airplane Mode Turn On, Use Wi-Fi or Cellular data to Access Data";
            } else {
                message = context.getResources().getString(R.string.no_internet_connection);
            }

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null
                    && activeNetwork.isConnected();
            if (!isConnected && isShowMessage) {
                showNoNetworkMessage(context, isClose, message, teamId);
            }
            return isConnected;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static void showNoNetworkMessage(final Context context, final boolean isClose, String message, int teamId) {
        new MaterialDialog.Builder(context)
                .content(message)
                .positiveText(R.string.action_settings)
                .negativeText(R.string.OK)
                .cancelable(false)
                .iconRes(android.R.drawable.ic_dialog_alert)
                .onPositive((dialog, which) -> {
                    Intent intent = new Intent(Settings.ACTION_SETTINGS);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    dialog.dismiss();
                })

                .onNegative((dialog, which) -> dialog.cancel()).show();





    }
}
