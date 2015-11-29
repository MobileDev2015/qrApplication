package bteamdevelopment.qrapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Date;

import bteamdevelopment.qrapplication.custom.CustomActivity;
import bteamdevelopment.qrapplication.model.Conversation;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    String qrCode;
    private static Button slideButton, btnHome, btnLogoutUser, btnScanQR, btnMyProfile, btnSendMessage;
    private static SlidingDrawer slidingDrawer;

    /** The Conversation list. */
    private ArrayList<Conversation> convList;

    /** The chat adapter. */
    private ChatAdapter adp;

    /** The Editext to compose the message. */
    private EditText userMessage;

    /** The user name of buddy. */
    private String buddy;

    /** The date of last message in conversation. */
    private Date lastMsgDate;

    /** Flag to hold if the activity is running or not. */
    private boolean isRunning;

    /** The handler. */
    private static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Get QRCode from Previous Intent
        Bundle qrMessage = getIntent().getExtras();
        qrCode = qrMessage.getString("qrCode");

       // sendMessage.setOnClickListener(this);
        TextView qrCodeTextView = (TextView)findViewById(R.id.qrCode);
        qrCodeTextView.setText(qrCode.toString());

        userMessage = (EditText)findViewById(R.id.editTextSMS);
        adp = new ChatAdapter();
        buddy = qrCode.toString();

        slideButton = (Button) findViewById(R.id.slideButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnScanQR = (Button) findViewById(R.id.btnScanQR);
        btnMyProfile = (Button) findViewById(R.id.btnMyProfile);
        btnLogoutUser = (Button) findViewById(R.id.btnLogoutUser);
        btnSendMessage = (Button)findViewById(R.id.buttonSend);

        // Setting Listeners to all buttons and textview
        setListeners();

        // Listeners for sliding drawer
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {

                // Change button text when slider is open
                slideButton.setText("-");
            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {

                // Change button text when slider is close
                slideButton.setText("+");
            }
        });

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
                Toast.makeText(getApplicationContext(), "Send Message",Toast.LENGTH_LONG).show();
            }
        });

    }

    // Listeners method
    void setListeners() {
        btnHome.setOnClickListener(this);
        btnMyProfile.setOnClickListener(this);
        btnScanQR.setOnClickListener(this);
        btnLogoutUser.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSend){

            // Send Message

            Toast.makeText(MessageActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();

            // Toast shown on sliding drawer items click
            if (v.getId() == R.id.btnHome) {
                Intent homeIntent = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }

            if (v.getId() == R.id.btnMyProfile)
            {
                Intent profileIntent = new Intent(MessageActivity.this, UserProfile.class);
                startActivity(profileIntent);
            }

            if (v.getId() == R.id.btnScanQR)
            {
                MainActivity scan = new MainActivity();
                scan.scanQR(v);
            }

            if (v.getId() == R.id.btnMyProfile)
            {
                Intent profileIntent = new Intent(MessageActivity.this, UserProfile.class);
                startActivity(profileIntent);
            }

            if (v.getId() == R.id.btnLogoutUser)
            {
                ParseUser.logOut();
                Intent intent = new Intent(MessageActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);//Menu Resource, Menu
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                Intent homeIntent = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.menu_profile:
                Intent profileIntent = new Intent(MessageActivity.this, UserProfile.class);
                startActivity(profileIntent);
                return true;
            case R.id.menu_logout:
                ParseUser.logOut();
                Intent intent = new Intent(MessageActivity.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sendMessage()
    {

        if (userMessage.length() == 0)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(userMessage.getWindowToken(), 0);

        String s = userMessage.getText().toString();
        ParseUser currentUser = ParseUser.getCurrentUser();
        final Conversation c = new Conversation(s, new Date(),
                currentUser.getUsername());
        c.setStatus(Conversation.STATUS_SENDING);
//        convList.add(c);
//        adp.notifyDataSetChanged();
//        userMessage.setText(null);

        ParseACL defaultACL = new ParseACL();
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true); //objects created are writable
        ParseACL.setDefaultACL(defaultACL, true);

        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.saveInBackground();

        ParseQuery pushQuery = ParseInstallation.getQuery();
        pushQuery.whereEqualTo("username", buddy);
        ParsePush push = new ParsePush();
        push.setQuery(pushQuery);
        push.setMessage("You have a new message");
        push.sendInBackground();

        ParseObject po = new ParseObject("Chat");
        po.setACL(defaultACL);
        po.put("sender", currentUser.getUsername());
        po.put("receiver", buddy);
        // po.put("createdAt", "");
        po.put("message", s);
        po.saveEventually(new SaveCallback() {

            @Override
            public void done(ParseException e)
            {
                if (e == null)
                    c.setStatus(Conversation.STATUS_SENT);
                else
                    c.setStatus(Conversation.STATUS_FAILED);
                adp.notifyDataSetChanged();
            }
        });
    }

    private class ChatAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return convList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Conversation getItem(int arg0)
        {
            return convList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItemId(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            Conversation c = getItem(pos);
            if (c.isSent())
                v = getLayoutInflater().inflate(R.layout.chat_item_sent, null);
            else
                v = getLayoutInflater().inflate(R.layout.chat_item_rcv, null);

            TextView lbl = (TextView) v.findViewById(R.id.lbl1);
            lbl.setText(DateUtils.getRelativeDateTimeString(MessageActivity.this, c
                            .getDate().getTime(), DateUtils.SECOND_IN_MILLIS,
                    DateUtils.DAY_IN_MILLIS, 0));

            lbl = (TextView) v.findViewById(R.id.lbl2);
            lbl.setText(c.getMsg());

            lbl = (TextView) v.findViewById(R.id.lbl3);
            if (c.isSent())
            {
                if (c.getStatus() == Conversation.STATUS_SENT)
                    lbl.setText("Delivered");

                else if (c.getStatus() == Conversation.STATUS_SENDING)
                    lbl.setText("Sending...");
                else
                    lbl.setText("Failed");
            }
            else
                lbl.setText("");

            return v;
        }

    }
}
