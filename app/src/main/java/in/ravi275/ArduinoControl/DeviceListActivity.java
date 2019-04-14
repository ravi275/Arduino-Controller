package in.ravi275.ArduinoControl ;

import java.util.Set;
import android.support.v7.app.AppCompatActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import java.util.*;
import android.support.v7.widget.*;
import android.graphics.*;
import android.view.*;


public class DeviceListActivity extends AppCompatActivity
{
    // Debugging for LOGCAT
    private static final String TAG = "DeviceListActivity";

    // EXTRA string to send on to mainactivity
    public static String DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter mBtAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onResume() 
    {
        super.onResume();
        checkBTState();

        ListView ls=(ListView)findViewById(R.id.card_listView);
        CustomListAdapter adapter;
	    ArrayList<DeviceCard> cardArrayList;
	    List<String> namesList=new ArrayList<>();
		List<String> addressList=new ArrayList<>();


        // Get a set of currently paired devices and append to 'pairedDevices'
        Set<BluetoothDevice> pairedDevices = mBtAdapter.getBondedDevices();

        // Add previosuly paired devices to the array
        if (pairedDevices.size() > 0)
		{

            for (BluetoothDevice device : pairedDevices)
			{
                namesList.add(device.getName());
                addressList.add(device.getAddress());
			}
		}
		else
		{

		}

		String[] names=new String[namesList.size()];
		namesList.toArray(names);
		String[] address=new String[addressList.size()];
		addressList.toArray(address);


		cardArrayList = new ArrayList<>();
		for (int i = 0; i < names.length; i++)
		{
			cardArrayList.add(new DeviceCard(names[i], address[i]));
		}

		adapter = new CustomListAdapter(cardArrayList, this);
		ls.setAdapter(adapter);
		ls.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id)
				{

					TextView address = view.findViewById(R.id.address);
					String mac = address.getText().toString();
				    Intent intent=new Intent(DeviceListActivity.this, ControlActivity.class);
				    intent.putExtra(DEVICE_ADDRESS, mac);
					startActivity(intent);
					finish();

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
		switch (item.getItemId())
		{
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
    

