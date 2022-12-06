package com.example.maindatabaseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PickFilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Integer diap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_filter);
        TextView textView = (TextView) findViewById(R.id.textView);
        TextView textView2 = (TextView) findViewById(R.id.textView2);
        TextView textView3 = (TextView) findViewById(R.id.textView3);
        Button button3 = (Button) findViewById(R.id.button3);
        Button button1 = (Button) findViewById(R.id.button);
        Button button4 = (Button) findViewById(R.id.button4);
        Switch onOffSwitch = (Switch) findViewById(R.id.switch2);
        Switch onOffSwitchTemp = (Switch) findViewById(R.id.switch3);
        Switch onOffSwitchHumid = (Switch) findViewById(R.id.switch4);
        EditText temp = (EditText) findViewById(R.id.editTextNumber);
        EditText humid = (EditText) findViewById(R.id.editTextNumber2);
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        Spinner spin2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.operations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spin.setAdapter(adapter);
        spin2.setAdapter(adapter);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textView2.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.VISIBLE);
                    button3.setVisibility(View.VISIBLE);
                    button1.setText("Начальная дата");
                    textView.setVisibility(View.GONE);
                } else {
                    textView2.setVisibility(View.GONE);
                    textView3.setVisibility(View.GONE);
                    button3.setVisibility(View.GONE);
                    button1.setText("Выбрать дату");
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });
        onOffSwitchTemp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    temp.setVisibility(View.VISIBLE);
                    spin.setVisibility(View.VISIBLE);
                } else {
                    temp.setVisibility(View.GONE);
                    spin.setVisibility(View.GONE);
                }
            }
        });
        onOffSwitchHumid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    humid.setVisibility(View.VISIBLE);
                    spin2.setVisibility(View.VISIBLE);
                } else {
                    humid.setVisibility(View.GONE);
                    spin2.setVisibility(View.GONE);
                }
            }
        });
        textView2.setVisibility(View.GONE);
        textView3.setVisibility(View.GONE);
        button3.setVisibility(View.GONE);
        temp.setVisibility(View.GONE);
        spin.setVisibility(View.GONE);
        humid.setVisibility(View.GONE);
        spin2.setVisibility(View.GONE);


        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String strDate = format.format(currentTime.getTime());
        //textView.setText(strDate);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diap = 1;
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diap = 2;
                DialogFragment datePicker2 = new DatePickerFragment();
                datePicker2.show(getSupportFragmentManager(), "date picker");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Store.INSTANCE.setStartDate(textView3.getText().toString());
                if (onOffSwitch.isChecked()) {
                    Store.INSTANCE.setEndDate(textView2.getText().toString());
                } else {
                    Store.INSTANCE.setEndDate("");
                }
                if (onOffSwitchTemp.isChecked()) {
                    Store.INSTANCE.setValTemp(Integer.valueOf(temp.getText().toString()));
                } else {
                    Store.INSTANCE.setValTemp(Integer.valueOf(99));
                }
                if (onOffSwitchHumid.isChecked()) {
                    Store.INSTANCE.setValHumid(Integer.valueOf(humid.getText().toString()));
                } else {
                    Store.INSTANCE.setValHumid(Integer.valueOf(99));
                }
                if (spin.getSelectedItemPosition() == 0) {
                    Store.INSTANCE.setSelectedOperator1(0);
                } else if (spin.getSelectedItemPosition() == 1) {
                    Store.INSTANCE.setSelectedOperator1(1);
                } else if (spin.getSelectedItemPosition() == 2) {
                    Store.INSTANCE.setSelectedOperator1(2);
                } else {
                    Store.INSTANCE.setSelectedOperator1(3);
                }
                if (spin2.getSelectedItemPosition() == 0) {
                    Store.INSTANCE.setSelectedOperator2(0);
                } else if (spin2.getSelectedItemPosition() == 1) {
                    Store.INSTANCE.setSelectedOperator2(1);
                } else if (spin2.getSelectedItemPosition() == 2) {
                    Store.INSTANCE.setSelectedOperator2(2);
                } else {
                    Store.INSTANCE.setSelectedOperator2(3);
                }
                Intent intent = new Intent(view.getContext(), Monitoring.class);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        String strDate = format.format(c.getTime());
        if (diap == 2) {
            TextView textView2 = (TextView) findViewById(R.id.textView2);
            textView2.setText(strDate);
        }
        if (diap == 1) {
            TextView textView = (TextView) findViewById(R.id.textView);
            textView.setText(strDate);
            TextView textView3 = (TextView) findViewById(R.id.textView3);
            textView3.setText(strDate);
        }

    }
}
