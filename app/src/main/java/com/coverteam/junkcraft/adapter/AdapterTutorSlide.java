package com.coverteam.junkcraft.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.coverteam.junkcraft.R;
import com.coverteam.junkcraft.model.DataTutorial;
import com.coverteam.junkcraft.model.DataTutorialSlide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterTutorSlide extends PagerAdapter {
    private Context context;
    List<DataTutorialSlide> dataTutorialSlides;
    LayoutInflater inflater;

    public AdapterTutorSlide(Context context, List<DataTutorialSlide> dataTutorialSlides) {
        this.context = context;
        this.dataTutorialSlides = dataTutorialSlides;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return dataTutorialSlides.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((View)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.viewtutorialdetail,container,false);

        ImageView gambartutor = view.findViewById(R.id.gambartutor);
        TextView judultutor = view.findViewById(R.id.jdltutor);
        TextView desktutor = view.findViewById(R.id.desctutor);
        final ProgressBar progressBar = view.findViewById(R.id.progressbar);

        //Picasso.with(context).load(dataTutorialSlides.get(position).getFototutor()).into(gambartutor);
        Picasso.with(context)
                .load(dataTutorialSlides.get(position).getFototutor())
                .into(gambartutor, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

        judultutor.setText(dataTutorialSlides.get(position).getJudultutor());
        desktutor.setText(dataTutorialSlides.get(position).getKettutor());

        container.addView(view);
        return view;
    }
}
