package song.personal.dessin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

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
                startActivityForResult(Intent.createChooser(i,"이미지 불러오기"),Dessin.GET_PIC_URI);
            }catch (Exception e){e.printStackTrace();}

        }else if(preference.getKey().equals("setting_curview")){
            Dessin.mainLayout.setDrawingCacheEnabled(true);//캐시를 허용
            Bitmap screenshot=Bitmap.createBitmap(Dessin.mainLayout.getDrawingCache());//화면 캡쳐
            Dessin.mainLayout.setDrawingCacheEnabled(false);//캐시 비허용

            //sd 카드에 접근하여 저장
            String dirPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Dessin";
            File dir= new File(dirPath);

            //폴더가 없으면 새로 만듦
            if(!dir.exists())
                dir.mkdirs();

            FileOutputStream fileout=null;
            try{
                File tmpfile=new File(dir, "img_"+System.currentTimeMillis()+".jpeg");
                fileout=new FileOutputStream(tmpfile);
                screenshot.compress(Bitmap.CompressFormat.JPEG, 100, fileout);
                fileout.close();

                Intent i=new Intent(Intent.ACTION_SEND);//전송 메소드 호출
                i.setType("image/*");//공유할 타입

                Uri tmpUri=Uri.fromFile(tmpfile);//파일 uri 반환
                i.putExtra(Intent.EXTRA_STREAM,tmpUri);
                startActivity(Intent.createChooser(i,"공유하기"));
            }catch(Exception e){       e.printStackTrace();       }
            finally {
                if(fileout!=null){
                    try{
                        fileout.close();
                    }catch (Exception e){ e.printStackTrace();}
                }
            }
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

        if(requestCode==Dessin.GET_PIC_URI){
            try {
                Bundle extra = data.getExtras();
                if (extra != null) {
                    Intent i=new Intent(Intent.ACTION_SEND);
                    i.setType("image/*");

                    i.putExtra(Intent.EXTRA_STREAM, data.getData());
                    startActivity(Intent.createChooser(i, "공유"));

                }
            }catch (Exception e){ e.printStackTrace();}
        }
    }
}
