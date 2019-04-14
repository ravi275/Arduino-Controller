package in.ravi275.ArduinoControl;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import java.util.Locale;
import java.util.ArrayList;

public class CustomListAdapter  extends BaseAdapter
{

    private List<DeviceCard> cardList;
	Context context;

    static class CardViewHolder
	{
        TextView name;
        TextView address;
    }

    public CustomListAdapter(List<DeviceCard> cardlist, Context context)
	{
		this.context = context;
		this.cardList = cardlist;
    }

    @Override
    public int getCount()
	{
        return this.cardList.size();
    }


	@Override
	public long getItemId(int p1)
	{
		return p1;
	}


    @Override
    public DeviceCard getItem(int index)
	{
        return this.cardList.get(index);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
	{
        View row = convertView;
        CardViewHolder viewHolder;
        if (row == null)
		{
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.card, parent, false);
            viewHolder = new CardViewHolder();
            viewHolder.name = (TextView) row.findViewById(R.id.name);
            viewHolder.address = (TextView) row.findViewById(R.id.address);
            row.setTag(viewHolder);
        }
		else
		{
            viewHolder = (CardViewHolder)row.getTag();
        }
        DeviceCard card = getItem(position);
        viewHolder.name.setText(card.getName());
        viewHolder.address.setText(card.getAddress());
        return row;
    }
}
	
	
