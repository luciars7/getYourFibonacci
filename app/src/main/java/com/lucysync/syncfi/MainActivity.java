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

/**
 * Class that manages landing (main) activity of the app. Here, a number is asked to user in order
 * to show another number. The number asked is the position in the fibonacci sequence, and the one
 * shown corresponds to that inside the fibonacci sequence according to position entered.
 * */
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

        // Initialize ui elements
        editText = findViewById(R.id.editText);
        numberText = findViewById(R.id.numberText);
        enterButton = findViewById(R.id.enterButton);
        historyButton = findViewById(R.id.historyButton);

        manageButtons();
    }

    /**
     * Function that manages buttons functionality.
     * */
    private void manageButtons() {
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Only compute Fibonacci if entered text in "edit text" is any number
                if (!String.valueOf(editText.getText()).equals(""))
                    computeFibonacci();
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start "records activity" to see history of numbers previously asked for
                Intent intent = new Intent(MainActivity.this, RecordsActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Function that computes fibonacci number.
     * */
    private void computeFibonacci() {
        long f = 1;
        long aux = f;
        int p = Integer.parseInt(String.valueOf(editText.getText()));

        for (int i = 2; i < p; i++) {
            f += aux;
            aux = f - aux;
        }

        Log.d(TAG, "Fibonacci is = " + f);

        // Only show and store number if it fits the variable, otherwise a number that doesn't
        // belong to the sequence would be shown
        if (f < 0)
            numberText.setText(R.string.too_big_text);
        else {
            numberText.setText(String.valueOf(f));
            saveRecord(String.valueOf(editText.getText()), String.valueOf(numberText.getText()));
        }
    }

    /**
     * Function that saves the record in a file. The file stores both position in the sequence and
     * the number. File is created with format "fibo" as of "Fibonacci"; "date-time" to know when
     * this record was generated; ".txt" as it's stored in this format.
     *
     * @param recordPosition position in the Fibonacci sequence
     * @param recordNumber   Fibonacci number corresponding in that position
     * */
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

            Toast.makeText(this, getResources().getString(R.string.error_write_file_text),
                    Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Function that generates a date and time in format YYYY/MM/DD_HH:MM:SS.
     * */
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