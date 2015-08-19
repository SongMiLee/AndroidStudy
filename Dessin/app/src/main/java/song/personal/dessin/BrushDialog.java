package song.personal.dessin;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class BrushDialog extends Activity {
    GridView grid;
    Button okBtn;
    PenAdapter penAdapter;

    public static OnPenSelectedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brush_dialog);

        this.setTitle("선 굵기 선택");

        grid=(GridView)findViewById(R.id.colorGrid);
        okBtn=(Button)findViewById(R.id.closeBtn);

        //그리드 뷰를 세팅
        grid.setColumnWidth(14);
        grid.setBackgroundColor(Color.DKGRAY);
        grid.setVerticalSpacing(4);
        grid.setHorizontalSpacing(4);

        //어댑터
        penAdapter=new PenAdapter(this);
        grid.setAdapter(penAdapter);
        grid.setNumColumns(penAdapter.getNumColumns());

        //버튼을 눌렀을 때 행동
        okBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //액티비티를 종료한다.
                finish();
            }
        });


    }
}
class PenAdapter extends BaseAdapter{
    Context context;

    //펜의 굵기 모음
    public static final int [] pens = new int[] {
            1,2,3,4,5,
            6,7,8,9,10,
            11,13,15,17,20
    };

    int rowCnt;
    int colCnt;

    public PenAdapter(Context context){
        super();
        this.context=context;

        rowCnt=5;
        colCnt=3;
    }

    public int getNumColumns() {
        return colCnt;
    }

    @Override
    public int getCount() {
        return rowCnt*colCnt;
    }

    @Override
    public Object getItem(int position) {
        return pens[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //위치 계산
        int rowIndex=position/rowCnt;
        int colIndex=position%rowCnt;

        GridView.LayoutParams params=new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.MATCH_PARENT
        );

        //펜 이미지 생성
        int areaWidth=10;
        int areaHeight=20;

        Bitmap penBitmap=Bitmap.createBitmap(areaWidth,areaHeight, Bitmap.Config.ARGB_8888);
        Canvas penCanvas=new Canvas();
        penCanvas.setBitmap(penBitmap);

        //붓의 바탕
        Paint paint=new Paint();
        paint.setColor(Color.WHITE);
        penCanvas.drawRect(0, 0, areaWidth, areaHeight, paint);

        //붓을 그림
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth((float) pens[position]);
        penCanvas.drawLine(0, areaHeight / 2, areaWidth - 1, areaHeight / 2, paint);
        BitmapDrawable penDrawable=new BitmapDrawable(context.getResources(), penBitmap);

        //색이 있는 버튼 만듦.
        Button Item=new Button(context);
        Item.setText(" ");
        Item.setLayoutParams(params);
        Item.setPadding(4, 4, 4, 4);
        Item.setBackgroundDrawable(penDrawable);
        Item.setHeight(64);
        Item.setTag(pens[position]);

        //Item 버튼 클릭시 이벤트
        Item.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(BrushDialog.listener!=null){

                    BrushDialog.listener.onPenSelected(((Integer)v.getTag()).intValue());

                }
                ((BrushDialog)context).finish();
            }
        });

        return Item;
    }
}