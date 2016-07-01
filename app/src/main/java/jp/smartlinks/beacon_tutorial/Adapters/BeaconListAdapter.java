package jp.smartlinks.beacon_tutorial.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.altbeacon.beacon.Beacon;

import java.text.SimpleDateFormat;
import java.util.List;

import jp.smartlinks.beacon_tutorial.Models.ListViewContainer;
import jp.smartlinks.beacon_tutorial.Models.Log;
import jp.smartlinks.beacon_tutorial.R;

/**
 * Created by hiro on 2016/02/12.
 */
public class BeaconListAdapter extends ArrayAdapter<ListViewContainer> {

    public static final String TAG = BeaconListAdapter.class.getSimpleName();


    private LayoutInflater mlayoutInflater;
    private Context mContext;

    public BeaconListAdapter( Context context, List<ListViewContainer> objects) {

        super(context,0,objects);

        mContext = context;
        mlayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent ) {
        View view = null;

        ListViewContainer lvc = getItem( position );
        int type = lvc.GetType();

        switch ( type ) {
            case ListViewContainer.TYPE_TITLE:
                String title = (String)lvc.GetObject();
                view = mlayoutInflater.inflate(R.layout.list_title, parent, false);
                TextView lblTitle = (TextView)view.findViewById(R.id.item_title);
                lblTitle.setText( title );

                break;
            case ListViewContainer.TYPE_BEACON:
                Beacon beacon = (Beacon)lvc.GetObject();

                view = mlayoutInflater.inflate(R.layout.list_beacon, parent, false);

                TextView lblUUID = (TextView)view.findViewById(R.id.item_uuid);
                lblUUID.setText( "UUID = " + beacon.getId1() );

                TextView lblMajor = (TextView)view.findViewById(R.id.item_major);
                lblMajor.setText( "Major = " + beacon.getId2() );

                TextView lblMinor = (TextView)view.findViewById(R.id.item_minor);
                lblMinor.setText( "Minor = " + beacon.getId3() );

                TextView lblProximity = (TextView)view.findViewById(R.id.item_proximity);
                lblProximity.setText( "Distance = " + beacon.getDistance() + "m" );

                break;
            case ListViewContainer.TYPE_LOG:
                Log log = (Log)lvc.GetObject();
                view = mlayoutInflater.inflate(R.layout.list_log, parent, false);

                TextView txtUUID = (TextView)view.findViewById(R.id.item_uuid);
                txtUUID.setText( "UUID = " + log.GetUUID() );

                TextView txtMajor = (TextView)view.findViewById(R.id.item_major);
                txtMajor.setText( "Major = " + log.GetMajor() );

                TextView txtMinor = (TextView)view.findViewById(R.id.item_minor);
                txtMinor.setText( "Minor = " + log.GetMinor() );

                TextView txtLatLng = (TextView)view.findViewById(R.id.item_latlng);
                txtLatLng.setText( "Lat,Lon = " + log.GetLat() + ", " + log.GetLon() );

                TextView txtDate = (TextView)view.findViewById(R.id.item_date);

                //フォーマットパターンを指定して表示する
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
                String date = sdf.format(log.GetDate());

                txtDate.setText( "DateTime= " + date );

                break;

        }


        return view;
    }

}
