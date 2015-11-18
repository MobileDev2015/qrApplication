package bteamdevelopment.qrapplication;

/**
 * Created by wkohusjr on 11/18/2015.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import bteamdevelopment.qrapplication.custom.CustomActivity;
import bteamdevelopment.qrapplication.utils.Const;
import bteamdevelopment.qrapplication.utils.Utils;

/**
 * The Class UserList is the Activity class. It shows a list of all users of
 * this app. It also shows the Offline/Online status of users.
 */
public class UserList extends CustomActivity
{

    /** The Chat list. */
    private ArrayList<ParseUser> uList;

    /** The user. */
    public static ParseUser user;

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_list);


    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onDestroy()
     */
    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        loadUserList();

    }

    /**
     * Load list of users.
     */
    private void loadUserList()
    {
        final ProgressDialog dia = ProgressDialog.show(this, null,
                getString(R.string.alert_loading));
        // Retrieve current user from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseUser.getQuery().whereNotEqualTo("username", currentUser.getUsername())
                .findInBackground(new FindCallback<ParseUser>() {

        @Override
        public void done(List<ParseUser> li, ParseException e) {
            dia.dismiss();
            if (li != null) {
                if (li.size() == 0)
                    Toast.makeText(UserList.this,
                            R.string.msg_no_user_found,
                            Toast.LENGTH_SHORT).show();

                uList = new ArrayList<ParseUser>(li);
                ListView list = (ListView) findViewById(R.id.list);
                list.setAdapter(new UserAdapter());
                list.setOnItemClickListener(new OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int pos, long arg3) {
                        startActivity(new Intent(UserList.this,
                                Chat.class).putExtra(
                                Const.EXTRA_DATA, uList.get(pos)
                                        .getUsername()));
                    }
                });
            } else {
                Utils.showDialog(
                        UserList.this,
                        getString(R.string.err_users) + " "
                                + e.getMessage());
                e.printStackTrace();
            }
        }
    });
    }

    /**
     * The Class UserAdapter is the adapter class for User ListView. This
     * adapter shows the user name and it's only online status for each item.
     */
    private class UserAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount()
        {
            return uList.size();
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public ParseUser getItem(int arg0)
        {
            return uList.get(arg0);
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
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.chat_item, null);

            ParseUser c = getItem(pos);
            TextView lbl = (TextView) v;
            lbl.setText(c.getUsername());
            lbl.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_online, 0, R.drawable.arrow, 0);

            return v;
        }

    }
}
