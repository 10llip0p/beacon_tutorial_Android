package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Receive1DescFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Receive1DescFragment extends Fragment {

    public static final String TAG = Receive1DescFragment.class.getSimpleName();

    private Activity mActivity;
    private AppController mAppCon;

    public Receive1DescFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Receive1DescFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Receive1DescFragment newInstance() {
        Receive1DescFragment fragment = new Receive1DescFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = getActivity();
        this.mAppCon = (AppController) mActivity.getApplication();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_receive1_desc, container, false);

        ImageButton btnExec = (ImageButton)v.findViewById(R.id.btnExecute);
        btnExec.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, Recv1Fragment.newInstance())
                        .addToBackStack(null)
                        .commit();

            }
        });

        TextView txtUUID = (TextView)v.findViewById(R.id.txtUUID);
        txtUUID.setText( "UUID="+ mAppCon.GetUUID() );

        TextView txtMajor = (TextView)v.findViewById(R.id.txtMajor);
        int major = mAppCon.GetMajor();
        if( major != -1 ) {
            txtMajor.setText( "Major="+ major );
        } else {
            txtMajor.setText( "Major=未指定");
        }

        TextView txtMinor = (TextView)v.findViewById(R.id.txtMinor);
        int minor = mAppCon.GetMinor();
        if( minor != -1 ) {
            txtMinor.setText( "Minor="+ minor );
        } else {
            txtMinor.setText( "Minor=未指定");
        }

        return v;
    }

}
