package com.example.whereuat;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class NavigationActivity extends ActionBarActivity implements SensorEventListener
{
	float currentDistance, heading, bearing;
	float currentDegree = 0f;
	final double METERS_TO_MILES = 0.0006213f;
	String destination_name;
	TextView tv1;
	ImageView compass_image;
	Location dest;	
    LocationManager mLocationManager;
    SensorManager mSensorManager;

    LocationListener listener = new LocationListener()
    {

		@Override
		public void onLocationChanged(Location location) {
			// Update navigation metrics
			currentDistance = location.distanceTo(dest);
			bearing = location.bearingTo(dest);
			tv1.setText(getDistanceString(currentDistance));
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}    	
    };

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navigation);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
		
		// Lock portrait mode
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		// Set views
		tv1 = (TextView) findViewById(R.id.textView1);
		
		compass_image = (ImageView) findViewById(R.id.imageView1);
		
		destination_name = getIntent().getStringExtra("name");
		setTitle("Navigating to " + destination_name);
		
		// Receive destination location from intent
		LatLng destination = new LatLng(getIntent().getDoubleExtra("lat", 0.0), getIntent().getDoubleExtra("long", 0.0));
		dest = new Location("dest");
		dest.setLatitude(getIntent().getDoubleExtra("lat", 0));
		dest.setLongitude(getIntent().getDoubleExtra("long", 0));
		dest.setLongitude(getIntent().getDoubleExtra("long", 0));
		dest.setTime(new Date().getTime());
		
		// Setup sensor manager
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		
		// Set up location manager
		mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);		
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3, 0, listener);	
	}
	
	protected void onResume()
	{
		super.onResume();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_GAME);
	}
	
	protected void onPause()
	{
		super.onPause();
		mSensorManager.unregisterListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.navigation, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Helper methods
	public String getDistanceString(double meters)
	{
		double miles = meters*METERS_TO_MILES;
		if(miles > 0.1)
			return String.valueOf(round(miles, 1) + " miles");
		else
			return String.valueOf(Math.floor(miles*5280) + "feet");
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_navigation,
					container, false);
			return rootView;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) 
	{
		heading = Math.round(event.values[0]); // Temporary fix for magnetic declination 
		compass_image.setRotation(-(heading - bearing));
	}
	
	public void launchMaps(View v)
	{
		String loc_string = String.format("%f,%f", dest.getLatitude(), dest.getLongitude());
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + loc_string)); 
			startActivity(i);
	}

}
