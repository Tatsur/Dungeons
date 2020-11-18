package ru.geekbrains.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hero {
    private ProjectileController projectileController;
    private Vector2 position;
    private Directions direction;
    private TextureRegion texture;
    private int weaponMode;
    private float speed = 100;

    public Hero(TextureAtlas atlas, ProjectileController projectileController) {
        this.position = new Vector2(100, 100);
        this.texture = atlas.findRegion("tank");
        this.projectileController = projectileController;
        this.weaponMode = 1;
        direction = Directions.RIGHT;
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
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            position.mulAdd(Vector2.X, dt *speed);
            direction = Directions.RIGHT;
        }else
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            position.mulAdd(Vector2.X,-dt *speed);
            direction = Directions.LEFT;
        }else
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            position.mulAdd(Vector2.Y, dt *speed);
            direction = Directions.UP;
        }else
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            position.mulAdd(Vector2.Y,-dt *speed);
            direction = Directions.DOWN;
        }
    }

    private void swapWeaponMode() {
        weaponMode = (weaponMode ==1)?2:1;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 20, position.y - 20);
    }
}
