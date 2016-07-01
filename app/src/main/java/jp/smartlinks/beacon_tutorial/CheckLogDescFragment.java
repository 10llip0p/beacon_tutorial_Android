package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CheckLogDescFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CheckLogDescFragment extends Fragment {

    public static final String TAG = CheckLogDescFragment.class.getSimpleName();

    private AppController mAppCon;
    private Activity mActivity;
    private Context mContext;

    private Handler mHandler;


    public CheckLogDescFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CheckLogDescFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckLogDescFragment newInstance() {
        CheckLogDescFragment fragment = new CheckLogDescFragment();
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
        View v = inflater.inflate(R.layout.fragment_check_log_desc, container, false);

        ImageButton btnCheckLog = (ImageButton)v.findViewById(R.id.btnCheckLog);
        btnCheckLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, CheckLogFragment.newInstance())
                        .addToBackStack(null)
                        .commit();

            }
        });

        ImageButton btnClearLog = (ImageButton)v.findViewById(R.id.btnClearLog);
        btnClearLog.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mAppCon.LogData.Clear();

                mHandler.post(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Beacon入門")
                                .setMessage("ログを消去しました。")
                                .setPositiveButton("OK", null)
                                .show();
                    }
                });


            }
        });

        return v;

    }


}
