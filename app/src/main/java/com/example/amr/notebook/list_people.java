package com.example.amr.notebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class list_people extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<String>();
    Intent intent;
    public final static String EXTRA_MESSAGE = "MESSAGE";
    private ListView obj;
    ArrayAdapter arrayAdapter;
    ArrayList array_list;
    EditText editText;
    EditText e;
    DBHelper mydb;
    int size_arraylist = 0;
    TextView textempty ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_people);
        //Toast.makeText(getApplication(), FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();
        intent = getIntent();

        editText = (EditText) findViewById(R.id.txtsearch);
        textempty = (TextView)findViewById(R.id.textempty);
        mydb = new DBHelper(this);
        array_list = mydb.getAllCotacts(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        arrayAdapter = new ArrayAdapter(list_people.this, android.R.layout.simple_list_item_1, array_list);
        size_arraylist = array_list.size();
        if (size_arraylist == 0)
        {
            textempty.setVisibility(View.VISIBLE);
            editText.setVisibility(View.INVISIBLE);
        }
        else {
            textempty.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(list_people.this, size_arraylist , Toast.LENGTH_SHORT).show();
        obj = (ListView) findViewById(R.id.listView1);
        obj.setAdapter(arrayAdapter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equalsIgnoreCase("")) {
                    // reset listview
                    array_list = mydb.getAllCotacts(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    arrayAdapter = new ArrayAdapter(list_people.this, android.R.layout.simple_list_item_1, array_list);

                    obj = (ListView) findViewById(R.id.listView1);
                    obj.setAdapter(arrayAdapter);
                } else {
                    // perform search
                    searchItem(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                // TODO Auto-generated method stub
                int id_To_Search = arg2 + 1;
                // Toast.makeText(list_people.this,arrayAdapter.getItem(arg2).toString(), Toast.LENGTH_SHORT).show();
                Bundle dataBundle = new Bundle();
                // dataBundle.putInt("size",size_arraylist);
                dataBundle.putInt("id", id_To_Search);
                dataBundle.putString("nameuser", arrayAdapter.getItem(arg2).toString());
                Intent intent = new Intent(getApplicationContext(), AddFriend.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
    }

    public void searchItem(String textToSearch) {
        for (String item : mydb.getAllCotacts(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            if (!item.contains(textToSearch)) {
                array_list.remove(item);
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int a = item.getItemId();
        if (a == R.id.item1) {
            Bundle dataBundle = new Bundle();
            dataBundle.putInt("id", 0);
            Intent intent = new Intent(getApplicationContext(), AddFriend.class);
            intent.putExtras(dataBundle);
            startActivity(intent);
            return true;
        }
        if (a == R.id.item2) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Logout ?!")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseAuth.getInstance().signOut();
                            finish();
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog d = builder.create();
            d.setTitle("Are you sure");
            d.show();
            return true;
        }
        if (a == R.id.item3) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to delete all your data ?!")
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            if (size_arraylist > 0) {

                                mydb.deleteAllContact(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                Toast.makeText(getApplicationContext(), "Deleted All Data Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(getIntent());

                            } else {
                                Toast.makeText(getApplicationContext(), "There is Nothing", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                }
            });
            AlertDialog d = builder.create();
            d.setTitle("Are you sure");
            d.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keycode, event);
    }

}
