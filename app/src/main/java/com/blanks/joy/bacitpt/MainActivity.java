package com.blanks.joy.bacitpt;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.blanks.joy.bacitpt.fragments.CancelFragment;
import com.blanks.joy.bacitpt.fragments.ComingSoonFragment;
import com.blanks.joy.bacitpt.fragments.LocationFragment;
import com.blanks.joy.bacitpt.fragments.RSafeFragment;
import com.blanks.joy.bacitpt.fragments.RequestAlignmentFragment;
import com.blanks.joy.bacitpt.fragments.SettingFragment;
import com.blanks.joy.bacitpt.fragments.UpdateFragment;
import com.blanks.joy.bacitpt.interfaces.FragmentCommute;
import com.blanks.joy.bacitpt.logon.SignInActivity;
import com.blanks.joy.bacitpt.operations.Message;
import com.blanks.joy.bacitpt.operations.MessageReceiver;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.koushikdutta.ion.Ion;

import java.util.List;



public class MainActivity extends AppCompatActivity
		implements
		NavigationView.OnNavigationItemSelectedListener
		,FragmentCommute
		,GoogleApiClient.OnConnectionFailedListener
		,Application.ActivityLifecycleCallbacks {

	private static final int MY_PERMISSIONS = 123;

	private NavigationView navigationView;
	private Toolbar toolbar;
	public int screen = 0;
	public FragmentManager fragmentManager;
	private Snackbar snackBarId;
	@Override
	public void setRosterType(String rosterType) {
		this.rosterType = rosterType;
	}

	String rosterType = "P";
	String time;
	String date;

	public void setTime(String time){
		this.time = time;
	}

	public void setDate(String date){
		this.date = date;
	}

	public static Activity activity;

	private Boolean exit = false;
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			//super.onBackPressed();
		}
		if (exit) {
			finish(); // finish activity
		} else {
			Toast.makeText(this, "Press Back again to Exit.",
					Toast.LENGTH_SHORT).show();
			exit = true;
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exit = false;
				}
			}, 3 * 1000);

		}

	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		fragmentManager = getSupportFragmentManager();
		activity = this;

		//registerActivityLifecycleCallbacks(this);
		FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
		fab.setVisibility(View.GONE);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(screen==0){
					snackBarId = Snackbar.make(view, "Select an operation from left panel", Snackbar.LENGTH_SHORT);
					snackBarId.setAction("", null).show();
					return;
				}else if(screen == R.id.nav_share || screen == R.id.nav_send){
					if(snackBarId != null){
						snackBarId.dismiss();
					}
					return;
				}else if(!(screen == R.id.croster || screen == R.id.uroster || screen == R.id.reachedsafe || screen == R.id.req_align)){
					snackBarId = Snackbar.make(view, "Under Construction", Snackbar.LENGTH_SHORT);
					snackBarId.setAction("", null).show();
					return;
				}else if(screen == R.id.croster){
					if(time == null || date == null){
						snackBarId = Snackbar.make(view, "Select Date and Time for this action", Snackbar.LENGTH_SHORT);
						snackBarId.setAction("", null).show();
						return;
					}
				}else if(screen == R.id.uroster){
					if(time == null || date == null){
						snackBarId = Snackbar.make(view, "Select Date and Time for this action", Snackbar.LENGTH_SHORT);
						snackBarId.setAction("", null).show();
						return;
					}
				}else if(screen == R.id.reachedsafe){
					if(time == null || date == null){
						snackBarId = Snackbar.make(view, "Select Date and Time for this action", Snackbar.LENGTH_SHORT);
						snackBarId.setAction("", null).show();
						return;
					}
				}else if(screen == R.id.req_align){
					if(time == null || date == null){
						snackBarId = Snackbar.make(view, "Select Date and Time for this action", Snackbar.LENGTH_SHORT);
						snackBarId.setAction("", null).show();
						return;
					}
				}

				if (!isRequestRequired()) {
					return;
				}else{

					Message.sendMessage(screen,rosterType, time, date);
					View parentLayout = findViewById(R.id.container_frame);
					snackBarId = Snackbar.make(parentLayout, Message.getMessage(screen, rosterType), Snackbar.LENGTH_INDEFINITE);
					snackBarId.setAction("Action", null).show();
				}
			}
		});

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.setDrawerListener(toggle);
		toggle.syncState();

		navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);

		((FloatingActionButton) findViewById(R.id.fab)).setVisibility(View.GONE);
		SettingFragment fragment = new SettingFragment();
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(R.id.container_frame,fragment);
		fragmentTransaction.commit();

		SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

		try{
			ImageView civ = ((ImageView)navigationView.findViewById(R.id.userView));
			civ.setVisibility(View.VISIBLE);
			civ.setImageDrawable(new BitmapDrawable(Ion.with((Context)activity).load(sharedPref.getString("pic","")).asBitmap().get()));
		}catch (Exception e){
			Log.e("bacitpt","some shit happened with your display pic");
		}
	}

	public void setRosterType(View view) {
		if(((Switch)view).isChecked()) {
			rosterType = "P";
		} else {
			rosterType = "D";
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
		date = null;
		time = null;
		if(snackBarId != null){
			snackBarId.dismiss();
		}

		((FloatingActionButton) findViewById(R.id.fab)).setVisibility(View.VISIBLE);
		if (id == R.id.croster) {
			CancelFragment fragment = new CancelFragment();
			/*Bundle bundle = new Bundle();
			bundle.putString("rosterType",rosterType);
			fragment.setArguments(bundle);
			*/
			fragment.setRetainInstance(true);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			activity.setTitle("Cancel Roster");
		} else if (id == R.id.uroster) {
			UpdateFragment fragment = new UpdateFragment();
			fragment.setRetainInstance(true);
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			activity.setTitle("Update Roster");
		} else if (id == R.id.eta) {
			ComingSoonFragment fragment = new ComingSoonFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			activity.setTitle("ETA Information");
		} else if (id == R.id.req_align) {
			RequestAlignmentFragment fragment = new RequestAlignmentFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			activity.setTitle("Request Pick Alignment");
		} else if (id == R.id.reachedsafe) {
			RSafeFragment fragment = new RSafeFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			activity.setTitle("Reached Safe?");
		} else if (id == R.id.nav_share) {
			LocationFragment fragment = new LocationFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			((FloatingActionButton) findViewById(R.id.fab)).setVisibility(View.GONE);
			activity.setTitle("Locate Transport");
		}else if(id == R.id.nav_send){
			((FloatingActionButton) findViewById(R.id.fab)).setVisibility(View.GONE);
			SettingFragment fragment = new SettingFragment();
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.container_frame,fragment);
			fragmentTransaction.commit();
			activity.setTitle("Settings");
		}else if(id == R.id.logout){
			Intent iLogout = new Intent(MainActivity.this, SignInActivity.class);
			iLogout.putExtra("status",false);
			startActivity(iLogout);
		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}

	private boolean isRequestRequired() {
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
			return true;
		}
		if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		if (shouldShowRequestPermissionRationale(Manifest.permission.SEND_SMS)) {
			Snackbar.make(findViewById(R.id.fab), "Please allow required access", Snackbar.LENGTH_INDEFINITE)
					.setAction(android.R.string.ok, new View.OnClickListener() {
						@Override
						@TargetApi(Build.VERSION_CODES.M)
						public void onClick(View v) {
							requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS);
						}
					}).show();
		} else {
			requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS);
		}
		return false;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		//super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		List<Fragment> fragments = getSupportFragmentManager().getFragments();
		if (fragments != null) {
			for (Fragment fragment : fragments) {
				if(fragment!=null && fragment instanceof LocationFragment)
					((LocationFragment)fragment).OnRequestPermissionsResultL(requestCode, permissions, grantResults);
			}
		}
		switch (requestCode) {
			case MY_PERMISSIONS: {
				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					View parentLayout = findViewById(R.id.container_frame);
					if(time !=null && date!=null) {
						Message.sendMessage(screen, rosterType, time, date);
						snackBarId = Snackbar.make(parentLayout, Message.getMessage(screen, rosterType), Snackbar.LENGTH_INDEFINITE);
						snackBarId.setAction("Action", null).show();
					}

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
	public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
		this.activity = activity;//here we get the activity
		Intent i = new Intent(this, MessageReceiver.class);
		sendBroadcast(i);
	}

	@Override
	public void onActivityStarted(Activity activity) {

	}

	@Override
	public void onActivityResumed(Activity activity) {

	}

	@Override
	public void onActivityPaused(Activity activity) {

	}

	@Override
	public void onActivityStopped(Activity activity) {

	}

	@Override
	public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

	}

	@Override
	public void onActivityDestroyed(Activity activity) {

	}

	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		//super.onConnectionFailed(connectionResult);
	}
}
