package com.example.assignment01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    //Declaring Java Objects to bind with UI elements
    EditText edtCustName;
    RadioGroup rgBevType;
    RadioButton rbCoffee, rbTea;
    CheckBox cbMilk, cbSugar;
    Spinner spinnerBevSize, spinnerAddedFlavour;
    AutoCompleteTextView autoTextProvince;
    Button btn_Order;

    //Declaring list options to populate spinner drop down
    public String[] BevSizeOptions = {"Small", "Medium", "Large"};
    public String[] coffeeFlavours = {"None", "Vanilla ", "Chocolate"};
    public String[] teaFlavours = {"None", "Lemon", "Mint"};
    public String[] provinces= {"Alberta", "British Columbia", "Montreal", "New Brunswick", "Quebec", "Saskatchewan", "Ontario"};

    //Declaring variables to calculate cost of coffee
    public float[] sizeCost = {1.50f, 2.50f, 3.25f};
    public float milkCost = 1.25f;
    public float sugarCost = 1f;
    public float[] coffeeFlavourCost = {0f, 0.25f, 0.75f};
    public float[] teaFlavourCost = {0f, 0.25f, 0.50f};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        assign();
    }

    private void assign() {
        //This method assigns all the UI elements to corresponding java objects
        edtCustName = findViewById(R.id.editTextCustomerName);
        rgBevType = findViewById(R.id.radioGroupBeverageType);
        rbCoffee = findViewById(R.id.radioButtonCoffee);
        rbTea = findViewById(R.id.radioButtonTea);
        cbMilk = findViewById(R.id.checkBoxAddMilk);
        cbSugar = findViewById(R.id.checkBoxAddSugar);
        spinnerBevSize = findViewById(R.id.spinnerSizeOptions);
        spinnerAddedFlavour = findViewById(R.id.spinnerAddedFlavour);
        autoTextProvince = findViewById(R.id.autoCompleteTextViewProvince);
        btn_Order = (Button) findViewById(R.id.buttonPlaceOrder);

        
        //Here we are binding the spinner to populate the dropdown list items
        spinnerBevSize.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, BevSizeOptions));

        spinnerAddedFlavour.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, coffeeFlavours));
        

        rgBevType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);

                // We check if the radio button is checked or not
                boolean isChecked = checkedRadioButton.isChecked();

                //We assign the appropriate list options in the spinner depending on the radio button
                if ((checkedId == R.id.radioButtonCoffee) && isChecked)
                    spinnerAddedFlavour.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, coffeeFlavours));
                else if ((checkedId == R.id.radioButtonTea) && isChecked)
                    spinnerAddedFlavour.setAdapter(new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, teaFlavours));
            }
        });
        //we populate the provinces list here for auto completion
        autoTextProvince.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, provinces));

        //setting an onclick event listener on Place Order button
        btn_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                        showCost();
            }
        });

    }

    //This method is called to compute the cost of the beverage
    private void showCost() {

        //this validates the user input if its empty
        if(edtCustName.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Customer name", Toast.LENGTH_SHORT).show();
            return ;
        }

        //this validates the user input if its empty
        if(autoTextProvince.getText().toString().equals("")) {
            Toast.makeText(this, "Please select a province", Toast.LENGTH_SHORT).show();
            return ;
        }

        //cost is computed if user input is validated
        
        float cost = 0;
        String beverageType = "", flavoring = "", addOn = "";

        String size = spinnerBevSize.getSelectedItem().toString();

        for(int i=0; i<BevSizeOptions.length; i++) {
            if (size.equals(BevSizeOptions[i])){
                cost += sizeCost[i];
                break;
            }
        }

        if(cbMilk.isChecked()) {
            cost += milkCost;
            addOn = "Milk";
        }
        if(cbSugar.isChecked()) {
            cost += sugarCost;
            if(!addOn.equals(""))
                addOn += " & ";
            addOn += "Sugar";
        }

        if(rbCoffee.isChecked()){
            beverageType = "Coffee";
            String flavor = spinnerAddedFlavour.getSelectedItem().toString();
            for(int i=0; i< coffeeFlavours.length; i++) {
                if (flavor.equals(coffeeFlavours[i])) {
                    cost += coffeeFlavourCost[i];
                    flavoring = coffeeFlavours[i];
                    break;
                }
            }
        }
        else if(rbTea.isChecked()){
            beverageType = "Tea";
            String flavor = spinnerAddedFlavour.getSelectedItem().toString();
            for(int i=0; i<teaFlavours.length; i++) {
                if (flavor.equals(teaFlavours[i])) {
                    cost += teaFlavourCost[i];
                    flavoring = teaFlavours[i];
                    break;
                }
            }
        }

        DecimalFormat df = new DecimalFormat("0.00");
        float finalcost = cost + ((cost * 13) / 100);

        String message = "For " + edtCustName.getText().toString() + ", from " + autoTextProvince.getText().toString() + ", a " + spinnerBevSize.getSelectedItem().toString() + " " + beverageType + ", ";
        if(!addOn.equals(""))
            message += "with " + addOn + ", ";
        if(flavoring.equals("None"))
            flavoring = "no";
        message += flavoring + " flavoring, ";
        message += "cost: $" + df.format(finalcost);

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}
