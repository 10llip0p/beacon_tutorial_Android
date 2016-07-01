package jp.smartlinks.beacon_tutorial.Models;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by hiro on 2016/02/12.
 */
public class Logs {


    private ArrayList<Log> arLog = new ArrayList<Log>();

    public void Clear() {
        arLog.clear();
    }

    public void Add(String UUID, int major, int minor, double lat, double lon, Date d ) {

        Log l = new Log( UUID,major,minor,lat,lon,d);
        arLog.add( l );
    }

    public ArrayList<Log> GetLogDatas() {
        return this.arLog;
    }

    public String CreateCSV() {
        StringBuilder sb = new StringBuilder();

        SimpleDateFormat sdfIso8601ExtendedFormatUtc = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        sdfIso8601ExtendedFormatUtc.setTimeZone(TimeZone.getTimeZone("UTC"));


        // CSV出力
        for( Log l : arLog ) {
            String strDate = sdfIso8601ExtendedFormatUtc.format(l.GetDate());

            sb.append(strDate + ",");
            sb.append(l.GetUUID().toString() + ",");
            sb.append(l.GetMajor() + ",");
            sb.append(l.GetMinor() + ",");
            sb.append(l.GetLat() + ",");
            sb.append(l.GetLon() + "\n");
        }

        return sb.toString();
    }
}
