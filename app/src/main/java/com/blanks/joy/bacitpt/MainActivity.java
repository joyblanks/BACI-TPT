package com.blanks.joy.bacitpt;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;

import com.blanks.joy.bacitpt.interfaces.FragmentCommute;
import com.blanks.joy.bacitpt.operations.Message;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity
		implements NavigationView.OnNavigationItemSelectedListener,FragmentCommute, OnMapReadyCallback {

	private static final int MY_PERMISSIONS = 123;

	NavigationView navigationView;
	Toolbar toolbar;
	int screen = 0;
	public FragmentManager fragmentManager;

	@Override
	public void setRosterType(String rosterType) {
		this.rosterType = rosterType;
	}

	String rosterType = "P";

	Activity activity;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		fragmentManager = getSupportFragmentManager();
		activity = this;
		//String rosterType = ((Switch)findViewById(R.id.rosterType)).getstate


		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(screen==0){
					Snackbar.make(view, "Select an operation from left panel", Snackbar.LENGTH_SHORT)
							.setAction("", null).show();
					return;
				}else if(screen != R.id.croster){
					Snackbar.make(view, "Under Construction", Snackbar.LENGTH_SHORT)
							.setAction("", null).show();
					return;
				}
				if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
					/*
					View.OnClickListener myOnClickListener = new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS);
						}
					};
					if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)) {
						//Explain to the user why we need to read the contacts
						Snackbar.make(view, "App requires permission to SEND SMS", Snackbar.LENGTH_INDEFINITE)
								.setAction("OK", myOnClickListener).show();
						return;
					}else{
						ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS);
					}*/
					ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.SEND_SMS},MY_PERMISSIONS);
				}else{

					Message.sendMessage(screen,rosterType);
					View parentLayout = findViewById(R.id.container_frame);
					Snackbar.make(parentLayout, Message.getMessage(screen, rosterType), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show();
				}
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
	}

	public void setRosterType(View view) {
		if(((Switch)view).isChecked()) {
			rosterType = "P";
		} else {
			rosterType = "D";
		}
	}


	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		screen = id;
		if (id == R.id.croster) {
			CancelFragment fragment = new CancelFragment();
			Bundle bundle = new Bundle();
			bundle.putString("rosterType",rosterType);
			fragment.setArguments(bundle);
			fragment.setRetainInstance(true);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
		} else if (id == R.id.uroster) {
			ComingSoonFragment fragment = new ComingSoonFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
		} else if (id == R.id.eta) {
			ComingSoonFragment fragment = new ComingSoonFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
		} else if (id == R.id.req_align) {
			ComingSoonFragment fragment = new ComingSoonFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
		} else if (id == R.id.reachedsafe) {
			ComingSoonFragment fragment = new ComingSoonFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
		} else if (id == R.id.nav_share) {
			/*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			mapFragment.getMapAsync(this);
			SupportMapFragment fragment = new SupportMapFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			*/
			LocationFragment fragment = new LocationFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
		}else if(id == R.id.nav_send){
			ComingSoonFragment fragment = new ComingSoonFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		switch (requestCode) {
			case MY_PERMISSIONS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
						&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					View parentLayout = findViewById(R.id.container_frame);
					Message.sendMessage(screen, rosterType);
					Snackbar.make(parentLayout, Message.getMessage(screen, rosterType), Snackbar.LENGTH_INDEFINITE).setAction("Action", null).show();

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
	public void onMapReady(GoogleMap map) {
		map.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
	}
}
