package mil.android.babysitter.adapter;

import android.content.Context;

import java.util.List;

import mil.android.babysitter.data.User;

public class CardViewAdapter {

    private List<User> listUsers;
    private Context context;

    public CardViewAdapter(List<User> listUsers, Context context) {
        this.listUsers = listUsers;
        this.context = context;
    }

    public void addUser(User user){
        listUsers.add(user);
        //notify
    }
}
