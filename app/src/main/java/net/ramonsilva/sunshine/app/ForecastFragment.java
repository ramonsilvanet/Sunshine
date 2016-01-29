package net.ramonsilva.sunshine.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import net.ramonsilva.sunshine.app.data.FetchWeatherTask;
import net.ramonsilva.sunshine.app.data.ForecastAdapter;
import net.ramonsilva.sunshine.app.data.WeatherContract;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;

import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ForecastFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = ForecastFragment.class.getSimpleName();

    private static final int FORECAST_CURSOR_LOADER = 0;

    private ForecastAdapter mForecastAdapter;

    private int mScrollPosition = ListView.INVALID_POSITION;

    private static String SCROLL_POSITION = "scrollPosition";

    private ListView mListView;

    private boolean mUseTodayLayout;

    private static final String[] FORECAST_COLUMNS = {
            WeatherContract.WeatherEntry.TABLE_NAME + "." + WeatherContract.WeatherEntry._ID,
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_SHORT_DESC,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.LocationEntry.COLUMN_LOCATION_SETTING,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.LocationEntry.COLUMN_COORD_LAT,
            WeatherContract.LocationEntry.COLUMN_COORD_LONG
    };


    public static final int COL_WEATHER_ID = 0;
    public static final int COL_WEATHER_DATE = 1;
    public static final int COL_WEATHER_DESC = 2;
    public static final int COL_WEATHER_MAX_TEMP = 3;
    public static final int COL_WEATHER_MIN_TEMP = 4;
    public static final int COL_LOCATION_SETTING = 5;
    public static final int COL_WEATHER_CONDITION_ID = 6;
    public static final int COL_COORD_LAT = 7;
    public static final int COL_COORD_LONG = 8;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Log.d(LOG_TAG, "onCreate");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        if(ListView.INVALID_POSITION != mScrollPosition) {
            savedInstanceState.putInt(SCROLL_POSITION, mScrollPosition);
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_refresh){
            updateWeather();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLocationChanged(){
        updateWeather();
        getLoaderManager().restartLoader(FORECAST_CURSOR_LOADER, null, this);
    }


    private void updateWeather(){
        Log.d(LOG_TAG, "updating Forecast");
        final FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity());
        final String location = Utility.getPreferredLocation(getActivity());
        weatherTask.execute(location);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(FORECAST_CURSOR_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mForecastAdapter = new ForecastAdapter(getActivity(), null, 0);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mListView= (ListView) rootView.findViewById(R.id.listview_forecast);
        mListView.setAdapter(mForecastAdapter);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                final Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                mScrollPosition = position;

                if(cursor != null){
                    String locationSetting = Utility.getPreferredLocation(getActivity());
                    ((Callback) getActivity())
                            .onItemSelected(WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
                                    locationSetting, cursor.getLong(COL_WEATHER_DATE)
                            ));
                }
            }

        });

        if(savedInstanceState != null && savedInstanceState.containsKey(SCROLL_POSITION)){
            mScrollPosition = savedInstanceState.getInt(SCROLL_POSITION);
        }

        mForecastAdapter.setUseTodayLayout(mUseTodayLayout);

        return rootView;
    }


    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        final String locationSettings  = Utility.getPreferredLocation(getActivity());

        //Sort order: ascending, by date
        final String sortOrder = WeatherContract.WeatherEntry.COLUMN_DATE + " ASC";
        final Uri weatherLocationUri = WeatherContract.WeatherEntry.buildWeatherLocationWithStartDate(
                locationSettings,
                System.currentTimeMillis()
        );

        return new CursorLoader(
                getActivity(),
                weatherLocationUri,
                FORECAST_COLUMNS,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        mForecastAdapter.swapCursor(cursor);
        
        if(mScrollPosition != ListView.INVALID_POSITION) {
            mListView.setSelection(mScrollPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        mForecastAdapter.swapCursor(null);
    }


    public interface Callback{

        void onItemSelected(Uri dateUri);
    }

    public void setUseTodayLayout(boolean useTodayLayout) {
        mUseTodayLayout = useTodayLayout;
        if (mForecastAdapter != null) {
            mForecastAdapter.setUseTodayLayout(mUseTodayLayout);
        }
    }
}
