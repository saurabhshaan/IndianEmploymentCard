package in.iecindia.www.indianemploymentcard;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by root on 30/1/18.
 */

public class PrefManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context con;

    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "IEC";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    public PrefManager(Context context){
        this.con = context;
        preferences = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor  = preferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFristTime){
        editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFristTime);
        editor.commit();
    }
    public boolean isFirstTimeLaunch(){
        return preferences.getBoolean(IS_FIRST_TIME_LAUNCH,true);
    }
}