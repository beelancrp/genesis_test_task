package com.beelancrp.genesis.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beelancrp.genesis.R;
import com.beelancrp.genesis.data.SearchItem;

import java.util.List;


public class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainVH> {
    private List<SearchItem> list;
    private OnSearchItemClickListener listener;

    public MainAdapter(List<SearchItem> list, OnSearchItemClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @Override
    public MainVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_search, parent, false);
        return new MainVH(view);
    }

    @Override
    public void onBindViewHolder(MainVH holder, int position) {
        final int pos = position;
        holder.getRootView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onSearchItemClicked(list.get(pos).getItemLink());
            }
        });
        holder.title.setText(list.get(position).getItemTitle());
        holder.link.setText(list.get(position).getItemLink());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MainVH extends RecyclerView.ViewHolder {
        View rootView;
        TextView title, link;

        MainVH(View itemView) {
            super(itemView);
            rootView = itemView;
            title = (TextView) itemView.findViewById(R.id.item_search_title_TV);
            link = (TextView) itemView.findViewById(R.id.item_for_search_link_TV);
        }

        public View getRootView() {
            return rootView;
        }
    }
}
