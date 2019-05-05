package com.hua.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView = view.findViewById(R.id.crime_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;
    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        mAdapter = new CrimeAdapter(crimeLab.getCrimes());
        mCrimeRecyclerView.setAdapter(mAdapter);
    }

    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (getItemViewType(i) == 1) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_crime_police, viewGroup, false);
                RecyclerView.ViewHolder crimeHolder2 = new CrimeHolder2(view);
                return crimeHolder2;
            }
            view = LayoutInflater.from(getActivity()).inflate(R.layout.list_item_crime, viewGroup, false);
            RecyclerView.ViewHolder crimeHolder = new CrimeHolder(view);
            return crimeHolder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
            if (getItemViewType(i) == 1) {
                ((CrimeHolder2) viewHolder).mTitleTextView.setText(mCrimes.get(i).getTitle());
                ((CrimeHolder2) viewHolder).mDateTextView.setText(mCrimes.get(i).getDate().toString());
                ((CrimeHolder2) viewHolder).mPoliceTextView.setText("我抱井啦！");
                ((CrimeHolder2) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), mCrimes.get(i).getTitle() + "clicked!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                ((CrimeHolder) viewHolder).mTitleTextView.setText(mCrimes.get(i).getTitle());
                ((CrimeHolder) viewHolder).mDateTextView.setText(mCrimes.get(i).getDate().toString());
                ((CrimeHolder) viewHolder).itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getActivity(), mCrimes.get(i).getTitle() + "clicked!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }

        @Override
        public int getItemViewType(int position) {
            if (mCrimes.get(position).isRequiresPolice()) {
                return 1;
            }
            return 0;
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDateTextView;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
        }
    }

    private class CrimeHolder2 extends RecyclerView.ViewHolder {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private TextView mPoliceTextView;

        public CrimeHolder2(View itemView) {
            super(itemView);
            mTitleTextView = itemView.findViewById(R.id.crime_title);
            mDateTextView = itemView.findViewById(R.id.crime_date);
            mPoliceTextView = itemView.findViewById(R.id.police);
        }
    }
}
