package com.blanks.joy.bacitpt.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.blanks.joy.bacitpt.R;
import com.blanks.joy.bacitpt.utils.Constants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

;


/**
 * A simple {@link Fragment} subclass.
 */
public class LocationFragment extends Fragment implements LocationListener {
	private static final int MY_LOC_PERMISSIONS = 123;
	private static GoogleMap mMap;
	private LocationManager locationManager;
	private static Double latitude, longitude;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	private static View view;


	private FragmentActivity myContext;
	private Activity activity;
	private LayoutInflater linf;
	private RequestQueue queue;

	public LocationFragment() {
		// Required empty public constructor
	}

	@Override
	public void onAttach(Context context) {
		myContext = (FragmentActivity) context;
		activity = (Activity) context;
		super.onAttach(context);
		queue = Volley.newRequestQueue(context);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (container == null) {
			return null;
		}
		mMap = null;
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
				|| ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOC_PERMISSIONS);
			ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOC_PERMISSIONS);
		} else if (mMap != null) {
			mMap.setMyLocationEnabled(true);
			mMap.getUiSettings().setAllGesturesEnabled(true);
			mMap.getUiSettings().setCompassEnabled(true);

			mMap.getUiSettings().setMyLocationButtonEnabled(true);
			mMap.getUiSettings().setTiltGesturesEnabled(true);

			locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
			locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
			// For dropping a marker at a point on the Map
			MarkerOptions m = new MarkerOptions().position(new LatLng(latitude, longitude)).title("BACI Prism Mindspace Mumbai INDIA").snippet("Bank of America").icon(BitmapDescriptorFactory.fromResource(R.mipmap.baci_marker));
			mMap.addMarker(m);//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo))
			// For zooming automatically to the Dropped PIN Location
			//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12.0f));
		}
	}


	public void OnRequestPermissionsResultL(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_LOC_PERMISSIONS: {
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
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case MY_LOC_PERMISSIONS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					setUpMap();
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.

				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (mMap != null) {
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			broadcastLocation(location);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12.0f);
			mMap.animateCamera(cameraUpdate);

			if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
					|| ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
				requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOC_PERMISSIONS);
				ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOC_PERMISSIONS);
			}else {
				locationManager.removeUpdates(this);
			}
		}

			callAPI();

	}


	private boolean isRequestRequired() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				|| ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)
				||shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)) {
			Snackbar.make(activity.findViewById(R.id.fab), "Please allow required access", Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOC_PERMISSIONS);
						}
					}).show();
		} else {
			ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOC_PERMISSIONS);
		}
		return false;
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {}

	@Override
	public void onProviderEnabled(String provider) {}

	@Override
	public void onProviderDisabled(String provider) {}

	private void broadcastLocation(Location location) {
		SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
		if((null == sharedPref.getString("nbkid",null) || null == sharedPref.getString("timeIn",null))){
			return;
		}
		String url = Constants.URL_PREFIX+"/"+sharedPref.getString("nbkid",null)+"/"+location.getLatitude()+","+location.getLongitude()+"/"+sharedPref.getString("timeIn",null).replace(":","")+"";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {}
		});
		queue.add(jsObjRequest);
	}

	@Override
	public void onResume() {
		setUpMap();
		super.onResume();
	}


	private void populateTravellers(JSONObject json){

		JSONArray recs;
		JSONObject user;
		Bitmap bmp;
		MarkerOptions m;


		if (mMap != null) {
			try {
				recs = json.getJSONArray("records");
				for (int i = 0; i < recs.length(); i++) {
					user = recs.getJSONObject(i);
					m = new MarkerOptions()
							.position(new LatLng(user.getDouble("lat"), user.getDouble("lon")))
							.title(user.getString("firstname") + " " + user.getString("lastname"));
					if (null != user.getString("pic") && !"".equalsIgnoreCase(user.getString("pic"))) {
						//bmp  = Ion.with((Context)activity).load(user.getString("pic")).asBitmap().get();

						//m.icon(BitmapDescriptorFactory.fromBitmap(bmp));
					}
					mMap.addMarker(m);
				}

			} catch (Exception e) {
				Log.e("bacitpt", e.getMessage());
			}
		}

	}

	private void callAPI(){
		SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
		if((null == sharedPref.getString("nbkid",null) || null == sharedPref.getString("timeIn",null))){
			return;
		}
		String url = Constants.URL_PREFIX+"/"+sharedPref.getString("timeIn",null).replace(":","")+"/routeId";
		JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				populateTravellers(response);//.toString();
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
		queue.add(jsObjRequest);
	}



}
