package vn.com.hoankiem360.activities;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

import vn.com.hoankiem360.R;
import vn.com.hoankiem360.infrastructure.Location;
import vn.com.hoankiem360.utils.Constants;
import vn.com.hoankiem360.utils.JsonKeys;
import vn.com.hoankiem360.utils.PostStringRequest;
import vn.com.hoankiem360.utils.ViewUtils;

/**
 * Created by Binh on 06-Oct-17.
 */

public class BookingActivity extends BaseWithDataActivity implements View.OnClickListener {

    private TextView hotelTitleTV, customerDateStartTV, customerDateEndTV;
    private TextInputLayout customerNameTextInput, customerNumberRoomTextInput,
            customerNumberPeopleTextInput, customerDateStartTextInput, customerDateEndTextInput,
            customerNumberTextInput, customerPhoneTextInput, customerEmailTextInput;
    String customerDateStart, customerDateEnd, customerTypeRoom, customerName, customerPhone,
            customerEmail, customerNumberRoom, customerNumberPeople, hotelId, hotelName,
            hotelEmail, hotelIdManager;
    private Button bookingButton;
    private Location location;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        location = getIntent().getParcelableExtra(Constants.EXTRA_LOCATION);
        location = createFakeLocation();
        Log.d(TAG, "onCreate: location = " + location.toString());
        if (location == null) {
            Log.e(TAG, "onCreate: lỗi 'location == null' khi khởi tạo BookingActivity từ class khác");
            return;
        }

        setContentView(R.layout.activity_booking, null, false);

        setupActivity();

        customerDateStartTV.setOnClickListener(this);
        customerDateEndTV.setOnClickListener(this);
        bookingButton.setOnClickListener(this);
    }

    private void setupActivity() {
        // initialize toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_settings_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setTitle(R.string.booking);
        }

        // initialize views
        hotelTitleTV = (TextView) findViewById(R.id.activity_booking_textView_hotel_title);
        customerNameTextInput = (TextInputLayout) findViewById(R.id.activity_booking_textInputLayout_customer_name);
        customerNumberRoomTextInput = (TextInputLayout) findViewById(R.id.activity_booking_textInputLayout_customer_room_numbers);
        customerNumberPeopleTextInput = (TextInputLayout) findViewById(R.id.activity_booking_textInputLayout_customer_people_numbers);
        customerDateStartTextInput = (TextInputLayout) findViewById(R.id.activity_booking_textInputLayout_customer_date_start);
        customerDateEndTextInput = (TextInputLayout) findViewById(R.id.activity_booking_textInputLayout_customer_date_end);
        customerDateStartTV = (TextView) findViewById(R.id.activity_booking_tv_check_in_date);
        customerDateEndTV = (TextView) findViewById(R.id.activity_booking_tv_check_out_date);
        customerPhoneTextInput = (TextInputLayout) findViewById(R.id.activity_booking_textInputLayout_customer_phone);
        customerEmailTextInput = (TextInputLayout) findViewById(R.id.activity_booking_textInputLayout_customer_email);
        bookingButton = (Button) findViewById(R.id.activity_booking_btn_booking);
        if (application.isVietnamese()) {
            hotelTitleTV.setText(location.getLocationName());
        } else {
            hotelTitleTV.setText(location.getLocationNameEn());
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_booking_tv_check_in_date:
                getDate(customerDateStartTV);
                break;
            case R.id.activity_booking_tv_check_out_date:
                getDate(customerDateEndTV);
                break;
            case R.id.activity_booking_btn_booking:
                getInputData();
                sendDataToServer();
                break;
        }
    }

    private void getInputData() {
        customerName = customerNameTextInput.getEditText().getText().toString();
        customerNumberRoom = customerNumberRoomTextInput.getEditText().getText().toString();
        customerNumberPeople = customerNumberPeopleTextInput.getEditText().getText().toString();
        customerDateStart = customerDateStartTV.getText().toString();
        customerDateEnd = customerDateEndTV.getText().toString();
        customerPhone = customerPhoneTextInput.getEditText().getText().toString();
        customerEmail = customerEmailTextInput.getEditText().getText().toString();
    }

    private boolean checkInputValidation() {

        boolean isFilled = checkEmpty(new String[]{customerName, customerNumberRoom, customerNumberPeople,
                        customerDateStart, customerDateEnd, customerPhone, customerEmail},
                new TextInputLayout[]{customerNameTextInput, customerNumberRoomTextInput,
                        customerNumberPeopleTextInput, customerDateStartTextInput,
                        customerDateEndTextInput, customerPhoneTextInput, customerEmailTextInput},
                R.string.error_missing_info);
        boolean isInputLogical = checkInputLogical();
        return isFilled && isInputLogical;
    }

    private boolean checkInputLogical() {
        // todo: check phone number more details
        if (customerPhone.contains("09")) {
            return (customerPhone.length() == 10);
        } else if (customerPhone.contains("01")) {
            return (customerPhone.length() == 11);
        } else {
            return false;
        }
        // todo : check email.
        // todo: check day start day end.
    }

    private void sendDataToServer() {
        boolean isInputValid = checkInputValidation();

        if (isInputValid) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.uploading_message));
            progressDialog.setCancelable(false);
            progressDialog.show();
            Response.Listener<String> listener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        progressDialog.hide();
                        JSONObject jo = new JSONObject(response);
                        switch (Integer.parseInt(jo.getString(JsonKeys.CODE))) {
                            case 0:
                                // TODO: 07-Oct-17 : design lại thông báo
                                ViewUtils.makeToast(BookingActivity.this, R.string.submit_success);
                                startActivity(new Intent(BookingActivity.this, MainActivity.class));
                                break;
                            case 1:
                                ViewUtils.makeToast(BookingActivity.this, R.string.submit_error);
                                break;
                            default:
                                ViewUtils.makeToast(BookingActivity.this, R.string.error);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            application.getQueue().add(new PostStringRequest(
                    new String[]{JsonKeys.DATE_START, JsonKeys.DATE_END, JsonKeys.TYPE_ROOM, JsonKeys.NAME,
                            JsonKeys.PHONE, JsonKeys.EMAIL, JsonKeys.NUMBER_ROOM, JsonKeys.NUMBER_PEOPLE,
                            JsonKeys.ID_HOTEL, JsonKeys.NAME_HOTEL, JsonKeys.EMAIL_HOTEL, JsonKeys.ID_MANAGER},
                    new String[]{customerDateStart, customerDateEnd, "", customerName,
                            customerPhone, customerEmail, customerNumberRoom, customerNumberPeople,
                            location.getLocationIdHotel(), location.getLocationName(), "", ""},
                    Constants.LINK_ORDER_HOTEL,
                    listener
            ));
        } else {
            Log.d(TAG, "sendDataToServer: checkInputValidation = " + isInputValid);
        }
    }

    private Location createFakeLocation() {
        return new Location("", "Khách sạn Châu Duy Khánh", "No data","", "", "", "598980f8a5d814d74fd854f8");
    }

    private boolean checkEmpty(String[] strings, final TextInputLayout[] layouts, @StringRes int error) {
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].isEmpty()) {
                final TextInputLayout layout = layouts[i];
                layout.setErrorEnabled(true);
                layout.setError(getResources().getString(error));
                layout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        layout.setErrorEnabled(false);
                        layout.setError(null);
                        return false;
                    }
                });
                layout.getEditText().setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        layout.setErrorEnabled(false);
                        layout.setError(null);
                        return false;
                    }
                });
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    private void getDate(final TextView dateSelectTV) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = dayOfMonth + "/" + month + "/" + year;
                dateSelectTV.setText(date);
            }
        };

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                datePickerListener,
                year, month, day);
        assert dialog.getWindow() != null;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (dateSelectTV.getId() == R.id.activity_booking_tv_check_in_date) {
                dialog.setTitle(getResources().getString(R.string.check_in));
            } else {
                dialog.setTitle(getResources().getString(R.string.check_out));
            }
        }
        dialog.show();
    }
}
