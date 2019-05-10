package com.hua.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

public class CrimeFragment extends Fragment {
    public static final String ARG_CRIME_ID = "crime_id";
    private static final String DIALOG__DATE = "DialogDate";
    private static final String EXTRA_DATE = "com.hua.criminalintent.date";
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_CONTACT = 1;
    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private Button mSuspectButton;
    private Button mReportButton;
    private Button mCallButton;

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime, container, false);
        mTitleField = view.findViewById(R.id.crime_title);
        mDateButton = view.findViewById(R.id.crime_date);
        mSolvedCheckBox = view.findViewById(R.id.crime_solved);
        mSuspectButton = view.findViewById(R.id.crime_suspect);
        mReportButton = view.findViewById(R.id.crime_report);
        mCallButton = view.findViewById(R.id.crime_call);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mCrime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        updateDate();
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG__DATE);
            }
        });
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mCrime.setSolved(isChecked);
            }
        });
        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("txt/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
                //强制创建选择器
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);*/
                Intent intent = ShareCompat.IntentBuilder.from(getActivity())
                        .setType("txt/plain")
                        .setText(getCrimeReport())
                        .setSubject(getString(R.string.crime_report_subject))
                        .getIntent();
                intent = Intent.createChooser(intent, getString(R.string.send_report));
                startActivity(intent);
            }
        });
        mSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContact = new Intent();
                pickContact.setAction(Intent.ACTION_PICK);
                pickContact.setData(ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });
        mCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getActivity().getContentResolver()
                        .query(ContactsContract.Contacts.CONTENT_URI,
                                null,
                                ContactsContract.Contacts.DISPLAY_NAME + " = ?",
                                new String[]{mCrime.getSuspect()},
                                null);
                if (cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    int id = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    //CommonDataKinds.Phone.CONTACT_ID 对应 Contacts._ID
                    cursor = getActivity().getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                    new String[]{"" + id},
                                    null);
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        String tel = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        Uri uri = Uri.parse("tel:" + tel);
                        Intent intent = new Intent();
                        //ACTION_DIAL只复制号码到拨号盘，ACTION_CALL直接拨打电话
                        intent.setAction(Intent.ACTION_DIAL);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                }
            }
        });
        if (mCrime.getSuspect() != null)
            mSuspectButton.setText(mCrime.getSuspect());
        PackageManager packageManager = getActivity().getPackageManager();
        Intent pickContact = new Intent();
        pickContact.setAction(Intent.ACTION_PICK);
        pickContact.setData(ContactsContract.Contacts.CONTENT_URI);
        //检查intent要启动的activity是否存在
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null)
            mSuspectButton.setEnabled(false);
        if (mCrime.getSuspect() != null) {
            mCallButton.setEnabled(true);
        } else {
            mCallButton.setEnabled(false);
        }
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DATE && resultCode == Activity.RESULT_OK) {
            Date date = (Date) data.getExtras().getSerializable(EXTRA_DATE);
            mCrime.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Cursor cursor = getActivity().getContentResolver()
                    .query(data.getData(),
                            null,
                            null,
                            null,
                            null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                mCrime.setSuspect(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                mSuspectButton.setText(mCrime.getSuspect());
                mCallButton.setEnabled(true);
            }
        }
    }

    private void updateDate() {
        mDateButton.setText(mCrime.getDate().toString());
    }

    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.get(getActivity()).updateCrime(mCrime);
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (mCrime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();

        String suspect = mCrime.getSuspect();
        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);

        return report;
    }
}