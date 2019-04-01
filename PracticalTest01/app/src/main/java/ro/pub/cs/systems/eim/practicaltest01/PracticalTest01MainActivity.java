package ro.pub.cs.systems.eim.practicaltest01;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PracticalTest01MainActivity extends AppCompatActivity {

    private final static int SECONDARY_ACTIVITY_REQUEST_CODE = 1;

    private EditText number1EditText;
    private EditText number2EditText;
    private Button number1Button;
    private Button number2Button;
    private Button switchAtivity;
    private Boolean serviceStatus = Constants.SERVICE_STOPPED;

    GeneralOnClickListener listener = new GeneralOnClickListener();
    private class GeneralOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int numberOfClicks = Integer.parseInt(number1EditText.getText().toString()) +
                    Integer.parseInt(number2EditText.getText().toString());
            Integer value1 = Integer.valueOf((number1EditText.getText().toString()));
            Integer value2 = Integer.valueOf((number2EditText.getText().toString()));

            switch (v.getId()) {
                case R.id.number_1_button:
                    value1++;
                    number1EditText.setText(value1.toString());
                    break;
                case R.id.number_2_button:
                    value2++;
                    number2EditText.setText(value2.toString());
                    break;
                case R.id.navigate_to_secondary_activity:
                    Intent intent = new Intent(getApplicationContext(), PracticalTest01SecondaryActivity.class);
                    intent.putExtra("numberOfClicks", numberOfClicks);
                    startActivityForResult(intent, SECONDARY_ACTIVITY_REQUEST_CODE);
                    break;
            }

            if (numberOfClicks > Constants.NUMBER_OF_CLICKS_THRESHOLD
                    && serviceStatus == Constants.SERVICE_STOPPED) {
                Intent intent = new Intent(getApplicationContext(), PracticalTest01Service.class);
                intent.putExtra("firstNumber", value1);
                intent.putExtra("secondNumber", value2);
                getApplicationContext().startService(intent);
                serviceStatus = Constants.SERVICE_STARTED;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test01_main);

        number1EditText = (EditText)findViewById(R.id.name_edit_text1);
        number2EditText = (EditText)findViewById(R.id.name_edit_text2);

        number1EditText.setText(String.valueOf(0));
        number2EditText.setText(String.valueOf(0));
        number1Button = (Button)findViewById(R.id.number_1_button);
        number2Button = (Button)findViewById(R.id.number_2_button);
        switchAtivity = (Button)findViewById(R.id.navigate_to_secondary_activity);
        number1Button.setOnClickListener(listener);
        number2Button.setOnClickListener(listener);
        switchAtivity.setOnClickListener(listener);

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("leftCount", number1EditText.getText().toString());
        savedInstanceState.putString("rightCount", number2EditText.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("leftCount")) {
            number1EditText.setText(savedInstanceState.getString("leftCount"));
        } else {
            number1EditText.setText(String.valueOf(0));
        }
        if (savedInstanceState.containsKey("rightCount")) {
            number2EditText.setText(savedInstanceState.getString("rightCount"));
        } else {
            number2EditText.setText(String.valueOf(0));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == SECONDARY_ACTIVITY_REQUEST_CODE) {
            Toast.makeText(this, "The activity returned with result " + resultCode, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(this, PracticalTest01Service.class);
        stopService(intent);
        super.onDestroy();
    }
}
