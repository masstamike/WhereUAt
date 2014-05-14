package com.example.whereuat;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

public class NewEventActivity extends ActionBarActivity 
{
	public static int m_hour, m_minute, m_day, m_month, m_year;
	public double latitude, longitude;
	public String title;
	
	
	@Override	
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_event);

		if (savedInstanceState == null) 
		{
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			latitude = extras.getDouble("lat");
			longitude = extras.getDouble("long");
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.new_event, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment 
	{
		public PlaceholderFragment(){}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) 
		{
			View rootView = inflater.inflate(R.layout.fragment_new_event,
					container, false);
			return rootView;
		}
	}
	
	// http://developer.android.com/guide/topics/ui/controls/pickers.html
	public static class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener
    {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) 
		{
			// Use the current time as the default values for the picker
			final Calendar c = Calendar.getInstance();
			int hour = c.get(Calendar.HOUR_OF_DAY);
			int minute = c.get(Calendar.MINUTE);

			// Create a new instance of TimePickerDialog and return it
			return new TimePickerDialog(getActivity(), this, hour, minute,
					DateFormat.is24HourFormat(getActivity()));
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) 
		{
			m_hour = hourOfDay;
			m_minute = minute;
			// Do something with the time chosen by the user
		}
	}
	
	public void showTimePickerDialog(View v)
	{
	    DialogFragment newFragment = new TimePickerFragment();
	    newFragment.show(getFragmentManager(), "timePicker");
	}

	public static class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) 
		{
			// Use the current date as the default date in the picker
			final Calendar c = Calendar.getInstance();
			int year = c.get(Calendar.YEAR);
			int month = c.get(Calendar.MONTH);
			int day = c.get(Calendar.DAY_OF_MONTH);

			// Create a new instance of DatePickerDialog and return it
			return new DatePickerDialog(getActivity(), this, year, month, day);
		}

		public void onDateSet(DatePicker view, int year, int month, int day) 
		{
			m_year = year;
			m_month = month;
			m_day = day;
		}
	}

	public void showDatePickerDialog(View v) 
	{
	    DialogFragment newFragment = new DatePickerFragment();
	    newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public void submitEvent(View v)
	{
		EditText eTitle = (EditText)findViewById(R.id.editText1); 
		title = eTitle.getText().toString();
		Intent resultIntent = new Intent();
		resultIntent.putExtra("title", title);
		resultIntent.putExtra("hour", m_hour);
		resultIntent.putExtra("minute", m_minute);
		resultIntent.putExtra("day", m_day);
		resultIntent.putExtra("month", m_month);
		resultIntent.putExtra("year", m_year);
		resultIntent.putExtra("lat", latitude);
		resultIntent.putExtra("long", longitude);
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}