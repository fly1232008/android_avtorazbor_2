package ru.amobilestudio.autorazborassistant.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import ru.amobilestudio.autorazborassistant.asyncs.MainAsync;
import ru.amobilestudio.autorazborassistant.helpers.ActivityHelper;

public class NetworkChangeReceiver extends BroadcastReceiver {

    private MainAsync _mainAsync;

    public NetworkChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager conMan = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();

            if (netInfo != null) {
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                    //start sync process
                    if(_mainAsync == null){
                        ActivityHelper.debug("not create async");
                        _mainAsync = new MainAsync(context);
                        _mainAsync.execute();
                    }else if(_mainAsync.getStatus() == AsyncTask.Status.FINISHED){
                        _mainAsync = new MainAsync(context);
                        _mainAsync.execute();
                    }
                }else{
                    cancelMainAsync();
//                    AlertDialogHelper.showAlertDialog(context, R.string.alert_title, R.string.turn_on_wifi, true);
                }
            }else{
                cancelMainAsync();
                //AlertDialogHelper.showAlertDialog(context, R.string.alert_title, R.string.turn_on_wifi, true);
                ActivityHelper.debug("wifi no");
            }
        }

    }

    public void cancelMainAsync(){
        if(_mainAsync != null)
            _mainAsync.cancel(false);
    }
}
