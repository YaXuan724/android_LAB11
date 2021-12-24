package com.example.lab11;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Currency;

public class MainActivity extends AppCompatActivity {
    private EditText ed_book,ed_price;
    private Button btn_query,btn_insert,btn_update,btn_delete;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> items = new ArrayList<>();
    //建立MyDataBaseHelper物件
    private SQLiteDatabase dbrw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ed_book = findViewById(R.id.ed_book);
        ed_price = findViewById(R.id.ed_price);
        btn_query = findViewById(R.id.btn_query);
        btn_insert = findViewById(R.id.btn_insert);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        listView = findViewById(R.id.listView);
        //宣告Adapter，使用simple_list_item_1並連結listView
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,items);
        listView.setAdapter(adapter);
        //取得資料庫實體
        dbrw = new MyDBHelper(this).getWritableDatabase();



        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //查詢myTable資料表，全部或book欄位為輸入字串(ed_book)的資料
                Cursor c;
                if(ed_book.length()<1)
                    c = dbrw.rawQuery("SELECT * FROM myTable",null);
                else
                    c = dbrw.rawQuery("SELECT * FROM myTable WHERE book " +
                            "LIKE '"+ed_book.getText().toString()+"'",null);
                //從第一筆輸出
                c.moveToFirst();
                items.clear();
                Toast.makeText(MainActivity.this,"共有"+c.getCount()+
                        "筆資料",Toast.LENGTH_SHORT).show();
                for(int i=0;i<c.getCount();i++){
                    //填入書名和價格
                    items.add("書名:"+c.getString(0)+"\t\t\t\t價格:"+c.getString(1));
                    //移動到下一筆
                    c.moveToNext();
                }
                //更新listView內容
                adapter.notifyDataSetChanged();
                //關閉Cursor
                c.close();
            }
        });

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判斷是否沒有填入書名或價格
                if(ed_book.length()<1||ed_price.length()<1)
                    Toast.makeText(MainActivity.this,"欄位請勿留空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try {
                        //新增一筆book與price資料進入myTable資料夾
                        dbrw.execSQL("INSERT INTO myTable(book,price)" +
                                "VALUES(?,?)",new Object[]{ed_book.getText().toString(),
                                                            ed_price.getText().toString()});
                        Toast.makeText(MainActivity.this,
                                "新增書名"+ed_book.getText().toString()
                                        +"    價格"+ed_price.getText().toString(),
                                                    Toast.LENGTH_SHORT).show();
                        //清空輸入框
                        ed_book.setText("");
                        ed_price.setText("");
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this,"新增失敗:"+
                                e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判斷是否沒有填入書名或價格
                if(ed_book.length()<1||ed_price.length()<1)
                    Toast.makeText(MainActivity.this,"欄位請勿留空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try {
                        //更新book欄位為輸入字串(ed_book)的資料的price欄位數值
                        dbrw.execSQL("UPDATE myTable SET price = "+
                                ed_price.getText().toString()+ " WHERE book LIKE'"+
                                ed_book.getText().toString()+"'");
                        Toast.makeText(MainActivity.this,
                                "更新書名"+ed_book.getText().toString()
                        +"    價格"+ed_price.getText().toString(),
                                Toast.LENGTH_SHORT).show();
                        //清空輸入框
                        ed_book.setText("");
                        ed_price.setText("");
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this,"更新失敗:"+
                                e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判斷是否沒有填入書名
                if(ed_book.length()<1)
                    Toast.makeText(MainActivity.this,"書名請勿留空",
                            Toast.LENGTH_SHORT).show();
                else{
                    try{
                        //從myTable資料表刪除book欄位為輸入字串(ed_book)的資料
                        dbrw.execSQL("DELETE FROM myTable WHERE book" +
                                " LIKE'"+ed_book.getText().toString()+"'");
                        Toast.makeText(MainActivity.this,"刪除書名"+
                                ed_book.getText().toString(),Toast.LENGTH_SHORT).show();
                        //清空輸入框
                        ed_book.setText("");
                        ed_price.setText("");
                    }
                    catch (Exception e){
                        Toast.makeText(MainActivity.this,"刪除失敗:"+
                                e.toString(),Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        //關閉資料庫
        dbrw.close();
    }

}

