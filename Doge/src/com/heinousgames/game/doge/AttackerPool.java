package com.heinousgames.game.doge;

import org.andengine.util.adt.pool.GenericPool;

public class AttackerPool extends GenericPool<Attacker> {
	
	public static AttackerPool instance;
	
	public static AttackerPool sharedAttackPool() {
        if (instance == null)
            instance = new AttackerPool();
        return instance;
    }

    private AttackerPool() {
        super();
    }

	@Override
	protected Attacker onAllocatePoolItem() {
		return new Attacker();
	}
	
	protected void onHandleRecycleItem(final Attacker a) {
		a.beetle.clearEntityModifiers();
		a.beetle.clearUpdateHandlers();
		a.beetle.setVisible(false);
		a.beetle.detachSelf();
	}

}
