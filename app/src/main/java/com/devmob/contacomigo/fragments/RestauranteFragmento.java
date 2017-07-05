package com.devmob.contacomigo.fragments;

import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.devmob.contacomigo.R;
import com.devmob.contacomigo.activities.MainActivity;

/**
 * Created by devmob on 03/05/17.
 */

public class RestauranteFragmento extends Fragment implements FragmentInterface{
    private static final String TAG = "RestauranteFragmento";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.restaurante_layout, container, false);
        Button button1 = (Button) view.findViewById( R.id.button_1 );
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialogFragment bottomSheetDialogFragment = new BottomSheetMenu();
                bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
            }
        });
        return view;
    }

    @Override
    public void fragmentBecameVisible() {
        Log.d(TAG, "restaurante frag interface");
    }
}
