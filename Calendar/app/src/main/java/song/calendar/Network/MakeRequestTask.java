package song.calendar.Network;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import song.calendar.Activity.MainActivity;
import song.calendar.Data.CalendarEvent;
import song.calendar.Global.StaticVariable;
import song.calendar.R;

/**
 * Created by home on 2016-05-19.
 */
public class MakeRequestTask extends AsyncTask<String, Void, List<CalendarEvent>> {
    Calendar service = null;
    Exception error = null;

    public MakeRequestTask(GoogleAccountCredential credential){
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        com.google.api.client.json.JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        service = new Calendar.Builder(transport, jsonFactory, credential)
                .setApplicationName(String.valueOf(R.string.app_name))
                .build();
    }

    @Override
    protected List<CalendarEvent> doInBackground(String... params) {
        try{
            Log.d("make request task", "execute");
            return getDataFromApi(params[0]);
        }catch (UserRecoverableAuthIOException e){//다시 계정을 선택한다.
            startActivityForResult(e.getIntent(), StaticVariable.REQUEST_PICK_ACCOUNT);
            return null;
        }catch (Exception e){
            e.printStackTrace();
            error = e;
            e.printStackTrace();
            cancel(true);
            return null;
        }
    }

    private void startActivityForResult(Intent intent, int requestPickAccount) {
    }

    private List<CalendarEvent> getDataFromApi(String date) throws IOException {
        String tmp[] = date.split("-");
        GregorianCalendar sd = new GregorianCalendar(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])-1, 1);
        GregorianCalendar ed = new GregorianCalendar(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1])-1, sd.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));

        String startDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").format(sd.getTime());
        String endDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZZZ").format(ed.getTime());

        Events events = service.events().list("primary")
                .setTimeMin(new DateTime(startDate))
                .setTimeMax(new DateTime(endDate))
                .execute();// Google로부터 내 캘린더 이벤트를 받아온다.

        List<Event> items = events.getItems();//캘린더 이벤트 집합
        List<CalendarEvent> list = new ArrayList<CalendarEvent>();

        for(Event event : items){
            DateTime start = event.getStart().getDateTime();
            DateTime end = event.getEnd().getDateTime();
            String place = event.getLocation();
            String summary = event.getSummary();
            String des = event.getDescription();

            if(start == null){   start = event.getStart().getDate();  }
            if(end == null){    end = event.getEnd().getDate(); }
            if(place == null) { place = "no place"; }
            if(summary == null) { summary = "no subject"; }
            if(des == null) { des = "no descripton"; }

            list.add(new CalendarEvent(summary, place, start.toString(), end.toString(), des));
        }
        return list;
    }

    @Override
    protected void onPostExecute(List<CalendarEvent> strings) {
        MainActivity.showEvent(strings);
        super.onPostExecute(strings);
    }
}
