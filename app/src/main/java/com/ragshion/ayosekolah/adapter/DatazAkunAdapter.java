package com.ragshion.ayosekolah.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.objek.Dataz;
import com.squareup.picasso.Picasso;

import java.util.List;


public class DatazAkunAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<Dataz> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;

    public DatazAkunAdapter(Context context, List<Dataz> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new DatazHolder(inflater.inflate(R.layout.recyclerview_item_ats_akun,parent,false));

        }else{
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if(position>=getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null){
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_MOVIE){
            ((DatazHolder)holder).bindData(movies.get(position));
        }
        //No else part needed as load holder doesn't bind any data
    }

    @Override
    public int getItemViewType(int position) {
        if(movies.get(position).verifikasi.equalsIgnoreCase("0") | movies.get(position).verifikasi.equalsIgnoreCase("1") | movies.get(position).verifikasi.equalsIgnoreCase("2") | movies.get(position).verifikasi.equalsIgnoreCase("LAKI-1")){
            return TYPE_MOVIE;
        }else{
            return TYPE_LOAD;
        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class DatazHolder extends RecyclerView.ViewHolder{
        TextView tv_nik, tv_nama, tv_alamat, tv_kategori, tv_alasan, tv_verifikasi;
        ImageView iv_foto;

        String stats="1";
        String id;


        public DatazHolder(View view){
            super(view);

            tv_nik = view.findViewById(R.id.tv_nik);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_alamat = view.findViewById(R.id.tv_alamat);
            tv_kategori = view.findViewById(R.id.tv_kategori);
            tv_alasan = view.findViewById(R.id.tv_alasan);
            iv_foto = view.findViewById(R.id.iv_foto);
            tv_verifikasi = view.findViewById(R.id.tv_verifikasi);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if(stats.equalsIgnoreCase("1")){
                            Toast.makeText(v.getContext(), "hmmm", Toast.LENGTH_SHORT).show();
                        }else{

                        }
                    }
                }
            });
        }

        void bindData(Dataz dataz){

            id = dataz.getNo();
            tv_nik.setText(dataz.getNik());
            tv_nama.setText(dataz.getNamaLgkp());
            tv_alamat.setText("Alamat : "+dataz.getAlamat());
            tv_kategori.setText("Kategori : "+dataz.getKategori());
            tv_alasan.setText("Alasan : "+dataz.getAlasan());
//            tv_verifikasi.setText(dataz.getVerifikasi());

            if (dataz.getVerifikasi().equals("0")){
                tv_verifikasi.setText("Sedang di Proses");
            }else if (dataz.getVerifikasi().equals("1")){
                tv_verifikasi.setText("Terverifikasi");
            }else{
                tv_verifikasi.setText("Verifikasi Gagal");
            }


            if (!dataz.getFoto().equals("default")){
                String url = itemView.getResources().getString(R.string.pathfoto)+dataz.getFoto();


                Glide.with(itemView.getContext())
                        .load(url)
                        .apply(new RequestOptions().override(96, 64))
                        .into(iv_foto);
            }else{
                iv_foto.setImageResource(R.drawable.ic_photo);
            }



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder{
        public LoadHolder(View itemView) {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable) {
        isMoreDataAvailable = moreDataAvailable;
    }

    public void notifyDataChanged(){
        notifyDataSetChanged();
        isLoading = false;
    }


    public interface OnLoadMoreListener{
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

}
