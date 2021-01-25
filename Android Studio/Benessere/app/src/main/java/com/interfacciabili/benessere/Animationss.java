package com.interfacciabili.benessere;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Animationss extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AppCompat_DayNight);
        setContentView(R.layout.layout_animation);


        ImageView view = (ImageView) findViewById(R.id.peso);
        ImageView run  = (ImageView) findViewById(R.id.run);
        ImageView fork = (ImageView) findViewById(R.id.fork);


            Animation animation_move = AnimationUtils.loadAnimation(Animationss.this, R.anim.moveupdown);
            Animation animation_run  = AnimationUtils.loadAnimation(Animationss.this,R.anim.movestartend);
            Animation animation_fork  = AnimationUtils.loadAnimation(Animationss.this,R.anim.moveupdown);
            animation_move.setRepeatCount(Animation.INFINITE);
            view.startAnimation(animation_move);
            fork.startAnimation(animation_fork);
            run.startAnimation(animation_run);



    }


}
