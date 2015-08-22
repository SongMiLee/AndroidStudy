package song.personal.dessin;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class Dessin extends AppCompatActivity {
    //액션바 메뉴
    ImageButton brushBtn;
    ImageButton eraseBtn;
    ImageButton toolBtn;
    ImageButton colorBtn;
    ImageButton menuBtn;

    //선의 기본 설정
    int width=10;
    int color=Color.BLACK;

    //그림 그릴 뷰
    LinearLayout inflateView;
    DrawingView drawingView;

    //슬라이드 메뉴
    DisplayMetrics metrics;
    LinearLayout mainLayout;
    LinearLayout menuLayout;
    FrameLayout.LayoutParams leftMenuLayoutParams;
    int leftMenuWidth;
    static boolean isLeftExpanded;
    Button menuErase;
    Button menuSave;

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

            //4.기타
            toolBtn=(ImageButton)findViewById(R.id.toolBtn);
            toolBtn.setOnClickListener(this);

            //5. 메뉴
            menuBtn=(ImageButton)findViewById(R.id.menuBtn);
            menuBtn.setOnClickListener(this);

            //6.왼쪽 메뉴 지우기
            menuErase=(Button)findViewById(R.id.menu_erase);
            menuErase.setOnClickListener(this);

            //7.왼쪽 메뉴 저장
            menuSave=(Button)findViewById(R.id.menu_save);
            menuSave.setOnClickListener(this);
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
                    EraseDialog.listener=new OnEraserSelectedListener() {
                        @Override
                        public void onEraserSelected(int erase) {
                            if(drawingView.getColor()!=Color.WHITE){ color=drawingView.getColor(); width=(int)drawingView.getStroke();}

                            width=erase;
                            drawingView.setColor(Color.WHITE);
                            drawingView.setStroke(width);

                        }
                    };
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

                //설정 버튼
                case R.id.toolBtn:
                    startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                    break;

                //메뉴 버튼 클릭시
                case R.id.menuBtn:
                    menuLeftSlideAnimatonToggle();
                    break;

                //왼쪽 메뉴 지우개
                case R.id.menu_erase:
                    drawingView.setClear();
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

}
