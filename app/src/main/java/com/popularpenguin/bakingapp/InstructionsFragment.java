package com.popularpenguin.bakingapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.popularpenguin.bakingapp.Data.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/** The fragment holding the individual instructions for a step */
public class InstructionsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = InstructionsFragment.class.getSimpleName();

    @BindView(R.id.tv_instructions) TextView mInstructions;
    Button mPrevious;
    Button mNext;

    private Recipe mRecipe;
    private int mIndex;
    private String mInstructionsText;
    private boolean isPhone;

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

        isPhone = getResources().getBoolean(R.bool.isPhone);

        if (isPhone) {
            mPrevious = view.findViewById(R.id.btn_previous);
            mPrevious.setOnClickListener(this);

            mNext = view.findViewById(R.id.btn_next);
            mNext.setOnClickListener(this);
        }

        ButterKnife.bind(this, view);

        Bundle args = getArguments();

        if (args != null) {
            setData(args);
        }

        return view;
    }

    public void setData(@NonNull Bundle args) {
        mRecipe = args.getParcelable(MainActivity.RECIPE_EXTRA);
        mIndex = args.getInt(RecipeActivity.INDEX_EXTRA);

        setViews();
    }

    /** set the text for the instructions and if it is a phone, enable/disable buttons based
     * on index */
    private void setViews() {
        mInstructionsText = mRecipe.getSteps().get(mIndex).getDescription();
        Log.d(TAG, "Instructions: " + mInstructionsText);
        mInstructions.setText(mInstructionsText);

        // disable buttons if they are at first/last index
        if (isPhone) {
            mPrevious.setEnabled(mIndex > 0);
            mNext.setEnabled(mIndex < mRecipe.getSteps().size() -1);
        }
    }

    /** Handle previous and next button clicks on mobile */
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_previous:
                if (mIndex > 0) {
                    mIndex--;
                    setViews();
                }

                break;

            case R.id.btn_next:
                if (mIndex < mRecipe.getSteps().size() - 1) {
                    mIndex++;
                    setViews();
                }

                break;

            default:
                throw new UnsupportedOperationException("Invalid view id");
        }
    }
}
