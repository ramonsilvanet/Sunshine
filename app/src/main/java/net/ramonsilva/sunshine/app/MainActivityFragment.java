package net.ramonsilva.sunshine.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {



    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        String [] weekForecasts = {
            "Today - Sunny - 88/63",
            "Tomorrow - Sunny - 89/69",
            "Friday - Cloudy - 78/91",
            "Saturday - Rainy - 64/51",
            "Sunday - Foggy - 70/46",
            "Monday - Sunny - 70/68" };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                        R.layout.list_item_forecast,
                                        R.id.list_item_forecast_textview,
                                        weekForecasts);

        ListView forecastListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(adapter);

        return rootView;
    }
}
