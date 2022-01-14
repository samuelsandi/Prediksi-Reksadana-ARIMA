package com.ta.prediksireksadanaarima;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Base class of all Activities of the Demo Application.
 *
 * @author Philipp Jahoda
 */
public  class DemoBase extends AppCompatActivity {

    protected final String[] months = new String[] {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"
    };

    protected Typeface tfRegular;
    protected Typeface tfLight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        tfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }
}
