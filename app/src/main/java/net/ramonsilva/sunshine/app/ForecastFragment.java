package net.ramonsilva.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import net.ramonsilva.sunshine.app.data.FetchWeatherTask;
import net.ramonsilva.sunshine.app.data.ForecastAdapter;
import net.ramonsilva.sunshine.app.data.WeatherContract;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment {
    public static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private ForecastAdapter mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateForecast();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_refresh){
            updateForecast();
            return true;
        }

        if (id == R.id.action_settings) {
            updateForecast();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateForecast(){
        final FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        final String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final String locationSetting = Utility.getPreferredLocation(getActivity());
        final String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC" ;

        final Uri weatherForLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSetting, System.currentTimeMillis());

        final Cursor cur = getActivity().getContentResolver().query(
                weatherForLocationUri,
                null,
                null,
                null,
                sortOrder);

        mForecastAdapter = new ForecastAdapter(getActivity(), cur, 0);

        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);

        return rootView;
    }





}
