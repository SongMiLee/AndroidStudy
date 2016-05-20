package song.calendar.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.util.ExponentialBackOff;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import song.calendar.Data.CalendarEvent;
import song.calendar.Global.StaticVariable;
import song.calendar.Network.MakeRequestTask;
import song.calendar.Oauth.AuthPreference;
import song.calendar.R;
import song.calendar.Adapter.DateAdapter;

public class MainActivity extends AppCompatActivity {

    GridView mGridView;
    TextView yearMonth;
    static DateAdapter adapter;
    List<String> arrData;
    Calendar mCal;

    private int month, year;
    private ImageButton pre, next;
    private AuthPreference authPreference;
    private GoogleAccountCredential credential;
    private AccountManager accountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

    }

    public void init() {
        // Calendar 객체 생성
        mCal = Calendar.getInstance();
        year = mCal.get(Calendar.YEAR);
        month = mCal.get(Calendar.MONTH) + 1;

        yearMonth = (TextView) findViewById(R.id.text_year_month);
        mGridView = (GridView) findViewById(R.id.calendar_grid_view);
        pre = (ImageButton) findViewById(R.id.pre);
        next = (ImageButton) findViewById(R.id.next);

        pre.setOnClickListener(preMonth);
        next.setOnClickListener(nexMonth);
        mGridView.setOnItemClickListener(itemClick);

        accountManager = AccountManager.get(this);
        authPreference = new AuthPreference(this);
        credential = GoogleAccountCredential.usingOAuth2(getApplicationContext(), Arrays.asList(StaticVariable.CALENDAR_SCOPES)) //credential 초기화!
                .setBackOff(new ExponentialBackOff());
        if (authPreference.getUser() != null && authPreference.getToken() != null)
            doAuthenticatedStuff();
        else
            chooseAccount();

        // 달력 세팅
        setCalendarDate(month);
    }

    private void doAuthenticatedStuff() {
        Log.d("Token", authPreference.getToken());
        //이미 기존의 값이 있다면 기존 값으로 credentail 초기화
        credential.setSelectedAccountName(authPreference.getUser());

         new MakeRequestTask(credential).execute(year+"-"+month);
    }

    public static void showEvent(List<CalendarEvent> list){   adapter.setCalendarEvents(list);  adapter.notifyDataSetChanged();    }

    /**
     * 어플리케이션과 연동할 Account 선택
     */
    private void chooseAccount() {
        Intent intent = AccountManager.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null);
        startActivityForResult(intent, StaticVariable.REQUEST_PICK_ACCOUNT);
    }

    /**
     * 구글에 토큰을 요청
     */
    private void requestToken() {
        Account userAccount = null;
        String user = authPreference.getUser();
        Log.d("Account user", user);

        Account[] account = accountManager.getAccountsByType("com.google");

        for (int i = 0; i < accountManager.getAccountsByType("com.google").length; i++) {
            Log.d("Account name", account[i].name);
            if (account[i].name.equals(user)) {
                userAccount = account[i];
                break;
            }
        }

        accountManager.getAuthToken(userAccount, "oauth2:" + StaticVariable.SCOPE, null, this, new OnTokenAcquired(), null);
    }

    /**
     * 기존의 토큰을 없애버리는 함수
     */
    private void invalidateToken() {
        AccountManager accountManager = AccountManager.get(this);
        accountManager.invalidateAuthToken("com.google", authPreference.getToken());
        authPreference.setToken(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("request code", requestCode + "");
        switch (requestCode) {
            case StaticVariable.REQUEST_AUTHORIZATION://승인이 된 account의 토큰 요청
                if (resultCode == RESULT_OK) {
                    requestToken();
                }
                break;

            case StaticVariable.REQUEST_PICK_ACCOUNT://Account를 고르는 요청
                if (resultCode == RESULT_OK && data != null) {
                    String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    credential.setSelectedAccountName(accountName);//선택된 account를 credential에 설정
                    authPreference.setUser(accountName);
                    invalidateToken();
                    requestToken();
                }
                break;
        }
    }

    View.OnClickListener preMonth = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            month -= 1;
            if (month < 1) {
                month = 12;
                year -= 1;
            }
            setCalendarDate(month);
            new MakeRequestTask(credential).execute(year+"-"+month);
        }
    };

    View.OnClickListener nexMonth = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            month += 1;
            if (month > 12) {
                month = 1;
                year += 1;
            }
            setCalendarDate(month);
            new MakeRequestTask(credential).execute(year+"-"+month);
        }
    };

    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        }
    };

    public void setCalendarDate(int month) {
        yearMonth.setText(year + "/" + month);
        arrData = new ArrayList<String>();

        // 요일은 +1해야 되기때문에 달력에 요일을 세팅할때에는 -1 해준다.
        mCal.set(year, month - 1, 1);

        //빈 공백을 넣어준다.
        int saturday = mCal.get(Calendar.DAY_OF_WEEK);
        if (saturday != 1) {
            for (int i = 0; i < saturday - 1; i++)
                arrData.add(" ");
        }

        //최대 날짜까지 넣어준다.
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            arrData.add((i + 1)+"");
        }

        adapter = new DateAdapter(this, arrData, year, month);
        mGridView.setAdapter(adapter);
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {

        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            try {
                Bundle bundle = result.getResult();

                Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (launch != null) {
                    startActivityForResult(launch, StaticVariable.REQUEST_AUTHORIZATION);
                } else {
                    String token = bundle
                            .getString(AccountManager.KEY_AUTHTOKEN);

                    authPreference.setToken(token);

                    doAuthenticatedStuff();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

    }
}
