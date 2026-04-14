package com.example.assignment_4_fragments;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);

        TextView tv = new TextView(getActivity());
        tv.setText("👤 User Profile\nName: Rosh\nCourse: CSE");
        tv.setTextSize(18);

        layout.addView(tv);

        return layout;
    }
}