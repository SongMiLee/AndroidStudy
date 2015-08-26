package song.personal.dessin;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

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
        Preference settingSaveview=(Preference)findPreference("setting_saveview");
        Preference settingCurview=(Preference)findPreference("setting_curview");

        //항목 클릭했을 때
        settingSaveview.setOnPreferenceClickListener(this);
        settingCurview.setOnPreferenceClickListener(this);
    }
    /**
     * 항목 클릭시 처리할 내용 오버라이드
     * */
    @Override
    public boolean onPreferenceClick(Preference preference) {
        if(preference.getKey().equals("setting_saveview")){
            Intent i=new Intent();
            //Gallery 불러오기
            i.setType("image/*");
            i.setAction(Intent.ACTION_PICK);

            try{
                i.putExtra("return-data",true);
                startActivityForResult(Intent.createChooser(i,"이미지 불러오기"),200);
            }catch (Exception e){e.printStackTrace();}

        }else if(preference.getKey().equals("setting_curview")){

        }
        return false;
    }

    /**
     * 함수 오버라이딩
     * 이미지 불러왔을때 처리
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==200){
            try {
                Bundle extra = data.getExtras();
                if (extra != null) {
                    Intent i=new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");

                    Log.d("URI : ", data.getDataString());
                    i.putExtra(Intent.EXTRA_STREAM, data.getData());
                    startActivity(Intent.createChooser(i, "공유"));

                }
            }catch (Exception e){ e.printStackTrace();}
        }
    }
}
