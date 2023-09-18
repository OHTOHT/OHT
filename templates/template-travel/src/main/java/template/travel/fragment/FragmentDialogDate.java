package template.travel.fragment;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import template.travel.R;
import com.squareup.timessquare.CalendarPickerView;

import java.util.Calendar;
import java.util.Date;

public class FragmentDialogDate extends DialogFragment {

    public CallbackResult callbackResult;
    public CallbackDismiss callbackDismiss;

    public void setOnCallbackResult(final CallbackResult callbackResult) {
        this.callbackResult = callbackResult;
    }

    public void setOnCallbackDismiss(final CallbackDismiss callbackDismiss) {
        this.callbackDismiss = callbackDismiss;
    }

    private Date date = new Date();
    private int request_code = 0;
    private String title = "Select Date";
    private View root_view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_travel_fragment_dialog_date, container, false);
        initComponent();
        return root_view;
    }

    private void initComponent() {
        ((TextView)root_view.findViewById(R.id.tv_title)).setText(title);
        ((ImageView) root_view.findViewById(R.id.img_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog();
            }
        });

        Calendar nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 1);

        CalendarPickerView calendar = (CalendarPickerView) root_view.findViewById(R.id.calendar_view);
        Date today = new Date();
        calendar.init(today, nextYear.getTime()).withSelectedDate(date);

        //action while clicking on a check_in
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(Date date) {
                sendDataResult(date);
                dismissDialog();
            }

            @Override
            public void onDateUnselected(Date date) {

            }
        });
    }

    private void sendDataResult(Date date) {
        if (callbackResult != null) {
            callbackResult.sendResult(request_code, date);
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

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public interface CallbackResult {
        void sendResult(int requestCode, Date date);
    }

    public interface CallbackDismiss {
        void dismiss();
    }

}