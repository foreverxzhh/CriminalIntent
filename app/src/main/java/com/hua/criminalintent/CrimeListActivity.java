package com.hua.criminalintent;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        if (CrimeLab.get(this).getCrimes().size() > 0)
            return new CrimeListFragment();
        else
            return new CrimeEmptyFragment();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (CrimeLab.get(this).getCrimes().size() > 0) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new CrimeListFragment()).commit();
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, new CrimeEmptyFragment()).commit();
        }
    }
}
