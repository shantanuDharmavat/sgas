package shantanu.testmap;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

/**
 * Created by Abhijit on 19-12-2016.
 */

public class SplashScreen extends AppCompatActivity {
    ImageView splashImage;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splashImage = (ImageView) findViewById(R.id.imageViewSplash);
        splashImage.setBackgroundResource(R.drawable.gif_frame);
        AnimationDrawable gifAnimation = (AnimationDrawable) splashImage.getBackground();
        gifAnimation.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Intent mainIntent = new Intent(SplashScreen.this, Dashboard.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, 5000);
    }
}
