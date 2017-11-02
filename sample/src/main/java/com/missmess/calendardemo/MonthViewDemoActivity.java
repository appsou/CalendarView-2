package com.missmess.calendardemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.missmess.calendarview.CalendarDay;
import com.missmess.calendarview.CalendarMonth;
import com.missmess.calendarview.CalendarUtils;
import com.missmess.calendarview.DayDecor;
import com.missmess.calendarview.MonthView;

import java.util.Calendar;

public class MonthViewDemoActivity extends AppCompatActivity {
    private TextView button12;
    private MonthView monthView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_view_demo);

        //find view
        monthView = (MonthView) findViewById(R.id.mv);
        button12 = (TextView) findViewById(R.id.button12);
        button12.setVisibility(View.GONE);

        init();
    }

    private void init() {
        CalendarMonth month = new CalendarMonth(2017, 2);
        monthView.setYearAndMonth(month);
        monthView.setToday(new CalendarDay(month, 12));
        // add decorators
        DayDecor dayDecor = new DayDecor();
        // circle bg
        dayDecor.putOne(new CalendarDay(month, 1), 0xFFFF6600);
        // rectangle bg
        decorWeekend(month, dayDecor);
        // drawable bg
        dayDecor.putOne(new CalendarDay(month, 21), getResources().getDrawable(R.drawable.a_decor));
        // styled background and text
        DayDecor.Style style = new DayDecor.Style();
        style.setTextSize(getResources().getDimensionPixelSize(R.dimen.big_text));
        style.setTextColor(0xFF72E6BC);
        style.setBold(true);
        style.setItalic(true);
        style.setUnderline(true);
        style.setStrikeThrough(true);
        style.setPureColorBgShape(DayDecor.Style.CIRCLE_STROKE);
        style.setPureColorBg(0xFF66AA76);
        dayDecor.putOne(new CalendarDay(month, 24), style);
        monthView.setDecors(dayDecor);
        // add listener
        monthView.setOnMonthTitleClickListener(new MonthView.OnMonthTitleClickListener() {
            @Override
            public void onMonthClick(MonthView monthView, CalendarMonth calendarMonth) {
                Toast.makeText(MonthViewDemoActivity.this, "title clicked: " + calendarMonth.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        monthView.setOnSelectionChangeListener(new MonthView.OnSelectionChangeListener() {
            @Override
            public void onSelectionChanged(MonthView monthView, CalendarDay now, CalendarDay old, boolean byUser) {
                Toast.makeText(MonthViewDemoActivity.this, "selection change to: " + now, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void decorWeekend(CalendarMonth month, DayDecor dayDecor) {
        int days = CalendarUtils.getDaysInMonth(month);
        for(int i = 1; i <= days; i++) {
            CalendarDay calendarDay = new CalendarDay(month, i);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(calendarDay.getDate());
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            if(dayOfWeek == 1 || dayOfWeek == 7) {
                dayDecor.putOne(calendarDay, 0xFFAAAAAA, DayDecor.Style.RECTANGLE);
            }
        }
    }

    public void click(View v) {
        switch (v.getId()) {
            case R.id.button5:
                monthView.setSelection(null);
                break;
            case R.id.button6:
                DayDecor.Style style = new DayDecor.Style();
                style.setBold(true);
                style.setTextSize(getResources().getDimensionPixelSize(R.dimen.big_text));
                style.setPureColorBg(Color.BLACK);
                monthView.setSelectionStyle(style);
                break;
            case R.id.button7:
                if(monthView.getCurrentMonth().getYear() == 2017) {
                    monthView.setYearAndMonth(2000, 4);
                } else {
                    monthView.setYearAndMonth(2017, 2);
                }
                break;
            case R.id.button8:
                if(monthView.isWeekMode()) {
                    monthView.showMonthMode();
                    button12.setVisibility(View.GONE);
                } else {
                    monthView.showWeekMode();
                    button12.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.button9:
                CalendarDay[] range = monthView.getShowingDayRange();
                String s = "从" + range[0].toString() + "到" + range[1].toString();
                Toast.makeText(this, s, Toast.LENGTH_LONG).show();
                break;
            case R.id.button10:
                monthView.setDayDisable(new CalendarDay(monthView.getCurrentMonth(), 12));
                monthView.setDayDisable(new CalendarDay(monthView.getCurrentMonth(), 13));
                monthView.setDayDisable(new CalendarDay(monthView.getCurrentMonth(), 14));
                monthView.setDayDisable(new CalendarDay(monthView.getCurrentMonth(), 15));
                monthView.setDayDisable(new CalendarDay(monthView.getCurrentMonth(), 16));
                break;
            case R.id.button11:
                int dayOfWeek = monthView.getStartDayOfWeek();
                int next = ++dayOfWeek % 7;
                monthView.setStartDayOfWeek(next == 0 ? 7 : next);
                break;
            case R.id.button12:
                int lineIndex = monthView.getSelectionLineIndex();
                if (lineIndex != -1)
                    monthView.setWeekIndex(lineIndex);
                break;
        }
    }
}
