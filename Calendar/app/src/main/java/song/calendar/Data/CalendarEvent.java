package song.calendar.Data;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by home on 2016-05-20.
 */
public class CalendarEvent {
    String start, end, place, summary, description;
    int startMonth,startDate,endMonth, endDate;

    public CalendarEvent(String name, String loc, String sd, String ed, String dec)  {
        summary = name;
        place = loc;
        start = sd;
        end = ed;
        description = dec;

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        GregorianCalendar tmpStart= new GregorianCalendar();
        GregorianCalendar tmpEnd = new GregorianCalendar();

        try {
            tmpStart.setTime(input.parse(start));
            tmpEnd.setTime(input.parse(end));

            startMonth = tmpStart.get(Calendar.MONTH)+1;
            endMonth = tmpEnd.get(Calendar.MONTH)+1;
        }catch (Exception e){
            try {
                tmpStart.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(start));
                tmpEnd.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(end));

            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            startMonth = tmpStart.get(Calendar.MONTH)+1;
            endMonth = tmpEnd.get(Calendar.MONTH)+1;
        }finally {
            startDate = tmpStart.get(Calendar.DATE);
            endDate = tmpEnd.get(Calendar.DATE);

            Log.d("CalenderEvent", start+" "+startMonth+"/"+startDate +" "+ endMonth+"/"+endDate);
        }
    }

    public String getSummary() {        return summary;    }
    public String getDescription() {        return description;    }
    public String getStart() {        return start;    }
    public String getEnd() {        return end;    }
    public String getPlace() {       return place;    }

    public int getStartMonth(){ return startMonth; }
    public int getStartDate() { return startDate;}

    public int getEndMonth() {      return endMonth;   }
    public int getEndDate() {       return endDate;    }
}
