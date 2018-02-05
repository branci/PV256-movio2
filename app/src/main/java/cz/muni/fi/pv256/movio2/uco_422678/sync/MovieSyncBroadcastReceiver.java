package cz.muni.fi.pv256.movio2.uco_422678.sync;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by BranislavSmik on 2/4/2018.
 */

public class MovieSyncBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        UpdaterSyncAdapter.getSyncAccount(context);
    }
}