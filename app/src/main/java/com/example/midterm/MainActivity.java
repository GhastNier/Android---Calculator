package com.example.midterm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static final String operand = "operand";
    public static final String leftOp = "left";
    public static final String rightOp = "right";
    public static SharedPreferences operandSelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        operandSelect = getSharedPreferences(operand, Context.MODE_PRIVATE);
        operandSelect = getSharedPreferences(leftOp, Context.MODE_PRIVATE);
        operandSelect = getSharedPreferences(rightOp, Context.MODE_PRIVATE);
        operandSelect.edit().putString(operand, "left").apply();
        setOp();
        operatorSelect();
        setOperandSide();
        setTheme();
        getSystemService(UI_MODE_SERVICE);
    }

    private void setOp() {
        TextView left = (TextView) findViewById(R.id.leftOperand);
        TextView right = (TextView) findViewById(R.id.rightOperand);
        left.setText("");
        right.setText("");
    }

    public void operatorSelect() {
        RadioGroup rg = (RadioGroup) findViewById(R.id.radioGroup);
        rg.setOnCheckedChangeListener((radioGroup, i) -> {
            View v = radioGroup.findViewById(i);
            int index = radioGroup.indexOfChild(v);
            switch (index) {
                case 0:
                    setOperator("+");
                    break;
                case 1:
                    setOperator("-");
                    break;
                case 2:
                    setOperator("×");
                    break;
                case 3:
                    setOperator("÷");
                    break;
            }
        });
    }

    private void setOperator(String s) {
        TextView tv = (TextView) findViewById(R.id.operator);
        tv.setText(s);
    }

    private void setOperandSide() {
        Button btn = (Button) findViewById(R.id.side);
        SharedPreferences.Editor operandSetter = operandSelect.edit();
        btn.setOnClickListener(view -> {
            String s = btn.getText().toString();
            if (s.equals(getString(R.string.left))) {
                operandSetter.putString("operand", "Right").apply();
                btn.setText(getString(R.string.right));
            } else {
                operandSetter.putString("operand", "Left").apply();
                btn.setText(getString(R.string.left));
            }
        });

    }

    private void setTheme() {
        CompoundButton sw = (CompoundButton) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            setOp();
        });

    }

    public void addNumbers(View view) {
        TextView left = (TextView) findViewById(R.id.leftOperand);
        TextView right = (TextView) findViewById(R.id.rightOperand);
        Button b = (Button) findViewById(view.getId());
        String v = b.getText().toString();
        switch (operandSelect.getString("operand", "").toLowerCase(Locale.ROOT)) {
            case "left":
                left.append(v);
                operandSelect.edit().putString(leftOp, left.getText().toString()).apply();
                break;
            case "right":
                right.append(v);
                operandSelect.edit().putString(rightOp, right.getText().toString()).apply();
                break;
            default:
                break;
        }
    }

    public void calculate(View view) {
        TextView left = (TextView) findViewById(R.id.leftOperand);
        TextView right = (TextView) findViewById(R.id.rightOperand);
        TextView result = (TextView) findViewById(R.id.total);
        TextView operator = (TextView) findViewById(R.id.operator);
        Float total;
        try {
            float valueLeft = Float.parseFloat(left.getText().toString());
            float valueRight = Float.parseFloat(right.getText().toString());
            switch (operator.getText().toString()) {
                case "+":
                    total = valueLeft + valueRight;
                    result.setText(String.valueOf(total));
                    break;
                case "-":
                    total = valueLeft - valueRight;
                    result.setText(String.valueOf(total));
                    break;
                case "×":
                    total = valueLeft * valueRight;
                    result.setText(String.valueOf(total));
                    break;
                case "÷":
                    if (valueRight == 0) {
                        result.setText("∞");
                    } else {
                        total = valueLeft / valueRight;
                        result.setText(String.valueOf(total));
                    }
                    break;
                default:
                    throw new IllegalStateException(getString(R.string.value_error) + operator.getText().toString());
            }

        } catch (NumberFormatException numberFormatException) {
            Toast.makeText(getApplicationContext(), getString(R.string.operand_error), Toast.LENGTH_SHORT).show();
        }

    }

    public void clearOperand(View view) {
        TextView result = (TextView) findViewById(R.id.total);
        result.setText("");
        SharedPreferences.Editor pref = operandSelect.edit();
        pref.putString(leftOp, "").apply();
        pref.putString(rightOp, "").apply();
        setOp();
        Toast.makeText(getApplicationContext(), getString(R.string.reset_toast), Toast.LENGTH_SHORT).show();
    }
}