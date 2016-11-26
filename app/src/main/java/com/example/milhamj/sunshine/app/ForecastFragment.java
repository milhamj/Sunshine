package com.example.milhamj.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.ArrayList;

import static com.example.milhamj.sunshine.app.BuildConfig.OPEN_WEATHER_MAP_API_KEY;
import static com.example.milhamj.sunshine.app.R.menu.detail;

/**
 * Created by M.Ilham on 11/19/2016.
 */

public class ForecastFragment extends Fragment {
    ArrayAdapter<String> forecastAdapter;

    public ForecastFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if(id == R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        forecastAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                new ArrayList<String>()
        );

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView forecastListView = (ListView) rootView.findViewById(R.id.listview_forecast);
        forecastListView.setAdapter(forecastAdapter);

        forecastListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String forecast = forecastAdapter.getItem(i);
//                Context context = getActivity();
//                int duration = Toast.LENGTH_SHORT;
//
//                Toast toast = Toast.makeText(context, forecast, duration);
//                toast.show();

                Intent detailIntent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(detailIntent);
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    public void updateWeather() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String location = preferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));

        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), forecastAdapter);
        weatherTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }
}