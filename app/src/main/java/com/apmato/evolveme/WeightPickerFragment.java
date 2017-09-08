package com.apmato.evolveme;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

/**
 * Created by hyuntae on 01/09/16.
 */
public class WeightPickerFragment extends DialogFragment{
    public static final String EXTRA_WEIGHT = "com.apmato.evolveme.weightint";

    private float mWeight = 0;
    private float mDecimal = 0;
    private float mInt = 0;

    public static WeightPickerFragment newInstance(float weight){
        Bundle args = new Bundle();
        args.putFloat(EXTRA_WEIGHT, weight);
        WeightPickerFragment fragment = new WeightPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void sendResult(int resultCode){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_WEIGHT, mWeight);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();

        mWeight = bundle.getFloat(EXTRA_WEIGHT);

        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_weight, null);

        NumberPicker intNumberPicker = (NumberPicker)v.findViewById(R.id.dialog_weight_intPicker);
        intNumberPicker.setMaxValue(200);
        intNumberPicker.setMinValue(0);
        intNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int num, int i1) {
                mInt = num;
            }
        });

        NumberPicker decimalNumberPicker = (NumberPicker)v.findViewById(R.id.dialog_weight_decimalPicker);
        decimalNumberPicker.setMaxValue(9);
        decimalNumberPicker.setMinValue(0);
        decimalNumberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                mDecimal = (float) i / 10 ;
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("Select the number")
                .setView(v)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mWeight = mInt+mDecimal+(float)1.1;
                        Bundle b = new Bundle();
                        b.putFloat(EXTRA_WEIGHT, mWeight);
                        sendResult(Activity.RESULT_OK);
                    }
                }).create();
    }
}
