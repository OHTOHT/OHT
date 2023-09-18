package code.data;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import code.system.Sell;

public class ThisApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // initialize admob
        Sell.initFirebase(this);

    }

}
