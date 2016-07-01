package jp.smartlinks.beacon_tutorial.Models;

import java.util.Date;

/**
 * Created by hiro on 2016/02/14.
 */
public class Log {
    private String UUID;
    private int major;
    private int minor;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Date date;

    public Log( String UUID, int major, int minor, double lat, double lon, Date d ) {
        this.UUID = UUID;
        this.major = major;
        this.minor = minor;
        this.latitude = lat;
        this.longitude = lon;
        this.date = d;
    }

    public String GetUUID() {
        return this.UUID;
    }
    public int GetMajor() {
        return this.major;
    }
    public int GetMinor() {
        return this.minor;
    }
    public double GetLat() {
        return this.latitude;
    }
    public double GetLon() {
        return this.longitude;
    }
    public Date GetDate() {
        return this.date;
    }
}
