package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.Identifier;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Recv3Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recv3Fragment extends Fragment implements BeaconConsumer, BeaconUpdateListener,LocationListener {

    public static final String TAG = Recv3Fragment.class.getSimpleName();

    private GoogleMap mGoogleMap ;
    private Activity mActivity;
    private Context mContext;
    private AppController mAppCon;

    private LocationManager mLocationManager;
    private String provider;


    private List<Beacon> mBeacons ;

    private int mInterval = 3000;
    private ArrayList<Marker> arMarker = new ArrayList<>();

    private TextView txtMsg1;
    private TextView txtMsg2;
    private TextView txtMsg3;


    // 中心点の緯度経度. 35.681391, 139.766052
    private static final LatLng TOKYO_STATION = new LatLng(35.681391, 139.766052); // 東京駅の緯度経度


    private Timer mTimer = null;
    private Handler mHandler;

    //
    // Members for AltBeacon
    //
    public BeaconManager mBeaconManager;
    private Identifier scan_uuid;
    private Identifier scan_major = null;
    private Identifier scan_minor = null;

    private Region mRegion;

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

    public Recv3Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Recv3Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Recv3Fragment newInstance() {
        Recv3Fragment fragment = new Recv3Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.mActivity = activity;
        this.mAppCon = (AppController)activity.getApplication();

        mLocationManager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE); // 低精度
        criteria.setPowerRequirement(Criteria.POWER_LOW); // 低消費電力

        provider = mLocationManager.getBestProvider(criteria, true);
        Log.d(TAG, " provider = " + provider);

//        mLocationManager.requestLocationUpdates(provider, 10000, 10, this);
//        Log.d(TAG, "mLocationManager.requestLocationUpdates Start" );


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recv3, container, false);

        txtMsg1 = (TextView)view.findViewById(R.id.msg1);
        txtMsg2 = (TextView)view.findViewById(R.id.msg2);
        txtMsg3 = (TextView)view.findViewById(R.id.msg3);

        // FragmentManager fm = getFragmentManager();
        // Fragment f = fm.findFragmentById(R.id.hogemap);

        SupportMapFragment m = ((SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map));

        this.mGoogleMap = m.getMap();

        // 地図タイプ設定
        this.mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mGoogleMap.setMyLocationEnabled(true);

        UiSettings settings = mGoogleMap.getUiSettings();
        settings.setMyLocationButtonEnabled(true);
        // クリック時のイベントハンドラ登録
        mGoogleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                // TODO Auto-generated method stub
                Toast.makeText(mActivity, "現在地に移動します", Toast.LENGTH_LONG).show();
                return false;
            }
        });

        SetMsg1("現在地；不明");
        SetMsg2("Beacon 未検出");
        SetMsg3("");


        return view;
    }

    @Override
    public void onResume(){
        super.onResume();

        // Toast.makeText(mActivity, "東京駅", Toast.LENGTH_LONG).show();

        // 表示位置を東京駅にする。
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TOKYO_STATION, 16));
        this.mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

        mLocationManager.requestLocationUpdates(provider, 10000, 10, this);
        Log.d(TAG, "mLocationManager.requestLocationUpdates Start" );


        mTimer = new Timer();
        /// ３秒後に再チェック
        mTimer.schedule(new timerA(), this.mInterval);

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

    private void SetMsg1( String msg ) {
        txtMsg1.setText( msg );
    }
    private void SetMsg2( String msg ) {
        txtMsg2.setText( msg );
    }
    private void SetMsg3( String msg ) {
        txtMsg3.setText( msg );
    }


    private void SetMaker( Beacon b ) {
        // Toast.makeText( mActivity, "" , Toast.LENGTH_LONG).show();
        this.mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mAppCon.GetLatLng(), 16));
        this.mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(16), 2000, null);

        // 地図へのマーカーの追加
        MarkerOptions mo1 = new MarkerOptions();
        mo1.position(mAppCon.GetLatLng());


        //現在日時を取得する
        Calendar c = Calendar.getInstance();
        Date d = c.getTime();

        //フォーマットパターンを指定して表示する
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
        String date = sdf.format(d);
        mo1.title(date);

        Marker mk = this.mGoogleMap.addMarker(mo1);
        arMarker.add(mk);

        String uuid = b.getId1().toString();
        int major = b.getId2().toInt();
        int minor = b.getId3().toInt();

        SetMsg2("major="+major+" minor="+minor);
        SetMsg3("更新日時 " + date );

        LatLng latlng = mAppCon.GetLatLng();

        Log.d(TAG, " SetMaker : " + latlng.latitude + ", " + latlng.longitude );

        // ログに登録する
        mAppCon.LogData.Add(uuid,major,minor,latlng.latitude,latlng.longitude,d);

    }


    private class timerA extends TimerTask {
        public timerA() {
        }

        @Override
        public void run() {

            if( mAppCon.IsLocation() == true ) {
                if( mHandler != null ) {
                    mHandler.post(new Runnable(){
                        public void run() {
                            LatLng latLng = mAppCon.GetLatLng();
                            String msg = "現在地；" + latLng.latitude + " , " + latLng.longitude;
                            SetMsg1( msg );
                        }
                    });
                }
            } else {
                if( mHandler != null ) {
                    mHandler.post(new Runnable() {
                        public void run() {
                            String msg = "現在地；不明";
                            SetMsg1( msg );
                        }
                    });
                }
            }

            mTimer = new Timer();
            /// ３秒後に再チェック
            mTimer.schedule(new timerA(), mInterval);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        Log.d(TAG, " locationManager:onLocationChanged : " + location.getLatitude() + ", " + location.getLongitude() );
        mAppCon.SetLocation( location.getLatitude(), location.getLongitude() );
    }

    @Override
    public void onProviderDisabled(String provider) {
        // TODO Auto-generated method stub
        Log.d(TAG, " locationManager:onProviderDisabled : " + provider );

    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
        Log.d(TAG, " locationManager:onProviderDisabled : " + provider );

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
        Log.d(TAG, " locationManager:onStatusChanged : " + provider + " status = " + status );

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
    public void BeaconChanged(final List<Beacon> beaconList ) {

        Log.d(TAG, " BeaconChanged() count = " + beaconList.size() );

        this.mBeacons = beaconList;
        if( beaconList.size() == 0) {
            return;
        }

        // GPSの位置測位ができていない場青は表示しない
        if( mAppCon.IsLocation() == false ) {
            return;
        }

        mHandler.post(new Runnable() {
            public void run() {
                // 最も近いビーコンでマーカを作成する
                SetMaker( beaconList.get(0));
                // mRecyclerView.setAdapter(new Recv1RecyclerViewAdapter(mBeacons, mListener));
                // Toast.makeText(mContext, "beacon count = " + mBeacons.size(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
