package com.example.whereuat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
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
public class MainActivity extends Activity 
{
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private String[] mPlanetTitles;

	private GoogleMap map;
	
	// LatLng values
	private final LatLng LOC_CHICO  = new LatLng(39.729676,-121.847957);
	private final LatLng LOC_HOME   = new LatLng(38.945694,-121.119814);
	private final LatLng LOC_TAIPEI = new LatLng(25.03364,121.564956);
	private LatLng LOC_ME;
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);        
//        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        
//        map.setMyLocationEnabled(true);
        
        Fragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putInt(PlaceFragment.ARG_PLACE_NUMBER, 0);
        fragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

        mTitle = getTitle();
        mPlanetTitles = getResources().getStringArray(R.array.places_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }
    
    public void onClick_Taipei(View v)
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_TAIPEI, 18);
    	map.animateCamera(update);
    }
    
    public void onClick_Chico(View v)
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_CHICO, 18);
    	map.animateCamera(update);    	
    }
    
    public void onClick_Home(View v)
    {
    	CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOC_HOME, 18);
    	map.animateCamera(update);	
    }
    
    public void onClick_Toggle(View v)
    {
    	if(map.getMapType() != GoogleMap.MAP_TYPE_SATELLITE)
    		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
    	else
    		map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    
    public void onClick_myLocation(View v)
    {
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
}