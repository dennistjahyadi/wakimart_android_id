package com.wakimart.wakimartindonesia;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.wakimart.wakimartindonesia.lookup.CityLookup;
import com.wakimart.wakimartindonesia.lookup.ProvinceLookup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private static final int PROVINCE_SELECT_REQUEST = 1;
    private static final int CITY_SELECT_REQUEST = 2;
    private TextView tvBtnBack, tvErrors, tvLabelAgent,tvGenderError;
    private EditText etNik, etName, etEmail, etPhone, etAddress, etBirthdate, etProvince, etCity, etPoscode, etAgentCode;
    private TextInputLayout tilNik, tilName, tilEmail, tilPhone, tilAddress, tilBirthdate, tilProvince, tilCity, tilPoscode, tilAgentCode;
    private Button btnRegister;
    private FrameLayout loadingBar;
    private NestedScrollView nestedScrollView;
    private RadioGroup rgGender;
    private RadioButton rbMale, rbFemale;
    private Calendar calendarBirthdate = Calendar.getInstance();
    private Integer stateId;
    private Integer cityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        getSupportActionBar().hide();
        loadingBar = findViewById(R.id.loadingBar);
        nestedScrollView = findViewById(R.id.nestedScrollView);
        tvBtnBack = findViewById(R.id.tvBtnBack);
        tvErrors = findViewById(R.id.tvErrors);
        tvLabelAgent = findViewById(R.id.tvLabelAgent);
        etNik = findViewById(R.id.etNik);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etBirthdate = findViewById(R.id.etBirthdate);
        etProvince = findViewById(R.id.etProvince);
        etCity = findViewById(R.id.etCity);
        etPoscode = findViewById(R.id.etPoscode);
        etAgentCode = findViewById(R.id.etAgentCode);
        tilNik = findViewById(R.id.tilNik);
        tilName = findViewById(R.id.tilName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhone = findViewById(R.id.tilPhone);
        tilAddress = findViewById(R.id.tilAddress);
        tilBirthdate = findViewById(R.id.tilBirthdate);
        tilProvince = findViewById(R.id.tilProvince);
        tilCity = findViewById(R.id.tilCity);
        tilPoscode = findViewById(R.id.tilPoscode);
        tilAgentCode = findViewById(R.id.tilAgentCode);
        tvGenderError = findViewById(R.id.tvGenderError);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        btnRegister = findViewById(R.id.btnRegister);
        tvBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etBirthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(RegisterActivity.this, R.style.BirthdateDialogStyle, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        calendarBirthdate.set(Calendar.YEAR, year);
                        calendarBirthdate.set(Calendar.MONTH, monthOfYear);
                        calendarBirthdate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        etBirthdate.setText("" + dayOfMonth + "-" + (monthOfYear + 1) + "-" + year + " ");
                    }
                }, calendarBirthdate.get(Calendar.YEAR), calendarBirthdate.get(Calendar.MONTH), calendarBirthdate.get(Calendar.DAY_OF_MONTH));
                dpd.setCancelable(false);
                dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        etBirthdate.setText("");
                    }
                });

                dpd.show();
            }
        });

        etProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ProvinceLookup.class);
                startActivityForResult(i, PROVINCE_SELECT_REQUEST);
            }
        });
        etCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stateId == null) {
                    Toast.makeText(RegisterActivity.this, "Tolong pilih provinsi terlebih dahulu", Toast.LENGTH_LONG).show();
                    return;
                }
                Intent i = new Intent(getApplicationContext(), CityLookup.class);
                i.putExtra("province_id", stateId);
                startActivityForResult(i, CITY_SELECT_REQUEST);
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(RegisterActivity.this);
                removeAllErrorMessages();
                if (!TextUtils.isEmpty(etAgentCode.getText())) {
                    checkAgent();
                } else {
                    doRegister();
                }
            }
        });
    }

    private void checkAgent() {
        btnRegister.setEnabled(false);
        loadingBar.setVisibility(View.VISIBLE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("agentcode", etAgentCode.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(Utils.API_URL + "getAgentByCode")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        tvLabelAgent.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue600));
                        tvLabelAgent.setText("Code Agent Benar");
                        tilAgentCode.setError("");

                        doRegister();
                    }

                    @Override
                    public void onError(ANError anError) {
                        tvLabelAgent.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.red500));
                        tilAgentCode.setError("Code Agent Salah");
                        btnRegister.setEnabled(true);
                        loadingBar.setVisibility(View.GONE);
                    }
                });
    }

    private void doRegister() {
        btnRegister.setEnabled(false);
        loadingBar.setVisibility(View.VISIBLE);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("nik", etNik.getText().toString());
            jsonObject.put("name", etName.getText().toString());
            jsonObject.put("email", etEmail.getText().toString());
            jsonObject.put("phone", etPhone.getText().toString());
            jsonObject.put("address", etAddress.getText().toString());
            if (rbMale.isChecked()) {
                jsonObject.put("gender", "male");
            } else if (rbFemale.isChecked()) {
                jsonObject.put("gender", "female");
            }

            if (!TextUtils.isEmpty(etBirthdate.getText())) {
                jsonObject.put("birth_year", calendarBirthdate.get(Calendar.YEAR));
                jsonObject.put("birth_month", calendarBirthdate.get(Calendar.MONTH) + 1);
                jsonObject.put("birth_day", calendarBirthdate.get(Calendar.DAY_OF_MONTH));
            }
            jsonObject.put("province", etProvince.getText().toString());
            jsonObject.put("district", etCity.getText().toString());
            jsonObject.put("zipcode", etPoscode.getText().toString());
            if (!TextUtils.isEmpty(etAgentCode.getText())) {
                jsonObject.put("agent_code", etAgentCode.getText().toString());
            } else {
                tvLabelAgent.setText("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.post(Utils.API_URL + "register")
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        loadingBar.setVisibility(View.GONE);
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setMessage("Pendaftaran berhasil, password anda akan diemail dan disms")
                                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        finish();
                                    }
                                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                finish();
                            }
                        });
                        // Create the AlertDialog object and return it
                        builder.create();
                        builder.show();
                    }

                    @Override
                    public void onError(ANError anError) {
                        try {
                            JSONObject jsonObj = new JSONObject(anError.getErrorBody());
                            if (jsonObj.get("validationError") != null && jsonObj.get("validationError").equals(1)) {
                                JSONArray keyErrors = jsonObj.getJSONObject("errors").names();
                                JSONObject errorObj = jsonObj.getJSONObject("errors");
                                String messages = "";
                                for (int i = 0; i < keyErrors.length(); i++) {
                                    messages += errorObj.getJSONArray(keyErrors.getString(i)).get(0) + "\n";
                                    String message = errorObj.getJSONArray(keyErrors.getString(i)).get(0) + "";
                                    if (keyErrors.getString(i).equalsIgnoreCase("name")) {
                                        tilName.setError(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("phone")) {
                                        tilPhone.setError(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("address")) {
                                        tilAddress.setError(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("gender")) {
                                        tvGenderError.setText(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("province")) {
                                        tilProvince.setError(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("district")) {
                                        tilCity.setError(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("birth_year")) {
                                        tilBirthdate.setError(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("zipcode")) {
                                        tilPoscode.setError(message);
                                    } else if (keyErrors.getString(i).equalsIgnoreCase("nik")) {
                                        tilNik.setError(message);
                                    }
                                }
                                //Toast.makeText(getApplicationContext(), messages, Toast.LENGTH_LONG).show();
                                // tvErrors.setText(messages);
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this, R.style.AlertDialogErrorStyle);
                                builder.setMessage(messages)
                                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                            }
                                        });
                                // Create the AlertDialog object and return it
                                builder.create();
                                builder.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        btnRegister.setEnabled(true);
                        loadingBar.setVisibility(View.GONE);

                    }
                });
    }


    private void removeAllErrorMessages(){
        String message = "";
        tilName.setError(message);
        tilPhone.setError(message);
        tilAddress.setError(message);
        tvGenderError.setText(message);
        tilProvince.setError(message);
        tilCity.setError(message);
        tilBirthdate.setError(message);
        tilPoscode.setError(message);
        tilNik.setError(message);
        tvLabelAgent.setText(message);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == PROVINCE_SELECT_REQUEST && resultCode == RESULT_OK) {
            stateId = data.getIntExtra("id", 1);
            etProvince.setText(data.getStringExtra("name"));
            etCity.setText("");
        } else if (requestCode == CITY_SELECT_REQUEST && resultCode == RESULT_OK) {
            cityId = data.getIntExtra("id", 1);
            etCity.setText(data.getStringExtra("name"));
        }
    }
}
