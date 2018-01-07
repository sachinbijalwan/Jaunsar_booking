package thenightswatch.jaunsar_booking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachin on 23/12/17.
 */

public class CustomArrayAdapter extends ArrayAdapter<element> {
    List<element> users;
    public CustomArrayAdapter(Context context, List<element> Users) {
        super(context, 0, Users);
        users=Users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        element user = users.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_inflator, parent, false);
        }
        // Lookup view for data population
        TextView first = convertView.findViewById(R.id.time_start);
        TextView second = convertView.findViewById(R.id.time_end);
        TextView third = convertView.findViewById(R.id.place_start);
        TextView fourth = convertView.findViewById(R.id.place_end);

        // Populate the data into the template view using the data object
        String a=user.first;
        String b=user.second;
        first.setText(a);
        second.setText(b);
        third.setText(user.third);
        fourth.setText(user.fourth);
        // Return the completed view to render on screen
        return convertView;
    }
}
