package jp.smartlinks.beacon_tutorial;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 */
public class Recv1Fragment extends Fragment implements BeaconConsumer {

    public static final String TAG = Recv1Fragment.class.getSimpleName();

    private AppController mAppCon;
    private Activity mActivity;
    private Context mContext;

    //
    // Members for AltBeacon
    //
    public BeaconManager mBeaconManager;
    private Identifier scan_uuid;
    private Identifier scan_major = null;
    private Identifier scan_minor = null;

    private Region mRegion;

    private Handler mHandler;

    private TextView txtStaus;
    private TextView txtUUID;
    private TextView txtMajor;
    private TextView txtMinor;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public Recv1Fragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static Recv1Fragment newInstance() {
        Recv1Fragment fragment = new Recv1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recv1, container, false);

        txtStaus = (TextView)view.findViewById(R.id.txtStatus);
        txtUUID = (TextView)view.findViewById(R.id.txtUUID);
        txtMajor = (TextView)view.findViewById(R.id.txtMajor);
        txtMinor = (TextView)view.findViewById(R.id.txtMinor);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.mActivity = activity;
        this.mAppCon = (AppController) activity.getApplication();
        this.mContext = this.mActivity.getBaseContext();
        this.mBeaconManager = this.mAppCon.mBeaconManager;

        scan_uuid = Identifier.parse(mAppCon.GetUUID());

        if( mAppCon.GetMajor() != -1 ) {
            scan_major = Identifier.parse( Integer.toString(mAppCon.GetMajor()) );
        } else {
            scan_major = null;
        }

        if( mAppCon.GetMinor() != -1 ) {
            scan_minor = Identifier.parse( Integer.toString(mAppCon.GetMinor()) );
        } else {
            scan_minor = null;
        }


        mRegion = new Region("townbeacon", scan_uuid, scan_major, scan_minor);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume this.mBeaconManager#bind");

        this.mBeaconManager.bind(this); // サービスの開始

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(TAG, "onResume this.mBeaconManager#unbind");

        this.mBeaconManager.unbind(this); // サービスの停止
    }


    private void setTxtStatus( int status, Region region ) {

        if( status == 0 ) {
            txtStaus.setText( "STATUS=OUTSIDE");
            txtStaus.setTextColor(Color.GREEN);

            txtUUID.setText( "UUID=" + region.getId1() );
            txtMajor.setText( "MAJOR=" + region.getId2() );
            txtMinor.setText( "MINOR=" + region.getId3() );
        } else if ( status == 1 ) {
            txtStaus.setText( "STATUS=INSIDE");
            txtStaus.setTextColor(Color.MAGENTA);

            txtUUID.setText( "UUID=" + region.getId1() );
            txtMajor.setText( "MAJOR=" + region.getId2() );
            txtMinor.setText( "MINOR=" + region.getId3() );
        } else {
            txtStaus.setText( "STATUS=UNKNOWN");
            txtStaus.setTextColor(Color.BLACK);

            txtUUID.setText( "UUID=" );
            txtMajor.setText( "MAJOR=" );
            txtMinor.setText( "MINOR=" );
        }
    }


    // for AltBeacon
    @Override
    public Context getApplicationContext() {
        return mContext.getApplicationContext();
    }
    @Override
    public boolean bindService(Intent intent, ServiceConnection connection, int mode) {
        return mContext.bindService(intent, connection, mode);
    }

    @Override
    public void unbindService(ServiceConnection connection) {
        mContext.unbindService(connection);
    }
    @Override
    public void onBeaconServiceConnect() {
        // BeaconManagerクラスのモニタリング通知受取り処理
        mAppCon.mBeaconManager.setMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                // 領域進入時に実行
                Log.d(TAG, "didEnterRegion");

                mHandler.post(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Beacon入門")
                                .setMessage("ビーコン領域に入りました。")
                                .setPositiveButton("OK", null)
                                .show();

                        // Toast.makeText(mActivity, "ビーコン領域に入りました。", Toast.LENGTH_SHORT).show();
                    }
                });

            }

            @Override
            public void didExitRegion(Region region) {
                // 領域退出時に実行
                Log.d(TAG, "didExitRegion");

                mHandler.post(new Runnable() {
                    public void run() {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Beacon入門")
                                .setMessage("ビーコン領域から外に出ました。")
                                .setPositiveButton("OK", null)
                                .show();

                        // Toast.makeText(mActivity, "ビーコン領域から外に出ました。", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void didDetermineStateForRegion(final int status, final Region region) {
                // 領域への侵入/退出のステータスが変化したときに実行
                Log.d(TAG, "didDetermineStateForRegion (" + status + ")" + " Region = " + region);

                mHandler.post(new Runnable() {
                    public void run() {
                        setTxtStatus(status, region);

//                        Toast.makeText(mActivity, "ステータスが変化しました", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        try {
            // ビーコン情報の監視を開始
            Log.d(TAG, "mBeaconManager.startMonitoringBeaconsInRegion");

            mBeaconManager.startMonitoringBeaconsInRegion(mRegion);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


}
