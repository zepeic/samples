package com.example.tipsplit;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
    private EditText billTotal;
    private EditText noOfPeople;
    private TextView perPerson;
    private TextView overage;
    private RadioGroup radioPercent;
    private TextView tipAmount;
    private TextView totalAndTip;
    private double totalWithTip;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        billTotal = findViewById(R.id.editTextBillTotal);
        noOfPeople = findViewById(R.id.editTextNumberOfPeople);
        perPerson = findViewById(R.id.editTextPerPerson);
        overage = findViewById(R.id.editTextOverage);
        tipAmount = findViewById(R.id.tipAmountValue);
        totalAndTip = findViewById(R.id.totalWithTip);
        radioPercent = findViewById(R.id.tipPercent);
    }

    @SuppressLint("DefaultLocale")
    public void doCalculate(View v){

        Log.d(TAG, "doCalculate: ");
        String totalBill = billTotal.getText().toString();
        String peopleNumber = noOfPeople.getText().toString();
        if(peopleNumber.trim().isEmpty()){
            return;
        }
        //double totalBillValue = Double.parseDouble(totalBill);
        int noOfPeopleValue = Integer.parseInt(peopleNumber);

        double split = totalWithTip/noOfPeopleValue;
        double amount =1-(((split * 1000.00)/10)%1);
        if(amount == 1.0){
            amount = 0;
        }

        double amountUp = split + (amount*0.01);

        double overageValue = (amountUp * noOfPeopleValue) - totalWithTip;
        if(overageValue < 0){
            overageValue = 0;
        }

        Log.d(TAG, "doCalculate: " + totalWithTip + "/" + noOfPeopleValue + " = " + amountUp + ", amount: " + amount + " amountUp: " + amountUp + ", Overage Value: " + overageValue + ", Total w/ Tip: " + totalWithTip);
        perPerson.setText(String.format("$%.2f", amountUp));
        overage.setText(String.format("$%.2f", overageValue ));

    }

    @SuppressLint("DefaultLocale")
    public void calculateTip(View v){
        Log.d(TAG, "calculateTip: ");
        String totalBill = billTotal.getText().toString();
        if(totalBill.trim().isEmpty()){
            radioPercent.clearCheck();
            return;
        }
        double totalBillValue = Double.parseDouble(totalBill);
        double tipValue;

        if(v.getId() == R.id.tipTwelve){
            tipValue = (totalBillValue*0.12);
            totalWithTip = tipValue + totalBillValue;
            tipAmount.setText(String.format("$%.2f", tipValue ));
            totalAndTip.setText(String.format("$%.2f", totalWithTip));
        }
        else if(v.getId() == R.id.tipFifteen){
            tipValue = (totalBillValue*0.15);
            totalWithTip = tipValue + totalBillValue;
            tipAmount.setText(String.format("$%.2f", tipValue ));
            totalAndTip.setText(String.format("$%.2f", totalWithTip));
        }
        else if(v.getId() == R.id.tipEighteen){
            tipValue = (totalBillValue*0.18);
            totalWithTip = tipValue + totalBillValue;
            tipAmount.setText(String.format("$%.2f", tipValue ));
            totalAndTip.setText(String.format("$%.2f", totalWithTip));
        }
        else if(v.getId() == R.id.tipTwenty){
            tipValue = (totalBillValue*0.20);
            totalWithTip = tipValue + totalBillValue;
            tipAmount.setText(String.format("$%.2f", tipValue ));
            totalAndTip.setText(String.format("$%.2f", totalWithTip));
        }
    }

    public void doClear(View v){
        billTotal.getText().clear();
        noOfPeople.getText().clear();
        perPerson.setText("");
        overage.setText("");
        tipAmount.setText("");
        totalAndTip.setText("");
        radioPercent.clearCheck();
    }


    protected void onSaveInstanceState(Bundle outState){
        outState.putString("BILL", billTotal.getText().toString());
        outState.putString("PEOPLE", noOfPeople.getText().toString());
        outState.putString("PERSON", perPerson.getText().toString());
        outState.putString("OVERAGE", overage.getText().toString());
        outState.putString("TIP", tipAmount.getText().toString());
        outState.putString("TOTAL_TIP", totalAndTip.getText().toString());
        //outstate.putString("RADIO", noOfPeople.getText().toString());

        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);

        billTotal.setText(savedInstanceState.getString("BILL"));
        noOfPeople.setText(savedInstanceState.getString("PEOPLE"));
        perPerson.setText(savedInstanceState.getString("PERSON"));
        overage.setText(savedInstanceState.getString("OVERAGE"));
        tipAmount.setText(savedInstanceState.getString("TIP"));
        totalAndTip.setText(savedInstanceState.getString("TOTAL_TIP"));


    }
}