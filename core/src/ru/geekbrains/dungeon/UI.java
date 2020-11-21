package ru.geekbrains.dungeon;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class UI {
    Label turns;
    Label experience;
    GameController gc;
    BitmapFont font;

    public UI(GameController gameController) {
        gc = gameController;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        //turns = new Label("10",labelStyle);
        //experience = new Label("0",labelStyle);
        font = new BitmapFont();
    }

    public void update(float dt){
        experience.setText(gc.getHero().getExperience());
        experience.setText(0);
    }
    public void render(SpriteBatch batch){
        font.draw(batch,"exp:",20,20);
        font.draw(batch,Integer.toString(gc.getHero().getExperience()),50,20);
        font.draw(batch,"turns:",70,20);
        font.draw(batch,Integer.toString(gc.getHero().getTurns()),110,20);
    }
}
