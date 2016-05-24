package com.blanks.joy.bacitpt;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements LocationListener {
	private static final int MY_PERMISSIONS = 123;
	private static GoogleMap mMap;
	private LocationManager locationManager;
	private static Double latitude, longitude;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	private static View view;

	private FragmentActivity myContext;
	private Activity activity;
	private LayoutInflater linf;

	public LocationFragment() {
		// Required empty public constructor
	}

	@Override
	public void onAttach(Context context) {
		myContext=(FragmentActivity) context;
		activity = (Activity)context;
		super.onAttach(context);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		linf = inflater;
		view = (RelativeLayout) inflater.inflate(R.layout.fragment_location, container, false);
		// Passing harcoded values for latitude & longitude. Please change as per your need. This is just used to drop a Marker on the Map
		latitude = 19.171622;
		longitude = 72.8329409;

		// Do a null check to confirm that we have not already instantiated the map.
		if (mMap == null) {
			// Try to obtain the map from the SupportMapFragment.
			mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
			// Check if we were successful in obtaining the map.
		}
		setUpMap();


		super.onCreateView(inflater, container, savedInstanceState);
		return view;
	}

	private void setUpMap() {
		// For showing a move to my loction button
		if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED) {
			ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS);
		}else if(mMap != null){
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setAllGesturesEnabled(true);
			mMap.getUiSettings().setCompassEnabled(true);

			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.getUiSettings().setTiltGesturesEnabled(true);

			locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
			// For dropping a marker at a point on the Map
			mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("BACI Prism Mindspace Mumbai INDIA").snippet("Bank of America"));//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo))
			// For zooming automatically to the Dropped PIN Location
			//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12.0f));
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					setUpMap();
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					super.onRequestPermissionsResult(requestCode, permissions, grantResults);
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if(mMap!=null) {
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12.0f);
			mMap.animateCamera(cameraUpdate);
			if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					|| ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS);
			} else
				locationManager.removeUpdates(this);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}
}
