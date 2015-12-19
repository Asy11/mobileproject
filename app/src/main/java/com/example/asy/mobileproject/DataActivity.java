package com.example.asy.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataActivity extends AppCompatActivity {

    TextView textView;
    EditText editText;
    Button bt_exit;
    Button bt_remove;

    // Database 관련 객체들
    SQLiteDatabase db;
    String dbName = "idList.db"; // name of Database;
    String tableName = "idListTable"; // name of Table;
    int dbMode = Context.MODE_PRIVATE;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        textView = (TextView) findViewById(R.id.editText);
        Intent intent = getIntent(); //전달된 인텐트
        String textIn = intent.getStringExtra("TextIn");

        bt_exit = (Button) findViewById(R.id.bt_exit);
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText = (EditText) findViewById(R.id.editText_remove);

        bt_remove = (Button) findViewById(R.id.bt_remove);
        bt_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    removeData(Integer.parseInt(editText.getText().toString()));
                    textView.setText(PrintData());
                }
                catch(NumberFormatException e){
                    Toast.makeText(DataActivity.this.getApplicationContext(), "입력이 잘못되었습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        String[] data = textIn.split(",");
        double lat = Double.parseDouble(data[0]);
        double lng = Double.parseDouble(data[1]);
        String text = data[2];


        // // Database 생성 및 열기
        db = openOrCreateDatabase(dbName, dbMode, null);
        // 테이블 생성
        createTable();

        //Insert
        insertData(text, lat, lng);

        textView.setText(PrintData());

    }

    // Table 생성
    public void createTable() {
        try {
            String sql = "create table " + tableName + "(id integer primary key autoincrement, "
                    + "name text not null, "
                    + "lat double not null, "
                    + "lng double not null)";
            db.execSQL(sql);
        } catch (android.database.sqlite.SQLiteException e) {
            Log.d("Lab sqlite", "error: " + e);
        }
    }

    // Data 추가
    public void insertData(String name, double lat, double lng) {
        String sql = "insert into " + tableName + " values(NULL, '" + name + "'," + lat + ',' + lng +");";
        db.execSQL(sql);
    }

    // Data 삭제
    public void removeData(int index) {
        String sql = "delete from " + tableName + " where id = " + index + ";";
        db.execSQL(sql);
    }

    // 모든 Data 읽기
    public String PrintData() {
        String str = "";

        Cursor cursor = db.rawQuery("select * from idListTable", null);
        while(cursor.moveToNext()) {
            str += cursor.getInt(0)
                    + " ) 일 혹은 사건 : "
                    + cursor.getString(1)
                    + "\n\n";
        }

        return str;
    }

}
