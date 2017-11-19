package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

/** The fragment holding the individual instructions for a step */
public class InstructionsFragment extends Fragment {

    private static final String TAG = InstructionsFragment.class.getSimpleName();

    @BindView(R.id.tv_instructions) TextView mInstructions;

    public static InstructionsFragment newInstance(@NonNull Bundle args) {
        InstructionsFragment fragment = new InstructionsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override @Nullable
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instructions, container, false);

        ButterKnife.bind(this, view);

        Bundle args = getArguments();

        if (args != null) {
            setData(args);
        }

        return view;
    }

    public void setData(@NonNull Bundle args) {
        String text = args.getString(RecipeActivity.INSTRUCTIONS_EXTRA);
        Log.d(TAG, "Instructions: " + text);

        String mInstructionsText = args.getString(RecipeActivity.INSTRUCTIONS_EXTRA, "");
        mInstructions.setText(mInstructionsText);
    }
}
