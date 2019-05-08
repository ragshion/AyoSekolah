package com.ragshion.ayosekolah.activities;

import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.ragshion.ayosekolah.R;

import java.util.HashMap;

import co.ceryle.radiorealbutton.RadioRealButton;
import co.ceryle.radiorealbutton.RadioRealButtonGroup;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    Toolbar toolbar;
    CoordinatorLayout coordinatorLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    SliderLayout sliderLayout;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.maintoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("xxx");

        coordinatorLayout = findViewById(R.id.coordinator);
        collapsingToolbarLayout =  findViewById(R.id.maincollapsing);
        collapsingToolbarLayout.setTitle("Ayo Sekolah");
        collapsingToolbarLayout.setExpandedTitleColor(Color.argb(0,0,0,0));
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.argb(0,0, 0,0));

        appBarLayout = findViewById(R.id.mainappbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if (Math.abs(i)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
//                    Toast.makeText(MainActivity.this, "Collapsed", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    //Expanded
//                    Toast.makeText(MainActivity.this, "Expanded", Toast.LENGTH_SHORT).show();


                }
            }
        });


        //====================Image Slider============================================
        sliderLayout = findViewById(R.id.imageSlider);

        HashMap<String,String> url_maps = new HashMap<String, String>();

        url_maps.put("Museum Batik Pekalongan", "http://192.168.1.100/faskes/assets/imageslider/museum_batik.jpg");
        url_maps.put("Tugu Pekalongan", "http://192.168.1.100/faskes/assets/imageslider/tugu_pekalongan.jpg");
        url_maps.put("Taman Sorogenen", "http://192.168.1.100/faskes/assets/imageslider/sorogenen_depan.jpg");
        url_maps.put("Masjid Al-Ikhlas", "http://192.168.1.100/faskes/assets/imageslider/masjid_depan.jpg");

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);

            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(MainActivity.this);

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
    }

    //==================================Override ImageSlider================================
    @Override
    protected void onStop() {
        sliderLayout.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
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
}
