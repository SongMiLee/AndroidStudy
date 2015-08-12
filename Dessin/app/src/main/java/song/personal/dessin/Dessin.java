package song.personal.dessin;

import android.app.ActionBar;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Dessin extends AppCompatActivity {

    int width=10;

    //그림 그릴 뷰
    DrawingView drawingView;

    BrushDialog Brush;

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
                    width=pen;
                    Log.i("pen Size : ", width+"");
                    drawingView.setStroke(Color.BLUE, width);
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
            drawingView.setStroke(Color.WHITE, 15);
        }
    };

    //3. 색
    private View.OnClickListener colorListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    //4.도구
    private View.OnClickListener toolListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
