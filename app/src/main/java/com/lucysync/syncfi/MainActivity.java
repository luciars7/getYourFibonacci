package com.lucysync.syncfi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileWriter;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    public static final String DIRECTORY = "/FIBO_NUMBERS/";

    private EditText editText;
    private TextView numberText;
    private Button enterButton;
    private Button historyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO Split stuff inside functions.
        //TODO Make things scalable.
        editText = findViewById(R.id.editText);
        numberText = findViewById(R.id.numberText);
        enterButton = findViewById(R.id.enterButton);
        historyButton = findViewById(R.id.historyButton);

        manageButtons();
    }

    private void manageButtons() {
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!String.valueOf(editText.getText()).equals(""))
                    computeFibonacci();

                if (!String.valueOf(numberText.getText()).equals("00"))
                    saveRecord(String.valueOf(editText.getText()), String.valueOf(numberText.getText()));
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);

                startActivity(intent);
            }
        });
    }

    private void computeFibonacci() {
        // TODO 1) When numbers are too big, they go negative.
        // TODO 2) When numbers are too long, they don't fit in the screen and go on several lines.
        int f = 1;
        int aux = f;
        int n = Integer.parseInt(String.valueOf(editText.getText()));


        for (int i = 2; i < n; i++) {
            f += aux;
            aux = f-aux;
        }

        numberText.setText(String.valueOf(f));

        Log.d(TAG, "Fibonacci is = " + f);
    }

    private void saveRecord(String recordPosition, String recordNumber) {
        String directoryPath = this.getExternalFilesDir(null).getAbsolutePath() + DIRECTORY;
        String fileName = "fibo-" + getDateAndTime() + ".txt";

        File filePath = new File(directoryPath);
        if (!filePath.exists())
            filePath.mkdir();

        try{
            File fileObj = new File(filePath, fileName);

            if (!fileObj.exists())
                fileObj.createNewFile();

            FileWriter fileWriter = new FileWriter(fileObj);

            String dataToStore = recordPosition + System.lineSeparator() + recordNumber;

            fileWriter.append(dataToStore);

            fileWriter.flush();
            fileWriter.close();

            Log.d(TAG, "Saving data to: " + filePath + "/" + fileName);

        } catch (Exception e) {
            Log.d(TAG, "Could not write to memory..." + e.toString());

            Toast.makeText(this, getResources().getString(R.string.error_file_text), Toast.LENGTH_LONG).show();
        }
    }

    private String getDateAndTime() {
        String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        String monthStr = "";
        if (month.length()<2)
            monthStr = "0" + month;
        else
            monthStr = month;

        String dayOfMonth = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        String dayOfMonthStr = "";
        if (dayOfMonth.length()<2)
            dayOfMonthStr = "0" + dayOfMonth;
        else
            dayOfMonthStr = dayOfMonth;

        String hour = String.valueOf(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        String hourStr = "";
        if (hour.length()<2)
            hourStr = "0" + hour;
        else
            hourStr = hour;

        String minute = String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        String minuteStr = "";
        if (minute.length()<2)
            minuteStr = "0" + minute;
        else
            minuteStr = minute;

        String seconds = String.valueOf(Calendar.getInstance().get(Calendar.SECOND));
        String secondsStr = "";
        if (seconds.length()<2)
            secondsStr = "0" + seconds;
        else
            secondsStr = seconds;


        String dateAndTime = Calendar.getInstance().get(Calendar.YEAR) +
                monthStr + dayOfMonthStr + "_" + hourStr + minuteStr + secondsStr;

        return dateAndTime;
    }
}