package com.heinousgames.game.doge;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;

public class Attacker {
	
	private BitmapTextureAtlas mBeetleTextureAtlas;
	private ITextureRegion mBeetleTextureRegion;
	public Sprite beetle;
	
	public Attacker() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBeetleTextureAtlas = new BitmapTextureAtlas(MainActivity.getSharedInstance().getTextureManager(), 256, 256, TextureOptions.BILINEAR);
		this.mBeetleTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBeetleTextureAtlas, MainActivity.getSharedInstance(), "beetle.png", 0, 0);
		this.mBeetleTextureAtlas.load();
		
		beetle = new Sprite(0, 0, 32, 32, mBeetleTextureRegion, MainActivity.getSharedInstance().getVertexBufferObjectManager());
		
	}

}
