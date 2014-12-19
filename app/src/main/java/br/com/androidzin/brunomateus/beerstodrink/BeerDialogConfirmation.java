package br.com.androidzin.brunomateus.beerstodrink;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import br.com.androidzin.brunomateus.beerstodrink.model.Beer;

/**
 * Created by bruno on 19/12/14.
 */
public class BeerDialogConfirmation extends DialogFragment {

    private Beer mBeer;

    public interface BeerDialogConfirmartionListener {
        public void onDialogPositiveClick(Beer beer);
    }

    private BeerDialogConfirmartionListener mListener;

    public void setBeer(Beer beer) {
        this.mBeer = beer;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.marked_not_drank);
        builder.setPositiveButton(R.string.no_i_havent, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mListener.onDialogPositiveClick(mBeer);
            }
        }).setNegativeButton(R.string.ive_already_drank, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (BeerDialogConfirmartionListener) getActivity();
        } catch (ClassCastException e){
            throw  new ClassCastException(activity.toString()
                    + "must implement BeerDialogConfirmationListener");
        }
    }
}
