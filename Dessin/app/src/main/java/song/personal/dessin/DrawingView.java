package song.personal.dessin;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by song on 2015-08-05.
 */
public class DrawingView extends View {
    Paint mpaint;
    ArrayList<Point> pointList;

    //생성자
   public DrawingView(Context context){
       super(context);

       //점의 경로 초기화
       pointList=new ArrayList<Point>();

       //페인트 초기화
       mpaint=new Paint();
       mpaint.setColor(Color.BLACK);
       mpaint.setStrokeWidth(10);
   }

    //선의 색 변경
    public void setStroke(int color, int width)
    {
        mpaint.setColor(color);
        mpaint.setStrokeWidth(width);
    }

    //터치 이벤트 함수
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action=event.getAction();

        int curX=(int)event.getX();
        int curY=(int)event.getY();

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
        canvas.drawColor(Color.WHITE);
        for(int i=0;i<pointList.size();i++)
        {
            //펜의 값과 색을 세팅
            mpaint.setStrokeWidth(pointList.get(i).strokeWidth);
            mpaint.setColor(pointList.get(i).strokeColor);

            //그리는 중일 때
            if(pointList.get(i).isDrawing)
                canvas.drawLine(pointList.get(i).x, pointList.get(i).y, pointList.get(i - 1).x, pointList.get(i - 1).y, mpaint);
            //찍었을 때
            else
                canvas.drawPoint(pointList.get(i).x, pointList.get(i).y, mpaint);
        }
    }
}
