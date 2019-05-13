package com.hua.criminalintent;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks{

    public static final String EXTRA_CRIME_ID = "com.hua.criminalintent.crime_id";
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if(findViewById(R.id.detail_fragment_container) != null)
        {
            Fragment fragment = CrimeFragment.newInstance(crime.getId());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detail_fragment_container,fragment).commit();
        }
        else
        {
            Intent intent = new Intent(this, CrimePagerActivity.class);
            intent.putExtra(EXTRA_CRIME_ID, crime.getId());
            startActivity(intent);
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        crimeListFragment.updateUI();
    }
}
