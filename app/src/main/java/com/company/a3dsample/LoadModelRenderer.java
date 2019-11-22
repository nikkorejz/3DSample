package com.company.a3dsample;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import org.rajawali3d.Object3D;
import org.rajawali3d.animation.Animation;
import org.rajawali3d.animation.Animation3D;
import org.rajawali3d.animation.EllipticalOrbitAnimation3D;
import org.rajawali3d.animation.RotateOnAxisAnimation;
import org.rajawali3d.lights.PointLight;
import org.rajawali3d.loader.ALoader;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.async.IAsyncLoaderCallback;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Cube;
import org.rajawali3d.renderer.Renderer;

/**
 * Project name: 3DSample.
 * Created by Nikita Gromik on 11/19/2019.
 * Copyright (c) 2019 NIKKOREJZ. All rights reserved.
 */
public class LoadModelRenderer extends Renderer implements IAsyncLoaderCallback {
    private PointLight mLight;
    private Cube mBaseObject;
    private Animation3D mCameraAnim, mLightAnim;

    private static final int MIN_Z = 10;

    private static final int MAX_Z = 35;

    private float lastK;

    public void setZ(float k) {
        k = k - 1;
//        k = k * -1;
        k = Math.max(-5, Math.min(5, k)); // k from -1 to 1.

        k = k / 2.75f;

        double currentZ = getCurrentCamera().getZ() - ((MAX_Z - MIN_Z) * (k - lastK));
        lastK = k;
        currentZ = Math.max(MIN_Z, Math.min(MAX_Z, currentZ));
        Log.i("App ctrl", "setZ: " + k + " > " + currentZ);
        getCurrentCamera().setZ(currentZ);

    }

    public void resetLastK() {
        lastK = 0;
    }

    private float rot;

    public void rotate(float k) {
        k = k / 100f;
        rot += k;
        rot %= 360;
        _Object3D.rotate(Vector3.Axis.Y, rot);
    }

    public LoadModelRenderer(Context context) {
        super(context);
    }

    protected void initScene() {
        mLight = new PointLight();
        mLight.setPosition(0, 0, 4);
        mLight.setPower(3);

        getCurrentScene().addLight(mLight);
        getCurrentCamera().setZ(16);
        getCurrentCamera().setX(-0.65);

        getCurrentScene().setBackgroundColor(0.7f, 0.7f, 0.7f, 1.0f);

        // Add the base object
        mBaseObject = new Cube(2.0f);
        mBaseObject.setPosition(-2.0, 3.0, 0.0);
        try {
            Material material = new Material();
            material.addTexture(new Texture("camdenTown", R.drawable.earthtruecolor_nasa_big));
            material.setColorInfluence(0);
            mBaseObject.setMaterial(material);
          //  getCurrentScene().addChild(mBaseObject);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        //Begin loading
        final LoaderOBJ loaderOBJ = new LoaderOBJ(mContext.getResources(),
                mTextureManager, R.raw.newobj);
        loadModel(loaderOBJ, this, R.raw.negev_obj);

        mLightAnim = new EllipticalOrbitAnimation3D(new Vector3(),
                new Vector3(0, 10, 0), Vector3.getAxisVector(Vector3.Axis.Z), 0,
                360, EllipticalOrbitAnimation3D.OrbitDirection.CLOCKWISE);

        mLightAnim.setDurationMilliseconds(3000);
        mLightAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        mLightAnim.setTransformable3D(mLight);

        getCurrentScene().registerAnimation(mLightAnim);
        mLightAnim.play();
    }

    private Object3D _Object3D;

    @Override
    public void onModelLoadComplete(ALoader aLoader) {
        Log.d("Basic", "Model load complete: " + aLoader);
        final LoaderOBJ obj = (LoaderOBJ) aLoader;
        _Object3D = obj.getParsedObject();

        try {
            Material material = new Material();
            material.addTexture(new Texture("camdenTown", R.drawable.bratatat));
            material.setColorInfluence(0);
            _Object3D.setMaterial(material);
            //  getCurrentScene().addChild(mBaseObject);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }

        _Object3D.setPosition(Vector3.ZERO);
        getCurrentScene().addChild(_Object3D);



      /*  mCameraAnim = new RotateOnAxisAnimation(Vector3.Axis.Y, 360);
        mCameraAnim.setDurationMilliseconds(8000);
        mCameraAnim.setRepeatMode(Animation.RepeatMode.INFINITE);
        mCameraAnim.setTransformable3D(parsedObject);*/

//        getCurrentScene().registerAnimation(mCameraAnim);

//        mCameraAnim.play();
    }

    @Override
    public void onModelLoadFailed(ALoader aLoader) {
        Log.e("Basic", "Model load failed: " + aLoader);
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}