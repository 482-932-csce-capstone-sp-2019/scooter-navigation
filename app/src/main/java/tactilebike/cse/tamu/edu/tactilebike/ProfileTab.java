package tactilebike.cse.tamu.edu.tactilebike;

import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileTab extends Fragment {
    private OnFragmentInteractionListener mListener;

    public ProfileTab() {
        // Required empty public constructor
    }

    public static ProfileTab newInstance() {
        ProfileTab fragment = new ProfileTab();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
        return inflater.inflate(R.layout.fragment_profile_tab, container, false);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
