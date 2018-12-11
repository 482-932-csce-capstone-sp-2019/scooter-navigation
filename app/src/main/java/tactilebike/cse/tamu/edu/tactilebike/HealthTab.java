package tactilebike.cse.tamu.edu.tactilebike;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;

public class HealthTab extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private OnFragmentInteractionListener mListener;

    Date start, end;

    public HealthTab() {
        // Required empty public constructor
    }

    public static HealthTab newInstance() {
        HealthTab fragment = new HealthTab();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        View view = inflater.inflate(R.layout.fragment_health_tab, container, false);
        TextView label = view.findViewById(R.id.miles_traveled_label);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        label.setText(String.valueOf(sharedPreferences.getFloat("meters",0)*0.000621371));
        return view;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("meters"))
        {
            TextView label = this.getView().findViewById(R.id.miles_traveled_label);
            label.setText(String.valueOf(sharedPreferences.getFloat("meters",0)));
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
