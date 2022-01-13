package com.example.halalfoodauthorityoss.loginsignupforgot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.halalfoodauthorityoss.BaseClass;
import com.example.halalfoodauthorityoss.R;
import com.example.halalfoodauthorityoss.model.Model;
import com.example.halalfoodauthorityoss.registerbusiness.Bussiness_Details;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sign_Up extends AppCompatActivity {
    
    TextInputEditText edtname,edtcnic,edtnumber,edtpassword,edtconfirmpassword;
    Spinner distspinner;
    TextView register;
    CheckBox checkBox;
    String name,cnic,number,password,confirmpassword;
    private static Pattern CNIC_PATTERN,AFG_CNIC_PATTERN,PASSWORD_PATTERN;
    ImageView ic_back;
    List<String> ListDistrictName;
    List<String> ListDistrictID;
    String districtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);
        
        initialization();
        displayDistrict();

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sign_Up.this,Login.class));
                finish();
            }
        });

        distspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtID=ListDistrictID.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 name=edtname.getText().toString().trim();
                 cnic=edtcnic.getText().toString().trim();
                 number=edtnumber.getText().toString().trim();
                 password=edtpassword.getText().toString().trim();
                 confirmpassword=edtconfirmpassword.getText().toString().trim();

                 if (name.equals(""))
                 {
                     edtname.setError("Please Enter Name");
                     return;
                 }
                if (cnic.equals(""))
                {
                    edtname.setError("Please Enter CNIC");
                    return;
                }
                 if (!checkBox.isChecked())
                 {
                     if (!CNIC_PATTERN.matcher(cnic).matches()) {
                         edtcnic.setError("Invalid CNIC Number");
                         return;
                     }
                 }
                 if (checkBox.isChecked())
                 {
                     if (!AFG_CNIC_PATTERN.matcher(cnic).matches()) {
                         edtcnic.setError("Invalid CNIC Number");
                         return;
                     }
                 }
                if(number.length()<10 && number.length()>11)
                {
                    edtnumber.setError("Invalid Mobile Number");
                    return;
                }
                if (districtID=="0")
                 {
                     Toast.makeText(Sign_Up.this, "Select District", Toast.LENGTH_SHORT).show();
                     return;
                 }
                if (!PASSWORD_PATTERN.matcher(password).matches())
                {
                    edtpassword.setError("Minimum Length 6, Must Contain Latters and Digits");
                    return;
                }
                if (!confirmpassword.equals(password))
                {
                    edtconfirmpassword.setError("Password Does Not Matched");
                    return;
                }

                Call<Model> call = BaseClass
                        .getInstance()
                        .getApi()
                        .Sign_Up(name,cnic,number,password,districtID);

                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        Model model=response.body();
                        if (response.isSuccessful())
                        {
                            if (model.getSuccess().equals("1"))
                            {
                                Toast.makeText(Sign_Up.this, "Registered", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(Sign_Up.this,Login.class));
                                finish();
                            }
                            else
                            {
                                Toast.makeText(Sign_Up.this, "This User Already Registered", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        Toast.makeText(Sign_Up.this, "failed", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

    }

    private void initialization() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        edtname=findViewById(R.id.edtname);
        edtcnic=findViewById(R.id.edtcnic);
        edtnumber=findViewById(R.id.edtnumber);
        edtpassword=findViewById(R.id.edtpassword);
        edtconfirmpassword=findViewById(R.id.edtconfirmpassword);
        register=findViewById(R.id.btnregister);
        checkBox=findViewById(R.id.checkBox);
        distspinner=findViewById(R.id.distspinner);
        ic_back=findViewById(R.id.ic_back);

        ListDistrictName =  new ArrayList<String>();
        ListDistrictID =  new ArrayList<String>();
        ListDistrictName.add("Select District");
        ListDistrictID.add("0");

        PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=\\S+$)" +           //no white spaces
                        ".{6,}" +               //at least 6 characters
                        "$");
        CNIC_PATTERN=Pattern.compile("^" +"[0-9]{5}-[0-9]{7}-[0-9]{1}"+"$");
        AFG_CNIC_PATTERN=Pattern.compile("^" +"[0-9]{4}-[0-9]{4}-[0-9]{5}"+"$");
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Sign_Up.this,Login.class));
        finish();
    }
    private void displayDistrict() {
        Call<List<Model>> call = BaseClass
                .getInstance()
                .getApi()
                .getDistrict();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                List<Model> model=response.body();
                int size=model.size();
                if (!model.equals(null))
                {
                    for (int i=0;i<size;i++)
                    {

                        ListDistrictName.add(model.get(i).getcName());
                        ListDistrictID.add(model.get(i).getID());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Sign_Up.this, android.R.layout.simple_spinner_item, ListDistrictName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        distspinner.setAdapter(adapter);
                    }
                }
                else {
                    Toast.makeText(Sign_Up.this, "Empty Category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Toast.makeText(Sign_Up.this, "out", Toast.LENGTH_SHORT).show();

            }
        });
    }
}