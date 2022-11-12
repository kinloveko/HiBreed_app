package com.example.hi_breed.classesFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.hi_breed.R;

import java.util.Calendar;

public class datePickerClass {
    DatePickerDialog datePickerDialog;
    Context activity;
    int id;

    public datePickerClass(DatePickerDialog datePickerDialog, Context context,int id){
        this.datePickerDialog = datePickerDialog;
        this.activity = context;
        this.id = id;
    }
    @SuppressLint("ResourceType")
    public void initDatePicker() {

        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = makeDateString(dayOfMonth,month,year);
                TextView txtView = (TextView) ((Activity)activity).findViewById(id);
                txtView.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;


        datePickerDialog = new DatePickerDialog(activity, style, dateSetListener, year, month,day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();


    }
    public String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " +dayOfMonth + " " + year;

    }
    public  String getMonthFormat(int month) {

        if(month == 1){
            return "Jan";
        }
        if(month == 2){
            return "Feb";
        }
        if(month == 3){
            return "Mar";
        }
        if(month == 4){
            return "Apr";
        }
        if(month == 5){
            return "May";
        }
        if(month == 6){
            return "Jun";
        }
        if(month == 7){
            return "Jul";
        }
        if(month == 8){
            return "Aug";
        }
        if(month == 9){
            return "Sep";
        }
        if(month == 10){
            return "Oct";
        }
        if(month == 11){
            return "Nov";
        }
        if(month == 12){
            return "Dec";
        }
        //for default but it will never happen
        return "Jan";
    }

}
