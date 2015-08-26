package song.personal.dessin;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class EraseDialog extends Activity implements SeekBar.OnSeekBarChangeListener {
    SeekBar eraseSeek;
    Button closeBtn;
    TextView eraseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase_dialog);

        this.setTitle("지우개 크기");

        eraseText=(TextView)findViewById(R.id.textErase);
        eraseSeek=(SeekBar)findViewById(R.id.seekBar);
        closeBtn=(Button)findViewById(R.id.closeErase);

        eraseSeek.setOnSeekBarChangeListener(this);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        eraseText.setText("지우개 크기 : "+progress);
        Dessin.drawingView.setStroke(progress);
        Dessin.drawingView.setColor(Color.WHITE);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}