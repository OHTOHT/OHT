package template.travel.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;

import template.travel.R;
import template.travel.adapter.AirportListAdapter;
import template.travel.data.GlobalVariable;
import template.travel.model.Airport;

public class FragmentDialogAirport extends DialogFragment {

    public CallbackResult callbackResult;
    public CallbackDismiss callbackDismiss;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    public void setOnCallbackDismiss(final CallbackDismiss callbackDismiss) {
        this.callbackDismiss = callbackDismiss;
    }

    private int request_code = 0;
    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_travel_fragment_dialog_airport, container, false);
        initToolbar();
        initComponent();
        return root_view;
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) root_view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initComponent() {
        final ImageView img_clear = (ImageView) root_view.findViewById(R.id.img_clear);
        final EditText et_search = (EditText) root_view.findViewById(R.id.et_search);
        RecyclerView recyclerView = (RecyclerView) root_view.findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        //set data and list adapter
        final AirportListAdapter mAdapter = new AirportListAdapter(getActivity(), GlobalVariable.getInstance(getContext().getApplicationContext()).getAirports());
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AirportListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Airport obj, int position) {
                sendDataResult(obj);
                dismissDialog();
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(et_search.getText().toString());
                if (!et_search.getText().toString().trim().equals("")) {
                    img_clear.setVisibility(View.VISIBLE);
                } else {
                    img_clear.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        img_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_search.setText("");
            }
        });

    }

    private void sendDataResult(Airport airport) {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, airport);
        }
    }

    private void dismissDialog() {
        if (callbackDismiss != null) {
            callbackDismiss.dismiss();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public void setRequestCode(int request_code) {
        this.request_code = request_code;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, Airport airport);
    }

    public interface CallbackDismiss {
        void dismiss();
    }

}