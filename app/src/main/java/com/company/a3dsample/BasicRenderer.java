package com.company.a3dsample;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import org.rajawali3d.Object3D;
import org.rajawali3d.lights.DirectionalLight;
import org.rajawali3d.loader.ALoader;
import org.rajawali3d.loader.LoaderOBJ;
import org.rajawali3d.loader.ParsingException;
import org.rajawali3d.loader.async.IAsyncLoaderCallback;
import org.rajawali3d.materials.Material;
import org.rajawali3d.materials.methods.DiffuseMethod;
import org.rajawali3d.materials.textures.ATexture;
import org.rajawali3d.materials.textures.Texture;
import org.rajawali3d.math.vector.Vector3;
import org.rajawali3d.primitives.Sphere;
import org.rajawali3d.renderer.Renderer;

/**
 * Project name: 3DSample.
 * Created by Nikita Gromik on 11/18/2019.
 * Copyright (c) 2019 NIKKOREJZ. All rights reserved.
 */
public class BasicRenderer extends Renderer implements IAsyncLoaderCallback {

    private static final String TAG = "BasicRenderer";

    private Context _Context;

    private Sphere _EarthSphere;

    private Object3D _Object3D;

    private DirectionalLight _DirectionalLight;

    public BasicRenderer(Context context) {
        super(context);
        this._Context = context;
        setFrameRate(60.0);
    }

    @Override
    protected void initScene() {


        /*_DirectionalLight = new DirectionalLight(1.0, 0.2, 5);
        _DirectionalLight.setColor(1f, 1f, 1f);
        _DirectionalLight.setPower(10f);
        getCurrentScene().addLight(_DirectionalLight);*/

        //Begin loading
        final LoaderOBJ loaderOBJ = new LoaderOBJ(_Context.getResources(), mTextureManager, R.raw.dual_berettas_obj);
        loadModel(loaderOBJ, this, R.raw.dual_berettas_obj);

        Material material = new Material();
        material.enableLighting(false);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColorInfluence(0);
        Texture earthTexture = new Texture("Earth", R.drawable.earthtruecolor_nasa_big);
        try{
            material.addTexture(earthTexture);
        } catch (ATexture.TextureException error){
            Log.d(TAG, "initScene: " + error.toString());
        }

        _EarthSphere = new Sphere(1, 24, 24);
        _EarthSphere.setMaterial(material);
//        getCurrentScene().addChild(_EarthSphere);
        getCurrentCamera().setFarPlane(1000);
        getCurrentCamera().setZ(10.2f);

        try {
            getCurrentScene().setSkybox(R.drawable.posx, R.drawable.negx,
                    R.drawable.posy, R.drawable.negy, R.drawable.posz, R.drawable.negz);
        } catch (ATexture.TextureException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        getCurrentCamera().rotate(Vector3.Axis.Y, event.getX() % 360);
        _EarthSphere.rotate(Vector3.Axis.Y, event.getX() % 360);
        Log.d(TAG, "onTouchEvent: " + event.getAction() + " " + event.getX());
    }

    public void rotate(float x) {
        //getCurrentCamera().rotate(Vector3.Axis.Y, (x / 100) % 360);
    }

    @Override
    protected void onRender(long ellapsedRealtime, double deltaTime) {
        super.onRender(ellapsedRealtime, deltaTime);
        if (_EarthSphere != null) {
          //  _EarthSphere.rotate(Vector3.Axis.Y, 1.0);
        }

        //getCurrentCamera().rotate(Vector3.Axis.Y, -0.2);

        if (_Object3D != null) {
            _Object3D.rotate(Vector3.Axis.Y, 1.0);
        }
    }

    @Override
    public void onModelLoadComplete(ALoader loader) {
        Log.d(TAG, "onModelLoadComplete: " + loader.toString());
        LoaderOBJ obj = null;
        try {
            obj = (LoaderOBJ) loader.parse();
        } catch (ParsingException e) {
            e.printStackTrace();
        }
        _Object3D = obj.getParsedObject();

        Material material = new Material();
        material.enableLighting(true);
        material.setDiffuseMethod(new DiffuseMethod.Lambert());
        material.setColorInfluence(0.5f);
        Texture earthTexture = new Texture("Sawed-off", R.drawable.earthtruecolor_nasa_big);
        try{
            material.addTexture(earthTexture);
            Log.d(TAG, "onModelLoadComplete: Material installed!");
        } catch (ATexture.TextureException error){
            Log.d(TAG, "initScene: " + error.toString());
        }


        _Object3D.setMaterial(material);
        _Object3D.setPosition(Vector3.ZERO);
        _Object3D.setScale(2, 2, 2);
//        parsedObject.setZ(4.2f);
        getCurrentScene().addChild(_Object3D);

        Log.d(TAG, "onModelLoadComplete: " + getCurrentScene().getChildrenCopy().size());

        Log.d(TAG, "onModelLoadComplete: Camera position" + getCurrentCamera().getPosition().toString());
        Log.d(TAG, "onModelLoadComplete: Object position" + _Object3D.getPosition().toString());
    }

    @Override
    public void onModelLoadFailed(ALoader loader) {
        Log.d(TAG, "onModelLoadFailed: " + loader.toString());
    }


}
