package in.ravi275.ArduinoControl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SwitchCompat;
import android.content.Context;
import android.widget.CompoundButton.*;
import android.widget.CompoundButton;


public class DigitalTab extends Fragment implements OnCheckedChangeListener
{

	private InterfaceF2A controlInterface;
	SwitchCompat d2,d3,d4,d5,d6,d7,d8,d9,d10,d11,d12,d13;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View rootView1=inflater.inflate(R.layout.fragment_digital, container, false);
		d2=rootView1.findViewById(R.id.d2);
		d3=rootView1.findViewById(R.id.d3);
		d4=rootView1.findViewById(R.id.d4);
		d5=rootView1.findViewById(R.id.d5);
		d6=rootView1.findViewById(R.id.d6);
		d7=rootView1.findViewById(R.id.d7);
		d8=rootView1.findViewById(R.id.d8);
		d9=rootView1.findViewById(R.id.d9);
		d10=rootView1.findViewById(R.id.d10);
		d11=rootView1.findViewById(R.id.d11);
		d12=rootView1.findViewById(R.id.d12);
		d13=rootView1.findViewById(R.id.d13);



		d2.setOnCheckedChangeListener(this);
		d3.setOnCheckedChangeListener(this);
		d4.setOnCheckedChangeListener(this);
		d5.setOnCheckedChangeListener(this);
		d6.setOnCheckedChangeListener(this);
		d7.setOnCheckedChangeListener(this);
		d8.setOnCheckedChangeListener(this);
		d9.setOnCheckedChangeListener(this);
		d10.setOnCheckedChangeListener(this);
		d11.setOnCheckedChangeListener(this);
		d12.setOnCheckedChangeListener(this);
		d13.setOnCheckedChangeListener(this);


		return rootView1;
	}

	@Override
	public void onCheckedChanged(CompoundButton switcher, boolean state)
	{
		switch(switcher.getId()){

			case R.id.d2:
				sendData(2,state);
				break;
			case R.id.d3:
				sendData(3,state);
				break;
			case R.id.d4:
				sendData(4,state);
				break;
			case R.id.d5:
				sendData(5,state);
				break;
			case R.id.d6:
				sendData(6,state);
				break;
			case R.id.d7:
				sendData(7,state);
				break;
			case R.id.d8:
				sendData(8,state);
				break;
			case R.id.d9:
				sendData(9,state);
				break;
			case R.id.d10:
				sendData(10,state);
				break;
			case R.id.d11:
				sendData(11,state);
				break;
			case R.id.d12:
				sendData(12,state);
				break;
			case R.id.d13:
				sendData(13,state);
				break;

		}
	}

	public void sendData(int pin ,Boolean state){

		short out;
		if(state){out=1;}
		else{out=0;}
		String s="#"+1+","+pin+","+out;
		controlInterface.blueWrite(s);

	}


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            controlInterface = (InterfaceF2A) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+" must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        controlInterface = null;
        super.onDetach();
    }

}

