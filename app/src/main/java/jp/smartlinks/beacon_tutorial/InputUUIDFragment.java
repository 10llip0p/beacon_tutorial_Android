package jp.smartlinks.beacon_tutorial;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InputUUIDFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputUUIDFragment extends Fragment {

    public static final String TAG = InputUUIDFragment.class.getSimpleName();


    private Activity mActivity;
    private Context mContext;
    private AppController mAppCon;

    private Handler mHandler;


    private String input_uuid = "48534442-4C45-4144-80C0-1800FFFFFFFF";
    private String uuid_b1;
    private String uuid_b2;
    private String uuid_b3;
    private String uuid_b4;
    private String uuid_b5;

    private TextView txtFullUUID;
    private TextView txtErrB1;
    private TextView txtErrB2;
    private TextView txtErrB3;
    private TextView txtErrB4;
    private TextView txtErrB5;

    private EditText et_uuid_b1;
    private EditText et_uuid_b2;
    private EditText et_uuid_b3;
    private EditText et_uuid_b4;
    private EditText et_uuid_b5;


    public InputUUIDFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment InputUUIDFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InputUUIDFragment newInstance() {
        InputUUIDFragment fragment = new InputUUIDFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = getActivity();
        this.mAppCon = (AppController)mActivity.getApplication();
        this.mContext = this.mActivity.getBaseContext();

        this.input_uuid = this.mAppCon.GetUUID();

        this.mHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_input_uuid, container, false);

        // フィルターを作成
        InputFilter inputFilter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (source.toString().matches("^[0-9a-zA-Z]+$")) {
                    return source;
                } else {
                    return "";
                }
            }
        };

        txtFullUUID = (TextView)v.findViewById(R.id.txtFullUUID);
        txtFullUUID.setText( this.input_uuid );

        txtErrB1 = (TextView)v.findViewById(R.id.txtErr_b1);
        txtErrB2 = (TextView)v.findViewById(R.id.txtErr_b2);
        txtErrB3 = (TextView)v.findViewById(R.id.txtErr_b3);
        txtErrB4 = (TextView)v.findViewById(R.id.txtErr_b4);
        txtErrB5 = (TextView)v.findViewById(R.id.txtErr_b5);

        uuid_b1 = this.input_uuid.substring(0,8);
        uuid_b2 = this.input_uuid.substring(9,13);
        uuid_b3 = this.input_uuid.substring(14,18);
        uuid_b4 = this.input_uuid.substring(19,23);
        uuid_b5 = this.input_uuid.substring(24);

        Log.d(TAG, "UUID = " + this.input_uuid);
        Log.d(TAG, "UUID B1 = " + uuid_b1);
        Log.d(TAG, "UUID B2 = " + uuid_b2);
        Log.d(TAG, "UUID B3 = " + uuid_b3);
        Log.d(TAG, "UUID B4 = " + uuid_b4);
        Log.d(TAG, "UUID B5 = " + uuid_b5);


        // フィルターの配列を作成
        InputFilter[] filters = new InputFilter[] { inputFilter };

        et_uuid_b1 = (EditText)v.findViewById(R.id.etUUID_b1);
        et_uuid_b1.setFilters(filters);
        et_uuid_b1.addTextChangedListener(watchHandlerB1);
        et_uuid_b1.setText(uuid_b1);

        et_uuid_b2 = (EditText)v.findViewById(R.id.etUUID_b2);
        et_uuid_b2.setFilters(filters);
        et_uuid_b2.addTextChangedListener(watchHandlerB2);
        et_uuid_b2.setText(uuid_b2);

        et_uuid_b3 = (EditText)v.findViewById(R.id.etUUID_b3);
        et_uuid_b3.setFilters(filters);
        et_uuid_b3.addTextChangedListener(watchHandlerB3);
        et_uuid_b3.setText(uuid_b3);

        et_uuid_b4 = (EditText)v.findViewById(R.id.etUUID_b4);
        et_uuid_b4.setFilters(filters);
        et_uuid_b4.addTextChangedListener(watchHandlerB4);
        et_uuid_b4.setText(uuid_b4);

        et_uuid_b5 = (EditText)v.findViewById(R.id.etUUID_b5);
        et_uuid_b5.setFilters(filters);
        et_uuid_b5.addTextChangedListener(watchHandlerB5);
        et_uuid_b5.setText(uuid_b5);

        Button btnSet = (Button)v.findViewById(R.id.btnSet);
        btnSet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String b1 = et_uuid_b1.getText().toString();
                String b2 = et_uuid_b2.getText().toString();
                String b3 = et_uuid_b3.getText().toString();
                String b4 = et_uuid_b4.getText().toString();
                String b5 = et_uuid_b5.getText().toString();

                if( b1.length() != 8 ) {
                    showAlert("桁数が正しくありません。");
                    return;
                }
                if( b2.length() != 4 ) {
                    showAlert("桁数が正しくありません。");
                    return;
                }
                if( b3.length() != 4 ) {
                    showAlert("桁数が正しくありません。");
                    return;
                }
                if( b4.length() != 4 ) {
                    showAlert("桁数が正しくありません。");
                    return;
                }
                if( b5.length() != 12 ) {
                    showAlert("桁数が正しくありません。");
                    return;
                }

                input_uuid = b1.toUpperCase() + "-" + b2.toUpperCase() + "-" + b3.toUpperCase() + "-" + b4.toUpperCase() + "-" + b5.toUpperCase();
                txtFullUUID.setText( input_uuid );

                SetEditText();

                mAppCon.SetUUID(input_uuid);
                mAppCon.Save(mContext);

                showAlert("UUIDを変更しました。");


            }
        });

        Button btnReset = (Button)v.findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                input_uuid = mAppCon.GetDefaultUUID();
                txtFullUUID.setText( input_uuid );

                SetEditText();

                mAppCon.SetUUID(null);
                mAppCon.Save(mContext);

                showAlert("初期値に戻しました。");
            }
        });

        return v;
    }

    private void SetEditText() {
        uuid_b1 = this.input_uuid.substring(0,8);
        uuid_b2 = this.input_uuid.substring(9,13);
        uuid_b3 = this.input_uuid.substring(14,18);
        uuid_b4 = this.input_uuid.substring(19,23);
        uuid_b5 = this.input_uuid.substring(24);

        et_uuid_b1.setText(uuid_b1);
        et_uuid_b2.setText(uuid_b2);
        et_uuid_b3.setText(uuid_b3);
        et_uuid_b4.setText(uuid_b4);
        et_uuid_b5.setText(uuid_b5);
    }

    private TextWatcher watchHandlerB1 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "beforeTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " count:" + String.valueOf(count) +
                    " after:" + String.valueOf(after));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " before:" + String.valueOf(before) +
                    " count:" + String.valueOf(count));

            txtErrB1.setText("");

            if( s.length() != 8 ) {
                txtErrB1.setText("8桁入力してください");
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChanged()");
        }
    };

    private TextWatcher watchHandlerB2 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "beforeTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " count:" + String.valueOf(count) +
                    " after:" + String.valueOf(after));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " before:" + String.valueOf(before) +
                    " count:" + String.valueOf(count));

            txtErrB2.setText("");

            if( s.length() != 4 ) {
                txtErrB2.setText("4桁入力してください");
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChanged()");
        }
    };

    private TextWatcher watchHandlerB3 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "beforeTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " count:" + String.valueOf(count) +
                    " after:" + String.valueOf(after));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " before:" + String.valueOf(before) +
                    " count:" + String.valueOf(count));

            txtErrB3.setText("");

            if( s.length() != 4 ) {
                txtErrB3.setText("4桁入力してください");
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChanged()");
        }
    };

    private TextWatcher watchHandlerB4 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "beforeTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " count:" + String.valueOf(count) +
                    " after:" + String.valueOf(after));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " before:" + String.valueOf(before) +
                    " count:" + String.valueOf(count));

            txtErrB4.setText("");

            if( s.length() != 4 ) {
                txtErrB4.setText("4桁入力してください");
                return;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            Log.d(TAG, "afterTextChanged()");
        }
    };

    private TextWatcher watchHandlerB5 = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            Log.d(TAG, "beforeTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " count:" + String.valueOf(count) +
                    " after:" + String.valueOf(after));
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            Log.d(TAG, "onTextChanged() s:" + s.toString() + " start:" + String.valueOf(start) + " before:" + String.valueOf(before) +
                    " count:" + String.valueOf(count));

            txtErrB5.setText("");

            if( s.length() != 12 ) {
                txtErrB5.setText("12桁入力してください");
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
