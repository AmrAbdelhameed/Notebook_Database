package com.example.amr.notebook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class AddFriend extends AppCompatActivity {

    int id_To_Update = 0;
    private DBHelper mydb;
    TextView name;
    TextView phone;
    TextView email;
    TextView street;
    TextView descrip;
    String usu = "";
    ArrayList array_listt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        // Toast.makeText(getApplication(), FirebaseAuth.getInstance().getCurrentUser().getEmail(),Toast.LENGTH_SHORT).show();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (TextView) findViewById(R.id.editTextName);
        phone = (TextView) findViewById(R.id.editTextPhone);
        email = (TextView) findViewById(R.id.editTextStreet);
        street = (TextView) findViewById(R.id.editTextEmail);
        descrip = (TextView) findViewById(R.id.editTextCity);

        mydb = new DBHelper(this);

        array_listt = mydb.getAllCotacts(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int Value = extras.getInt("id");
            String us = extras.getString("nameuser");
            // Toast.makeText(AddFriend.this, us , Toast.LENGTH_SHORT).show();
            if (Value > 0) {
                //means this is the view part not the add contact part.
                Cursor rs = mydb.getData(us, FirebaseAuth.getInstance().getCurrentUser().getEmail());
                id_To_Update = Value;
                usu = us;
                rs.moveToFirst();

                String nam = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_NAME));
                String phon = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_PHONE));
                String emai = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_EMAIL));
                String stree = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_STREET));
                String desc = rs.getString(rs.getColumnIndex(DBHelper.CONTACTS_COLUMN_DES));

                if (!rs.isClosed()) {
                    rs.close();
                }
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.INVISIBLE);

                name.setText((CharSequence) nam);
                name.setFocusable(false);
                name.setClickable(false);
                name.setCursorVisible(false);

                phone.setText((CharSequence) phon);
                phone.setFocusable(false);
                phone.setClickable(false);
                phone.setCursorVisible(false);

                email.setText((CharSequence) emai);
                email.setFocusable(false);
                email.setClickable(false);
                email.setCursorVisible(false);

                street.setText((CharSequence) stree);
                street.setFocusable(false);
                street.setClickable(false);
                street.setCursorVisible(false);

                descrip.setText((CharSequence) desc);
                descrip.setFocusable(false);
                descrip.setClickable(false);
                descrip.setCursorVisible(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                setTitle(usu);
                getMenuInflater().inflate(R.menu.display_contact, menu);
            }
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.Edit_Contact:
                Button b = (Button) findViewById(R.id.button1);
                b.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                name.setFocusableInTouchMode(true);
                name.setClickable(true);
                name.setCursorVisible(true);

                phone.setEnabled(true);
                phone.setFocusableInTouchMode(true);
                phone.setClickable(true);
                phone.setCursorVisible(true);

                email.setEnabled(true);
                email.setFocusableInTouchMode(true);
                email.setClickable(true);
                email.setCursorVisible(true);

                street.setEnabled(true);
                street.setFocusableInTouchMode(true);
                street.setClickable(true);
                street.setCursorVisible(true);

                descrip.setEnabled(true);
                descrip.setFocusableInTouchMode(true);
                descrip.setClickable(true);
                descrip.setCursorVisible(true);

                return true;
            case R.id.Delete_Contact:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.deleteContact)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteContact(usu, FirebaseAuth.getInstance().getCurrentUser().getEmail());
                                Toast.makeText(getApplicationContext(), "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), list_people.class);
                                startActivity(intent);
                                finish();
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
            case R.id.home:
                finish();
                //NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keycode, event);
    }

    public void run(View view) {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int Value = extras.getInt("id");
            if (Value > 0) {
                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Try Again ... ", Toast.LENGTH_SHORT).show();
                } else {
                    if (!usu.equals(name.getText().toString()) && array_listt.contains(name.getText().toString())) {
                        Toast.makeText(AddFriend.this, "This Name is not allowed", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mydb.updateContact(usu, name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), descrip.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), list_people.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Not Updated ... Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } else {
                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please Try Again ... ", Toast.LENGTH_SHORT).show();
                } else {
                    if (!array_listt.contains(name.getText().toString())) {
                        if (mydb.insertContact(name.getText().toString(), phone.getText().toString(), email.getText().toString(), street.getText().toString(), descrip.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Not Done ... Try Again", Toast.LENGTH_SHORT).show();
                        }
                        Intent intent = new Intent(getApplicationContext(), list_people.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "This Name is not allowed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}

