package in.ravi275.ArduinoControl;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.view.*;
import android.bluetooth.BluetoothAdapter;
import android.util.*;
import android.widget.Toast;
import android.support.v7.app.ActionBarDrawerToggle;



//Provides UI for the main screen.

public class MainActivity extends AppCompatActivity
{

    private DrawerLayout mDrawerLayout;
	private BluetoothAdapter mBtAdapter;
	private String TAG="MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		ImageView btnConnect;
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        // menu icon to Toolbar
		
		ActionBarDrawerToggle mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.lbl_open, R.string.lbl_close);
		mActionBarDrawerToggle.syncState();
		mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);


        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener() {
				// This method will trigger on item Click of navigation menu
				@Override
				public boolean onNavigationItemSelected(MenuItem menuItem)
				{

					mDrawerLayout.closeDrawers();
					switch (menuItem.getItemId())
					{

						case R.id.terms:
							/*Intent intent = new Intent(MainActivity.this, NoticeActivity.class);
							 startActivity(intent);*/
							return false;

						case R.id.help:
							/*Intent intent2 = new Intent(MainActivity.this, HelpActivity.class);
							 startActivity(intent);*/
							return false;
							
						case R.id.about:
							Intent intent3 = new Intent(MainActivity.this, AboutActivity.class);
							startActivity(intent3);
							return false;


						case R.id.exit:
							if(mBtAdapter!=null){
							mBtAdapter.disable();
							}
							MainActivity.this.finish();
							return false;

						default:
							//msg("something wrong");
							return true;
					}
				}

			});
		checkBTState();
		btnConnect =(ImageView) findViewById(R.id.btnConnect);

		btnConnect.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View p1)
				{

					Intent intent=new Intent(MainActivity.this, DeviceListActivity.class);
					startActivity(intent);

				}
			});
    }

	private void checkBTState()
	{
        // Check device has Bluetooth and that it is turned on
		mBtAdapter = BluetoothAdapter.getDefaultAdapter(); 
        if (mBtAdapter == null)
		{ 
            Toast.makeText(getBaseContext(), "Device does not support Bluetooth", Toast.LENGTH_SHORT).show();
        }
		else
		{

			if (mBtAdapter.isEnabled())
			{
				Log.d(TAG, "...Bluetooth ON...");
			}
		    else
			{

				mBtAdapter.enable(); 
            }
		}
	}



	@Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home)
		{
            mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }



	@Override
	protected void onDestroy()
	{
		
		super.onDestroy();
		if(mBtAdapter!=null){
		mBtAdapter.disable();
		}
		System.exit(0);
	}



}
