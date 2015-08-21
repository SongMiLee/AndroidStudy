package song.personal.dessin;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;

/**
 * Created by song on 2015-08-21.
 */
public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        //불러올 화면, setContentView와 일맥상통.
        addPreferencesFromResource(R.xml.activity_setting);

        //항목과 연결
        Preference settingFacebook=(Preference)findPreference("setting_facebook");
        Preference setttingTwitter=(Preference)findPreference("setting_twitter");
        Preference settingInstagram=(Preference)findPreference("setting_instagram");

        //항목 클릭했을 때
        settingFacebook.setOnPreferenceClickListener(this);
        setttingTwitter.setOnPreferenceClickListener(this);
        settingInstagram.setOnPreferenceClickListener(this);
    }
    /**
     * 항목 클릭시 처리할 내용 오버라이드
     * */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals("setting_facebook")){

        }else if(preference.getKey().equals("setting_twitter")){

        }else if(preference.getKey().equals("setting_instagram")){

        }
        return false;
    }
}
