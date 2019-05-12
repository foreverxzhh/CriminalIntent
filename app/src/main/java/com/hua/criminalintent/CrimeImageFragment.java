package com.hua.criminalintent;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import java.io.File;

public class CrimeImageFragment extends DialogFragment {
    private static final String ARG_File = "file";
    ImageView mImageView;

    public static CrimeImageFragment newInstance(File file) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_File, file);
        CrimeImageFragment fragment = new CrimeImageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        File file = (File) getArguments().getSerializable(ARG_File);
        Bitmap bitmap = PictureUtils.getScaledBitmap(file.getPath(), getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image, null);
        mImageView = view.findViewById(R.id.crime_image);
        mImageView.setImageBitmap(bitmap);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setPositiveButton(android.R.string.ok, null).create();

    }
}
