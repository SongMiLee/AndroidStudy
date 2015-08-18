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

    int width=10;
    int color=Color.BLACK;

    //그림 그릴 뷰
    DrawingView drawingView;
    //색상 다이얼로그
    ColorDialog colorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_canvas);

        //그림 그릴 뷰를 가져온다.
        drawingView=new DrawingView(this);
        setContentView(drawingView);


        //스플래시 화면 실행
        startActivity(new Intent(this, SplashActivity.class));

        //커스텀 뷰가 보일 수 있도록 옵션을 줌
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //커스텀 레이아웃을 보인다.
        getSupportActionBar().setCustomView(R.layout.activity_canvas);


        //1. 브러시 함수
        ImageButton imgBtn=(ImageButton)findViewById(R.id.brushBtn);
        imgBtn.setOnClickListener(brushListener);

        //2. 지우개 함수
        ImageButton eraseBtn=(ImageButton)findViewById(R.id.eraseBtn);
        eraseBtn.setOnClickListener(eraseListener);

        //3.칼라
        ImageButton colorBtn=(ImageButton)findViewById(R.id.colorBtn);
        colorBtn.setOnClickListener(colorListener);

        //4.기타
        ImageButton toolBtn=(ImageButton)findViewById(R.id.toolBtn);
        toolBtn.setOnClickListener(toolListener);

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
                    int color=drawingView.returnColor();
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
            drawingView.setErase();
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
}
