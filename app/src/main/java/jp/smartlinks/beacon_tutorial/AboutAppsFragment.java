package jp.smartlinks.beacon_tutorial;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AboutAppsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AboutAppsFragment extends Fragment {
    public static final String TAG = AboutAppsFragment.class.getSimpleName();


    public AboutAppsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AboutAppsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutAppsFragment newInstance() {
        AboutAppsFragment fragment = new AboutAppsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_about_apps, container, false);

        return v;
    }

}
