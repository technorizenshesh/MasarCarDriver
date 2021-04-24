package com.masarcardriver.helper;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.masarcardriver.R;

import static com.masarcardriver.retrofit.Constant.CONNECTIVITY_ACTION;


public class App extends Application {
    private static Snackbar snackbar = null;
    private static App mInstance;
    private static Toast toast = null;
    public static IntentFilter intentFilter;
    private static boolean activityVisible;


    public static synchronized App getInstance() {
        return mInstance;
    }

    // Showing network status in Snackbar
    public static void showSnack(final Context context, View view, boolean isConnected) {
        if (snackbar == null) {
            snackbar = Snackbar
                    .make(view, context.getString(R.string.network_failure), Snackbar.LENGTH_INDEFINITE)
                    .setAction("SETTINGS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            context.startActivity(intent);
                        }
                    });
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
        }

        if (!isConnected && !snackbar.isShown()) {
            snackbar.show();
        } else {
            snackbar.dismiss();
            snackbar = null;
        }
    }

    public static void showToast(final Context context, String text, int duration) {

        if (toast == null || toast.getView().getWindowVisibility() != View.VISIBLE) {
            toast = Toast.makeText(context, text, duration);
            toast.show();
        } else toast.cancel();
    }

    public static void hideSoftKeyboard(Activity context, View view) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException npe) {
        } catch (Exception e) {
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        intentFilter = new IntentFilter();
        intentFilter.addAction(CONNECTIVITY_ACTION);

    }

    public void setConnectivityListener(NetworkReceiver.ConnectivityReceiverListener listener) {
        NetworkReceiver.connectivityReceiverListener = listener;
    }


}

