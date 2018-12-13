package tactilebike.cse.tamu.edu.tactilebike;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingsTab extends Fragment {
    private OnFragmentInteractionListener mListener;

    public SettingsTab() {
        // Required empty public constructor
    }

    public static SettingsTab newInstance() {
        SettingsTab fragment = new SettingsTab();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_tab, container, false);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
