package bteamdevelopment.qrapplication;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseUser;
import com.parse.SaveCallback;

import net.glxn.qrgen.android.QRCode;

/**
 * Created by Andrew on 11/15/2015.
 */
public class UserProfile extends Activity implements View.OnClickListener{
    Button changePassword, btnSave, btnCancel;
    EditText txtPassword, txtConfirmPassword;

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

        usernameEt.setText(user.getUsername());
        changePassword.setOnClickListener(this);
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


}
