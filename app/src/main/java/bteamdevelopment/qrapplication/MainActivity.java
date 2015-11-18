package bteamdevelopment.qrapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseACL;
import com.parse.ParseObject;
import com.parse.ParseUser;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Declare Variable
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private static Button slideButton, home, logoutUser, scanQR, myProfile;
    private static TextView textView;
    private static SlidingDrawer slidingDrawer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the view
        setContentView(R.layout.activity_main);

        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Convert currentUser into String
        String struser = currentUser.getUsername();

        // Locate TextView in welcome.xml
        TextView txtuser = (TextView) findViewById(R.id.txtuser);

        // Set the currentUser String into TextView
        txtuser.setText("You are logged in as " + struser);

        slideButton = (Button) findViewById(R.id.slideButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.SlidingDrawer);
        home = (Button) findViewById(R.id.home);
        scanQR = (Button) findViewById(R.id.scanQR);
        myProfile = (Button) findViewById(R.id.myProfile);
        logoutUser = (Button) findViewById(R.id.logoutUser);

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
    }

        // Listeners method
        void setListeners() {
            home.setOnClickListener(this);
            myProfile.setOnClickListener(this);
            scanQR.setOnClickListener(this);
            logoutUser.setOnClickListener(this);
        }

    @Override
    public void onClick(View v) {

        // Toast shown on sliding drawer items click
        if (v.getId() == R.id.home) {
            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(homeIntent);
        }

        if (v.getId() == R.id.myProfile)
        {
            Intent profileIntent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(profileIntent);
        }

        if (v.getId() == R.id.scanQR)
        {
            scanQR(v);
        }

        if (v.getId() == R.id.myProfile)
        {
            Intent profileIntent = new Intent(MainActivity.this, UserProfile.class);
            startActivity(profileIntent);
        }

        if (v.getId() == R.id.logoutUser)
        {
            ParseUser.logOut();
            Intent intent = new Intent(MainActivity.this, LoginSignupActivity.class);
            startActivity(intent);
            finish();
        }

    }

    //product qr code mode
    public void scanQR(View v) {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            showDialog(MainActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                // Set read/write Access
                ParseACL defaultACL = new ParseACL();
                defaultACL.setPublicReadAccess(true);
                defaultACL.setPublicWriteAccess(true);
                ParseACL.setDefaultACL(defaultACL, true);

                // Add Contents to Parse.com Database
                ParseObject qrData = new ParseObject("qrData");

                qrData.put("qrCode", contents);

                qrData.saveInBackground();

                Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG).show();

                Intent sendMessage = new Intent(MainActivity.this, MessageActivity.class);
                sendMessage.putExtra("qrCode", contents);
                startActivity(sendMessage);
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
            case R.id.home:
                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.profile:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.scan:
                Toast.makeText(getApplicationContext(),"Item 3 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.logout:
                Toast.makeText(getApplicationContext(),"Item 4 Selected",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}