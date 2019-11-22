package com.company.a3dsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import org.rajawali3d.renderer.Renderer;
import org.rajawali3d.view.ISurface;
import org.rajawali3d.view.SurfaceView;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    LoadModelRenderer renderer;

    private ScaleGestureDetector _ScaleGestureDetector;

    float startX, lastX;

    float lastValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final SurfaceView surface = new SurfaceView(this);
        surface.setFrameRate(60.0);
        surface.setRenderMode(ISurface.RENDERMODE_WHEN_DIRTY);

        addContentView(surface, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        renderer = new LoadModelRenderer(this);
        surface.setSurfaceRenderer(renderer);

        _ScaleGestureDetector = new ScaleGestureDetector(this, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if (detector.getScaleFactor() < (lastValue - 0.05f) || detector.getScaleFactor() > (lastValue + 0.05f)) {
                    Log.i(TAG, "onScale: " + detector.getScaleFactor());
                    renderer.setZ(detector.getScaleFactor());
                    lastValue = detector.getScaleFactor();
                }
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                lastValue = 0;
                renderer.resetLastK();
            }
        });

        surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _ScaleGestureDetector.onTouchEvent(event);

                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
//                        if ((event.getX() + 10) < lastX || (event.getX() - 10) > lastX) {
//                            startX = lastX;
//                        }

                        if (event.getX() > startX) {
                            renderer.rotate(1);
                        } else {
                            renderer.rotate(-1);
                        }
//                        lastX = event.getX();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
//                        lastX = -999;
                        break;
                        case MotionEvent.ACTION_UP:
                            startX = 0;
                            break;
                }
                return true;
            }
        });
        /*surface.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        if ((event.getX() + 10) < lastX || (event.getX() - 10) > lastX) {
                            startX = lastX;
                        }
                        renderer.rotate(startX - event.getX());
                        lastX = event.getX();
                        break;
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        lastX = -999;
                        break;
                }
                Log.d("Basic", "onTouch: " + event.toString());
                return true;
            }
        });*/
    }
}
