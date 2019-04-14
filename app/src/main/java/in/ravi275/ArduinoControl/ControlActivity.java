package in.ravi275.ArduinoControl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.content.ContextCompat;
import android.os.Build;
import com.afollestad.materialdialogs.MaterialDialog;
import android.widget.TextView;
import android.util.Log;
import android.os.AsyncTask;
import android.os.*;
import android.content.Context;

import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.view.KeyEvent;
//import com.google.android.gms.ads.*;

public class ControlActivity extends AppCompatActivity implements InterfaceF2A
{


	private String TAG="ControlActivity";
	//AdView adView1;


	BluetoothAdapter myBluetooth = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;

	Adapter adapter = new Adapter(getSupportFragmentManager());

    //SPP UUID. Look for it
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static String address=null;

	Thread workerThread;
	byte[] readBuffer;
	int readBufferPosition;
	int counter;
	volatile boolean stopWorker;




	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control);

		Intent newIntent = getIntent();
        address = newIntent.getStringExtra(DeviceListActivity.DEVICE_ADDRESS); 	
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        
		// Set Tabs inside Toolbar
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
		new ConnectBT().execute();

		/*adView1=(AdView)findViewById(R.id.adView1);
		 AdRequest adRequest1=new AdRequest.Builder()
		 .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
		 .addTestDevice("511DDA13A467F60FF3C07617722CCC8D").build();
		 adView1.loadAd(adRequest1);*/


	}

	// Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager)
	{

        adapter.addFragment(new DigitalTab(), "DIGITAL");
        adapter.addFragment(new PwmTab(), "PWM");
        adapter.addFragment(new DataTab(), "DATA");
        viewPager.setAdapter(adapter);
    }

	
    static class Adapter extends FragmentPagerAdapter
	{
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager)
		{
            super(manager);
        }

        @Override
        public Fragment getItem(int position)
		{
            return mFragmentList.get(position);
        }

        @Override
        public int getCount()
		{
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title)
		{
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
		{
            return mFragmentTitleList.get(position);
        }
    }




	private class ConnectBT extends AsyncTask<Void, Void, Void>
    {
        private boolean ConnectSuccess = true;
		private MaterialDialog dialog;

		
        @Override
        protected void onPreExecute()
        {
			dialog = new MaterialDialog.Builder(ControlActivity.this)
				.title("Connecting")
				.content("Please wait..!!!")
				.progress(true, 0)
				.show();  
        }

		
		
        @Override
        protected Void doInBackground(Void... devices) 
        {
            try
            {
                if (btSocket == null || !isBtConnected)
                {
					myBluetooth = BluetoothAdapter.getDefaultAdapter();
					BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);
					btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);
					BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
					btSocket.connect();
                }
            }
            catch (IOException e)
            {
                ConnectSuccess = false;
            }
            return null;
        }

		
		
        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
                msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
            }
            else
            {
				Log.d(TAG, ">>Connected to " + address);
                msg("Connected.");
                isBtConnected = true;
				blueRead();
            }
			dialog.dismiss();

        }
    }



	public void blueWrite(String s)
	{

		if (btSocket != null)
        {
            try
            {
                btSocket.getOutputStream().write(s.toString().getBytes());
            }
            catch (IOException e)
            {
                msg("Please Check Your Connection");
            }
        }

	}

	public void blueRead()
	{


		final Handler handler = new Handler(); 
		final byte delimiter = 10; //This is the ASCII code for a newline character

		stopWorker = false;
		readBufferPosition = 0;
		readBuffer = new byte[1024];
		workerThread = new Thread(new Runnable()
			{
				public void run()
				{                
					while (!Thread.currentThread().isInterrupted() && !stopWorker)
					{
						try 
						{
							int bytesAvailable = btSocket.getInputStream().available();                        
							if (bytesAvailable > 0)
							{
								byte[] packetBytes = new byte[bytesAvailable];
								btSocket.getInputStream().read(packetBytes);
								for (int i=0;i < bytesAvailable;i++)
								{
									byte b = packetBytes[i];
									if (b == delimiter)
									{
										byte[] encodedBytes = new byte[readBufferPosition];
										System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
										final String data = new String(encodedBytes, "US-ASCII");
										readBufferPosition = 0;
										handler.post(new Runnable()
											{
												public void run()
												{
													DataTab fragment=(DataTab) adapter.getItem(2);
													if (fragment != null)
													{
														fragment.recieveMessage(data);
													}
												}
											});
									}
									else
									{
										readBuffer[readBufferPosition++] = b;
									}
								}
							}
						} 
						catch (IOException ex) 
						{
							stopWorker = true;
						}
					}
				}
			});

		workerThread.start();
	}


	
	
	
	protected void dialog()
	{
		AlertDialog.Builder build = new AlertDialog.Builder(ControlActivity.this);
		build.setTitle("Do you want to Exit");
		build.setPositiveButton("yes",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
					if (btSocket != null) 
					{
						try
						{
							btSocket.close();
						}
						catch (IOException e)
						{ 
						}
					}
					ControlActivity.this.finish();
				}
			});
		build.setNegativeButton("no",
			new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		build.create().show();
	}

	

	

	public final void msg(String s)
    { 
        Snackbar snkbr=Snackbar.make(findViewById(R.id.control_activity), s, Snackbar.LENGTH_LONG);
		snkbr.show();
    }

	
	
	@Override
	public void onResume()
	{
		super.onResume();
		/*if(adView1!=null){
		 adView1.resume();
		 }*/
	}

	
	@Override
	public void onPause()
	{
		/*if(adView1!=null){
		 adView1.pause();
		 }*/
		super.onPause();
	}



	@Override
	public void onDestroy()
	{

		/*if(adView1!=null){
		 adView1.destroy();
		 }*/
		//Disconnect();
		super.onDestroy();
	}

	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
		{
			dialog();
			return false;
		}
		return false;
	}

 
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
	{
        int id = item.getItemId();

		if ((id == android.R.id.home))
		{
			dialog();
			return true;
		}

        return super.onOptionsItemSelected(item);
    }

}
