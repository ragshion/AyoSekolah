package com.ragshion.ayosekolah.fragment_menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.ragshion.ayosekolah.R;
import com.ragshion.ayosekolah.activities.DatazActivity;
import com.ragshion.ayosekolah.adapter.MenuAdapter;
import com.ragshion.ayosekolah.objek.MenuData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    SliderLayout sliderLayout;

    RecyclerView mRecyclerView;
    List<MenuData> mMenuKelas, mMenuKategori, mMenuLokasi;
    MenuData mMenuData;
    MenuAdapter menuAdapter;
    GridLayoutManager mGridLayoutManager;

    TextView ls_kelas, ls_kategori;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_beranda, container, false);

        createMenuIcon();

        mRecyclerView = view.findViewById(R.id.recyclerview_kelas);
        mGridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        menuAdapter = new MenuAdapter(view.getContext(), mMenuKelas);

        mRecyclerView.setAdapter(menuAdapter);

        mRecyclerView = view.findViewById(R.id.recyclerview_kategori);
        mGridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        menuAdapter = new MenuAdapter(view.getContext(), mMenuKategori);

        mRecyclerView.setAdapter(menuAdapter);

        mRecyclerView = view.findViewById(R.id.recyclerview_lokasi);
        mGridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        menuAdapter = new MenuAdapter(view.getContext(), mMenuLokasi);

        mRecyclerView.setAdapter(menuAdapter);

        ls_kategori = view.findViewById(R.id.lihat_semua_kategori);
        ls_kelas = view.findViewById(R.id.lihat_semua_kelas);

        ls_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(v.getContext(), DatazActivity.class);
                mIntent.putExtra("kategori", "kategori");
                mIntent.putExtra("data_kategori", "ALL");
                startActivity(mIntent);
            }
        });

        ls_kelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(v.getContext(), DatazActivity.class);
                mIntent.putExtra("kategori", "pddk_akh");
                mIntent.putExtra("data_kategori", "ALL");
                startActivity(mIntent);
            }
        });


        //====================Image Slider============================================
        sliderLayout = view.findViewById(R.id.imageslider);


        HashMap<String,String> url_maps = new HashMap<String, String>();

        url_maps.put("Pendopo", "https://kudusekolah.000webhostapp.com/assets/azxc/hmm.jpg");
        url_maps.put("Kajen", "https://kudusekolah.000webhostapp.com/assets/azxc/hmm2.jpg");
//        url_maps.put("Tugu Pekalongan", "http://192.168.1.100/faskes/assets/imageslider/tugu_pekalongan.jpg");
//        url_maps.put("Taman Sorogenen", "http://192.168.1.100/faskes/assets/imageslider/sorogenen_depan.jpg");
//        url_maps.put("Masjid Al-Ikhlas", "http://192.168.1.100/faskes/assets/imageslider/masjid_depan.jpg");

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(view.getContext());

            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);
//                    .setOnSliderClickListener(MainActivity.this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            sliderLayout.addSlider(textSliderView);
        }

        // you can change animasi, time page and anythink.. read more on github
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Right_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(5000);
        sliderLayout.startAutoCycle();
        //====================END OF IMAGE SLIDER===============================================

        return view;
    }

    //==================================Override ImageSlider================================
    @Override
    public void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getContext(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "xx", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //===================end of overade image slider===================================

    public void createMenuIcon(){
        mMenuKelas = new ArrayList<>();
        mMenuData = new MenuData("SD","SD", "pddk_akh", R.drawable.icon_sd);
        mMenuKelas.add(mMenuData);
        mMenuData = new MenuData("SMP","SMP", "pddk_akh",R.drawable.icon_smp);
        mMenuKelas.add(mMenuData);
        mMenuData = new MenuData("SMA/SMK","SMA", "pddk_akh",R.drawable.icon_sma);
        mMenuKelas.add(mMenuData);
//        mMenuData = new MenuData("Lihat Semua","ALL","pddk_akh", R.drawable.icon_lihat_semua);
//        mMenuKelas.add(mMenuData);

        mMenuKategori = new ArrayList<>();
        mMenuData = new MenuData("Drop Out","DROP OUT", "kategori", R.drawable.icon_drop_out);
        mMenuKategori.add(mMenuData);
        mMenuData = new MenuData("Tidak Melanjutkan","TIDAK MELANJUTKAN", "kategori", R.drawable.icon_tidak_melanjutkan);
        mMenuKategori.add(mMenuData);
        mMenuData = new MenuData("Tidak Pernah Sekolah","Belum/Tidak Pernah Sekolah", "kategori", R.drawable.icon_tidak_pernah);
        mMenuKategori.add(mMenuData);
//        mMenuData = new MenuData("Disabilitas","DISABILITAS", "kategori", R.drawable.icon_disabilitas);
//        mMenuKategori.add(mMenuData);
//        mMenuData = new MenuData("Lihat Semua","ALL", "kategori", R.drawable.icon_lihat_semua);
//        mMenuKategori.add(mMenuData);

        mMenuLokasi = new ArrayList<>();
        mMenuData = new MenuData("Kecamatan","KECAMATAN", "lokasi", R.drawable.icon_kecamatan);
        mMenuLokasi.add(mMenuData);
        mMenuData = new MenuData("Desa","DESA", "lokasi", R.drawable.icon_desa);
        mMenuLokasi.add(mMenuData);
        mMenuData = new MenuData("Di Sekitar Saya","SEKITAR", "lokasi", R.drawable.icon_sekitar);
        mMenuLokasi.add(mMenuData);

    }

}
