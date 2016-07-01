package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingDescFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingDescFragment extends Fragment {

    public static final String TAG = SettingDescFragment.class.getSimpleName();

    private Activity mActivity;
    private AppController mAppCon;
    private Context mContext;


    private TextView txtUUID;
    private TextView txtMajor;
    private TextView txtMinor;

    private Handler mHandler;


    public SettingDescFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingDescFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingDescFragment newInstance() {
        SettingDescFragment fragment = new SettingDescFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mActivity = getActivity();
        this.mAppCon = (AppController) mActivity.getApplication();
        this.mContext = this.mActivity.getBaseContext();

        mHandler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_setting_desc, container, false);

        txtUUID = (TextView)v.findViewById(R.id.txtUUID);
        txtMajor = (TextView)v.findViewById(R.id.txtMajor);
        txtMinor = (TextView)v.findViewById(R.id.txtMinor);
        refresh();

        Button btnChangeUUID = (Button)v.findViewById(R.id.btnChangeUUID);
        btnChangeUUID.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, InputUUIDFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button btnChangeMajor = (Button)v.findViewById(R.id.btnChangeMajor);
        btnChangeMajor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, InputMajorFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button btnChangeMinor = (Button)v.findViewById(R.id.btnChangeMinor);
        btnChangeMinor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, InputMinorFragment.newInstance())
                        .addToBackStack(null)
                        .commit();
            }
        });

        Button btnDefault = (Button)v.findViewById(R.id.btnDefault);
        btnDefault.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mAppCon.SetUUID(null);
                mAppCon.SetMajor(-1);
                mAppCon.SetMinor(-1);
                mAppCon.Save(mContext);


                if( mHandler != null ) {
                    mHandler.post(new Runnable() {
                        public void run() {
                            refresh();
                        }
                    });
                }
            }
        });

        return v;
    }



    private void refresh() {
        txtUUID.setText( "UUID="+ mAppCon.GetUUID() );

        int major = mAppCon.GetMajor();
        if( major != -1 ) {
            txtMajor.setText( "Major="+ major );
        } else {
            txtMajor.setText( "Major=未指定");
        }

        int minor = mAppCon.GetMinor();
        if( minor != -1 ) {
            txtMinor.setText( "Minor="+ minor );
        } else {
            txtMinor.setText( "Minor=未指定");
        }

    }

}
