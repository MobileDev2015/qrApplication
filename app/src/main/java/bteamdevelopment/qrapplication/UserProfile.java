package bteamdevelopment.qrapplication;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.parse.ParseUser;

import net.glxn.qrgen.android.QRCode;
import com.parse.ParseUser;

/**
 * Created by Andrew on 11/15/2015.
 */
public class UserProfile extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        ImageView qrImage = (ImageView) findViewById(R.id.usersQR);
        EditText usernameEt = (EditText) findViewById(R.id.username);

        ParseUser user = ParseUser.getCurrentUser();

        String userName = user.getUsername();

        Bitmap qrBitmap = QRCode.from(userName).bitmap();
        qrImage.setImageBitmap(qrBitmap);

        usernameEt.setText(user.getUsername());
    }
}
