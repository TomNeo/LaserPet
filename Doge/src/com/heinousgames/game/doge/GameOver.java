package com.heinousgames.game.doge;

import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;

import android.graphics.Color;
import android.graphics.Typeface;

public class GameOver extends Scene {

	private Font mFont;
	private Text gameOverText, playAgainText;

	public GameOver() {
		
		MainActivity.scoreCount = 0;
		MainActivity.life = 5;

		this.mFont = FontFactory.create(MainActivity.getSharedInstance().getFontManager(), MainActivity.getSharedInstance().getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48, Color.WHITE);
		this.mFont.load();
		
		gameOverText = new Text(0, 0, mFont, "Game Over", "Game Over".length(), MainActivity.getSharedInstance().getVertexBufferObjectManager());
		playAgainText = new Text(0, 0, mFont, "Tap Screen to Play Again", "Tap Screen to Play Again".length(), MainActivity.getSharedInstance().getVertexBufferObjectManager());

		gameOverText.setColor(0.8f, 0, 0);
		playAgainText.setColor(0.8f, 0, 0);

		gameOverText.setPosition((MainActivity.CAMERA_WIDTH/2)-(gameOverText.getWidth()/2), (MainActivity.CAMERA_HEIGHT/2)-(gameOverText.getHeight()/2));
		playAgainText.setPosition((MainActivity.CAMERA_WIDTH/2)-(playAgainText.getWidth()/2), gameOverText.getY() + gameOverText.getHeight() + 15);

		attachChild(gameOverText);
		attachChild(playAgainText);

		setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					TouchEvent pSceneTouchEvent) {
				if (pSceneTouchEvent.isActionDown()) {
					MainActivity.getSharedInstance().setCurrentScene(new GameScene());
					return true;
				}
				
				return false;
			}

		});

	}
}
