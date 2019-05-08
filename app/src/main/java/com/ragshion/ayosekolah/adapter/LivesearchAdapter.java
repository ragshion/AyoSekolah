package com.ragshion.ayosekolah.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.objek.Dataz;

import java.util.List;


public class LivesearchAdapter extends RecyclerView.Adapter<LivesearchAdapter.MyViewHolder> {

    private List<Dataz> contacts;
    private Context context;

    public LivesearchAdapter(List<Dataz> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_ats, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        String alamat = contacts.get(position).getAlamat()+" Rt "+contacts.get(position).getRt()+" Rw "+contacts.get(position).getRw()
//                +" Desa "+contacts.get(position).getNama_desa().substring(0, 1).toUpperCase()+contacts.get(position).getNama_desa().substring(1).toLowerCase()
//                +" Kecamatan "+contacts.get(position).getNama_kec().substring(0, 1).toUpperCase()+contacts.get(position).getNama_kec().substring(1).toLowerCase();

        String alamat = contacts.get(position).getAlamat();
        holder.tv_nik.setText(contacts.get(position).getNik());
        holder.tv_nama.setText(contacts.get(position).getNamaLgkp());
        holder.tv_alamat.setText("Alamat : "+alamat);
        holder.tv_kategori.setText("Kategori : "+contacts.get(position).getKategori());
        holder.tv_alasan.setText("Alasan : "+contacts.get(position).getAlasan());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tv_nik, tv_nama, tv_alamat, tv_kategori, tv_alasan;
        ImageView iv_foto;

        public MyViewHolder(View view) {
            super(view);

            tv_nik = view.findViewById(R.id.tv_nik);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_alamat = view.findViewById(R.id.tv_alamat);
            tv_kategori = view.findViewById(R.id.tv_kategori);
            tv_alasan = view.findViewById(R.id.tv_alasan);
            iv_foto = view.findViewById(R.id.iv_foto);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), tv_nama.getText(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
