package com.example.hi_breed.screenLoading;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.hi_breed.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity activity){
            this.activity = activity;
    }
   public void startDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater  = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_dialog, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }
    public void dismissDialog(){
        dialog.dismiss();
    }

}
