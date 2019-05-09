package com.hua.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CrimeEmptyFragment extends Fragment {
    public static final String EXTRA_CRIME_ID = "com.hua.criminalintent.crime_id";
    private Button mAddButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_empty, container, false);
        mAddButton = view.findViewById(R.id.add_button);
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Crime crime = new Crime();
                CrimeLab.get(getActivity()).addCrime(crime);
                Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
                intent.putExtra(EXTRA_CRIME_ID, crime.getId());
                startActivity(intent);
            }
        });
        return view;
    }
}
