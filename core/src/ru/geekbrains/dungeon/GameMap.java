package ru.geekbrains.dungeon;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class GameMap {
    public static final int CELLS_X = 20;
    public static final int CELLS_Y = 20;

    private byte[][] data;
    private TextureRegion grassTexture;

    private Vector2 size = new Vector2();

    public GameMap(TextureAtlas atlas) {
        this.data = new byte[CELLS_X][CELLS_Y];
        this.grassTexture = atlas.findRegion("grass40");
        size.set(CELLS_X*40,CELLS_Y*40);
    }

    public Vector2 getSize() {
        return size;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < CELLS_X; i++) {
            for (int j = 0; j < CELLS_Y; j++) {
                batch.draw(grassTexture, i * 40, j * 40);
            }
        }
    }
}
