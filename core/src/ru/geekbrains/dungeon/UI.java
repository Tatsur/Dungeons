package ru.geekbrains.dungeon;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class UI {
    Label turns;
    Label experience;
    GameController gc;

    public UI(GameController gameController) {
        gc = gameController;
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        turns = new Label("10",labelStyle);
        experience = new Label("0",labelStyle);
    }

    public void update(float dt){
        experience.setText(gc.getHero().getExperience());
        experience.setText(0);
    }
}
