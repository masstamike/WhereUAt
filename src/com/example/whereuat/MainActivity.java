package com.example.whereuat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends ActionBarActivity implements OnMapLongClickListener, OnInfoWindowClickListener
{
	final Context context = this;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private String[] mPlaceTitles;
	private GoogleMap map;
	private double nav_lat, nav_long;

	// LatLng values
	private final LatLng LOC_CHICO = new LatLng(39.729676, -121.847957);
	private final LatLng LOC_HOME = new LatLng(38.945694, -121.119814);
	private final LatLng LOC_TAIPEI = new LatLng(25.03364, 121.564956);

	private LatLng LOC_ME,newLoc;

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
		// Do something with the map
		switch (position) {
		case 1:
			onClick_Taipei();
			break;
		case 2:
			onClick_Chico();
			break;
		case 3:
			onClick_Home();
			break;
		case 4:
			onClick_myLocation();
			break;
		}

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlaceTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		try {
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			map.setMyLocationEnabled(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		map.setOnMapLongClickListener(this);
        map.setOnInfoWindowClickListener(this);
        
        Fragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putInt(PlaceFragment.ARG_PLACE_NUMBER, 0);
        fragment.setArguments(args);
        
        mPlaceTitles = getResources().getStringArray(R.array.places_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setSelected(true);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlaceTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        
        // Start tutorial
        AlertDialog.Builder ab = new AlertDialog.Builder(context);
        ab.setTitle("Welcome to WhereUAt!")
        	.setMessage("-WhereUAt is a social meetup application.\n"
			+ "-Long press anywhere on the map to create a new event.\n"
			+ "-To view a list of events, slide the drawer out from the left side of your screen.\n"
			+ "-Touch a pin for details about the event and navigation options.\n")
			.setInverseBackgroundForced(true)
        	.setNeutralButton("Ok", new DialogInterface.OnClickListener()
        	{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();
				}
        	});
        AlertDialog alertDialog = ab.create();
        alertDialog.show();
   }
    
    public void onClick_Taipei()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_TAIPEI, 18);    	
    	map.animateCamera(update);
    	nav_lat = LOC_TAIPEI.latitude;
    	nav_long = LOC_TAIPEI.longitude;
    }
    
    public void onClick_Chico()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_CHICO, 18);
    	map.animateCamera(update);   
    	nav_lat = LOC_CHICO.latitude;
    	nav_long = LOC_CHICO.longitude;
    }
    
    public void onClick_Home()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_HOME, 18);
    	map.animateCamera(update);
    	nav_lat = LOC_HOME.latitude;
    	nav_long = LOC_HOME.longitude;
    }
    
    public void onClick_Toggle()
    {
    	if(map.getMapType() != GoogleMap.MAP_TYPE_SATELLITE)
    		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    	else
    		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    
    public void onClick_myLocation()
    {
    	try {
    		Location myLoc = map.getMyLocation();
			LOC_ME = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
			map.addMarker(new MarkerOptions().position(LOC_ME).title("Party at my house"));
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_ME, 19);
			map.animateCamera(update);
			nav_lat = LOC_ME.latitude;
			nav_long = LOC_ME.longitude;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Error", "This error is due to the myLocation button");
		}
	}

	public static class PlaceFragment extends Fragment {
		public static final String ARG_PLACE_NUMBER = "place_number";

		public PlaceFragment() {
			// Empty constructor required for fragment subclasses
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			int i = getArguments().getInt(ARG_PLACE_NUMBER);
			String place = getResources().getStringArray(R.array.places_array)[i];
			getActivity().setTitle(place);
			return rootView;
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu items for use in the action bar
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main_activity_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_add:
			// open new activity to create event
			Intent intent = new Intent(this, NewEventActivity.class);
			// add coords
			startActivity(intent);
			return true;
		case R.id.action_swap_map:
			onClick_Toggle();
			return true;
		case R.id.begin_navigation:
			try {
				Intent nav_intent = new Intent(this, NavigationActivity.class);
				// nav_intent.putExtra("lat", 39.729676);
				// nav_intent.putExtra("long", -121.847957);
				nav_intent.putExtra("lat", nav_lat);
				nav_intent.putExtra("long", nav_long);
				nav_intent.putExtra("name", "Chico State");
				startActivity(nav_intent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("Error", "Error pushing navigation menu button.");
			}
			return true;
		case R.id.action_settings:
			// openSettings();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onMapLongClick(final LatLng point) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);
		alertDialogBuilder.setTitle("New event");
		alertDialogBuilder.setIcon(R.drawable.whereuat);
		alertDialogBuilder
				.setMessage("Create a new event at this location?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Intent add_intent = new Intent(context,
										NewEventActivity.class);
								add_intent.putExtra("lat", point.latitude);
								add_intent.putExtra("long", point.longitude);
								startActivityForResult(add_intent, 0);
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case (0): {
			if (resultCode == Activity.RESULT_OK) {
				
				Bundle extras = data.getExtras();
				if (extras != null) {					
					String newText = extras.getString("title");
					double latitude = extras.getDouble("lat");
					double longitude = extras.getDouble("long");
					int hour = extras.getInt("hour");
					int minute = extras.getInt("minute");
					int day = extras.getInt("day");
					int month = extras.getInt("month");
					int year = extras.getInt("year");
					LatLng marker_position = new LatLng(latitude, longitude);
					String info_snippet = String.format
					(
							"Time: %d:%d\nDate: %d/%d/%d\n",
							hour, minute, month, day, year
					);
					map.addMarker(new MarkerOptions().position(marker_position).title(newText).snippet(info_snippet));
					Toast.makeText(getApplicationContext(), "Event created", Toast.LENGTH_LONG).show();
				}
			}
		}
			break;
		}
	}

	@Override
	public void onInfoWindowClick(final Marker marker)
	{
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle(marker.getTitle());
		alertDialogBuilder.setIcon(R.drawable.whereuat);
		alertDialogBuilder
			.setMessage(marker.getSnippet() + "\nNavigate to this event?")
			.setCancelable(false)
			.setPositiveButton("Yes", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					LatLng loc = marker.getPosition();
					Intent nav_intent = new Intent(context, NavigationActivity.class);
			    	nav_intent.putExtra("lat", loc.latitude);
			    	nav_intent.putExtra("long", loc.longitude);
			    	nav_intent.putExtra("name", marker.getTitle());
			    	startActivity(nav_intent);
				}
			})
			.setNegativeButton("No", new DialogInterface.OnClickListener()
			{
				public void onClick(DialogInterface dialog, int id)
				{
					dialog.cancel();
				}
			});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}