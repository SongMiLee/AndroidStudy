package song.personal.dessin;

import com.larswerkman.holocolorpicker.ColorPicker;

/**
 * Created by song on 2015-08-18.
 */
public interface OnColorSelectedListener extends ColorPicker.OnColorChangedListener{

    @Override
    public void onColorChanged(int i) ;
}
