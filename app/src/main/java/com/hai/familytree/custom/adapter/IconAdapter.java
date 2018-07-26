package com.hai.familytree.custom.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hai.familytree.R;
import com.hai.familytree.interfaces.ItemOnClick;

/**
 * Created by Hai on 25/07/2018.
 */

public class IconAdapter extends RecyclerView.Adapter<IconAdapter.IconHolder> {
    int pos;
    int icon[] = {
            R.drawable.ic_member_1,
            R.drawable.ic_member_2,
            R.drawable.ic_member_3,
            R.drawable.ic_member_4,
            R.drawable.ic_member_5,
            R.drawable.ic_member_6,
            R.drawable.ic_member_7,
            R.drawable.ic_member_8,
            R.drawable.ic_member_9,
            R.drawable.ic_member_10,
            R.drawable.ic_member_11,
            R.drawable.ic_member_12,
            R.drawable.ic_member_13,
            R.drawable.ic_member_14,
            R.drawable.ic_member_15,
            R.drawable.ic_member_16,
            R.drawable.ic_member_17
    };

    public IconAdapter(int pos, ItemOnClick itemOnClick) {
        this.pos = pos;
        this.itemOnClick = itemOnClick;
    }

    public int getIconCurrent(int pos) {
        return icon[pos];
    }

    public int getPos() {
        return pos;
    }

    ItemOnClick itemOnClick;

    public IconAdapter(ItemOnClick itemOnClick) {
        this.itemOnClick = itemOnClick;
    }

    @Override
    public IconHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon, parent, false);
        return new IconHolder(v);
    }

    @Override
    public void onBindViewHolder(IconHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemOnClick.onClick(icon[position], position);
                pos = position;
            }
        });
        holder.iv.setBackgroundResource(icon[position]);
    }

    @Override
    public int getItemCount() {
        return icon.length;
    }

    class IconHolder extends RecyclerView.ViewHolder {
        ImageView iv;

        public IconHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv_icon);
        }
    }
}
