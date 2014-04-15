package com.heinousgames.game.doge;

import java.io.IOException;
import java.util.Random;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.MoveModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.debug.Debug;

import android.graphics.Color;
import android.graphics.Typeface;

public class GameScene extends Scene {

	MainActivity activity;

	private BitmapTextureAtlas mDogeTextureAtlas;
	private ITextureRegion mDogeTextureRegion;

	private Sprite doge;

	public Random rand = new Random();
	public int randGen;
	public float attackerSpeed;
	private Sound mLaserSound;
	
	private Font mFont;

	public MoveModifier attackMove;

	public Attacker att;
	public Line line, line2;
	public float leftEyeX, leftEyeY, rightEyeX, rightEyeY, x, y, centerScreenX, centerScreenY;

	public GameScene() {
		activity = MainActivity.getSharedInstance();
		
		// create the font
		this.mFont = FontFactory.create(activity.getFontManager(), activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, Color.WHITE);
		this.mFont.load();
		
		// create the text for the score and life
		final Text score = new Text(550, 10, this.mFont, "Score: " + MainActivity.scoreCount, "Score: XXXXX".length(), activity.getVertexBufferObjectManager());
		final Text life = new Text(10, 10, this.mFont, "Life: " + MainActivity.life, "Life: X".length(), activity.getVertexBufferObjectManager());
		
		// make the text black
		score.setColor(0, 0, 0);
		life.setColor(0, 0, 0);
		
		// set the path with the images
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		// load the image
		this.mDogeTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.mDogeTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mDogeTextureAtlas, activity, "doge.png", 0, 0);
		this.mDogeTextureAtlas.load();
		
		// load the lazer sound
		SoundFactory.setAssetBasePath("sfx/");
        try {
                this.mLaserSound = SoundFactory.createSoundFromAsset(MainActivity.getSharedInstance().getSoundManager(), MainActivity.getSharedInstance(), "laser.mp3");
        } catch (final IOException e) {
                Debug.e(e);
        }

        // make the background white
		setBackground(new Background(1, 1, 1));
		
		// center of the screen where the doge image goes
		centerScreenX = (MainActivity.CAMERA_WIDTH/2)-(mDogeTextureRegion.getWidth()/2);
		centerScreenY = (MainActivity.CAMERA_HEIGHT/2)-(mDogeTextureRegion.getHeight()/2); 

		// sprite
		doge = new Sprite(centerScreenX, centerScreenY, 256, 256, mDogeTextureRegion, activity.getVertexBufferObjectManager());

		// where the eyes should start the lazers
		leftEyeX = doge.getX()+69;
		rightEyeX = doge.getX() + 106;
		leftEyeY = doge.getY()+87;
		rightEyeY = doge.getY() + 94;

		// attach sprite to the scene
		attachChild(doge);
		
		// attach the texts to the scene
		attachChild(score);
		attachChild(life);

		// make a new attacker
		generateAttacker();

		// if the user touches the screen
		setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {

				// if the user touches the screen, make the lazers
				if (pSceneTouchEvent.isActionDown()) {
					// draw the lines that create the lasers
					line = new Line(leftEyeX, leftEyeY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), activity.getVertexBufferObjectManager());
					line2 = new Line(rightEyeX, rightEyeY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY(), activity.getVertexBufferObjectManager());
					
					// attach the lines on the screen
					attachChild(line);
					attachChild(line2);
					
					// draw them red
					line.setColor(1, 0, 0);
					line2.setColor(1, 0, 0);
					
					// make them thick
					line.setLineWidth(5);
					line2.setLineWidth(5);
					
					// play the laser sound
					mLaserSound.play();
					return true;
				}

				// if the user drags their finger, update the laser to follow
				if (pSceneTouchEvent.isActionMove()) {
					line.setPosition(leftEyeX, leftEyeY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
					line2.setPosition(rightEyeX, rightEyeY, pSceneTouchEvent.getX(), pSceneTouchEvent.getY());
				}

				// if the user lifts their figner
				if (pSceneTouchEvent.isActionUp()) {
					detachChild(line);
					detachChild(line2);
					mLaserSound.stop();
					return true;
				}

				return false;
			}

		});

		registerUpdateHandler(new IUpdateHandler() {

			/**
			 * if at anytime
			 */
			@Override
			public void onUpdate(float pSecondsElapsed) {
				
				// if at any time a laser collides with a beetle attacker class
				if (att.beetle.collidesWith(line) || att.beetle.collidesWith(line2)) {
					
					// increase the score
					MainActivity.scoreCount += 5;
					
					// update the score text
					score.setText("Score: " + MainActivity.scoreCount);
					
					//detach the beetle attack from the screen
					
					detachChild(att.beetle);
					
					// reset the movem odifier for the beetle sprite
					attackMove.reset();
					
					// make a new beetle
					generateAttacker();
				}

				// if at any time a beetle collides with the doge
				if (att.beetle.collidesWith(doge)) {
					MainActivity.life--;
					life.setText("Life: " + MainActivity.life);
					detachChild(att.beetle);
					attackMove.reset();
					if (MainActivity.life >= 1) {
						generateAttacker();
					} else {
						activity.setCurrentScene(new GameOver());
					}
				}

			}

			@Override
			public void reset() {
			}			
		});		

	}

	public void generateAttacker() {

		// determine speed of beetle based on user's current score
		// attackerspeed is actually the time it takes for the beetle to
		// move to the center of the screen, not it's mph
		if (MainActivity.scoreCount <= 25) {
			attackerSpeed = rand.nextInt(3) + 3;
		} else if (MainActivity.scoreCount >= 30 && MainActivity.scoreCount <= 50) {
			attackerSpeed = rand.nextInt(2) + 3;
		} else if (MainActivity.scoreCount >= 55 && MainActivity.scoreCount <= 75) {
			attackerSpeed = rand.nextInt(1) + 3;
		} else if (MainActivity.scoreCount >= 80 && MainActivity.scoreCount <= 100) {
			attackerSpeed = rand.nextInt(1) + 2;
		} else if (MainActivity.scoreCount >= 105) {
			attackerSpeed = rand.nextInt(1) + 1;
		}
		
		// make a new beetle attacker class

		att = AttackerPool.sharedAttackPool().obtainPoolItem();
		
		// determine which side of the screen it's coming out of
		randGen = rand.nextInt(4) + 1;

		if (randGen % 4 == 1) {
			x = -60;
			y = rand.nextInt(1401) - 60;
		}

		if (randGen % 4 == 2) {
			x = 920;
			y = rand.nextInt(1401) - 60;
		}

		if (randGen % 4 == 3) {
			x = rand.nextInt(921) - 60;
			y = -60;
		}

		if (randGen % 4 == 0) {
			x = rand.nextInt(921) - 60;
			y = 1340;
		}
		
		// set that beetle's position
		att.beetle.setPosition(x, y);

		// assign the move modifier to move the beetle with the determined speed from its determined position to the center of the screen 
		attackMove = new MoveModifier(attackerSpeed, att.beetle.getX(), centerScreenX, att.beetle.getY(), centerScreenY);

		attachChild(att.beetle);
		att.beetle.registerEntityModifier(attackMove);
	}


}
