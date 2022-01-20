package com.example.halalfoodauthorityoss.complaint;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.halalfoodauthorityoss.BaseClass;
import com.example.halalfoodauthorityoss.CoreActivity;
import com.example.halalfoodauthorityoss.R;
import com.example.halalfoodauthorityoss.model.AppData;
import com.example.halalfoodauthorityoss.model.Model;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Complaint extends AppCompatActivity {

    TextInputEditText edtBusinessName, edtBusinessAddress, edtComplaint;
    TextView btnSubmit;
    Spinner categorySpinner, districtSpinner;
    ImageView ic_back;
    ProgressDialog progressDialog;
    List<String> ListDistrictName;
    List<String> ListDistrictID;
    String districtID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);

        initialization();
        displayDistrict();

        ic_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Complaint.this, CoreActivity.class));
                finish();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String BusinessName = edtBusinessName.getText().toString().trim();
                String BusinessAddress = edtBusinessAddress.getText().toString().trim();
                String Complaint = edtComplaint.getText().toString().trim();
                String Category = String.valueOf(categorySpinner.getSelectedItem());

                if (Category.equals("Select Category")) {
                    Toast.makeText(Complaint.this, "Select Complaint Category", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (districtID.equals("0")) {
                    Toast.makeText(Complaint.this, "Select District", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (BusinessName.equals("") || BusinessAddress.equals("") || Complaint.equals("")) {
                    Toast.makeText(Complaint.this, "Field Cann't be Empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressDialog.show();
                String FullComplaint = "Business Name: " + BusinessName +", "+ "Complaint Details: " + Complaint;

                Call<Model> call = BaseClass
                        .getInstance()
                        .getApi()
                        .Complaint(Category, FullComplaint, AppData.id,BusinessAddress,districtID);

                call.enqueue(new Callback<Model>() {
                    @Override
                    public void onResponse(Call<Model> call, Response<Model> response) {
                        if (response.isSuccessful()) {
                            Model loginResponse = response.body();
                            if (loginResponse.success.equals("0")) {
                                progressDialog.dismiss();
                                DialogBOX();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Complaint.this, "Complaint Not Submitted", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(Complaint.this, "Not Successful", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Model> call, Throwable t) {
                        progressDialog.dismiss();
                        Toast.makeText(Complaint.this, "Out", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                districtID = ListDistrictID.get(i);
                Log.d("LOG", districtID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initialization() {
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        edtBusinessName = findViewById(R.id.edtBusinessName);
        districtSpinner = findViewById(R.id.districtSpinner);
        edtBusinessAddress = findViewById(R.id.edtBusinessAddress);
        edtComplaint = findViewById(R.id.edtComplaint);
        categorySpinner = findViewById(R.id.complaintSpinner);
        btnSubmit = findViewById(R.id.btnSubmit);
        ic_back = findViewById(R.id.ic_back);
        ListDistrictName = new ArrayList<String>();
        ListDistrictID = new ArrayList<String>();
        ListDistrictName.add("Select District");
        ListDistrictID.add("0");
    }

    private void DialogBOX() {
        AlertDialog alertDialog = new AlertDialog.Builder(Complaint.this).create();
        alertDialog.setTitle("Complaint");
        alertDialog.setMessage("Your Complaint Has Been Submitted!");
        alertDialog.setCancelable(false);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Complaint.this, CoreActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        alertDialog.show();
    }

    private void displayDistrict() {
        Call<List<Model>> call = BaseClass
                .getInstance()
                .getApi()
                .getDistrict();
        call.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                List<Model> model = response.body();
                int size = model.size();
                if (!model.equals(null)) {
                    for (int i = 0; i < size; i++) {
                        ListDistrictName.add(model.get(i).getcName());
                        ListDistrictID.add(model.get(i).getID());
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Complaint.this, android.R.layout.simple_spinner_item, ListDistrictName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        districtSpinner.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(Complaint.this, "Empty Category", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Toast.makeText(Complaint.this, "out", Toast.LENGTH_SHORT).show();
                Log.d("errror2", call.request().toString());

            }
        });
    }
}