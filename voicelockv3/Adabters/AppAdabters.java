package com.voicelock.voicelockv3.Adabters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.voicelock.voicelockv3.INterface.AppOnClickListener;
import com.voicelock.voicelockv3.R;
import com.voicelock.voicelockv3.ViewHolder.AppViewHolder;
import com.voicelock.voicelockv3.model.AppItem;
import com.voicelock.voicelockv3.utils.Utils;

import java.util.List;

public class AppAdabters extends RecyclerView.Adapter<AppViewHolder> {

    private Context mContext;
    private List<AppItem> appItemsList;
    private Utils utils;

    public AppAdabters(Context mContext, List<AppItem> appItemsList) {
        this.mContext = mContext;
        this.appItemsList = appItemsList;
        this.utils = new Utils(mContext);
    }

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_apps,parent,false);
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppViewHolder holder, int position) {
        holder.name_app.setText(appItemsList.get(position).getName());
        holder.icone_app.setImageDrawable(appItemsList.get(position).getIcone());

        final String pk = appItemsList.get(position).getPackageName();
        if (utils.isLock(pk)){
            holder.lock_app.setImageResource(R.drawable.ic_lock_outline_black_24dp);
        }else{
            holder.lock_app.setImageResource(R.drawable.ic_lock_open_black_24dp);
        }

        holder.setListener((new AppOnClickListener() {
            @Override
            public void selectApp(int pos) {
                if (utils.isLock(pk)){
                    holder.lock_app.setImageResource(R.drawable.ic_lock_open_black_24dp);
                    utils.unlock(pk);
                }else{
                    holder.lock_app.setImageResource(R.drawable.ic_lock_outline_black_24dp);
                    utils.lock(pk);
                }
            }
        }));

    }

    @Override
    public int getItemCount() {
        return appItemsList.size();
    }
}
