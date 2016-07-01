package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

import jp.smartlinks.beacon_tutorial.Adapters.BeaconListAdapter;
import jp.smartlinks.beacon_tutorial.Models.ListViewContainer;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recv2Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recv2Fragment extends ListFragment implements BeaconConsumer, BeaconUpdateListener {
    public static final String TAG = Recv2Fragment.class.getSimpleName();

    private AppController mAppCon;
    private Activity mActivity;
    private Context mContext;

    // 表示用のコンテナ
    private BeaconListAdapter adapter;
    private ArrayList<ListViewContainer> mContainer = new ArrayList<ListViewContainer>();

    //
    // Members for AltBeacon
    //
    public BeaconManager mBeaconManager;
    private Identifier scan_uuid;
    private Identifier scan_major = null;
    private Identifier scan_minor = null;

    private Region mRegion;

    private Handler mHandler;

    // iBeacon の Ranging 中
    private Boolean isBeaconRanging = false;

    public void SetStartRanging() {
        Log.d(TAG, "Beacon Status Change :SetStartRanging()");
        isBeaconRanging = true;
    }
    public void SetStopRanging() {
        Log.d(TAG, "Beacon Status Change :SetStopRanging()");
        isBeaconRanging = false;
    }

    public Boolean IsBeaconRanging() {
        Log.d(TAG, " IsBeaconRanging() isBeaconRanging " + isBeaconRanging );
        return this.isBeaconRanging;
    }

    public Recv2Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Recv2Fragment.
     */
    public static Recv2Fragment newInstance() {
        Recv2Fragment fragment = new Recv2Fragment();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();

        adapter = new BeaconListAdapter( getActivity(), mContainer );
        setListAdapter(adapter);

        updateContainer( null );
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

        // リスナをセットする。
        this.mAppCon.SetBeaconUpdateListener(this);
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

        if( IsBeaconRanging() == true ) {
            StopRanging();
        }

        this.mBeaconManager.unbind(this); // サービスの停止
    }


    private void updateContainer( List<Beacon> beaconList ) {
        mContainer.clear();

        ListViewContainer title = new ListViewContainer(ListViewContainer.TYPE_TITLE, 0, "検出したビーコン" );
        mContainer.add(title);

        if( beaconList != null ) {
            for( Beacon b : beaconList ) {

                ListViewContainer item = new ListViewContainer(ListViewContainer.TYPE_BEACON, 0, b );
                mContainer.add(item);
            }
        }


        Log.d(TAG, " createContainer() notifyDataSetChanged!!");
        adapter.notifyDataSetChanged();
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

                // ビーコン領域に侵入したので、距離測定を開始する
                StartRanging();
            }

            @Override
            public void didExitRegion(Region region) {
                // 領域退出時に実行
                Log.d(TAG, "didExitRegion");

                // ビーコン領域から出たので距離測定を停止する
                StopRanging();
            }

            @Override
            public void didDetermineStateForRegion(final int status, final Region region) {
                // 領域への侵入/退出のステータスが変化したときに実行
                Log.d(TAG, "didDetermineStateForRegion (" + status + ")" + " Region = " + region);

                if( status == 1 && IsBeaconRanging() == false ) {
                    // ビーコン領域に侵入したので、距離測定を開始する
                    StartRanging();
                }
            }
        });

        // BeaconManagerクラスのレンジング設定
        mAppCon.mBeaconManager.setRangeNotifier(new RangeNotifier() {
            @Override
            public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {


                List<Beacon> bl = new ArrayList<Beacon>();

                // 検出したビーコンの情報を全部Logに書き出す
                for (Beacon beacon : beacons) {
                    Log.d(TAG, "didRangeBeaconsInRegion UUID:" + beacon.getId1() + ", major:" + beacon.getId2() + ", minor:" + beacon.getId3() + ", Distance:" + beacon.getDistance() + ",RSSI" + beacon.getRssi() + ", TxPower" + beacon.getTxPower());

                    bl.add(beacon);
                }

                BeaconUpdateListener listner = mAppCon.GetBeaconUpdateListener();
                if (listner != null) {
                    listner.BeaconChanged(bl);
                }
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

    private void StartRanging() {
        Log.d(TAG, " StartRanging()");
        try {
            // レンジングの開始
            mAppCon.mBeaconManager.startRangingBeaconsInRegion(mRegion);
            SetStartRanging();
        } catch (RemoteException e) {
            // 例外が発生した場合
            e.printStackTrace();
        }
    }

    private void StopRanging() {
        Log.d(TAG, " StopRanging()");

        try {
            // レンジングの停止
            mAppCon.mBeaconManager.stopRangingBeaconsInRegion(mRegion);
            SetStopRanging();
        } catch (RemoteException e) {
            // 例外が発生した場合
            e.printStackTrace();
        }
    }

    @Override
    public void BeaconChanged( final List<Beacon> beaconList ) {
        Log.d(TAG, " BeaconChanged() count = " + beaconList.size() );

        mHandler.post(new Runnable() {
            public void run() {
                updateContainer( beaconList );
            }
        });

    }

}
