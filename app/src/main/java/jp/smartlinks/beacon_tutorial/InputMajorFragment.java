package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.IntegerRes;
import android.support.v4.app.Fragment;
import android.text.BoringLayout;
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

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputMajorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputMajorFragment extends Fragment implements RadioGroup.OnCheckedChangeListener {
    public static final String TAG = InputMajorFragment.class.getSimpleName();


    private Activity mActivity;
    private Context mContext;
    private AppController mAppCon;

    private Handler mHandler;

    private boolean isInput = false;
    private int input_major;

    private RadioGroup mRadioGroup;
    private TextView txtErr;
    private EditText et_major;
    private TextView txtInputMajor;


    public InputMajorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputMajorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputMajorFragment newInstance() {
        InputMajorFragment fragment = new InputMajorFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = getActivity();
        this.mAppCon = (AppController)mActivity.getApplication();
        this.mContext = this.mActivity.getBaseContext();

        this.input_major = this.mAppCon.GetMajor();

        this.mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input_major, container, false);

        // RadioGroupをメンバ変数に保存しておく
        mRadioGroup = (RadioGroup)v.findViewById(R.id.grpMajor);
        mRadioGroup.setOnCheckedChangeListener(this);

        txtErr = (TextView)v.findViewById(R.id.txtErr);
        txtErr.setText("");

        et_major = (EditText)v.findViewById(R.id.etMajor);
        et_major.addTextChangedListener(watchHandler);

        txtInputMajor = (TextView)v.findViewById(R.id.txtInputMajor);


        if( this.input_major == -1 ) {
            mRadioGroup.check(R.id.rb1);
            et_major.setText("");
            txtInputMajor.setText("未指定");
            isInput = false;
        } else {
            mRadioGroup.check(R.id.rb2);
            et_major.setText( Integer.toString(this.input_major) );
            txtInputMajor.setText( Integer.toString(this.input_major) );
            isInput = true;
        }

        Button btnSet = (Button)v.findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if( isInput ) {
                    String tMajor = et_major.getText().toString();

                    if( tMajor.length() == 0 ) {
                        showAlert("Major値が入力されていません。");
                        return;
                    }

                    int val = Integer.parseInt(tMajor);
                    if( val < 0 || val > 65535 ) {
                        showAlert("Major値が正しくありません。");
                        return;
                    }

                    mAppCon.SetMajor( val );
                    mAppCon.Save(mContext);

                    input_major = val;
                    txtInputMajor.setText(tMajor);

                    showAlert("Major値を設定しました。");

                } else {
                    mAppCon.SetMajor(-1);
                    mAppCon.Save(mContext);

                    input_major = -1;
                    txtInputMajor.setText("未指定");
                    showAlert("Majorを未指定にしました。");
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

            Log.d(TAG, "onCheckedChanged = Major値を指定する" );
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
