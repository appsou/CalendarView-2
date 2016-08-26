package com.missmess.calendardemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.missmess.calendardemo.adapter.EventAdapter;
import com.missmess.calendardemo.model.DayEvent;
import com.missmess.calendardemo.model.EventType;
import com.missmess.calendarview.AnimTransiter;
import com.missmess.calendarview.CalendarDay;
import com.missmess.calendarview.CalendarMonth;
import com.missmess.calendarview.MonthView;
import com.missmess.calendarview.TransitRootView;
import com.missmess.calendarview.YearMonthTransformer;
import com.missmess.calendarview.YearView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransitDemoActivity extends AppCompatActivity {
    private final int YEAR = 2016;
    private List<DayEvent> yearEvents;
    private YearMonthTransformer transformer;
    private TransitRootView rootView;
    private YearView yearView;
    private MonthView monthView;
    private View rl_title;
    private View ll_data;
    private ListView listView;
    private EventAdapter adapter;
    private TextView tv_year;
    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_demo);
        // find view
        rootView = (TransitRootView) findViewById(R.id.trv);
        ll_data = findViewById(R.id.ll_data);
        tv_year = (TextView) findViewById(R.id.tv_year);
        textView1 = (TextView) findViewById(R.id.textView1);
        textView2 = (TextView) findViewById(R.id.textView2);
        yearView = (YearView) findViewById(R.id.yv);
        listView = (ListView) findViewById(R.id.lv);
        rl_title = findViewById(R.id.rl_title);
        monthView = (MonthView) findViewById(R.id.mv);
        transformer = new YearMonthTransformer(rootView, yearView, monthView);

        // obtain events and decors
        createEvents();
        // init YearView
        initYearView();
        // init other view data
        initDatas();
        // init listener
        initListener();
    }

    private void createEvents() {
        DayEvent dayEvent1 = new DayEvent(YEAR, 3, 14, EventType.EAT, new String[]{"a big turkey", "picnic"});
        DayEvent dayEvent2 = new DayEvent(YEAR, 3, 15, EventType.ENTERTAINMENT, new String[]{"play VR game", "watch movie"});
        DayEvent dayEvent3 = new DayEvent(YEAR, 6, 25, EventType.BEAUTY, new String[]{"Yoga lesson"});
        DayEvent dayEvent4 = new DayEvent(YEAR, 8, 13, EventType.SPORT, new String[]{"swimming match"});
        DayEvent dayEvent5 = new DayEvent(YEAR, 11, 30, EventType.SPORT, new String[]{"play basketball", "i don't like football", "ping pang!"});

        if(yearEvents == null) {
            yearEvents = new ArrayList<>();
        }
        yearEvents.add(dayEvent1);
        yearEvents.add(dayEvent2);
        yearEvents.add(dayEvent3);
        yearEvents.add(dayEvent4);
        yearEvents.add(dayEvent5);
    }

    private void initYearView() {
        yearView.setYear(YEAR);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, YEAR);
        calendar.set(Calendar.MONTH, Calendar.MAY);
        calendar.set(Calendar.DAY_OF_MONTH, 17);
        yearView.setToday(calendar);

        for(DayEvent event : yearEvents) {
            CalendarDay calendarDay = new CalendarDay(event.getYear(), event.getMonth(), event.getDay());
            yearView.decorateDay(calendarDay, event.getType().getColor());
        }
    }

    private void initDatas() {
        adapter = new EventAdapter();
        listView.setAdapter(adapter);

        tv_year.setText(yearView.getYearString());
        textView1.setText(getString(R.string.event_str, yearEvents.size()));
        ArrayList<EventType> temp = new ArrayList<>();
        for(DayEvent event : yearEvents) {
            if(!temp.contains(event.getType())) {
                temp.add(event.getType());
            }
        }
        textView2.setText(getString(R.string.event_type_str, temp.size()));
    }

    private void initListener() {
        yearView.setOnMonthClickListener(new YearView.OnMonthClickListener() {
            @Override
            public void onMonthClick(YearView simpleMonthView, CalendarMonth calendarMonth) {
                transformer.applyShow(calendarMonth.getMonth());
            }
        });
        monthView.setOnDayClickListener(new MonthView.OnDayClickListener() {
            @Override
            public void onDayClick(MonthView monthView, CalendarDay calendarDay) {
                for(DayEvent event : yearEvents) {
                    if(event.isThisDay(calendarDay)) {
                        adapter.setDetails(event.getType().name(), event.getEventDetails());
                        adapter.notifyDataSetChanged();
                        return;
                    }
                }
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        });
        transformer.setOnTransitListener(new YearMonthTransformer.OnTransitListener() {
            @Override
            public void onY2MTransitStart(AnimTransiter transiter, YearView yearView, MonthView monthView) {
                transiter.slideOutView(tv_year, false);
                transiter.alphaView(rl_title, false);
            }

            @Override
            public void onY2MTransitEnd(AnimTransiter transiter, YearView yearView, MonthView monthView) {
                transiter.slideInView(ll_data, false);
            }

            @Override
            public void onM2YTransitStart(AnimTransiter transiter, YearView yearView, MonthView monthView) {
                transiter.slideOutView(ll_data, true);
            }

            @Override
            public void onM2YTransitEnd(AnimTransiter transiter, YearView yearView, MonthView monthView) {
                // clear event info
                adapter.clear();
                adapter.notifyDataSetChanged();
                transiter.slideInView(tv_year, true);
                transiter.alphaView(rl_title, true);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!transformer.applyHide())
            super.onBackPressed();
    }

}