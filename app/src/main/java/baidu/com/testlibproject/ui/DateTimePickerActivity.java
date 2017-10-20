package baidu.com.testlibproject.ui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

import baidu.com.testlibproject.R;

public class DateTimePickerActivity extends Activity implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener {

    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_time_picker_layout);
        initView();
    }

    private void initView() {
        Calendar cal = Calendar.getInstance();
        DatePicker datePicker = findViewById(R.id.date_picker);
        datePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH), this);
        TimePicker timePicker = findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
        mTextView = findViewById(R.id.select_time);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        showTime(mYear, mMonth, mDay, mHour, mMinute);
    }

    private void showTime(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minute) {
        String text = "select time : " + year + "年" + monthOfYear + "月"
                + dayOfMonth + "日" + hourOfDay + "时" + minute + "分";
        mTextView.setText(text);
    }
}