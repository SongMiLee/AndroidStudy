package song.personal.dessin.View;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by song on 2015-08-05.
 */
public class DrawingView extends View {
    Paint mpaint;
    ArrayList<Point> pointList;

    int mWidth=10;
    int mColor=Color.BLACK;

    Paint epaint;
    boolean mode=false;

    //생성자
   public DrawingView(Context context){
       super(context);

       //점의 경로 초기화
       pointList=new ArrayList<Point>();

       //페인트 초기화
       mpaint=new Paint();
       mpaint.setColor(Color.BLACK);
       mpaint.setAntiAlias(true);
       mpaint.setStrokeWidth(10);

       epaint=new Paint();
       epaint.setColor(Color.WHITE);
       epaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
       epaint.setAntiAlias(true);
       epaint.setStrokeWidth(20);

   }

    public void setErase(int width){
        mWidth=width;
    }

    //선의 색 반환하기
    public int getColor()    {        return mpaint.getColor();    }

    //선의 굵기 반환하기
    public float getStroke(){ return mpaint.getStrokeWidth(); }

    //선의 색 설정
    public void setColor(int color)
    {
        mpaint.setColor(color);
        mColor=color;
    }

    //선의 굵기 설정
    public void setStroke(int width)
    {
        mpaint.setStrokeWidth(width);
        mWidth=width;
    }

    //터치 이벤트 함수
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //터치 액션을 받아온다.
        int action=event.getAction();

        //현재 터치한 화면의 x,y 좌표 값을 받아온다.
        int curX=(int)event.getX();
        int curY=(int)event.getY();

        //현재 설정된 선의 굵기와 색이 맞지 않으면 맞춰줌.
        if(mpaint.getStrokeWidth()!=mWidth)
            mpaint.setStrokeWidth(mWidth);

        if(mpaint.getColor()!=mColor)
            mpaint.setColor(mColor);

        switch (action)
        {
            //손으로 움직이는 중
            case MotionEvent.ACTION_MOVE:
                pointList.add(new Point(curX,curY, true,(int)mpaint.getStrokeWidth(), mpaint.getColor()));
                break;
            //손으로 터치했을 때
            case MotionEvent.ACTION_DOWN:
                pointList.add(new Point(curX,curY, false,(int)mpaint.getStrokeWidth(), mpaint.getColor()));
                break;
        }

        invalidate(); //화면 갱신 onDraw 호출
        return true;
    }

    //화면을 그려주는 함수
    @Override
    protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);
       canvas.drawColor(Color.TRANSPARENT);

        for (int i = 0; i < pointList.size(); i++) {
            //펜의 값과 색을 세팅
            mpaint.setStrokeWidth(pointList.get(i).strokeWidth);
            mpaint.setColor(pointList.get(i).strokeColor);

            //그리는 중일 때
            if (pointList.get(i).isDrawing)
                canvas.drawLine(pointList.get(i).x, pointList.get(i).y, pointList.get(i - 1).x, pointList.get(i - 1).y, mpaint);
                //찍었을 때
            else
                canvas.drawPoint(pointList.get(i).x, pointList.get(i).y, mpaint);
        }
    }
}
