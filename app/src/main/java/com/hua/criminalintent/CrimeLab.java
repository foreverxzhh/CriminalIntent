package com.hua.criminalintent;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab mCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context) {
        if (mCrimeLab == null) {
            mCrimeLab = new CrimeLab(context);
        }
        return mCrimeLab;
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved(i % 2 == 0);
            mCrimes.add(crime);
        }

    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    //优化的话，可以以空间换时间，将UUID作为key，ArrayList的position作为value，建立一个HashMap，懒得写了。。。
    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id)) {
                return crime;
            }
        }
        return null;
    }
}