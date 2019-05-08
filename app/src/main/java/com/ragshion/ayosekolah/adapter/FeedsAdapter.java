package com.ragshion.ayosekolah.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.activities.DetailActivity;
import com.ragshion.ayosekolah.objek.Dataz;

import java.util.List;


public class FeedsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public final int TYPE_MOVIE = 0;
    public final int TYPE_LOAD = 1;

    static Context context;
    List<Dataz> movies;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading = false, isMoreDataAvailable = true;
//    SharedPrefManager sharedPrefManager;

    public FeedsAdapter(Context context, List<Dataz> movies) {
        this.context = context;
        this.movies = movies;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if(viewType==TYPE_MOVIE){
            return new DatazHolder(inflater.inflate(R.layout.recycler_feed_ats,parent,false));

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
        TextView tv_ats_nik, tv_ats_nama, tv_ats_alamat, tv_ats_kategori, tv_ats_alasan, tv_ats_tgl, tv_ats_usia, tv_ats_jk;
        ImageView iv_ats_foto;

        TextView feed_nama_png;

        String id;

        TextView nama;
        String alamat,kelurahan,kecamatan,kategori,hbuka,htutup,jbuka,pagi,sore,jtutup,pemilik,latitude,longitude,foto,stats="1";
        TextView hari,jarak;
        TextView jam;
        TextView status;
        ImageView image;

        public DatazHolder(View view){
            super(view);

            tv_ats_nik = view.findViewById(R.id.tv_ats_nik);
            tv_ats_nama = view.findViewById(R.id.tv_ats_nama);
            tv_ats_alamat = view.findViewById(R.id.tv_ats_alamat);
            tv_ats_kategori = view.findViewById(R.id.tv_ats_kategori);
            tv_ats_alasan = view.findViewById(R.id.tv_ats_alasan);
            tv_ats_tgl = view.findViewById(R.id.tv_ats_tgl);
            tv_ats_usia = view.findViewById(R.id.tv_ats_usia);
            tv_ats_jk = view.findViewById(R.id.tv_ats_jk);
            iv_ats_foto = view.findViewById(R.id.iv_ats_foto);
            feed_nama_png = view.findViewById(R.id.feed_nama_png);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        if(stats.equalsIgnoreCase("1")){
                            Intent intent = new Intent(v.getContext(), DetailActivity.class);
                            intent.putExtra("id",id);
//                            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//
//
                            v.getContext().startActivity(intent);
//                            Toast.makeText(v.getContext(), "hmmm", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(v.getContext(), "Maaf, Fasilitas Kesehatan yang anda ajukan sedang dalam proses verifikasi, Mohon untuk Ditunggu", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            });
        }

        void bindData(Dataz dataz){

            tv_ats_nik.setText(dataz.getNik());
            tv_ats_nama.setText(dataz.getNamaLgkp());
            tv_ats_alamat.setText(dataz.getAlamat());
            tv_ats_kategori.setText(dataz.getKategori());
            tv_ats_alasan.setText(dataz.getAlasan());
            tv_ats_tgl.setText(dataz.getTglLahir());
            tv_ats_usia.setText(dataz.getUsia());
            tv_ats_jk.setText(dataz.getJk());
            feed_nama_png.setText(dataz.getUser());
            ViewGroup.LayoutParams layout = iv_ats_foto.getLayoutParams();
            if (!dataz.getFoto().equals("default")){
                iv_ats_foto.setVisibility(View.VISIBLE);
                layout.height = 240;
                iv_ats_foto.setLayoutParams(layout);
                String url = itemView.getResources().getString(R.string.pathfoto)+dataz.getFoto();
                Glide.with(itemView.getContext())
                        .load(url)
                        .apply(new RequestOptions().override(96, 64))
                        .into(iv_ats_foto);
            }else{
                iv_ats_foto.setImageResource(R.drawable.ic_photo);
                iv_ats_foto.setVisibility(View.GONE);
                layout.height = 0;
                iv_ats_foto.setLayoutParams(layout);
            }

            id = dataz.getNo();


//
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
