package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListFragment extends Fragment {

    @BindView(R.id.tv_mock_item1) TextView text1;
    @BindView(R.id.tv_mock_item2) TextView text2;

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    // TODO: Remove, this is for testing purposes

    @Override
    public void onResume() {
        super.onResume();

        text1.setText("This is item 1");
        text2.setText("This is item 2");
    }
}
