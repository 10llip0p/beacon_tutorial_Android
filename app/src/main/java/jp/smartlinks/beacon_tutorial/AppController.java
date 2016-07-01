package jp.smartlinks.beacon_tutorial;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;

import java.util.ArrayList;
import java.util.List;

import jp.smartlinks.beacon_tutorial.Models.Logs;

/**
 * Created by hiro on 2015/12/01.
 */
public class AppController extends Application {

    private BeaconUpdateListener mListener = null ;

    //
    // Members for beacon
    //
    private boolean isBLE = true;
    private static String default_uuid =  "48534442-4C45-4144-80C0-1800FFFFFFFF";
    private List<Beacon> mBeacons = new ArrayList<>();


    public static String IBEACON_FORMAT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";
    public BeaconManager mBeaconManager;

    private String scan_uuid = null;
    private int scan_major = -1;
    private int scan_minor = -1;

    public void SetBeaconUpdateListener( BeaconUpdateListener l ) {
        this.mListener = l;
    }
    public BeaconUpdateListener GetBeaconUpdateListener() {
        return this.mListener;
    }

    public void SetUUID( String uuid ) {
        this.scan_uuid = uuid;
    }
    public String GetUUID() {
        if( this.scan_uuid == null ) {
            return this.default_uuid;
        }

        return this.scan_uuid;
    }
    public String GetDefaultUUID() {
        return this.default_uuid;
    }

    public void SetMajor( int major ) {
        this.scan_major = major;
    }
    public int GetMajor() {
        return this.scan_major;
    }
    public void SetMinor( int minor ) {
        this.scan_minor = minor;
    }
    public int GetMinor() {
        return this.scan_minor;
    }

    public void SetBeacons( List<Beacon> beacons) {
        this.mBeacons = beacons;
    }
    public List<Beacon> GetBeacons() {
        return this.mBeacons;
    }


    static double latitude = 0.0;
    static double longitude = 0.0;
    static boolean isLocation = false;

    public static double GetLatitude() { return latitude; }
    public static double GetLongitude() { return longitude; }

    public static Boolean IsLocation() { return isLocation; }

    public static void SetLocation( double lat, double lon ) {
        latitude = lat ;
        longitude = lon;
        isLocation = true;
    }

    public static LatLng GetLatLng() {
        LatLng ret = new LatLng( latitude, longitude );
        return ret;
    }


    // Log
    public static Logs LogData = new Logs();


    // SharedPreferences

    private static String KEY_APPNAME = "BeaconTutorial";
    private static String KEY_SCAN_UUID = "SCAN_UUID";
    private static String KEY_SCAN_MAJOR = "SCAN_MAJOR";
    private static String KEY_SCAN_MINOR = "SCAN_MINOR";

    public void Load(Context context){
        SharedPreferences pref = context.getSharedPreferences(KEY_APPNAME, Context.MODE_PRIVATE);

        this.scan_uuid = pref.getString(KEY_SCAN_UUID, null);
        this.scan_major = pref.getInt(KEY_SCAN_MAJOR, -1);
        this.scan_minor = pref.getInt(KEY_SCAN_MINOR, -1);
    }

    public void Save(Context context){
        SharedPreferences pref = context.getSharedPreferences(KEY_APPNAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        editor.putString(KEY_SCAN_UUID, this.scan_uuid );
        editor.putInt(KEY_SCAN_MAJOR, this.scan_major );
        editor.putInt(KEY_SCAN_MINOR, this.scan_minor);
        editor.commit();


    }
}
