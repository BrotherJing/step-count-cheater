package brotherjing.com.stepcountmaster;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by jingyanga on 2016/8/18.
 */
public class SharedPrefUtil {

    public static SharedPreferences getSharedPreference(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void putString(Context context, String key, String value){
        SharedPreferences.Editor editor = getSharedPreference(context).edit();
        editor.putString(key, value).apply();
    }

    public static String getString(Context context, String key, String defaultValue){
        return getSharedPreference(context).getString(key, defaultValue);
    }

}
