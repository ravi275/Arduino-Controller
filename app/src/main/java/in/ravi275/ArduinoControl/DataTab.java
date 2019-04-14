package in.ravi275.ArduinoControl;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.*;
import android.view.View.OnClickListener;
import android.os.Handler;
import android.view.*;
import android.database.DataSetObserver;
import android.speech.RecognizerIntent;
import java.util.Locale;
import java.util.ArrayList;
import android.content.ActivityNotFoundException;
import android.content.Context;


public class DataTab extends Fragment
{
	private InterfaceF2A controlInterface;
	private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private ImageView buttonSend,buttonVoice;
	
	private static final int REQ_CODE_SPEECH_INPUT = 100;
	//variables to save and restore listview items on destroy of fragment
	private ChatArrayAdapter backupAdapter;
	private boolean listRestoreFlag=false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		View rootView3 = inflater.inflate(R.layout.fragment_data, container, false);
	    buttonSend = rootView3.findViewById(R.id.send);
		buttonVoice=rootView3.findViewById(R.id.voice);
        listView = rootView3.findViewById(R.id.msgview);
        chatArrayAdapter = new ChatArrayAdapter(getActivity().getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = rootView3.findViewById(R.id.msg);
		
        chatText.setOnKeyListener(new View.OnKeyListener() {
				public boolean onKey(View v, int keyCode, KeyEvent event) {
					if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
						return sendChatMessage();
					}
					return false;
				}
			});
			
        buttonSend.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View arg0) {
					sendChatMessage();
				}
			});

			
		buttonVoice.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View arg){
				startVoiceInput();
			}
		});
		
        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		if(listRestoreFlag){
			chatArrayAdapter=backupAdapter;
		}
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
				@Override
				public void onChanged() {
					super.onChanged();
					listView.setSelection(chatArrayAdapter.getCount() - 1);
				}
			});

		return rootView3;
	}
	
	
	private boolean sendChatMessage() {
		
		String sendString=chatText.getText().toString();
		if(sendString!=""){
        	chatArrayAdapter.add(new ChatMessage(true,sendString));
			controlInterface.blueWrite("#3"+sendString+"*");
        	chatText.setText("");
		}
		
		return true;
    }



	public void recieveMessage(String s){	
		chatArrayAdapter.add(new ChatMessage(false,s));
	}

	@Override
	public void onDestroyView()
	{
		
		super.onDestroyView();
		backupAdapter=chatArrayAdapter;
		listRestoreFlag=true;
	}
	
	private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Listening...");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {

        }
    }
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
					if (resultCode == getActivity().RESULT_OK && null != data) {
						ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
						chatText.setText(result.get(0));
						sendChatMessage();
					}
					break;
				}

        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            controlInterface = (InterfaceF2A) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
										 + " must implement IFragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        controlInterface = null;
        super.onDetach();
    }
}

