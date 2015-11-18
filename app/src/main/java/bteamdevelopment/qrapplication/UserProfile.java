package bteamdevelopment.qrapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
public class UserProfile extends AppCompatActivity implements View.OnClickListener{
    // Change Password Buttons
    Button changePassword, btnSave, btnCancel;
    EditText txtPassword, txtConfirmPassword;

    // Menu Slider
    private static Button slideButton, home, logoutUser, scanQR, myProfile;
    private static TextView textView;
    private static SlidingDrawer slidingDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ImageView qrImage = (ImageView) findViewById(R.id.usersQR);
        EditText usernameEt = (EditText) findViewById(R.id.username);
        changePassword = (Button)findViewById(R.id.changePWButton);

        ParseUser user = ParseUser.getCurrentUser();
        String userName = user.getUsername();

        Bitmap qrBitmap = QRCode.from(userName).bitmap();

        qrImage.setImageBitmap(qrBitmap);

        usernameEt.setText("Username: " + user.getUsername());
        changePassword.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        if (v == changePassword ) {

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
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Toast shown on sliding drawer items click
                    if (v.getId() == R.id.home) {
                        Intent homeIntent = new Intent(UserProfile.this, MainActivity.class);
                        startActivity(homeIntent);
                    }

                    if (v.getId() == R.id.myProfile)
                    {
                        Intent profileIntent = new Intent(UserProfile.this, UserProfile.class);
                        startActivity(profileIntent);
                    }

                    if (v.getId() == R.id.scanQR)
                    {
                        MainActivity scan = new MainActivity();
                        scan.scanQR(v);
                    }

                    if (v.getId() == R.id.myProfile)
                    {
                        Intent profileIntent = new Intent(UserProfile.this, UserProfile.class);
                        startActivity(profileIntent);
                    }

                    if (v.getId() == R.id.logoutUser)
                    {
                        ParseUser.logOut();
                        Intent intent = new Intent(UserProfile.this, LoginSignupActivity.class);
                        startActivity(intent);
                        finish();
                    }

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
            btnCancel.setOnClickListener(new View.OnClickListener() {
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
        home.setOnClickListener(this);
        myProfile.setOnClickListener(this);
        scanQR.setOnClickListener(this);
        logoutUser.setOnClickListener(this);
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
