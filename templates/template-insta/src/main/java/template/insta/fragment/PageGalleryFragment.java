package template.insta.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import template.insta.R;

public class PageGalleryFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.app_insta_page_fragment_gallery, container, false);
        ((Button) view.findViewById(R.id.bt_photo)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Take Photo Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });

        ((Button) view.findViewById(R.id.bt_video)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Take Video Clicked", Snackbar.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
