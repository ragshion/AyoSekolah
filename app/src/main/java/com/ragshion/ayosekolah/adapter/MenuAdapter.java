package com.ragshion.ayosekolah.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.activities.DatazActivity;
import com.ragshion.ayosekolah.activities.DatazDesaActivity;
import com.ragshion.ayosekolah.activities.DatazKecamatanActivity;
import com.ragshion.ayosekolah.activities.MainActivity;
import com.ragshion.ayosekolah.activities.MapsActivity;
import com.ragshion.ayosekolah.mapbox.ClickOnLayerActivity;
import com.ragshion.ayosekolah.objek.MenuData;

import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder> {

    private Context mContext;
    private List<MenuData> mFlowerList;

    public MenuAdapter(Context mContext, List<MenuData> mFlowerList) {
        this.mContext = mContext;
        this.mFlowerList = mFlowerList;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_data, parent, false);
        return new MenuViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder holder, int position) {
        holder.mImage.setImageResource(mFlowerList.get(position).getFlowerImage());
        holder.mTitle.setText(mFlowerList.get(position).getFlowerName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (
                        mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription().equals("DISABILITAS")
                    ){

                    Toast.makeText(mContext, "Maaf, data untuk menu tersebut belum dapat di gunakan", Toast.LENGTH_SHORT).show();

                }else if (mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription().equals("SEKITAR")) {
                    Intent x = new Intent(mContext, ClickOnLayerActivity.class);
                    mContext.startActivity(x);
                }else if(mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription().equals("KECAMATAN")){
                    Intent x = new Intent(mContext, DatazKecamatanActivity.class);
                    x.putExtra("kategori", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription2());
                    x.putExtra("data_kategori", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription());
                    mContext.startActivity(x);
                }else if(mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription().equals("DESA")){
                    Intent x = new Intent(mContext, DatazDesaActivity.class);
                    x.putExtra("kategori", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription2());
                    x.putExtra("data_kategori", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription());
                    mContext.startActivity(x);
                }else{
                    Intent mIntent = new Intent(mContext, DatazActivity.class);
                    mIntent.putExtra("kategori", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription2());
                    mIntent.putExtra("data_kategori", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription());
//                mIntent.putExtra("Description", mFlowerList.get(holder.getAdapterPosition()).getFlowerDescription());
//                mIntent.putExtra("Image", mFlowerList.get(holder.getAdapterPosition()).getFlowerImage());
                    mContext.startActivity(mIntent);
//                Toast.makeText(view.getContext(), mFlowerList.get(holder.getAdapterPosition()).getFlowerName(), Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return mFlowerList.size();
    }
}

class MenuViewHolder extends RecyclerView.ViewHolder {

    ImageView mImage;
    TextView mTitle;
    CardView mCardView;

    MenuViewHolder(View itemView) {
        super(itemView);

        mImage = itemView.findViewById(R.id.ivImage);
        mTitle = itemView.findViewById(R.id.tvTitle);
        mCardView = itemView.findViewById(R.id.cardview);
    }
}
