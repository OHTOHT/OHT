package template.taxi.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import template.taxi.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import template.taxi.adapter.LocationListAdapter;
import template.taxi.data.Constant;
import template.taxi.data.Tools;

public class FragmentDialogLocation extends DialogFragment {

    public CallbackResult callbackResult;
    private String hint = "";

    private LocationListAdapter mAdapter;
    private ProgressBar progress_bar;
    private RecyclerView recyclerView;
    private List<String> items = new ArrayList<>();

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    private int request_code = 0;
    private boolean on_search = false;
    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_taxi_fragment_dialog_location, container, false);
        initComponent();

        Tools.setSystemBarColorFragment(getActivity(), this, R.color.app_taxi_grey_soft);
        Tools.setSystemBarLightFragment(this);
        Tools.checkInternetConnection(getActivity());

        return root_view;
    }

    private void initComponent() {
        final ImageView img_clear = (ImageView) root_view.findViewById(R.id.img_clear);
        final EditText et_search = (EditText) root_view.findViewById(R.id.et_search);
        recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);
        progress_bar = (ProgressBar) root_view.findViewById(R.id.progress_bar);

        items = Constant.getLocations(getActivity());

        //set data and list adapter
        mAdapter = new LocationListAdapter(getActivity());
        mAdapter.setItems(items);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        et_search.setHint(hint);

        progress_bar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = et_search.getText().toString().trim();
                if (!str.trim().equals("") && !on_search) {
                    img_clear.setVisibility(View.VISIBLE);
                    progress_bar.setVisibility(View.VISIBLE);
                    on_search = true;
                    new Handler().postDelayed(() -> {
                        on_search = false;
                        Collections.shuffle(items);
                        mAdapter.setItems(items);
                        progress_bar.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }, 500);
                } else {
                    img_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter.setOnItemClickListener(new LocationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String obj, int position) {
                sendDataResult(obj);
                dismissDialog();
            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

        ((ImageView) root_view.findViewById(R.id.img_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        ((View) root_view.findViewById(R.id.lyt_select_from_map)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Select location from map", Toast.LENGTH_SHORT).show();
            }
        });

        //Tools.hideKeyboardFragment(this);
    }

    private void sendDataResult(String loc) {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, loc);
        }
    }

    private void dismissDialog() {
        Tools.hideKeyboardFragment(this);
        dismiss();
    }

    @Override
    public int getTheme() {
        return R.style.App_Taxi_AppTheme_FullScreenDialog;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, String loc);
    }

}