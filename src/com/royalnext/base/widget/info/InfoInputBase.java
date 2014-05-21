package com.royalnext.base.widget.info;

import android.app.DatePickerDialog;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.royalnext.base.R;
import com.royalnext.base.util.DateTime;
import com.royalnext.base.util.Ran;

import java.util.Calendar;

public abstract class InfoInputBase extends InfoBase {
    public String cur = "";
    public String valStr = "";
    public EditText input_box;
    public TextView error;

    public InfoInputBase(LinearLayout main, int resId) {
        super(main, resId);
        input_box = (EditText) self.findViewById(R.id.info_input_box);
        input_box.setId(Ran.ranInt());
        input_box.setSingleLine(true);

        error = (TextView) self.findViewById(R.id.info_error);
        error.setId(Ran.ranInt());
        error.setVisibility(View.GONE);
    }

    public EditText getInputView() {
        return input_box;
    }

    public void setInput(String src) {
        input_box.setText(src);
        input_box.setSelection(input_box.getText().length());
    }

    public String getInput() {
        return input_box.getText().toString().trim();
    }

    public void setAaUsername() {
        input_box.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    public void setAsPassword() {
        input_box.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void setAsPin() {
        input_box.setInputType(InputType.TYPE_CLASS_NUMBER);
        input_box.setTransformationMethod(PasswordTransformationMethod.getInstance());
    }

    public void setAsNumber() {
        input_box.setInputType(InputType.TYPE_CLASS_NUMBER);
    }

    public void setAsName() {
        input_box.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
    }

    public void setAsCaps() {
        input_box.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
    }

    public void setAsSearch(TextView.OnEditorActionListener listener) {
        input_box.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input_box.setOnEditorActionListener(listener);
    }

    private int mYear;
    private int mMonth;
    private int mDay;

    public String getDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(mYear, mMonth, mDay, 0, 0, 0);
        return cal.getTimeInMillis() + "";
    }

    public void setDate(int year, int month, int date) {
        this.mYear = year;
        this.mMonth = month;
        this.mDay = date;
    }

    private void displayDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(mYear, mMonth, mDay, 0, 0, 0);
        input_box.setText(DateTime.dateF.format(cal.getTime()));
    }

    public void setAsDate() {
        Calendar cal = Calendar.getInstance();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay = cal.get(Calendar.DAY_OF_MONTH);
        displayDate();
        getInputView().setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            displayDate();
                        }
                    };

                    DatePickerDialog dpd = new DatePickerDialog(input_box.getContext(), mDateSetListener, mYear, mMonth, mDay);
                    dpd.show();
                }
                return true;
            }
        });
    }

}
