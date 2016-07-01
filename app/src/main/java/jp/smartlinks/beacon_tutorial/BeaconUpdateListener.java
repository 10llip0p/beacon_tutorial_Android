package jp.smartlinks.beacon_tutorial;

import org.altbeacon.beacon.Beacon;

import java.util.EventListener;
import java.util.List;

/**
 * Created by hiro on 2015/12/15.
 */
public interface BeaconUpdateListener extends EventListener {

    void BeaconChanged(List<Beacon> beaconList);
}
