package song.personal.dessin;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;

public class Dessin extends AppCompatActivity {
    public static int GET_PIC_URI=200;

    //액션바 메뉴
    ImageButton brushBtn;
    ImageButton eraseBtn;
    ImageButton saveBtn;
    ImageButton colorBtn;
    ImageButton menuBtn;
    ImageButton loadBtn;

    //선의 기본 설정
    int width=10;
    int color=Color.BLACK;

    //그림 그릴 뷰
    LinearLayout inflateView;
    public static DrawingView drawingView;
    ImageView imageView;

    //슬라이드 메뉴
    DisplayMetrics metrics;
    static LinearLayout mainLayout;
    LinearLayout menuLayout;
    FrameLayout.LayoutParams leftMenuLayoutParams;
    int leftMenuWidth;
    static boolean isLeftExpanded;
    Button menuBackground;
    Button menuSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //스플래시 화면 실행
        startActivity(new Intent(this, SplashActivity.class));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dessin);

        inflateView=(LinearLayout)findViewById(R.id.InflateLayout);

        //그림 그릴 뷰를 가져온다.
        drawingView=new DrawingView(this);
        inflateView.addView(drawingView);
        inflateView.bringToFront();

        imageView=(ImageView)findViewById(R.id.imageView);

        //시간이 걸리는 작업을 처리한다.
        initialize();

    }

    /**
     * 스플래시를 표시하는 것과 초기화를 동시에 진행시키기 위해 쓰레드 처리
     * */
    public void initialize(){
        InitializationRunnable init=new InitializationRunnable();
        new Thread(init).start();
    }

    /**
     * 초기화 작업 처리
     * */
    class InitializationRunnable implements Runnable, View.OnClickListener {

        @Override
        public void run() {
            //초기화 처리 작업

            //슬라이드 메뉴를 초기화 한다.
            initSlideMenu();

            //커스텀 뷰가 보일 수 있도록 옵션을 줌
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //커스텀 레이아웃을 보인다.
            getSupportActionBar().setCustomView(R.layout.activity_canvas);

            //1. 브러시 함수
            brushBtn=(ImageButton)findViewById(R.id.brushBtn);
            brushBtn.setOnClickListener(this);

            //2. 지우개 함수
            eraseBtn=(ImageButton)findViewById(R.id.eraseBtn);
            eraseBtn.setOnClickListener(this);

            //3.칼라
            colorBtn=(ImageButton)findViewById(R.id.colorBtn);
            colorBtn.setOnClickListener(this);

            //4.저장 메뉴
            saveBtn =(ImageButton)findViewById(R.id.saveBtn);
            saveBtn.setOnClickListener(this);

            //5. 메뉴
            menuBtn=(ImageButton)findViewById(R.id.menuBtn);
            menuBtn.setOnClickListener(this);

            //열기 버튼
            loadBtn=(ImageButton)findViewById(R.id.loadBtn);
            loadBtn.setOnClickListener(this);

            //.왼쪽 메뉴 지우기
            menuBackground =(Button)findViewById(R.id.menu_background);
            menuBackground.setOnClickListener(this);

            //.왼쪽 메뉴 설정
            menuSetting =(Button)findViewById(R.id.menu_setting);
            menuSetting.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){

                //선 굵기 버튼
                case R.id.brushBtn:
                    BrushDialog.listener=new OnPenSelectedListener() {
                        @Override
                        public void onPenSelected(int pen) {
                            width=pen;

                            if(color!=Color.WHITE) {
                                drawingView.setColor(color);
                                drawingView.setStroke(width);
                            }
                            else {
                                drawingView.setColor(Color.BLACK);
                                drawingView.setStroke(width);
                            }
                        }
                    };

                    startActivity(new Intent(getApplicationContext(),BrushDialog.class));
                    break;

                //지우개 버튼
                case R.id.eraseBtn:
                    if(drawingView.getColor()!=Color.WHITE){
                        color=drawingView.getColor();
                        width=(int)drawingView.getStroke();
                    }
                    startActivity(new Intent(getApplicationContext(),EraseDialog.class));
                    break;

                //색상 버튼
                case R.id.colorBtn:
                    ColorDialog.listener=new OnColorSelectedListener() {
                        @Override
                        public void onColorChanged(int pen) {
                            color=pen;
                            drawingView.setColor(color);
                        }
                    };

                    startActivity(new Intent(getApplicationContext(),ColorDialog.class));
                    break;

                //저장 버튼
                case R.id.saveBtn:
                    savePicture();
                    break;

                //열기 버튼
                case R.id.loadBtn:
                    loadPicture();
                    break;

                //메뉴 버튼 클릭시
                case R.id.menuBtn:
                    menuLeftSlideAnimatonToggle();
                    break;

                case R.id.menu_background:
                    ColorDialog.listener=new OnColorSelectedListener() {
                        @Override
                        public void onColorChanged(int i) {
                            imageView.setImageResource(android.R.color.transparent);
                            imageView.setBackgroundColor(i);
                        }
                    };
                    startActivity(new Intent(getApplicationContext(), ColorDialog.class));
                    menuLeftSlideAnimatonToggle();
                    break;
                case R.id.menu_setting:
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                    menuLeftSlideAnimatonToggle();//화면을 닫아준다.(닫지 않으면 현화면 캡쳐시 전부 캡쳐가 되지 않음)
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * sliding Menu 관련한 변수들을 초기화 해준다.
     * */
    private void initSlideMenu() {
        //left menu 가로를 초기화
        metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        leftMenuWidth=(int)((metrics.widthPixels)*0.75);

        //main view 초기화
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);

        //left menu 초기화
        menuLayout=(LinearLayout)findViewById(R.id.menuLayout);
        leftMenuLayoutParams=(FrameLayout.LayoutParams)menuLayout.getLayoutParams();
        leftMenuLayoutParams.width=leftMenuWidth;
        menuLayout.setLayoutParams(leftMenuLayoutParams);
    }

    /**
     * left menu toggle
     * */
    private void menuLeftSlideAnimatonToggle(){
        if(!isLeftExpanded){

            isLeftExpanded=true;

            //확장시
            new OpenAnimation(mainLayout,leftMenuWidth,
                    Animation.RELATIVE_TO_SELF,0.0f,
                    Animation.RELATIVE_TO_SELF, 0.75f, 0, 0.0f,0,0.0f);

            //main view를 사용 못하게
            FrameLayout viewGroup=(FrameLayout)findViewById(R.id.InflateLayout).getParent();
            enableDisableViewGroup(viewGroup, false);

            //empty view를 사용하게
            ((LinearLayout)findViewById(R.id.emptyLayout)).setVisibility(View.VISIBLE);

            findViewById(R.id.emptyLayout).setEnabled(true);
            findViewById(R.id.emptyLayout).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    menuLeftSlideAnimatonToggle();
                    return true;
                }
            });

        }else{

            isLeftExpanded=false;

            //닫기
            new CloseAnimation(mainLayout,leftMenuWidth,
                    TranslateAnimation.RELATIVE_TO_SELF,0.75f,
                    TranslateAnimation.RELATIVE_TO_SELF, 0.0f, 0, 0.0f, 0, 0.0f);

            //mainView를 가능하게
            FrameLayout viewGroup=(FrameLayout)findViewById(R.id.InflateLayout).getParent();
            enableDisableViewGroup(viewGroup,true);

            //emptyView 사용 불가능하게
            ((LinearLayout)findViewById(R.id.emptyLayout)).setVisibility(View.GONE);
            findViewById(R.id.emptyLayout).setEnabled(false);
        }
    }

    /**
     * 뷰의 동작을 제어
     * */
    private void enableDisableViewGroup(ViewGroup viewGroup, boolean enabled) {
        int childCount=viewGroup.getChildCount();
        for(int i=0;i<childCount;i++){
            View view=viewGroup.getChildAt(i);
            if(view.getId()!=R.id.menuBtn){
                view.setEnabled(enabled);

                if(view instanceof ViewGroup)
                    enableDisableViewGroup((ViewGroup)view, enabled);
            }
        }
    }

    private void loadPicture(){
        Intent i=new Intent();
        //Gallery 불러오기
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        //잘라내기
        i.putExtra("crop","true");
        i.putExtra("aspectX",0);
        i.putExtra("aspectY",0);
        i.putExtra("outputX",200);
        i.putExtra("outputY", 150);

        try{
            i.putExtra("return-data",true);
            startActivityForResult(Intent.createChooser(i,"이미지 불러오기"),GET_PIC_URI);
        }catch (Exception e){e.printStackTrace();}

    }

    /**
     * 뷰의 이미지를 저장
     * 캐쉬를 허용한 뒤 뷰를 스크린 샷을 가져와 jpeg 파일로 저장
     * */
    private void savePicture(){
        mainLayout.setDrawingCacheEnabled(true);//캐시를 허용
        Bitmap screenshot=Bitmap.createBitmap(mainLayout.getDrawingCache());//화면 캡쳐
        mainLayout.setDrawingCacheEnabled(false);//캐시 비허용

        //sd 카드에 접근하여 저장
        String dirPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)+"/Dessin";//파일 경로
        File dir= new File(dirPath);

        //폴더가 없으면 새로 만듦
        if(!dir.exists())
            dir.mkdirs();

        FileOutputStream fileout=null;
        try{
            //파일을 저장
            fileout=new FileOutputStream(new File(dir, "img_"+System.currentTimeMillis()+".jpeg"));//시간을 기준으로 파일명을 지음.
            screenshot.compress(Bitmap.CompressFormat.JPEG,100,fileout);//jpeg 이미지로 압축
            fileout.close();

            Toast.makeText(this,"저장 되었습니다.",Toast.LENGTH_SHORT).show();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"저장 하지 못하였습니다.",Toast.LENGTH_SHORT).show();
        }
        finally {
            if(fileout!=null){
                try{
                    fileout.close();
                }catch (Exception e){ e.printStackTrace();}
            }
        }
    }

    /**
     * 함수 오버라이딩
     * 이미지 불러왔을때 처리
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GET_PIC_URI){
            try {
                Bundle extra = data.getExtras();
                if (extra != null) {
                    Bitmap photo = extra.getParcelable("data");
                    imageView.setImageBitmap(photo);
                    Toast.makeText(this,"이미지를 불러왔습니다.",Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e){ e.printStackTrace();}
        }
    }
}
