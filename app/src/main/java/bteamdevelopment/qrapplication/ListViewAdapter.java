package bteamdevelopment.qrapplication;

/**
 * Created by wkohusjr on 11/19/2015.
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context context;
    LayoutInflater inflater;
    private List<Message> messageList = null;
    private ArrayList<Message> arraylist;
    AlertDialog test;
    Dialog colorDialog;

    // Constructor
    public ListViewAdapter(Context context,
                           List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
        inflater = LayoutInflater.from(context);
        this.arraylist = new ArrayList<>();
        this.arraylist.addAll(messageList);
    }

    public class ViewHolder {
        TextView sender;
        TextView receiver;
        TextView message;
    }

    // Get Count
    @Override
    public int getCount() {
        return messageList.size();
    }

    // Get Item at Passed in Position
    @Override
    public Object getItem(int position) {
        return messageList.get(position);
    }

    // Get ID and Return Position
    @Override
    public long getItemId(int position) {
        return position;
    }

    // Get Items for MainActivity ListView
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.chat_list, null);
            // Locate the TextViews in listview_item.xml
            holder.sender = (TextView) view.findViewById(R.id.sender);
//            holder.receiver = (TextView) view.findViewById(R.id.receiver);
            holder.message = (TextView) view.findViewById(R.id.message);
            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        // Set the results into TextViews
        holder.sender.setText(messageList.get(position).getSender());

        holder.message.setText(messageList.get(position).getMessage());

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder test = new AlertDialog.Builder(context);
                test.setTitle("Delete Message");
                test.setMessage("Are you sure you want to delete the message?");
                test.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ParseObject.createWithoutData("Chat", messageList.get(position).getId()).deleteEventually();
                        dialog.dismiss();
                        Intent intent = new Intent(context, MainActivity.class);
                        context.startActivity(intent);

                    }
                });

                // Cancel Save
                test.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                test.show();

                return false;
            }
        });

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                // Send single item click data to Chat Class
                Intent intent = new Intent(context, Chat.class);
                // Pass all data ID
                intent.putExtra("objectId",
                        messageList.get(position).getId());
                // Pass all data Sender
                intent.putExtra("sender",
                        (messageList.get(position).getSender()));
                // Pass all data Message
                intent.putExtra("message",
                        (messageList.get(position).getMessage()));

                // Start Chat Class
                context.startActivity(intent);
            }
        });
        return view;
    }

}