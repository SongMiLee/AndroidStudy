package song.calendar.Global;

import com.google.api.services.calendar.CalendarScopes;

/**
 * Created by home on 2016-05-19.
 */
public class StaticVariable {

    public static final String SCOPE="https://www.googleapis.com/auth/calendar";
    public static final String[] CALENDAR_SCOPES={CalendarScopes.CALENDAR};
    public static final int REQUEST_PICK_ACCOUNT = 1000;
    public static final int REQUEST_AUTHORIZATION = 2000;
}
