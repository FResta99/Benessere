package com.interfacciabili.benessere.adapter;

import android.content.ServiceConnection;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.interfacciabili.benessere.R;
import com.interfacciabili.benessere.control.DatabaseService;
import com.interfacciabili.benessere.model.Prodotto;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingViewHolder>  {
    private List<Prodotto> mProductList;
    private DatabaseService mDatabaseService;
    private setOnItemClickListener mListener;

    public interface setOnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(setOnItemClickListener listener){
        mListener = listener;
    }


    public static class ShoppingViewHolder extends RecyclerView.ViewHolder {
        public CheckBox mCheckBox;
        public TextView mTextview;

        public ShoppingViewHolder(@NonNull View itemView, setOnItemClickListener listener) {
            super(itemView);
            mCheckBox = itemView.findViewById(R.id.mCheckbox);
            mTextview = itemView.findViewById(R.id.tvItemSpesa);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position!=RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ShoppingListAdapter(DatabaseService databaseService){
        mDatabaseService = databaseService;
    }

    @NonNull
    @Override
    public ShoppingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spesa, parent, false);
        ShoppingViewHolder svh = new ShoppingViewHolder(v, mListener);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingViewHolder holder, int position) {
        Prodotto prodottoCorrente = mProductList.get(position);
        holder.mTextview.setText(prodottoCorrente.getNome());
        holder.mCheckBox.setChecked(prodottoCorrente.getStatus() == 1 ? true : false);
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mDatabaseService.aggiornaStatus(prodottoCorrente.getId(), 1);
                } else {
                    mDatabaseService.aggiornaStatus(prodottoCorrente.getId(), 0);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mProductList.size();
    }

    public void setProductList(List<Prodotto> mList){
        this.mProductList = mList;
        notifyDataSetChanged();
    }

    public void cancellaProdotto(int position){
        Prodotto item = mProductList.get(position);
        mDatabaseService.eliminaProdotto(item.getId());
        mProductList.remove(position);
        notifyItemRemoved(position);
    }

    public void aggiornaProdotto(int position){
        Prodotto item = mProductList.get(position);
/*
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(mActivity.getSupportFragmentManager(), task.getTag());

 */
    }
}
