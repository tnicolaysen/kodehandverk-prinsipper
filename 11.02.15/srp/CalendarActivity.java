package com.blopp.bloppasthma.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.joda.time.DateTime;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.blopp.bloppasthma.R;
import com.blopp.bloppasthma.adapters.AirQualityAdapter;
import com.blopp.bloppasthma.adapters.PollenDistributionAdapter;
import com.blopp.bloppasthma.adapters.TakenMedicinesAdapter;
import com.blopp.bloppasthma.mockups.ChildIdService;
import com.blopp.bloppasthma.airqualityfeed.AirQualityCast;
import com.blopp.bloppasthma.models.AirQualityState;
import com.blopp.bloppasthma.models.LogModel;
import com.blopp.bloppasthma.models.PollenState;
import com.blopp.bloppasthma.utils.DateAdapter;
import com.blopp.bloppasthma.views.CalendarView;
import com.blopp.bloppasthma.views.CalendarView.OnCellTouchListener;
import com.blopp.bloppasthma.views.Cell;
import com.blopp.bloppasthma.xmlfeed.PollenCast;


public class CalendarActivity extends Activity implements
		OnCellTouchListener
{
	private static final String TAG = CalendarActivity.class.getSimpleName();
	CalendarView calendarView;
	TextView monthTextView;
	Button nextMonthButton, previousMonthButton;
	
	private ListView medicineTakenListView;
	private ListView pollenListView;
	private ListView airQualityView;
	private LogModel logModel;
	
	private PollenCast pollenCast;
	private DateAdapter dateAdapter;
	private int day, month, year;
	
	private AirQualityCast airQualityCast;
	
	private DateTime dateTime = new DateTime();
	private TakenMedicinesAdapter medicineGridAdapter;
	private ChildIdService childIdService;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar);
		
		childIdService = new ChildIdService(getApplicationContext());
		
		logModel = new LogModel(childIdService.getChildId(), dateTime.getMonthOfYear(), dateTime.getYear());
		
		calendarView = (CalendarView) findViewById(R.id.calendarview);
		calendarView.setOnCellTouchListener(this);
		
		initializeDaysShownInMedicineList();
		
		nextMonthButton = (Button) findViewById(R.id.next_month_button);
		nextMonthButton.setOnClickListener(new NextMonthClickListener());
		
		previousMonthButton = (Button) findViewById(R.id.prev_month_button);
		previousMonthButton.setOnClickListener(new PreviousMonthClickListener());
		
		monthTextView = (TextView) findViewById(R.id.month_of_year_textview);
		updateMonthTextField();
		
		medicineTakenListView = (ListView) findViewById(R.id.medicine_taken_listView);
		medicineGridAdapter = new TakenMedicinesAdapter(getApplicationContext(), getAmountOfMedicinesTaken());
		medicineTakenListView.setAdapter(medicineGridAdapter);
		
		pollenListView = (ListView)findViewById(R.id.pollen_listView);
		pollenListView.setAdapter(new PollenDistributionAdapter(getApplicationContext(), getPollenStates()));
		
		airQualityView = (ListView)findViewById(R.id.airquality_listview);
		airQualityView.setAdapter(new AirQualityAdapter(getApplicationContext(), getAirQualityStates()));
	}
	

	/**
	 * Updates medicineTakenListView according to day selected.
	 */
	public void onTouch(Cell cell)
	{
		day = cell.getDayOfMonth();
		month = calendarView.getMonth()+1;
		year = calendarView.getYear();
		
		dateAdapter = new DateAdapter(day, month, year);
		medicineTakenListView.setAdapter(new TakenMedicinesAdapter(getApplicationContext(), getAmountOfMedicinesTaken()));
		
		makeToast(day+"-"+month+"-"+year, Toast.LENGTH_SHORT);
	}
	
	
	
	private void initializeDaysShownInMedicineList()
	{
		day = dateTime.getDayOfMonth();
		month = dateTime.getMonthOfYear();
		year = dateTime.getYear();
		dateAdapter = new DateAdapter(day, month, year);
		makeToast(day + "-" + month + "-" + year, Toast.LENGTH_SHORT);
	}
	
	private void updateMonthTextField()
	{
		
		String month = DateAdapter.getMonth(dateTime);
		DateTime.Property year = dateTime.year();
		monthTextView.setText(month + "-" + year.getAsText());
	}
	
	/**
	 * When a user clicks "Next" or "Previous", update the days such that the JSON-call becomes correct.
	 */
	
	private void updateDates()
	{
		day = dateTime.getDayOfMonth();
		month = dateTime.getMonthOfYear();
		year = dateTime.getYear();
	}
	
	
	private void refreshMedicinesTaken(int day, int month, int year)
	{
		dateAdapter = new DateAdapter(day, month, year);
		medicineTakenListView.setAdapter(new TakenMedicinesAdapter(getApplicationContext(), getAmountOfMedicinesTaken()));		
		makeToast(day+"-"+month+"-"+year, Toast.LENGTH_SHORT);
	}

	private HashMap<Integer, Integer> getAmountOfMedicinesTaken()
	{
		return logModel.getAmountOfMedicineAtDate(dateAdapter.getSqlFormattedDate());
	}
	
	/**
	 * WARNING: NOT COMPLETED YET.
	 * @return ArrayList<PollenState> which contains pollenstates for the selected day
	 */
	private ArrayList<PollenState> getPollenStates()
	{
		this.pollenCast = new PollenCast();
		pollenCast.execute();
		try
		{
			pollenCast.get();
		} catch (InterruptedException e)
		{
			makeToast(R.string.download_error, Toast.LENGTH_SHORT);
			e.printStackTrace();
		} catch (ExecutionException e)
		{
			makeToast(R.string.download_error, Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		return pollenCast.getPollenStateAtDayModel().getPollenStatesAtDay();
		
	}
	
	private ArrayList<AirQualityState> getAirQualityStates()
	{
		this.airQualityCast = new AirQualityCast();
		airQualityCast.execute();
		try
		{
			airQualityCast.get();
		} catch (InterruptedException e)
		{
			makeToast(R.string.download_error, Toast.LENGTH_SHORT);
			e.printStackTrace();
		} catch (ExecutionException e) {
			makeToast(R.string.download_error, Toast.LENGTH_SHORT);
			e.printStackTrace();
		}
		return airQualityCast.getAirQualityStateAtDayModel().getAirQualityAtDay();
	}
	/*
	 * Used to infrom the user about which date is set.
	 * @param toastMessage, date to be displayed or the error message. 
	 * @param length
	 */
	private void makeToast(int toastMessage, int length)
	{
		Toast.makeText(this, toastMessage, length).show();
	}
	private void makeToast(String toastMessage, int length)
	{
		Toast.makeText(this, toastMessage, length).show();
	}
	
	private class NextMonthClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			calendarView.nextMonth();
			dateTime = dateTime.plusMonths(1);
			logModel = new LogModel(childIdService.getChildId(), dateTime.getMonthOfYear(), dateTime.getYear());
			update();
		}
		
	}
	private class PreviousMonthClickListener implements OnClickListener
	{

		@Override
		public void onClick(View v)
		{
			calendarView.previousMonth();
			dateTime = dateTime.minusMonths(1);
			logModel = new LogModel(childIdService.getChildId(), dateTime.getMonthOfYear(), dateTime.getYear());
			update();
		}
		
	}
	private void update()
	{
		updateMonthTextField();	
		updateDates();
		refreshMedicinesTaken(day, month, year);
	}
}