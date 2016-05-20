package song.calendar.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import song.calendar.Data.CalendarEvent;
import song.calendar.R;

/**
 * Created by home on 2016-05-19.
 */
public class DateAdapter extends BaseAdapter {
    private Context context;
    private List<String> arrData;
    private LayoutInflater inflater;
    private int curYear, curMonth;

    private List<CalendarEvent> calendarEvents;

    public DateAdapter(Context c, List<String> arr, int year, int month) {
        this.context = c;
        arrData = arr;
        curYear = year;
        curMonth = month;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setCalendarEvents(List<CalendarEvent> events){ calendarEvents = events; }

    public int getCount() {
        return arrData.size();
    }

    public Object getItem(int position) {
        return arrData.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_calendar, parent, false);
        }

        TextView viewText = (TextView)convertView.findViewById(R.id.item_calendar_date);
        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.item_calendar_layout);
        viewText.setText(arrData.get(position));

        //일정이 있는 부분 색칠해주기
        if(calendarEvents !=null && !(arrData.get(position).equals(" ")))
            setBackgroundColor(linearLayout, Integer.parseInt(arrData.get(position)));

        if(position%7==0)
            viewText.setTextColor(context.getResources().getColor(R.color.colorAccent));
        if(position%7==6)
            viewText.setTextColor(context.getResources().getColor(R.color.colorPrimary));

        return convertView;

    }

    //일정 있는 부분 색칠해주는 함수
    protected void setBackgroundColor(LinearLayout text, int date){
        for(int i=0; i<calendarEvents.size(); i++){
            CalendarEvent temp = calendarEvents.get(i);

            //이벤트의 시작 월과 끝 월이 같을 때
            if(temp.getStartMonth() == temp.getEndMonth())
            {
                if(date>= temp.getStartDate() && date<=temp.getEndDate())
                    text.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }
            //이벤트의 시작 월과 끝 월이 다를 때
            //현재 월이 이벤트 끝 월보다 작을 때
            else if(curMonth < temp.getEndMonth())
            {
                if(date >= temp.getStartDate())
                    text.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }
            //이벤트 시작 월 < 현재 월
            //현재 월과 이벤트 끝 월이 같을 때
            else if(curMonth == temp.getEndMonth()){
                if(date<=temp.getEndDate())
                    text.setBackgroundColor(context.getResources().getColor(R.color.yellow));
            }
        }
    }


}
