package song.personal.dessin.Dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import song.personal.dessin.R;

public class ColorDialog extends Activity {
    ColorPicker picker;
    SVBar svBar;
    OpacityBar opacityBar;
    SaturationBar saturationBar;
    ValueBar valueBar;
    Button colorClose;

    public static OnColorSelectedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_dialog);

        this.setTitle("색상 선택");

        //레이아웃과 연결한다.
        picker = (ColorPicker) findViewById(R.id.picker);
        svBar = (SVBar) findViewById(R.id.svbar);
        opacityBar = (OpacityBar) findViewById(R.id.opacitybar);
        saturationBar = (SaturationBar) findViewById(R.id.saturationbar);
        valueBar = (ValueBar) findViewById(R.id.valuebar);
        colorClose=(Button)findViewById(R.id.colorClose);

        picker.addSVBar(svBar);
        picker.addOpacityBar(opacityBar);
        picker.addSaturationBar(saturationBar);
        picker.addValueBar(valueBar);

        //To get the color
        picker.getColor();

        //To set the old selected color u can do it like this
        picker.setOldCenterColor(picker.getColor());
        // adds listener to the colorpicker which is implemented
        //in the activity
        picker.setOnColorChangedListener(listener);

        //to turn of showing the old color
        picker.setShowOldCenterColor(false);


        colorClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //액티비티를 종료한다.
                finish();
            }
        });
    }
}
