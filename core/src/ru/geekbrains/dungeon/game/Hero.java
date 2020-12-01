package ru.geekbrains.dungeon.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lombok.Data;
import ru.geekbrains.dungeon.helpers.Assets;
import ru.geekbrains.dungeon.game.GameController;
import ru.geekbrains.dungeon.screens.ScreenManager;

public class Hero extends Unit {
    private String name;
    private final int vision = 4;

    public int getVision() {
        return vision;
    }

    public Hero(GameController gc) {
        super(gc, 1, 1, 10);
        this.name = "Sir Lancelot";
        this.hpMax = 100;
        this.hp = this.hpMax;
        this.texture = Assets.getInstance().getAtlas().findRegion("knight");
        this.textureHp = Assets.getInstance().getAtlas().findRegion("hp");
    }

    public void update(float dt) {
        super.update(dt);
        if (Gdx.input.justTouched() && canIMakeAction()) {
            if(gc.getCursorX() == cellX && gc.getCursorY() == cellY) {
                endTurn = true;
                return;
            }
            Monster m = gc.getUnitController().getMonsterController().getMonsterInCell(gc.getCursorX(), gc.getCursorY());
            target = m;
            if (m != null && canIAttackThisTarget(m)) {
                attack(m);
            } else {
                goTo(gc.getCursorX(), gc.getCursorY());
            }
        }
    }
    @Override
    public boolean canIAttackInRange(Unit target){
        for (int i = -attackRange; i < attackRange; i++) {
            if(gc.getUnitController().getMonsterController().getMonsterInCell(cellX + i,cellY) != null ||
                    gc.getUnitController().getMonsterController().getMonsterInCell(cellX,cellY + i) != null) return true;
        }
        return false;
    }

    public void renderHUD(SpriteBatch batch, BitmapFont font, int x, int y) {
        stringHelper.setLength(0);
        stringHelper
                .append("Player: ").append(name).append("\n")
                .append("Gold: ").append(gold).append("\n");
        font.draw(batch, stringHelper, x, y);
    }
}
