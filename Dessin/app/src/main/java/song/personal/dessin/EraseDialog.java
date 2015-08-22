package song.personal.dessin;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

public class EraseDialog extends Activity {
    GridView grid;
    Button closeErase;
    EraseAdapter eraseAdapter;

    public static OnEraserSelectedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase_dialog);

        this.setTitle("지우개 선택");

        grid=(GridView)findViewById(R.id.eraseGrid);
        closeErase=(Button)findViewById(R.id.closeErase);

        //그리드 뷰 세팅
        grid.setColumnWidth(14);
        grid.setBackgroundColor(Color.DKGRAY);
        grid.setVerticalSpacing(4);
        grid.setHorizontalSpacing(4);

        //어댑터
        eraseAdapter=new EraseAdapter(this);
        grid.setAdapter(eraseAdapter);
        grid.setNumColumns(eraseAdapter.getNumColumns());

        closeErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
class EraseAdapter extends BaseAdapter{
    Context context;
    int rowCnt;
    int colCnt;

    //지우개 크기 모음
    public static final int[] erasers=new int[]{
            1,2,3,4,5,
            6,7,8,9,10,
            11,13,15,17,20
    };

    public EraseAdapter(Context context){
        super();
        this.context=context;

        rowCnt=5;
        colCnt=3;
    }

    public int getNumColumns(){ return colCnt;}
    @Override
    public int getCount() {
        return rowCnt*colCnt;
    }

    @Override
    public Object getItem(int position) {
        return erasers[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //위치 계산
        int rowIndex=position/rowCnt;
        int colIndex=position/rowCnt;

        GridView.LayoutParams params=new GridView.LayoutParams(
                GridView.LayoutParams.MATCH_PARENT,
                GridView.LayoutParams.MATCH_PARENT
        );

        //지우개 이미지 생성
        int areaWidth=10;
        int areaHeight=20;

        Bitmap eraseBitmap=Bitmap.createBitmap(areaWidth,areaHeight, Bitmap.Config.ARGB_8888);
        Canvas eraseCanvas=new Canvas();
        eraseCanvas.setBitmap(eraseBitmap);

        //지우개 바탕
        Paint paint=new Paint();
        paint.setColor(Color.BLACK);
        eraseCanvas.drawRect(0, 0, areaWidth, areaHeight, paint);

        //지우개 그림
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth((float)erasers[position]);
        eraseCanvas.drawLine(0, areaHeight / 2, areaWidth - 1, areaHeight / 2, paint);
        BitmapDrawable eraseDrawable=new BitmapDrawable(context.getResources(), eraseBitmap);

        //색이 있는 버튼을 만듦
        Button Item=new Button(context);
        Item.setText(" ");
        Item.setLayoutParams(params);
        Item.setPadding(4, 4, 4, 4);
        Item.setBackgroundDrawable(eraseDrawable);
        Item.setHeight(64);
        Item.setTag(erasers[position]);

        //아이템 클릭시 이벤트
        Item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(EraseDialog.listener!=null){
                    EraseDialog.listener.onEraserSelected(((Integer)v.getTag()).intValue());
                }
                ((EraseDialog)context).finish();
            }
        });
        return Item;
    }
}