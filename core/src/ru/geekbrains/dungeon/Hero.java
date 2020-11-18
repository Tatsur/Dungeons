package ru.geekbrains.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private ProjectileController projectileController;
    private Vector2 position;
    private Vector2 heroSize;
    private Directions direction;
    private TextureRegion texture;
    private int weaponMode;
    private float speed = 100;
    private GameMap gameMap;

    private float xMax;
    private float xMin;
    private float yMax;
    private float yMin;

    public Hero(TextureAtlas atlas, ProjectileController projectileController,GameMap gameMap) {
        this.position = new Vector2(100, 100);
        this.texture = atlas.findRegion("tank");
        heroSize = new Vector2(this.texture.getRegionWidth(),this.texture.getRegionHeight());
        this.projectileController = projectileController;
        this.weaponMode = 1;
        direction = Directions.RIGHT;
        this.gameMap = gameMap;
        getLimits();
    }
    private void getLimits(){
        Vector2 mapSize = gameMap.getSize();
        mapSize.y = mapSize.y>720?720:mapSize.y;
        mapSize.x = mapSize.x>1280?1280:mapSize.x;
        xMin = heroSize.x/2;
        xMax = mapSize.x - heroSize.x/2;
        yMin = heroSize.y/2;
        yMax = mapSize.y - heroSize.y/2;
    }
    public void update(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.Q)) {
            swapWeaponMode();
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            switch (weaponMode) {
                case 2: {
                    projectileController.activate(position.x, position.y, 200, 5,direction);
                    projectileController.activate(position.x, position.y, 200, -5,direction);
                }
               break;
                default:projectileController.activate(position.x, position.y, 200, 0,direction);
            }
        }
        movement(dt);
    }

    private void movement(float dt) {
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                position.mulAdd(Vector2.X, dt * speed);
                direction = Directions.RIGHT;
            } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                position.mulAdd(Vector2.X, -dt * speed);
                direction = Directions.LEFT;
            } else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                position.mulAdd(Vector2.Y, dt * speed);
                direction = Directions.UP;
            } else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                position.mulAdd(Vector2.Y, -dt * speed);
                direction = Directions.DOWN;
            }
        if(position.x<xMin) position.x = xMin;
        if(position.x>xMax) position.x = xMax;
        if(position.y<yMin) position.y = yMin;
        if(position.y>yMax || position.y > 720) position.y = yMax;
    }

    private void swapWeaponMode() {
        weaponMode = (weaponMode ==1)?2:1;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 20, position.y - 20);
    }
}
