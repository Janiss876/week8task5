package com.example.week8task5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    TextView bottom;
    TextView money;
    SeekBar slider;
    BottleDispenser bottled = BottleDispenser.getInstance();
    Spinner spinner;
    double m;
    String choice;
    String receipt;
    MainActivity context = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = MainActivity.this;
        System.out.println("SIJAINTI" + context.getFilesDir());
        bottom = (TextView) findViewById(R.id.bottomText);
        money = (TextView) findViewById(R.id.moneyAmount);
        slider = (SeekBar) findViewById(R.id.slider);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.bottleChoices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                DecimalFormat df = new DecimalFormat("#.##");
                money.setText(df.format(0.02 * progress) + "€");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                m = 0.02 * slider.getProgress();
            }
        });
    }

    public void add(View v) {
        bottled.addMoney(bottom, m);
        slider.setProgress(0);
    }

    public void buy(View v) {
        receipt = bottled.buyBottle(choice, bottom);

    }

    public void returnMoney(View v) {
        bottled.returnMoney(bottom);
    }

    public void writeReceipt(View v) {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(context.openFileOutput("receipt.txt", Context.MODE_PRIVATE));
            osw.write(receipt);
            osw.close();
            bottom.setText("Please take your receipt");
        } catch(IOException e) {
            Log.e("IOException", "Virhe syötteessä");
        } finally {
            System.out.println("File written");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        choice = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
