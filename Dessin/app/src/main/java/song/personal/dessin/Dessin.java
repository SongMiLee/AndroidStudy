package song.personal.dessin;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class Dessin extends AppCompatActivity {
    //액션바 메뉴
    ImageButton brushBtn;
    ImageButton eraseBtn;
    ImageButton toolBtn;
    ImageButton colorBtn;

    //선의 기본 설정
    int width=10;
    int color=Color.BLACK;

    //그림 그릴 뷰
    DrawingView drawingView;

    static int erasecnt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //스플래시 화면 실행
        startActivity(new Intent(this, SplashActivity.class));

        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_canvas);

        //그림 그릴 뷰를 가져온다.
        drawingView=new DrawingView(this);
        setContentView(drawingView);

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

    //1. 브러시
    private View.OnClickListener brushListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           // drawingView.callBrushDialog();

            Log.d("Call Brush Dialog", "now call Brush Dialog");
            BrushDialog.listener=new OnPenSelectedListener() {
                @Override
                public void onPenSelected(int pen) {
                    int color=drawingView.getColor();
                    width=pen;
                    Log.i("pen Size : ", width+"");

                    if(color==Color.WHITE) {
                        drawingView.setColor(Color.BLACK);
                        drawingView.setStroke(width);
                    }
                    else
                        drawingView.setStroke(width);
                }
            };

            Intent intent=new Intent(getApplicationContext(),BrushDialog.class);
            startActivity(intent);
        }
    };

    //2. 지우개
    private View.OnClickListener eraseListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            erasecnt+=1;
            if(erasecnt%2==1) {
                //이전의 상태 저장
                color=drawingView.getColor();
                width=(int)drawingView.getStroke();

                //지우개 적용
                drawingView.setErase();

                //색, 굵기 버튼 비활성화
                brushBtn.setEnabled(false);
                colorBtn.setEnabled(false);
            }
            else
            {
                drawingView.setColor(color);
                drawingView.setStroke(width);

                //색, 굵기 버튼 활성화
                brushBtn.setEnabled(true);
                colorBtn.setEnabled(true);
            }
        }
    };

    //3. 색
    private View.OnClickListener colorListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ColorDialog.listener=new OnColorSelectedListener() {
                @Override
                public void onColorChanged(int pen) {
                    color=pen;
                    drawingView.setColor(color);
                }
            };

            Intent i=new Intent(getApplicationContext(),ColorDialog.class);
            startActivity(i);
        }
    };

    //4.도구
    private View.OnClickListener toolListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    /**
     * 초기화 작업 처리
     * */
    class InitializationRunnable implements Runnable{

        @Override
        public void run() {
            //초기화 처리 작업
            //커스텀 뷰가 보일 수 있도록 옵션을 줌
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            //커스텀 레이아웃을 보인다.
            getSupportActionBar().setCustomView(R.layout.activity_canvas);

            //1. 브러시 함수
            brushBtn=(ImageButton)findViewById(R.id.brushBtn);
            brushBtn.setOnClickListener(brushListener);

            //2. 지우개 함수
            eraseBtn=(ImageButton)findViewById(R.id.eraseBtn);
            eraseBtn.setOnClickListener(eraseListener);

            //3.칼라
            colorBtn=(ImageButton)findViewById(R.id.colorBtn);
            colorBtn.setOnClickListener(colorListener);

            //4.기타
            toolBtn=(ImageButton)findViewById(R.id.toolBtn);
            toolBtn.setOnClickListener(toolListener);
        }
    }
}
