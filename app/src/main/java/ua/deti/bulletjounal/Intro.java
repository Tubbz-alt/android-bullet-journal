package ua.deti.bulletjounal;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
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
        String description="Bullet Journal is a new way of organizing your life, separating it in a Daily Log, a Monthly Log,a Yearly Log and Collections. ";

        sliderPage.setTitle(title);
        sliderPage.setDescription(description);
        sliderPage.setImageDrawable(R.drawable.home);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        String title8="Hub's";
        String description8="In your Hub's you add new Logs. If you click on the square you can go to the log, if you long press it you can delete it.";
        SliderPage sliderPage2 = new SliderPage();

        sliderPage2.setTitle(title8);
        sliderPage2.setDescription(description8);
        sliderPage2.setImageDrawable(R.drawable.hub);
        sliderPage2.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage2));



        String title2="Daily Log";
        String description2="In your Daily Log you can organize from the present day to the next 4 days by adding Tasks,Events and Notes through the + button. Keep track of your Daily life!";
        SliderPage sliderPage3 = new SliderPage();

        sliderPage3.setTitle(title2);
        sliderPage3.setDescription(description2);
        sliderPage3.setImageDrawable(R.drawable.daily_icon);
        sliderPage3.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage3));




        String title3="Monthly Log";
        String description3="In your Monthly Log Log you can organize from the present month to the next 2 months adding items refering to a specific day or to the all month. " +
                "Keep track of your Monthly life!";
        SliderPage sliderPage4 = new SliderPage();

        sliderPage4.setTitle(title3);
        sliderPage4.setDescription(description3);
        sliderPage4.setImageDrawable(R.drawable.monthly_notes);
        sliderPage4.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage4));

        String title4="Yearly Log";
        String description4="In your Yearly Log Log you can organize your present year, by adding for example the date of your next trip or the birthday of your friend. " +
                "Keep track of your Yearly life!";
        SliderPage sliderPage5 = new SliderPage();

        sliderPage5.setTitle(title4);
        sliderPage5.setDescription(description4);
        sliderPage5.setImageDrawable(R.drawable.yearly_icon);
        sliderPage5.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage5));

        String title5="Collections";
        String description5="The Collections page is where you can keep a list of your favorite movies, gift ideas for your loved one or even your bucket list. " +
                "Keep track of your Mind!";
        SliderPage sliderPage6 = new SliderPage();

        sliderPage6.setTitle(title5);
        sliderPage6.setDescription(description5);
        sliderPage6.setImageDrawable(R.drawable.collections_icon);
        sliderPage6.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage6));

        String title6="Help";
        String description6="If you eat a lot of cheese and forget how the app works you can always go the 'Help and Documentation' tab.  "
                ;
        SliderPage sliderPage7 = new SliderPage();

        sliderPage7.setTitle(title6);
        sliderPage7.setDescription(description6);
        sliderPage7.setImageDrawable(R.drawable.drawer);
        sliderPage7.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage7));

        String title7="You are all set.Enjoy Bullet Journal";
        String description7="What are you waiting for? Get Started."
                ;
        SliderPage sliderPage8 = new SliderPage();

        sliderPage8.setTitle(title7);
        sliderPage8.setDescription(description7);
        sliderPage8.setImageDrawable(R.drawable.success);
        sliderPage8.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage8));


    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
       finish();
    }


}
