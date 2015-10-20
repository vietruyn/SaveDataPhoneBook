package com.example.admin.savedataphonebook.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.savedataphonebook.R;
import com.example.admin.savedataphonebook.model.PhoneBook;

import java.util.ArrayList;

/**
 * Created by jce on 8/10/15.
 */
public class PhoneBookAdapter extends BaseAdapter {

    Context context;
    ArrayList<PhoneBook> listData;

    public PhoneBookAdapter(Context context, ArrayList<PhoneBook> listData) {
        this.context = context;
        this.listData = listData;
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private TextView tvUserName, tvPhoneNumber, tvEmail;
        private LinearLayout  lnAppNotInstalled;
        private RelativeLayout lnAppInstalled;
        private ImageView imgAgree, imgCancer, imgAddFriend, imgSentRequest;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_phonebook, null);
            viewHolder = new ViewHolder();
            viewHolder.tvUserName = (TextView) view.findViewById(R.id.tvName);
            viewHolder.tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
            viewHolder.lnAppInstalled = (RelativeLayout) view.findViewById(R.id.lnAppInstalled);
            viewHolder.lnAppNotInstalled = (LinearLayout) view.findViewById(R.id.lnAppNotInstalled);
            viewHolder.imgAgree = (ImageView) view.findViewById(R.id.imgAgree);
            viewHolder.imgCancer = (ImageView) view.findViewById(R.id.imgCancer);
            viewHolder.imgAddFriend = (ImageView) view.findViewById(R.id.imgAddFriend);
            viewHolder.imgSentRequest = (ImageView) view.findViewById(R.id.imgSentRequest);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        final PhoneBook phoneBook = listData.get(position);
        String userName = phoneBook.getName();
        String phoneNumber = phoneBook.getPhoneNumber();

        viewHolder.imgAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneBook.setType(2);
                notifyDataSetChanged();
                                Toast.makeText(context,phoneBook.getPhoneNumber().toString(),Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.imgCancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listData.remove(position);
                phoneBook.setType(3);
                notifyDataSetChanged();
            }
        });
        final ViewHolder finalViewHolder = viewHolder;
        viewHolder.imgAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalViewHolder.imgAddFriend.setVisibility(View.INVISIBLE);
                finalViewHolder.imgSentRequest.setVisibility(View.VISIBLE);

                //Socket


            }
        });
        int type = phoneBook.getType();
        switch (type){
            case 2:
                view.setBackgroundResource(R.drawable.img_itemcheck);
                viewHolder.lnAppInstalled.setVisibility(View.INVISIBLE);
                viewHolder.lnAppNotInstalled.setVisibility(View.INVISIBLE);
                break;
            case 3:
                view.setBackgroundResource(R.drawable.img_itemcheck);
                viewHolder.lnAppInstalled.setVisibility(View.VISIBLE);
                viewHolder.lnAppNotInstalled.setVisibility(View.INVISIBLE);
                break;
            case 0:
                view.setBackgroundResource(R.drawable.img_itemuncheck);
                viewHolder.lnAppNotInstalled.setVisibility(View.VISIBLE);
                viewHolder.lnAppInstalled.setVisibility(View.INVISIBLE);
                break;
            case 1:
                view.setBackgroundResource(R.drawable.img_itemuncheck);
                viewHolder.lnAppNotInstalled.setVisibility(View.VISIBLE);
                viewHolder.lnAppInstalled.setVisibility(View.INVISIBLE);
                break;
        }
        viewHolder.tvUserName.setText(userName);
        viewHolder.tvPhoneNumber.setText(phoneNumber);
        return view;
    }
}