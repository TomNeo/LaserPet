package com.heinousgames.game.doge;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.ui.activity.SimpleBaseGameActivity;

public class MainActivity extends SimpleBaseGameActivity {

	static final int CAMERA_WIDTH = 800;
	static final int CAMERA_HEIGHT = 1280;

	public Camera mCamera;

	public Scene mCurrentScene;

	public static MainActivity instance;
	public static int scoreCount, life;

	@Override
	public EngineOptions onCreateEngineOptions() {
		instance = this;

		mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), mCamera);

		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);

		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
	}

	@Override
	protected Scene onCreateScene() {
		mEngine.registerUpdateHandler(new FPSLogger());

		mCurrentScene = new GameScene();
		scoreCount = 0;
		life = 5;

		return mCurrentScene;
	}

	// to change the current main scene
	public void setCurrentScene(Scene scene) {
		mCurrentScene = null;
		mCurrentScene = scene;
		getEngine().setScene(mCurrentScene);
	}

	public static MainActivity getSharedInstance() {
		return instance;
	}
	
	public void onGameDestroyed() {
		scoreCount = 0;
		life = 5;
	}

}
