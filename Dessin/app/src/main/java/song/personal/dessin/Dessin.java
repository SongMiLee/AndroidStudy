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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Dessin extends AppCompatActivity {

    int width=10;

    //그림 그릴 뷰
    DrawingView drawingView;

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
        getSupportActionBar().setCustomView(R.layout.menu_drawing);


        //1. 브러시 함수
        ImageButton imgBtn=(ImageButton)findViewById(R.id.brushBtn);
        imgBtn.setOnClickListener(brushListener);

        //2. 지우개 함수
        ImageButton eraseBtn=(ImageButton)findViewById(R.id.eraseBtn);
        eraseBtn.setOnClickListener(eraseListener);

    }

    //1. 브러시
    private View.OnClickListener brushListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            drawingView.setStroke(Color.BLACK, 15);
        }
    };

    //2. 지우개
    private View.OnClickListener eraseListener=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            drawingView.setStroke(Color.WHITE, 15);
        }
    };
}
