package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputMinorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputMinorFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG = InputMinorFragment.class.getSimpleName();


    private Activity mActivity;
    private Context mContext;
    private AppController mAppCon;

    private Handler mHandler;

    private boolean isInput = false;
    private int input_minor;

    private RadioGroup mRadioGroup;
    private TextView txtErr;
    private EditText et_minor;
    private TextView txtInputMinor;


    public InputMinorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputMajorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputMinorFragment newInstance() {
        InputMinorFragment fragment = new InputMinorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = getActivity();
        this.mAppCon = (AppController)mActivity.getApplication();
        this.mContext = this.mActivity.getBaseContext();

        this.input_minor = this.mAppCon.GetMinor();

        this.mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input_minor, container, false);

        // RadioGroupをメンバ変数に保存しておく
        mRadioGroup = (RadioGroup)v.findViewById(R.id.grpMinor);
        mRadioGroup.setOnCheckedChangeListener(this);

        txtErr = (TextView)v.findViewById(R.id.txtErr);
        txtErr.setText("");

        et_minor = (EditText)v.findViewById(R.id.etMinor);
        et_minor.addTextChangedListener(watchHandler);

        txtInputMinor = (TextView)v.findViewById(R.id.txtInputMinor);


        if( this.input_minor == -1 ) {
            mRadioGroup.check(R.id.rb1);
            et_minor.setText("");
            txtInputMinor.setText("未指定");
            isInput = false;
        } else {
            mRadioGroup.check(R.id.rb2);
            et_minor.setText( Integer.toString(this.input_minor) );
            txtInputMinor.setText( Integer.toString(this.input_minor));
            isInput = true;
        }

        Button btnSet = (Button)v.findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if( isInput ) {
                    String tMinor = et_minor.getText().toString();

                    if( tMinor.length() == 0 ) {
                        showAlert("Minor値が入力されていません。");
                        return;
                    }

                    int val = Integer.parseInt(tMinor);
                    if( val < 0 || val > 65535 ) {
                        showAlert("Minor値が正しくありません。");
                        return;
                    }

                    mAppCon.SetMinor( val );
                    mAppCon.Save(mContext);

                    input_minor = val;
                    txtInputMinor.setText(tMinor);

                    showAlert("Minor値を設定しました。");

                } else {
                    mAppCon.SetMinor(-1);
                    mAppCon.Save(mContext);

                    input_minor = -1;
                    txtInputMinor.setText("未指定");
                    showAlert("Minorを未指定にしました。");
                }
            }
        });
        return v;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        Log.d(TAG, "onCheckedChanged = " + checkedId);

        if( checkedId == R.id.rb1 ) {

            Log.d(TAG, "onCheckedChanged = 未指定" );
            isInput = false;

        }
        if( checkedId == R.id.rb2 ) {

            Log.d(TAG, "onCheckedChanged = Minor値を指定する" );
            isInput = true;

        }
    }

    private TextWatcher watchHandler = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "beforeTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " count:" + String.valueOf(count) +
                    " after:" + String.valueOf(after));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " before:" + String.valueOf(before) +
                    " count:" + String.valueOf(count));

            txtErr.setText("");
            if( s.length() == 0 ) {
                return;
            }

            int val = Integer.parseInt(s.toString());
            if( val < 0 || val > 65535 ) {
                txtErr.setText("0〜65535 の範囲を指定してください。");
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChanged()");
        }
    };

    private void showAlert( final String msg ) {
        mHandler.post(new Runnable() {
            public void run() {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Beacon入門")
                        .setMessage(msg)
                        .setPositiveButton("OK", null)
                        .show();
            }
        });

    }

}
