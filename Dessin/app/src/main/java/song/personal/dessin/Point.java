package song.personal.dessin;

import java.io.Serializable;

/**
 * Created by song on 2015-08-06.
 */
public class Point implements Serializable {
    //x,y 좌표 값
    int x;
    int y;
    //그림을 그리는 중인가
    boolean isDrawing;

    //페인트의 색과 굵기
    int strokeWidth;
    int strokeColor;

    //Point class 생성자
    public Point(int ux, int uy, boolean ud, int width, int color)
    {
        x=ux;
        y=uy;
        isDrawing=ud;
        strokeWidth=width;
        strokeColor=color;
    }
}
