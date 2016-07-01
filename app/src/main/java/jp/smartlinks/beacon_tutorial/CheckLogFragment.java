package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import jp.smartlinks.beacon_tutorial.Adapters.BeaconListAdapter;
import jp.smartlinks.beacon_tutorial.Models.ListViewContainer;


/**
 */
public class CheckLogFragment extends ListFragment {
    public static final String TAG = CheckLogFragment.class.getSimpleName();

    private AppController mAppCon;
    private Activity mActivity;
    private Context mContext;

    private ArrayList<jp.smartlinks.beacon_tutorial.Models.Log> mLogDatas;

    // 表示用のコンテナ
    private BeaconListAdapter adapter;
    private ArrayList<ListViewContainer> mContainer = new ArrayList<ListViewContainer>();

    public CheckLogFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckLogFragment newInstance() {
        CheckLogFragment fragment = new CheckLogFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new BeaconListAdapter( getActivity(), mContainer );
        setListAdapter(adapter);

        this.mActivity = getActivity();
        this.mAppCon = (AppController) mActivity.getApplication();
        this.mContext = this.mActivity.getBaseContext();
        this.mLogDatas = mAppCon.LogData.GetLogDatas();

        createContainer();

    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_log, container, false);
    }
    */


    private void createContainer() {
        mContainer.clear();

        ListViewContainer title = new ListViewContainer(ListViewContainer.TYPE_TITLE, 0, "ログデータ" );
        mContainer.add(title);

        for( jp.smartlinks.beacon_tutorial.Models.Log l : this.mLogDatas ) {

            ListViewContainer item = new ListViewContainer(ListViewContainer.TYPE_LOG, 0, l );
            mContainer.add(item);
        }


        Log.d(TAG, " createContainer() notifyDataSetChanged!!");
        adapter.notifyDataSetChanged();
    }

}
