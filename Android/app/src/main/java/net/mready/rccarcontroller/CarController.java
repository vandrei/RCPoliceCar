package net.mready.rccarcontroller;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import static net.mready.rccarcontroller.R.id.slower_button;


public class CarController extends Activity {
    private Button slowerButton;
    private Button fasterButton;
    private ImageButton sirenButton;
    private ImageButton headlightsButton;
    private ImageButton policeLightsButton;
    private ImageButton upButton;
    private ImageButton downButton;
    private ImageButton leftButton;
    private ImageButton rightButton;
    private ImageView indicatorView;
    private ImageView signalIndicatorImageView;
    private ImageView headlightsIndicatorImageView;
    private ImageView policeLightsIndicatorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_controller);

        initGui();
    }

    private void initGui () {
        indicatorView = (ImageView) findViewById(R.id.indicator_imageView);
        slowerButton = (Button) findViewById(R.id.slower_button);
        fasterButton = (Button) findViewById(R.id.faster_button);
        sirenButton = (ImageButton) findViewById(R.id.siren_imageButton);
        headlightsButton = (ImageButton) findViewById(R.id.headlights_imageButton);
        policeLightsButton = (ImageButton) findViewById(R.id.sirenLights_imageButton);
        upButton = (ImageButton) findViewById(R.id.upCommand_button);
        downButton = (ImageButton) findViewById(R.id.downCommand_button);
        leftButton = (ImageButton) findViewById(R.id.leftCommand_button);
        rightButton = (ImageButton) findViewById(R.id.rightCommand_button);
        signalIndicatorImageView = (ImageView) findViewById(R.id.signalStrength_imageView);
        headlightsIndicatorImageView = (ImageView) findViewById(R.id.headlightsIndicator_imageView);
        policeLightsIndicatorImageView = (ImageView) findViewById(R.id.policeLightsIndicator_imageView);

        setEvents();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_car_controller, menu);
        return true;
    }

    private void setEvents() {
        slowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                RotateAnimation a = new RotateAnimation(0, 195 + 15,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                a.setDuration(400);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        indicatorView.setRotation(195);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                indicatorView.startAnimation(a);
            }
        });

        fasterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RotateAnimation a = new RotateAnimation(0, indicatorView.getRotation() + 15,
                        Animation.RELATIVE_TO_SELF, 0.5f,
                        Animation.RELATIVE_TO_SELF, 0.5f);
                a.setDuration(200);
                a.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        indicatorView.setRotation(-15);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                indicatorView.startAnimation(a);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
