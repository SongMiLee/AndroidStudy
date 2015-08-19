package song.personal.dessin;

import android.view.Gravity;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

/**
 * Created by song on 2015-08-19.
 */
public class OpenAnimation extends TranslateAnimation implements Animation.AnimationListener {
    private LinearLayout mainLayout;
    int panelWidth;

    /**
     * 생성자*/
    public OpenAnimation(LinearLayout layout, int width, int fromXType,
                         float fromXValue, int toXType, float toXValue,
                         int fromYType,float fromYValue, int toYType, float toYValue)     {

        super(fromXType, fromXValue, toXType, toXValue, fromYType, fromYValue,toYType, toYValue);

        // 받아온 속성들을 초기화
        mainLayout = layout;
        panelWidth = width;
        setDuration(250); //메뉴가 열릴 때의 속도
        setFillAfter(false);
        setInterpolator(new AccelerateDecelerateInterpolator());
        setAnimationListener(this);
        mainLayout.startAnimation(this);
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        LinearLayout.LayoutParams params=(LinearLayout.LayoutParams)mainLayout.getLayoutParams();
        params.leftMargin=panelWidth;
        params.gravity= Gravity.LEFT;
        mainLayout.clearAnimation();
        mainLayout.setLayoutParams(params);
        mainLayout.requestLayout();
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
