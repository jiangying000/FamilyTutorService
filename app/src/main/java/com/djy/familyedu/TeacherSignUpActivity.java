package com.djy.familyedu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by djy-ubuntu16 on 10/4/17.
 */

public class TeacherSignUpActivity extends AppCompatActivity {
    private AutoCompleteTextView mMobileNumberView;
//    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
//    private EditText mUsernameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_login);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.parent_register_title));

        mMobileNumberView = findViewById(R.id.mobile_number);
        mPasswordView = findViewById(R.id.password);

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
    }

    private void attemptLogin() {
//        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mMobileNumberView.setError(null);

//        final String username = mUsernameView.getText().toString();
        final String password = mPasswordView.getText().toString();
        final String mobile_number = mMobileNumberView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (!TextUtils.isEmpty(mobile_number) && !isMobileNumberValid(mobile_number)) {
            mPasswordView.setError(getString(R.string.error_invalid_mobile_number));
            focusView = mPasswordView;
            cancel = true;
        }

//        if (TextUtils.isEmpty(username)) {
//            mUsernameView.setError(getString(R.string.error_field_required));
//            focusView = mUsernameView;
//            cancel = true;
//        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            showProgress(true);
            AVUser user = new AVUser();

//            user.setUsername(username);
            user.setPassword(password);
            user.setMobilePhoneNumber(mobile_number);
//            user.put("mobilePhoneNumber", mobile_number);


            user.signUpInBackground(new SignUpCallback() {
                public void done(AVException e) {
                    if (e == null) {
                        TeacherSignUpActivity.this.finish();
                        startActivity(new Intent(TeacherSignUpActivity.this, SucceededLoginActivity.class));
                    } else {
                        showProgress(false);
                        Toast.makeText(TeacherSignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private boolean isMobileNumberValid(String mobile_number) {
        return mobile_number.length() == 11 && mobile_number.startsWith("1");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 6;
    }

    private void showProgress(final boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }
}
