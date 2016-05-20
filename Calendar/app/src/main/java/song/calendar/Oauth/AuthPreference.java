package song.calendar.Oauth;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by home on 2016-05-19.
 */
public class AuthPreference {
    private SharedPreferences preferences;

    public AuthPreference(Context context){
        preferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    public void setUser(String user){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("user", user);
        editor.commit();
    }

    public void setToken(String password){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("token", password);
        editor.commit();
    }

    public String getUser(){ return preferences.getString("user", null); }
    public String getToken(){ return  preferences.getString("token", null); }
}
