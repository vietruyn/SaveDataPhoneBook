package com.example.admin.savedataphonebook;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.admin.savedataphonebook.connect.ConnectWebservice;
import com.example.admin.savedataphonebook.model.PhoneBook;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ListPhoneBookActivity extends Activity {

    private ListView lvPhoneBook;
    private ArrayList<PhoneBook> phoneBookArrayList;

    private String phoneNumber = null;
    private String email = null;

    private Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
    private String _ID = ContactsContract.Contacts._ID;
    private String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
    private String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

    private Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
    private String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
    private String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

    private Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
    private String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
    private String DATA = ContactsContract.CommonDataKinds.Email.DATA;

    private StringBuffer output = new StringBuffer();

    private ProgressDialog mProgressDialog;
    private Dialog dialog;
    private LinearLayout lnCallapp, lnCallphone, lnChat, lnSms;
    private String url, type;
    RegistrationAdapter adapter;
    RegistrationOpenHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonebook);
        lvPhoneBook = (ListView) findViewById(R.id.lvPhoneNumber);
        new getListContact().execute();

    }

    class getListContact extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(ListPhoneBookActivity.this);
            mProgressDialog.setMessage("Please Waiting...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ContentResolver contentResolver = getContentResolver();
            adapter = new RegistrationAdapter(ListPhoneBookActivity.this);
            Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null, null);

            phoneBookArrayList = new ArrayList<PhoneBook>();
            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    PhoneBook phoneBook = new PhoneBook();
                    String contact_id = cursor.getString(cursor.getColumnIndex(_ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {

                        output.append("\n First Name:" + name);
                        phoneBook.setName(name);

                        // Query and loop for every phone number of the contact
                        Cursor phoneCursor = contentResolver.query(PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?", new String[]{contact_id}, null);

                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            output.append("\n Phone number:" + phoneNumber);
                            phoneBook.setPhoneNumber(phoneNumber);

                        }

                        //Connect Sever
                        String url = "http://closechat.com/closechatwebservice/index.php?route=webservice/users/phonebook&username=?&array_telephone=" + phoneNumber.replaceAll("\\D+", "");
                        Log.e("URL: ", url);
                        ConnectWebservice connectWebservice = new ConnectWebservice(url);
                        connectWebservice.fetchJSON();
                        while (connectWebservice.parsingComplete) ;

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(connectWebservice.getData());
                            JSONObject number = jsonObject.getJSONObject(phoneNumber.replaceAll("\\D+", ""));
                            type = number.getString("check");
                            phoneBook.setType(Integer.parseInt(type));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        phoneCursor.close();

                        // Query and loop for every email of the contact
                        Cursor emailCursor = contentResolver.query(EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?", new String[]{contact_id}, null);

                        while (emailCursor.moveToNext()) {

                            email = emailCursor.getString(emailCursor.getColumnIndex(DATA));
                            phoneBook.setEmail(email);
                            output.append("\nEmail:" + email);
                        }

                        emailCursor.close();
                        long val = adapter.insertDetails(name, phoneNumber, email, Integer.parseInt(type));
                        Log.e("PhoneBook: ", name + " " + phoneNumber + " " + type + "\n");
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mProgressDialog.hide();
            Cursor cursor;
            String[] from = { helper.FNAME, helper.PNUMBER};
            int[] to = { R.id.tvName, R.id.tvPhoneNumber };
            cursor = adapter.queryName();
            SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(ListPhoneBookActivity.this,
                    R.layout.list_item_phonebook, cursor, from, to);
            lvPhoneBook.setAdapter(cursorAdapter);

            lvPhoneBook.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (phoneBookArrayList.get(position).getType() == 2) {
                        dialog = new Dialog(ListPhoneBookActivity.this, R.style.mydialogstyle);
                        dialog.setContentView(R.layout.custom_dialog);
                        lnCallapp = (LinearLayout) dialog.findViewById(R.id.callApp);
                        lnCallphone = (LinearLayout) dialog.findViewById(R.id.callPhone);
                        lnChat = (LinearLayout) dialog.findViewById(R.id.chat);
                        lnSms = (LinearLayout) dialog.findViewById(R.id.sms);
                        dialog.show();
                    }
                }
            });


        }
    }

}