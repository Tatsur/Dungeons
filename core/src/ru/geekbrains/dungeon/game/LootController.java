package ru.geekbrains.dungeon.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.geekbrains.dungeon.helpers.ObjectPool;

public class LootController extends ObjectPool<Loot> {
    @Override
    protected Loot newObject() {
        return new Loot();
    }
    public Loot activate(int cellX, int cellY, int coinsAmount) {
        return getActiveElement().activate(cellX, cellY, coinsAmount);
    }

    public Loot getLootInCell(int cellX, int cellY) {
        for (int i = 0; i < getActiveList().size(); i++) {
            Loot l = getActiveList().get(i);
            if (l.getCellX() == cellX && l.getCellY() == cellY) {
                return l;
            }
        }
        return null;
    }

    public void update(float dt) {
        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).update(dt);
        }
        checkPool();
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < getActiveList().size(); i++) {
            getActiveList().get(i).render(batch);
        }
    }
}
