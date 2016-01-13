package net.ramonsilva.sunshine.app.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import net.ramonsilva.sunshine.app.ForecastFragment;
import net.ramonsilva.sunshine.app.R;
import net.ramonsilva.sunshine.app.Utility;

/**
 * Created by ramonsilva on 08/01/16.
 */
public class ForecastAdapter extends CursorAdapter {

    private Context mContext;

    private static final int VIEW_TYPE_TODAY  = 0;
    private static final int VIEW_TYPE_FUTURE_DAY  = 1;

    public ForecastAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        final int viewType = getItemViewType(cursor.getPosition());
        int layoutId = -1;

        if(VIEW_TYPE_TODAY == viewType){
            layoutId = R.layout.list_item_forescast_today;
        } else {
            layoutId = R.layout.list_item_forecast;
        }

        final View view = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        return view;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? VIEW_TYPE_TODAY : VIEW_TYPE_FUTURE_DAY;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final int weatherId = cursor.getInt(ForecastFragment.COL_WEATHER_ID);
        final ImageView iconView = (ImageView) view.findViewById(R.id.list_item_icon);
        iconView.setImageResource(R.mipmap.ic_launcher);

        final String forecast = cursor.getString(ForecastFragment.COL_WEATHER_DESC);
        final TextView forecastView = (TextView) view.findViewById(R.id.list_item_forecast_textview);
        forecastView.setText(forecast);

        final String date = Utility.getFriendlyDayString(context, cursor.getLong(ForecastFragment.COL_WEATHER_DATE));
        final TextView dateView = (TextView) view.findViewById(R.id.list_item_date_textview);
        dateView.setText(date);


        final boolean isMetric = Utility.isMetric(context);

        final double high = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        final TextView highView = (TextView) view.findViewById(R.id.list_item_high_textview);
        highView.setText(Utility.formatTemperature(high, isMetric));

        final double low = cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP);
        final TextView lowView = (TextView) view.findViewById(R.id.list_item_low_textview);
        lowView.setText(Utility.formatTemperature(low, isMetric));


    }


    private String formatHighLows(double high, double low) {
        final boolean isMetric = Utility.isMetric(mContext);
        String highLowStr = Utility.formatTemperature(high, isMetric) + "/" + Utility.formatTemperature(low, isMetric);
        return highLowStr;
    }

    private String convertCursorRowToUXFormat(Cursor cursor) {

        final String highAndLow = formatHighLows(
                cursor.getDouble(ForecastFragment.COL_WEATHER_MAX_TEMP),
                cursor.getDouble(ForecastFragment.COL_WEATHER_MIN_TEMP));

        return Utility.formatDate(cursor.getLong(ForecastFragment.COL_WEATHER_DATE)) +
                " - " + cursor.getString(ForecastFragment.COL_WEATHER_DESC) +
                " - " + highAndLow;
    }
}
