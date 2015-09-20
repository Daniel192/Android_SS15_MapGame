package android.mi.ur.de.android_ss15_mapgame;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Daniel on 20.09.2015.
 */
public class ParseApplication extends Application {

    @Override public void onCreate() {
        super.onCreate();

        Parse.initialize(this, "FjnQ7hWVa83BIknGsWTu1Hh0NuQFHbzIpAgMmggK", "i1tHxKksviI1V7kbxsnMN2y3x0AX5DrtUEG5YTow");
    }
}
