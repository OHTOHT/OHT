package template.messenger.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import template.messenger.R;


public class PageSettingFragment extends Fragment {
    View root_view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root_view = inflater.inflate(R.layout.app_messenger_page_fragment_setting, container, false);
        return root_view;
    }

    public void actionClick(View v) {
        if(v.getId() == R.id.lyt_username){
            Snackbar.make(root_view, "Username Clicked", Snackbar.LENGTH_SHORT).show();
        }else if(v.getId() == R.id.lyt_phone){
            Snackbar.make(root_view, "Phone Clicked", Snackbar.LENGTH_SHORT).show();
        }else {
            TextView textView = getChildTextView(v);
            String str = textView == null ? "-" : textView.getText().toString();
            Snackbar.make(root_view, str + " Clicked", Snackbar.LENGTH_SHORT).show();
        }
    }

    public TextView getChildTextView(View v) {
        for (int index = 0; index < ((LinearLayout) v).getChildCount(); ++index) {
            View nextChild = ((LinearLayout) v).getChildAt(index);
            if(nextChild instanceof TextView){
                return (TextView) nextChild;
            }
        }
        return null;
    }
}
