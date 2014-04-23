package com.wfchat.ui;

import java.util.List;
import com.wfchat.data.Friend;
import com.wfchat.ui.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FriendsListAdapter<T> extends ArrayAdapter<T> {
    private List<T> friends;
    int mListItemLayoutResId;
    
    public FriendsListAdapter(Context context, int textViewResourceId, List<T> friends) {
    	super(context, textViewResourceId, friends);
    	this.friends = friends;
    	mListItemLayoutResId = textViewResourceId;
    }
    
    public int getCount() {
        return friends.size();
    }

    public T getItem(int position) {
        return friends.get(position);
    }

    public long getItemId(int position) {
        return ((com.wfchat.data.Friend) friends.get(position)).getFid();
    }
    


	public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        SpaceHolder holder;

        if (null == convertView) { 
            convertView = inflater.inflate(mListItemLayoutResId, parent, false);
            holder = new SpaceHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
        	holder = (SpaceHolder) convertView.getTag();
        }
        

        Friend f = (Friend)getItem(position);
        holder.name.setText(f.getName());
        holder.status.setText(f.getStatus());
        
        return convertView;
    }

    static class SpaceHolder {
    	TextView name;
    	TextView status;
    }
}

