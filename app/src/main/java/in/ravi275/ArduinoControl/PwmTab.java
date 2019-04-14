package in.ravi275.ArduinoControl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.*;
import android.widget.*;
import android.widget.SeekBar.*;


public class PwmTab extends Fragment implements OnSeekBarChangeListener
{



	private InterfaceF2A controlInterface;
	SeekBar p3,p5,p6,p9,p10,p11;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{


		View rootView2= inflater.inflate(R.layout.fragment_pwm, container, false);

		p3=rootView2.findViewById(R.id.p3);
		p5=rootView2.findViewById(R.id.p5);
		p6=rootView2.findViewById(R.id.p6);
		p9=rootView2.findViewById(R.id.p9);
		p10=rootView2.findViewById(R.id.p10);
		p11=rootView2.findViewById(R.id.p11);

		p3.setMax(255);
		p5.setMax(255);
		p6.setMax(255);
		p9.setMax(255);
		p10.setMax(255);
		p11.setMax(255);

		p3.setOnSeekBarChangeListener(this);
		p5.setOnSeekBarChangeListener(this);
		p6.setOnSeekBarChangeListener(this);
		p9.setOnSeekBarChangeListener(this);
		p10.setOnSeekBarChangeListener(this);
		p11.setOnSeekBarChangeListener(this);


		return rootView2;
	}



	@Override
	public void onProgressChanged(SeekBar bar, int position, boolean bool)
	{

		switch(bar.getId())
		{
			case R.id.p3:
				sendData(3,position);
				break;
			case R.id.p5:
				sendData(5,position);
				break;
			case R.id.p6:
				sendData(6,position);
				break;
			case R.id.p9:
				sendData(9,position);
				break;
			case R.id.p10:
				sendData(10,position);
				break;
			case R.id.p11:
				sendData(11,position);
				break;
			default:
				break;

		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar p1)
	{

	}

	@Override
	public void onStopTrackingTouch(SeekBar p1)
	{

	}

	public void sendData(int pin,int position){

		String str="#"+2+","+pin+","+position;
		controlInterface.blueWrite(str);
	}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            controlInterface = (InterfaceF2A) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement InterfaceF2A");
        }
    }

    @Override
    public void onDetach() {
        controlInterface = null;
        super.onDetach();
    }

}

