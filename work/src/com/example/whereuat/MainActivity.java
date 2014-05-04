package com.example.whereuat;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
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
        
        Fragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putInt(PlaceFragment.ARG_PLACE_NUMBER, 0);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();
        
        mPlaceTitles = getResources().getStringArray(R.array.places_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlaceTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


//        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        
//        map.setMyLocationEnabled(true);
        
/*        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();


        mTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.places_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));*/
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }
    
    public void onClick_Taipei()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_TAIPEI, 18);
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    	map.animateCamera(update);
    }
    
    public void onClick_Chico()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_CHICO, 18);
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    	map.animateCamera(update);    	
    }
    
    public void onClick_Home()
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_HOME, 18);
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    	map.animateCamera(update);	
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
    	map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
    	Location myLoc = map.getMyLocation();
    	LOC_ME = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
    	map.addMarker(new MarkerOptions().position(LOC_ME).title("You are here!"));
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_ME, 19);
    	map.animateCamera(update);
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
            case R.id.action_settings:
                //openSettings();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}