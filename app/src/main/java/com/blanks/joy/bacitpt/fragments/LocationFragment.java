package com.blanks.joy.bacitpt.fragments;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

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
	private HashMap<String, Marker> markers = new HashMap<String, Marker>();

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
		if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M && (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
				|| ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOC_PERMISSIONS);
		} else if (mMap != null) {
				mMap.setMyLocationEnabled(true);
				mMap.getUiSettings().setAllGesturesEnabled(true);
				mMap.getUiSettings().setCompassEnabled(true);

				mMap.getUiSettings().setMyLocationButtonEnabled(true);
				mMap.getUiSettings().setTiltGesturesEnabled(true);

				locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
				// For dropping a marker at a point on the Map
				MarkerOptions m = new MarkerOptions().position(new LatLng(latitude, longitude)).title("BACI Prism Mindspace Mumbai INDIA").snippet("Bank of America").icon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mark_baci));;
											//.icon(BitmapDescriptorFactory.fromBitmap(mergeToPin(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))));
				mMap.addMarker(m);//.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo))
				// For zooming automatically to the Dropped PIN Location
				//mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 12.0f));
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

			}else {
				locationManager.removeUpdates(this);
			}
		}

			callAPI();

	}


	public boolean isRequestRequired() {
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
							requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOC_PERMISSIONS);
						}
					}).show();
		} else {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, MY_LOC_PERMISSIONS);
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
		SharedPreferences sharedPref = activity.getSharedPreferences(Constants.BACITPT,Context.MODE_PRIVATE);
		if((null == sharedPref.getString("id",null) || null == sharedPref.getString("timeIn",null))){
			return;
		}
		String url = Constants.URL_PREFIX+"/"+sharedPref.getString("id",null)+"/"+location.getLatitude()+","+location.getLongitude()+"/"+sharedPref.getString("timeIn",null).replace(":","")+"";
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

	private Bitmap getCircleBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.GREEN;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final Rect rect2= new Rect(bitmap.getWidth()/3, bitmap.getWidth()/3, bitmap.getWidth()*3/4, bitmap.getHeight()*3/4);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(new RectF(rect2), paint);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect2, paint);
		bitmap.recycle();
		return output;
		//return Bitmap.createScaledBitmap(output, 200, 200, false);
	}

	public Bitmap mergeToPin(Bitmap front) {
		front = getCircleBitmap(front);
		Bitmap back = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.map_mark_bkg_cyan);
		Bitmap result = Bitmap.createBitmap(200, 200, back.getConfig());
		Canvas canvas = new Canvas(result);
		canvas.drawBitmap(back,new Matrix(), null);
		Matrix matrix = new Matrix();
		matrix.postScale(0.4f, 0.4f);
		matrix.postTranslate(7,-20);
		canvas.drawBitmap(front, matrix, null);
		//return result;
		return Bitmap.createScaledBitmap(result, 300, 300, false);
	}

	private void populateTravellers(JSONObject json){

		JSONArray recs;
		JSONObject user;
		Bitmap bmp;
		MarkerOptions m;
		String id;

		if (mMap != null) {
			try {
				recs = json.getJSONArray("records");
				for (int i = 0; i < recs.length(); i++) {
					user = recs.getJSONObject(i);
					id = user.getString("id");
					if(markers.containsKey(id)){
						markers.get(id).setPosition(new LatLng(user.getDouble("latitude"), user.getDouble("longitude")));
					}else {
						m = new MarkerOptions()
								.position(new LatLng(user.getDouble("latitude"), user.getDouble("longitude")))
								.title(user.getString("name"));
						if (null != user.getString("pic") && !"".equalsIgnoreCase(user.getString("pic"))) {
							bmp = Ion.with((Context) activity).load(user.getString("pic")).asBitmap().get();


							m.icon(BitmapDescriptorFactory.fromBitmap(mergeToPin(bmp)));
						}
						markers.put(id, mMap.addMarker(m));
					}
				}

			} catch (Exception e) {
				Log.e("bacitpt", e.getMessage());
			}
		}

	}

	private void callAPI(){
		SharedPreferences sharedPref = activity.getSharedPreferences(Constants.BACITPT,Context.MODE_PRIVATE);
		if((null == sharedPref.getString("id",null) || null == sharedPref.getString("timeIn",null))){
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
