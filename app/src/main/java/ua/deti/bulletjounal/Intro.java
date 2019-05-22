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

        sliderPage.setTitle(title8);
        sliderPage.setDescription(description8);
        sliderPage.setImageDrawable(R.drawable.hub);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));



        String title2="Daily Log";
        String description2="In your Daily Log you can organize from the present day to the next 4 days by adding Tasks,Events and Notes through the + button. Keep track of your Daily life!";

        sliderPage.setTitle(title2);
        sliderPage.setDescription(description2);
        sliderPage.setImageDrawable(R.drawable.daily_log);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));




        String title3="Monthly Log";
        String description3="In your Monthly Log Log you can organize from the present month to the next 2 months adding items refering to a specific day or to the all month. " +
                "Keep track of your Monthly life!";

        sliderPage.setTitle(title3);
        sliderPage.setDescription(description3);
        sliderPage.setImageDrawable(R.drawable.monthly_notes);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        String title4="Yearly Log";
        String description4="In your Yearly Log Log you can organize your present year, by adding for example the date of your next trip or the birthday of your friend. " +
                "Keep track of your Yearly life!";

        sliderPage.setTitle(title4);
        sliderPage.setDescription(description4);
        sliderPage.setImageDrawable(R.drawable.yearly_log);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        String title5="Collections";
        String description5="The Collections page is where you can keep a list of your favorite movies, gift ideas for your loved one or even your bucket list. " +
                "Keep track of your Mind!";

        sliderPage.setTitle(title5);
        sliderPage.setDescription(description5);
        sliderPage.setImageDrawable(R.drawable.collections);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        String title6="Help";
        String description6="If you eat a lot of cheese and forget how the app works you can always go the 'Help and Documentation' tab.  "
                ;

        sliderPage.setTitle(title6);
        sliderPage.setDescription(description6);
        sliderPage.setImageDrawable(R.drawable.drawer);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));

        String title7="You are all set.Enjoy Bullet Journal";
        String description7="What are you waiting for? Get Started."
                ;

        sliderPage.setTitle(title7);
        sliderPage.setDescription(description7);
        sliderPage.setImageDrawable(R.drawable.success);
        sliderPage.setBgColor(getResources().getColor(R.color.BluePrimary));
        addSlide(AppIntroFragment.newInstance(sliderPage));


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
