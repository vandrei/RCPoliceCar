package net.mready.rccarcontroller;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import static net.mready.rccarcontroller.R.id.slower_button;


public class CarController extends Activity {
    private static final int MINSPEED = 30;
    private static final int MAXSPEED = 100;
    private static final int MIN_INDICATOR_ANGLE = -15;
    private static final int MAX_INDICATOR_ANGLE = 195;
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
    private CarState carState = new CarState();
    private BluetoothManager btManager;
    private int speed = MINSPEED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_controller);

        btManager = new BluetoothManager(carState, this);
        btManager.start();

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

        upButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        carState.traction = CarState.Traction.FORWARD;
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        carState.traction = CarState.Traction.NONE;
                        break;
                }

                displayIndicatorValue();

                return true;
            }
        });

        downButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        carState.traction = CarState.Traction.REVERSE;
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        carState.traction = CarState.Traction.NONE;
                        break;
                }

                displayIndicatorValue();

                return true;
            }
        });

        leftButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        carState.steering = CarState.Steering.LEFT;
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        carState.steering = CarState.Steering.FRONT;
                        break;
                }

                displayIndicatorValue();

                return true;
            }
        });

        rightButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        carState.steering = CarState.Steering.RIGHT;
                        break;
                    case MotionEvent.ACTION_UP:
                        // RELEASED
                        carState.steering = CarState.Steering.FRONT;
                        break;
                }

                displayIndicatorValue();

                return true;
            }
        });

        slowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (carState.traction == CarState.Traction.FORWARD) {
                    speed -= 10;
                    if (speed < MINSPEED) {
                        speed = MINSPEED;
                    }
                }

                btManager.sendAction('V');

                displayIndicatorValue();
            }
        });

        fasterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (carState.traction == CarState.Traction.FORWARD) {
                    speed += 10;
                    if (speed > MAXSPEED) {
                        speed = MAXSPEED;
                    }
                }

                btManager.sendAction('F');

                displayIndicatorValue();
            }
        });

        headlightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carState.headlights = !carState.headlights;
                if (carState.headlights) {
                    headlightsIndicatorImageView.setImageResource(R.drawable.icon_lights_on);
                } else {
                    headlightsIndicatorImageView.setImageResource(R.drawable.icon_lights_off);
                }
            }
        });

        policeLightsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                carState.policelights = !carState.policelights;
                if (carState.policelights) {
                    policeLightsIndicatorImageView.setImageResource(R.drawable.icon_policelights_on);
                } else {
                    policeLightsIndicatorImageView.setImageResource(R.drawable.icon_policelights_off);
                }
            }
        });
    }

    void displayIndicatorValue() {
        if (carState.traction == CarState.Traction.FORWARD) {
            int angle = MIN_INDICATOR_ANGLE + (MAX_INDICATOR_ANGLE - MIN_INDICATOR_ANGLE) * speed / 100;
            indicatorView.setRotation(angle);
        } else {
            indicatorView.setRotation(MIN_INDICATOR_ANGLE);
        }
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