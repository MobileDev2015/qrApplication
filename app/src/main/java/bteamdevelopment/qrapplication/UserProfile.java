package bteamdevelopment.qrapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.glxn.qrgen.android.QRCode;

/**
 * Created by Andrew on 11/15/2015.
 */
@SuppressWarnings("deprecation")
public class UserProfile extends AppCompatActivity implements OnClickListener{
    // Change Password Buttons
    Button btnChange, btnSave, btnCancel, btnreSave;
    EditText txtPassword, txtConfirmPassword, txtUsername;

    // Menu Slider
    private static Button btnSlideButton, btnHome, btnLogoutUser, btnScanQR, btnMyProfile;
    private static TextView textView;
    private static SlidingDrawer slidingDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ImageView qrImage = (ImageView) findViewById(R.id.usersQR);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        btnChange = (Button)findViewById(R.id.btnChange);
        btnSlideButton = (Button) findViewById(R.id.slideButton);
        slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        btnHome = (Button) findViewById(R.id.btnHome);
        btnScanQR = (Button) findViewById(R.id.btnScanQR);
        btnMyProfile = (Button) findViewById(R.id.btnMyProfile);
        btnLogoutUser = (Button) findViewById(R.id.btnLogoutUser);
        btnreSave = (Button) findViewById(R.id.btnReSave);

        ParseUser user = ParseUser.getCurrentUser();
        String userName = user.getUsername();

        Bitmap qrBitmap = QRCode.from(userName).bitmap();

        qrImage.setImageBitmap(qrBitmap);

        txtUsername.setText(user.getUsername());

        // Setting Listeners to all buttons and textview
        setListeners();

        // Listeners for sliding drawer
        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {

                // Change button text when slider is open
                btnSlideButton.setText("-");
            }
        });

        slidingDrawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {

                // Change button text when slider is close
                btnSlideButton.setText("+");
            }
        });
    }

    @Override
    public void onClick(View v) {

        // Toast shown on sliding drawer items click
        if (v.getId() == R.id.btnHome) {
            Intent homeIntent = new Intent(UserProfile.this, MainActivity.class);
            startActivity(homeIntent);
        }

        // Go to Profile Activity
        if (v.getId() == R.id.btnMyProfile)
        {
            Intent profileIntent = new Intent(UserProfile.this, UserProfile.class);
            startActivity(profileIntent);
        }

        // Launch Scan Activity
        if (v.getId() == R.id.btnScanQR)
        {
            MainActivity scan = new MainActivity();
            scan.scanQR(v);
        }

        // View Profile Intent
        if (v.getId() == R.id.btnMyProfile)
        {
            Intent profileIntent = new Intent(UserProfile.this, UserProfile.class);
            startActivity(profileIntent);
        }

        // Log User Out - Return to Sign Up/Sign In Form
        if (v.getId() == R.id.btnLogoutUser)
        {
            ParseUser.logOut();
            Intent intent = new Intent(UserProfile.this, LoginSignupActivity.class);
            startActivity(intent);
            finish();
        }

        // Re-Save Image to Device
        if (v == btnreSave) {
            LoginSignupActivity reSave = new LoginSignupActivity();
            reSave.saveQrToGallery();
        }

        if (v == btnChange ) {

            // Create Object of Dialog class
            final Dialog changePw = new Dialog(this);
            // Set GUI of changePw screen
            changePw.setContentView(R.layout.password_dialog);
            changePw.setTitle("Change Password");

            // Init button of changePw GUI
            btnSave = (Button) changePw.findViewById(R.id.save);
            btnCancel = (Button) changePw.findViewById(R.id.cancel);
            txtPassword = (EditText)changePw.findViewById(R.id.password);
            txtConfirmPassword = (EditText)changePw.findViewById(R.id.confirmPassword);

            // Attached listener for changePw GUI button
            btnSave.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Change Password Button OnclickListeners
                    if (checkPassword())
                    {
                        ParseUser user = ParseUser.getCurrentUser();
                        user.setPassword(txtPassword.getText().toString());
                        user.saveInBackground(new SaveCallback() {
                            public void done(com.parse.ParseException e) {
                                // TODO Auto-generated method stub
                                if (e == null) {
                                    Toast.makeText(UserProfile.this,
                                            "User Password Changed", Toast.LENGTH_LONG).show();
                                            changePw.dismiss();
                                } else {
                                    Toast.makeText(UserProfile.this,
                                            "User Password not Changed", Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    } else {
                        Toast.makeText(UserProfile.this,
                                "Passwords do not Match!", Toast.LENGTH_LONG).show();
                    }


                }
            });
            btnCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    changePw.dismiss();
                }
            });

            // Make dialog box visible.
            changePw.show();
        }
    }

    // Listeners method
    void setListeners() {
        btnHome.setOnClickListener(this);
        btnMyProfile.setOnClickListener(this);
        btnScanQR.setOnClickListener(this);
        btnLogoutUser.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnreSave.setOnClickListener(this);

    }

    public Boolean checkPassword()
    {
        String strPass1 = txtPassword.getText().toString();
        String strPass2 = txtConfirmPassword.getText().toString();
        if (strPass1.equals(strPass2)) {
            return true;
        } else {
            return false;
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
                Intent homeIntent = new Intent(UserProfile.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.menu_profile:
                Intent profileIntent = new Intent(UserProfile.this, UserProfile.class);
                startActivity(profileIntent);
                return true;
            case R.id.menu_logout:
                ParseUser.logOut();
                Intent intent = new Intent(UserProfile.this, LoginSignupActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
