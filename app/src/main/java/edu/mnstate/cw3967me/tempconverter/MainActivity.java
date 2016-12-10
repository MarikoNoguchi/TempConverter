package edu.mnstate.cw3967me.tempconverter;
/*
convert temperture from Fahrenheit to Celsius
Mariko Noguchi
9/30/2016
 */


import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements TextView.OnEditorActionListener {
    private EditText etFahrenheit;
    private TextView tvCelciusValue;
    private ImageView imgPic;

    private float f;//fahrenheit temperature
    private float c;//celcius temperature
    String result;//celcius temprrature as a formatted string

    // define the SharedPreferences object
    private SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFahrenheit = (EditText) findViewById(R.id.etFahrenheit);
        tvCelciusValue = (TextView) findViewById(R.id.tvCelciusValue);
        imgPic = (ImageView) findViewById(R.id.imgPic);

        // set the listeners for EditText
        etFahrenheit.setOnEditorActionListener(this);

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

    @Override
    public void onPause() {
        // save the instance variables
        SharedPreferences.Editor editor = savedValues.edit();
        editor.putFloat("f", f);
        editor.putFloat("c", c);
        editor.putString("rsult",result);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        String sf = etFahrenheit.getText().toString();
        if(sf.equals("")) {
            reset();
        } else {
            //get the instance variables
            f = savedValues.getFloat("f", 0);
            c = savedValues.getFloat("c", 0);
            result = savedValues.getString("result",result);
            calc();
            showPic();
        }
    }

    //set listener for EditText
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {//if done button is pressed
            try {
                f = Float.parseFloat(etFahrenheit.getText().toString());
                calc();
                showPic();
            } catch (NumberFormatException ex) {
                f = 0.f; // default
                reset();
            }
            return true;
        }
        return false;
    }

    //convert from fahrenheit to celsius
    public void calc() {
        c = (f - 32) * 5 / 9;
        result = String.format("%.3f", c);
        tvCelciusValue.setText(result);
    }

    //show pictures according to the celsius temperature
    public void showPic() {
        if (c >= 35.0) imgPic.setImageResource(R.drawable.sun);
        else if (c <= 15.0) imgPic.setImageResource(R.drawable.icicle);
        else if (20 <= c && 30 >= c) imgPic.setImageResource(R.drawable.smile);
        else imgPic.setImageResource(0);
    }

    public void click_reset(View v) {
        reset();
    }

    public void reset() {
        etFahrenheit.getEditableText().clear();//clear an EditText
        tvCelciusValue.setText("");
        imgPic.setImageResource(0);//clear an ImageView
    }
}
