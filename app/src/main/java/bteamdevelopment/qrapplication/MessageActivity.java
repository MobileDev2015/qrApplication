package bteamdevelopment.qrapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    String qrCode;
    private static Button slideButton, btnHome, btnLogoutUser, btnScanQR, btnMyProfile, sendMessage;;
    private static SlidingDrawer slidingDrawer;

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

        slideButton = (Button) findViewById(R.id.slideButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnScanQR = (Button) findViewById(R.id.btnScanQR);
        btnMyProfile = (Button) findViewById(R.id.btnMyProfile);
        btnLogoutUser = (Button) findViewById(R.id.btnLogoutUser);

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
}
