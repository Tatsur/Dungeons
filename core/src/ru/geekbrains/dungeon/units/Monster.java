package ru.geekbrains.dungeon.units;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import ru.geekbrains.dungeon.BattleCalc;
import ru.geekbrains.dungeon.GameController;

import java.util.prefs.BackingStoreException;

public class Monster extends Unit {
    private float aiBrainsImplseTime;
    private Unit target;
    private float aggroDistance;
    private boolean chase = false;
    private int tempX;
    private int tempY;

    public Monster(TextureAtlas atlas, GameController gc) {
        super(gc, 5, 2, 10);
        this.texture = atlas.findRegion("monster");
        this.textureHp = atlas.findRegion("hp");
        this.hp = -1;
        this.aggroDistance = 5;
    }

    public void activate(int cellX, int cellY) {
        this.cellX = cellX;
        this.cellY = cellY;
        this.targetX = cellX;
        this.targetY = cellY;
        this.tempX = cellX;
        this.tempY = cellY;
        this.hpMax = 10;
        this.hp = hpMax;
        this.target = gc.getUnitController().getHero();
    }
    private boolean checkDestination(){
        if(BattleCalc.destination(gc.getUnitController().getHero(),this)<=aggroDistance) {
            chase = true;
            return true;
        }
        return false;
    }
    public void update(float dt) {
        super.update(dt);
        if (canIMakeAction()) {
            if (isStayStill()) {
                aiBrainsImplseTime += dt;
            }
            if (aiBrainsImplseTime > 0.4f) {
                aiBrainsImplseTime = 0.0f;
                if (canIAttackThisTarget(target)) {
                    attack(target);
                } else {
                    if(checkDestination() || chase) {
                        this.target = gc.getUnitController().getHero();
                        tryToMove(target.getCellX(),target.getCellY());
                    } else {
                        if(cellX != tempX && cellY != tempY) {
                            if(gc.getGameMap().isCellPassable(tempX,tempY) && gc.getUnitController().isCellFree(tempX,tempY))
                                tryToMove(tempX,tempY);
                        }else randomCoordinates();
                        tryToMove(tempX,tempY);
                    }
                }
            }
        }
    }
    private void randomCoordinates(){
        tempX = MathUtils.random(gc.getGameMap().getCellsX());
        tempY = MathUtils.random(gc.getGameMap().getCellsY());
    }
    public void tryToMove(int x, int y) {
        int bestX = -1, bestY = -1;
        float bestDst = 10000;
        for (int i = cellX - 1; i <= cellX + 1; i++) {
            for (int j = cellY - 1; j <= cellY + 1; j++) {
                if (Math.abs(cellX - i) + Math.abs(cellY - j) == 1 && gc.getGameMap().isCellPassable(i, j) && gc.getUnitController().isCellFree(i, j)) {
                    float dst = (float) Math.sqrt((i - x) * (i - x) + (j - y) * (j - y));
                    if (dst < bestDst) {
                        bestDst = dst;
                        bestX = i;
                        bestY = j;
                    }
                }
            }
        }
        goTo(bestX, bestY);
    }
}
