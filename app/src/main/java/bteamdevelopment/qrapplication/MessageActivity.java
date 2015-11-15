package bteamdevelopment.qrapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener {

    String qrCode;
    Button sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        // Get QRCode from Previous Intent
        Intent qrMessage = getIntent();
        qrCode = qrMessage.getStringExtra("qrCode");

        sendMessage.setOnClickListener(this);
        TextView qrCode = (TextView)findViewById(R.id.qrCode);
        qrCode.setText(qrCode.toString());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.buttonSend){

            // Send Message

            Toast.makeText(MessageActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
        }
    }
}
