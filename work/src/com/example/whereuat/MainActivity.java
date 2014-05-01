package com.example.whereuat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity 
{
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
        map = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        setContentView(R.layout.fragment_main);
        
        map.setMyLocationEnabled(true);        
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
    
    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class MainFragment extends Fragment {

        public MainFragment() {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
/*            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            String planet = getResources().getStringArray(R.array.planets_array)[i];

            int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
                            "drawable", getActivity().getPackageName());
            ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);*/
            return rootView;
        }
    }
}