package com.lucysync.syncfi;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * Class that manages an activity that shows a list of the records generated when asking for the
 * Fibonacci numbers. The records are shown in a list where every item in it shows the date when
 * the number was asked, the position in the Fibonacci sequence where it belongs to, and the number
 * that was asked for. This activity is opened when clicking button "check history" in
 * {@link MainActivity}.
 * */
public class RecordsActivity extends AppCompatActivity {

    private static final String TAG = "RecordsActivity";

    private TextView mNoRecordsText;
    private RecyclerView mRecyclerViewRecords;

    private RecordAdapter mAdapter;

    private int mPosition;

    private Record mNewRecord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records);

        // Initialize ui elements
        mNoRecordsText = findViewById(R.id.noRecordsText);
        mRecyclerViewRecords = findViewById(R.id.recyclerViewRecords);
        mRecyclerViewRecords.setLayoutManager(new LinearLayoutManager(RecordsActivity.this));

        mPosition = 0;

        getRecords();
    }

    /**
     * Terminate activity when clicking "back button" of android system.
     * */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }

    /**
     * Function that reads system directory where records are saved. If there are any, they are
     * saved in an array and shown on screen in a recycler view list.
     * */
    private void getRecords() {
        String directoryPath = this.getExternalFilesDir(null).getAbsolutePath() +
                MainActivity.DIRECTORY;
        File filePath = new File(directoryPath);

        File[] files = filePath.listFiles();

        // If there are no records generated yet, a message is shown on screen
        if (files.length <= 0)
            modifyVisibility(true);
        else {
            modifyVisibility(false);

            for (int i = 0; i < files.length; i++)
            {
                mNewRecord = new Record();
                mNewRecord.setDate(computeDateString(files[i].getName()));
                String[] rawData = getDataFromFile(files[i]);
                mNewRecord.setPosition(rawData[0]);
                mNewRecord.setNumber(rawData[1]);

                Log.d(TAG, "FileName: " + files[i].getName() + " - Record: " + mNewRecord);

                updateUI(true);
            }
        }
    }

    /**
     * Function that transforms date string from file name into format "YYYY/MM/DD HH:MM:SS".
     * @param date date string that comes in format "YYYYMMDD_HHMMSS".
     * */
    private String computeDateString(String date) {
        // Get only date and time from file name
        date = date.substring(5, date.length()-4);

        String[] dateData = date.split("_");
        String rawDate = dateData[0];
        String rawTime = dateData[1];

        // Get date string
        String dateString = "";
        for (int i = 0; i < rawDate.length(); i++) {
            if (i == 0)
                dateString += rawDate.substring(i, i+4) + "/";
            else if (i == 4)
                dateString += rawDate.substring(i, i+2) + "/";
            else if (i == 6)
                dateString += rawDate.substring(i, i+2);
        }

        // Get time string
        String timeString = "";
        for (int i = 0; i < rawTime.length(); i++) {
            if (i % 2 == 0) {
                if (i < rawTime.length() - 2)
                    timeString += rawTime.substring(i, i+2) + ":";
                else
                    timeString += rawTime.substring(i, i+2);
            }
        }

        return dateString + " " + timeString;
    }

    /**
     * Opens the file and gets the data that is saved in it.
     *
     * @param file the file that contains the data.
     * */
    private String[] getDataFromFile(File file) {
        String[] rawData = new String[2];

        try {
            if (file.exists()) {
                BufferedReader buffer = new BufferedReader(new FileReader(file));

                int index = 0;
                while (index < 2){
                    rawData[index] = buffer.readLine();
                    index += 1;
                }

                buffer.close();
            }
        }
        catch (Exception e) {
            Log.d(TAG, "There was an error while reading a file: " + e);

            Toast.makeText(this, getResources().getString(R.string.error_read_file_text),
                    Toast.LENGTH_LONG).show();
        }

        return rawData;
    }



    /**
     * Function that helps managing UI items visibility whether there are any records to show or
     * not.
     *
     * @param noRecords true if there are no records to show, false otherwise.
     * */
    private void modifyVisibility(boolean noRecords) {
        if (noRecords) {
            mNoRecordsText.setVisibility(View.VISIBLE);
            mRecyclerViewRecords.setVisibility(View.GONE);
        }
        else {
            mNoRecordsText.setVisibility(View.GONE);
            mRecyclerViewRecords.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Function that updates list every time a new record file is opened and read.
     *
     * @param newItem whether new item is added to the list or not.
     * */
    private void updateUI (boolean newItem) {
        if (mAdapter == null)
            mAdapter = new RecordAdapter();
        else if (newItem) {
            mAdapter.addNewRecord(mNewRecord);
            mAdapter.notifyItemInserted(mPosition);
        }
        mRecyclerViewRecords.setAdapter(mAdapter);
    }

    private class RecordHolder extends RecyclerView.ViewHolder {
        private TextView timeText;
        private TextView positionText;
        private TextView numberText;

        public RecordHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize variables
            timeText = itemView.findViewById(R.id.timeText);
            positionText = itemView.findViewById(R.id.positionText);
            numberText = itemView.findViewById(R.id.numberText);
        }

        /**
         * Function that adds data to item in list.
         *
         * @param record the record object containing all data.
         * */
        private void bindRecord(Record record) {
            timeText.setText(record.getDate());
            String posString = getResources().getString(R.string.position_text) + " " +
                    record.getPosition() + " - ";
            positionText.setText(posString);
            numberText.setText(record.getNumber());
        }
    }

    private class RecordAdapter extends RecyclerView.Adapter<RecordsActivity.RecordHolder> {
        private ArrayList<Record> recordList;

        public RecordAdapter() {
            recordList = new ArrayList<>();
            mPosition = 0;
        }

        @NonNull
        @Override
        public RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(RecordsActivity.this);
            View view = layoutInflater.inflate(R.layout.list_item_record, parent, false);

            return new RecordHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecordHolder holder, int position) {
            holder.bindRecord(recordList.get(position));
        }

        @Override
        public int getItemCount() {
            return recordList.size();
        }

        /**
         * Function that adds a new record when a file is opened and its data is read.
         *
         * @param r record object containing data from file.
         * */
        public void addNewRecord(Record r) {
            recordList.add(r);
            mPosition += 1;
        }
    }
}
