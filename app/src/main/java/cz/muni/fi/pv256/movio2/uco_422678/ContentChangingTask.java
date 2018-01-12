package cz.muni.fi.pv256.movio2.uco_422678;

import android.os.AsyncTask;
import android.support.v4.content.Loader;


/**
 * Created by BranislavSmik on 12/13/2017.
 */
abstract class ContentChangingTask<T1, T2, T3> extends
        AsyncTask<T1, T2, T3> {
    private Loader<?> loader=null;

    ContentChangingTask(Loader<?> loader) {
        this.loader=loader;
    }

    @Override
    protected void onPostExecute(T3 param) {
        loader.onContentChanged();
    }
}
