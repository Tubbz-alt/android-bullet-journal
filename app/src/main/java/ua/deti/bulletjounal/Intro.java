package ua.deti.bulletjounal;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.model.SliderPage;

public class Intro extends AppIntro2 {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SliderPage sliderPage = new SliderPage();
        getSupportActionBar().hide();

        String title="Welcome to your Bullet Journal";
        String description="Bullet Journal is a new way of organizing your life, separating your life in a Daily Log, that includes the present day and the next 4 days, a Monthly Log, that includes the present month and the next 2 months,a Yearly Log, that includes the present year and Collections where you can write whatever you want";

        sliderPage.setTitle(title);
        sliderPage.setDescription(description);
        sliderPage.setImageDrawable(R.drawable.home);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));


    }


}
