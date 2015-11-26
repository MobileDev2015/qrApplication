package bteamdevelopment.qrapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import net.glxn.qrgen.android.QRCode;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginSignupActivity extends AppCompatActivity {
    // Declare Variables
    Button loginbutton;
    Button signup;
    String usernametxt;
    String passwordtxt;
    EditText password;
    EditText username;
    File imageFile;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.activity_login_signup);
        // Locate EditTexts in main.xml
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        // Locate Buttons in main.xml
        loginbutton = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.createUser);

        // Login Button Click Listener
        loginbutton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Send data to Parse.com for verification
                ParseUser.logInInBackground(usernametxt, passwordtxt,
                        new LogInCallback() {
                            public void done(ParseUser user, ParseException e) {
                                if (user != null) {
                                    // If user exist and authenticated, send user to MainActivity.class
                                    Intent intent = new Intent(
                                            LoginSignupActivity.this,
                                            MainActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(),
                                            "Successfully Logged In",
                                            Toast.LENGTH_LONG).show();

                                    ParseUser currentUser = ParseUser.getCurrentUser();
                                    String userName = currentUser.getUsername();

                                    ParseInstallation installation = ParseInstallation.getCurrentInstallation();
                                    installation.put("username", userName);
                                    installation.saveInBackground();

                                    finish();
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Username and Password Combination Does not Exist, Check Login Information.",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        // Sign up Button Click Listener
        signup.setOnClickListener(new View.OnClickListener() {

            public void onClick(View arg0) {
                // Retrieve the text entered from the EditText
                usernametxt = username.getText().toString();
                passwordtxt = password.getText().toString();

                // Force user to fill up the form
                if (usernametxt.equals("") && passwordtxt.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            "Please Complete the Form!",
                            Toast.LENGTH_LONG).show();

                } else {
                    // Save new user data into Parse.com Data Storage
                    ParseUser user = new ParseUser();
                    user.setUsername(usernametxt);
                    user.setPassword(passwordtxt);

                    // Save qrCode (Username) to qrData on parse.com
                    ParseObject qrCode = new ParseObject("qrData");
                    qrCode.put("qrCode", usernametxt);
                    qrCode.saveInBackground();

                    user.signUpInBackground(new SignUpCallback() {
                        public void done(ParseException e) {
                            if (e == null) {
                                // Show a simple Toast message upon successful registration
                                Toast.makeText(getApplicationContext(),
                                        "Successfully Signed up, please log in.",
                                        Toast.LENGTH_LONG).show();
                                saveQrToGallery();
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "Sign up Error", Toast.LENGTH_LONG)
                                        .show();
                            }
                        }
                    });
                }
            }
        });
    }

    public void saveQrToGallery(){

        ParseUser user = ParseUser.getCurrentUser();
        String userName = user.getUsername();

        // QRCode Image
        Bitmap qrBitmap = QRCode.from(userName).bitmap();

        //create the new file with following filePath
        String root = Environment.getExternalStorageDirectory().toString();
        imageFile = new File(root + "/Pictures" + createImageName());

        //try to compress the image in the stream and notify the user the image is saved
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            qrBitmap.compress(Bitmap.CompressFormat.JPEG,90,out);
            out.flush();
            out.close();
            Toast.makeText(LoginSignupActivity.this, "QR Code Saved to: " + imageFile.toString(), Toast.LENGTH_SHORT).show();
        }
        catch (java.io.IOException e)
        {
            e.printStackTrace();
        }
    }

    private String createImageName() {
        //file name is the (name of this app + the date + time) the image was taken
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = sdf.format(new Date());
        return "/QR" + timeStamp + ".jpg";
    }

}
