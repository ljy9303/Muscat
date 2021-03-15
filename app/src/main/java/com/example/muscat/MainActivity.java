package com.example.muscat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private final static int SWIPE_MIN_DISTANCE = 120;
    private final static int SWIPE_MAX_OFF_PATH = 250;
    private final static int SWIPE_THRESHOLD_VELOCITY_VERTICAL = 100;
    private final static int SWIPE_THRESHOLD_VELOCITY_HORIZONTAL = 400;

    private final static int GESTURE_SWIPE_LEFT = 0;
    private final static int GESTURE_SWIPE_RIGHT = 1;
    private final static int GESTURE_SWIPE_UP = 2;
    private final static int GESTURE_SWIPE_DOWN = 3;

    private GestureDetectorCompat gestureScanner;
    private float fingerX = 0;
    private float fingerY = 0;

    int screenWidth;
    int screenHeight;

    Fragment aFragment;
    Fragment bFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gestureScanner = new GestureDetectorCompat(this, new MyGestureListener());

        // get screen size
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        // fragment a 로드
        aFragment = new MainCalenderFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.aFrameLayout, aFragment)
                .commitAllowingStateLoss();

        // fragment b 로드
        bFragment = new BFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.bFrameLayout, bFragment)
                .commitAllowingStateLoss();

//        getWindow().getDecorView().findViewById(R.id.activity_main).invalidate();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureScanner.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final String DEBUG_TAG = "Gestures";

        @Override
        public boolean onDown(MotionEvent event) {
//            Log.d(DEBUG_TAG,"onDown: " + event.toString());
//            fingerX = event.getX();
//            fingerY = event.getY();
            return true;
        }


        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.d(DEBUG_TAG, "onScroll: " + " " + (e2.getY()/screenHeight));

            // 위아래 스와이프를 통해 화면 크기 조절
            //ConstraintLayout
            FrameLayout aFrameLayout = findViewById(R.id.aFrameLayout);
            ConstraintLayout.LayoutParams lp_a = (ConstraintLayout.LayoutParams) aFrameLayout.getLayoutParams();
            FrameLayout bFrameLayout = findViewById(R.id.bFrameLayout);
            ConstraintLayout.LayoutParams lp_b = (ConstraintLayout.LayoutParams) bFrameLayout.getLayoutParams();

            float h = 1-(e2.getY()/screenHeight);
//            h = h + lp.matchConstraintPercentHeight;
            if(h < 0)
                h = 0;
            else if(h > 1)
                h = 1;

//            lp_b.matchConstraintPercentHeight = h;
//            bFrameLayout.setLayoutParams(lp_b);
//            lp_a.matchConstraintPercentHeight = 1-h;
//            aFrameLayout.setLayoutParams(lp_a);


//            //LinearLayout
//            FrameLayout aFrameLayout = findViewById(R.id.aFrameLayout);
//            LinearLayout.LayoutParams lp_a = (LinearLayout.LayoutParams) aFrameLayout.getLayoutParams();
//            FrameLayout bFrameLayout = findViewById(R.id.bFrameLayout);
//            LinearLayout.LayoutParams lp_b = (LinearLayout.LayoutParams) bFrameLayout.getLayoutParams();

//            float h = (screenHeight-lp_a.height) - (screenHeight - (e2.getY() - e1.getY())) / (float)screenHeight * (screenHeight-lp_a.height);
////            if(h>lp_b.height)
////                h = lp_b.height;
////            else if(h<0)
////                h = 0;
//
//            Log.d("aa",String.valueOf(h));
//
//            lp_b.height = (int)h;
//            bFrameLayout.setLayoutParams(lp_b);

            // 스와이프 제스처 분류
            if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                return false;
            float d;
            // right to left swipe
            d = e1.getX() - e2.getX();
            if (d > SWIPE_MIN_DISTANCE && Math.abs(d) > SWIPE_THRESHOLD_VELOCITY_HORIZONTAL){
//                Log.d(DEBUG_TAG, "Left Swipe");
                gestureDetected(GESTURE_SWIPE_LEFT,d);
            }

            // left to right swipe
            d = e2.getX() - e1.getX();
            if (d > SWIPE_MIN_DISTANCE && Math.abs(d) > SWIPE_THRESHOLD_VELOCITY_HORIZONTAL){
//                Log.d(DEBUG_TAG, "Right Swipe");
                gestureDetected(GESTURE_SWIPE_RIGHT,d);
            }

            // down to up swipe
            d = e1.getY() - e2.getY();
            if(d> SWIPE_MIN_DISTANCE && Math.abs(d) > SWIPE_THRESHOLD_VELOCITY_VERTICAL){
//                Log.d(DEBUG_TAG, "Up Swipe");
                gestureDetected(GESTURE_SWIPE_UP,d);
            }

            // up to down swipe
            d = e2.getY() - e1.getY();
            if(d > SWIPE_MIN_DISTANCE && Math.abs(d) > SWIPE_THRESHOLD_VELOCITY_VERTICAL){
//                Log.d(DEBUG_TAG, "Down Swipe");
                gestureDetected(GESTURE_SWIPE_DOWN,d);
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            return super.onFling(e1, e2, velocityX, velocityY);
        }

    }

    // 제스처 엑션
    private void gestureDetected(int g, float d){
//        FrameLayout bFrameLayout = findViewById(R.id.bFrameLayout);
//        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) bFrameLayout.getLayoutParams();
        float p_x = d/(float)screenWidth;
        float p_y = d/(float)screenHeight;

        switch(g){
            case GESTURE_SWIPE_LEFT:

                break;
            case GESTURE_SWIPE_RIGHT:

                break;
            case GESTURE_SWIPE_UP:
//                lp.matchConstraintPercentHeight += p_y;
//                bFrameLayout.setLayoutParams(lp);
                break;

            case GESTURE_SWIPE_DOWN:
//                lp.matchConstraintPercentHeight -= p_y;
//                bFrameLayout.setLayoutParams(lp);
                break;
        }
    }





    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}