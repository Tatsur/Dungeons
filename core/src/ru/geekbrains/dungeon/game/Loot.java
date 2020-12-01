package ru.geekbrains.dungeon.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import lombok.Data;
import ru.geekbrains.dungeon.helpers.Assets;
import ru.geekbrains.dungeon.helpers.Poolable;

@Data
public class Loot implements Poolable {
    TextureAtlas.AtlasRegion texture;
    int cellX;
    int cellY;
    int coins;
    boolean taken;

    public Loot(){
        this.texture = Assets.getInstance().getAtlas().findRegion("coin");
    }
    public void render(SpriteBatch batch){
        batch.setColor(1,1,1,1);
        batch.draw(texture,cellX*GameMap.CELL_SIZE, cellY*GameMap.CELL_SIZE);
    }

    @Override
    public boolean isActive() {
        return !taken;
    }
    public void takeLoot(){
        taken = true;
    }
    public Loot activate(int cellX, int cellY, int coinsAmount) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.coins = coinsAmount;
        this.taken = false;
        return this;
    }

    public void update(float dt) {
    }
}
