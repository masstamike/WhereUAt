package com.example.whereuat;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)

public class MainActivity extends ActionBarActivity
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private String[] mPlaceTitles;
    private GoogleMap map;
    private double nav_lat, nav_long;
    
	// LatLng values
	private final LatLng LOC_CHICO  = new LatLng(39.729676,-121.847957);
	private final LatLng LOC_HOME   = new LatLng(38.945694,-121.119814);
	private final LatLng LOC_TAIPEI = new LatLng(25.03364,121.564956);

	private LatLng LOC_ME;
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
	    @Override
	    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	        selectItem(position);
	    }
	}
	
	/** Swaps fragments in the main content view */
	private void selectItem(int position) {
	    //Do something with the map
		switch(position) {
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
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        try {
			map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
			map.setMyLocationEnabled(true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        Fragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putInt(PlaceFragment.ARG_PLACE_NUMBER, 0);
        fragment.setArguments(args);
        
        mPlaceTitles = getResources().getStringArray(R.array.places_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlaceTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
   }
    
    public void onClick_Taipei()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_TAIPEI, 18);    	
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    	map.animateCamera(update);
    	nav_lat = LOC_TAIPEI.latitude;
    	nav_long = LOC_TAIPEI.longitude;
    }
    
    public void onClick_Chico()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_CHICO, 18);
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    	map.animateCamera(update);   
    	nav_lat = LOC_CHICO.latitude;
    	nav_long = LOC_CHICO.longitude;
    }
    
    public void onClick_Home()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_HOME, 18);
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    	map.animateCamera(update);
    	nav_lat = LOC_HOME.latitude;
    	nav_long = LOC_HOME.longitude;
    }
    
    public void onClick_Toggle()
    {
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
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
			map.addMarker(new MarkerOptions().position(LOC_ME).title("You are here!"));
			CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_ME, 19);
			map.animateCamera(update);
	    	nav_lat = LOC_ME.latitude;
	    	nav_long = LOC_ME.longitude;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.d("Error","This error is due to the myLocation button");
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
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
        		//open new activity to create event
        		Intent intent = new Intent(this, NewEventActivity.class);
        	    startActivity(intent);
        		return true;
            case R.id.action_swap_map:
            	onClick_Toggle();
                return true;
            case R.id.begin_navigation:            	
				try {
					Intent nav_intent = new Intent(this, NavigationActivity.class);
	            	//nav_intent.putExtra("lat", 39.729676);
	            	//nav_intent.putExtra("long", -121.847957);
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
                //openSettings();            	
            	return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}