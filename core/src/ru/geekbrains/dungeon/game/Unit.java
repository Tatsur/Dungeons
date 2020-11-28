package ru.geekbrains.dungeon.game;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.geekbrains.dungeon.helpers.Poolable;
import ru.geekbrains.dungeon.helpers.Utils;

public abstract class Unit implements Poolable {
    GameController gc;
    TextureRegion texture;
    TextureRegion textureHp;
    int damage;
    int defence;
    int hp;
    int hpMax;
    int cellX;
    int cellY;
    int attackRange;
    float movementTime;
    float movementMaxTime;
    int targetX, targetY;
    int turns, maxTurns;
    float innerTimer;
    int coins;
    int maxCoinsLoot;

    public int getDefence() {
        return defence;
    }

    public int getDamage() {
        return damage;
    }

    public int getCellX() {
        return cellX;
    }

    public int getCellY() {
        return cellY;
    }

    public Unit(GameController gc, int cellX, int cellY, int hpMax) {
        this.gc = gc;
        this.hpMax = hpMax;
        this.hp = hpMax;
        this.cellX = cellX;
        this.cellY = cellY;
        this.targetX = cellX;
        this.targetY = cellY;
        this.coins = 0;
        this.damage = 2;
        this.defence = 1;
        this.maxTurns = 5;
        this.movementMaxTime = 0.2f;
        this.attackRange = 2;
        this.innerTimer = MathUtils.random(1000.0f);
    }

    public void startTurn() {
        turns = maxTurns;
        if(hp<hpMax) ++hp;
    }

    @Override
    public boolean isActive() {
        return hp > 0;
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            gc.getUnitController().removeUnitAfterDeath(this);
        }
        return hp <= 0;
    }

    public boolean canIMakeAction() {
        return gc.getUnitController().isItMyTurn(this) && turns > 0 && isStayStill();
    }

    public boolean isStayStill() {
        return cellY == targetY && cellX == targetX;
    }

    public void goTo(int argCellX, int argCellY) {
        if (!gc.getGameMap().isCellPassable(argCellX, argCellY) || !gc.getUnitController().isCellFree(argCellX, argCellY)) {
            return;
        }
        if (Math.abs(argCellX - cellX) + Math.abs(argCellY - cellY) == 1) {
            targetX = argCellX;
            targetY = argCellY;
        }
    }

    public boolean canIAttackThisTarget(Unit target) {
        return cellX - target.getCellX() == 0 && Math.abs(cellY - target.getCellY()) <= attackRange ||
                cellY - target.getCellY() == 0 && Math.abs(cellX - target.getCellX()) <= attackRange;
    }

    public void attack(Unit target) {
        target.takeDamage(BattleCalc.attack(this, target));
        this.takeDamage(BattleCalc.checkCounterAttack(this, target));
        if(target.hp<=0) loot(target);
        turns--;
    }

    private int loot(Unit target){
        return coins += MathUtils.random(1,target.maxCoinsLoot);
    }

    public void update(float dt) {
        innerTimer += dt;
        if (!isStayStill()) {
            movementTime += dt;
            if (movementTime > movementMaxTime) {
                movementTime = 0;
                cellX = targetX;
                cellY = targetY;
                turns--;
            }
        }
    }
    private float alphaHpBar(){
        if(isMaxHp()) return 0.2f;
        return 1f;
    }
    private boolean isMaxHp(){
        return hp >= hpMax;
    }
    public void render(SpriteBatch batch, BitmapFont font18) {
        float px = cellX * GameMap.CELL_SIZE;
        float py = cellY * GameMap.CELL_SIZE;
        if (!isStayStill()) {
            px = cellX * GameMap.CELL_SIZE + (targetX - cellX) * (movementTime / movementMaxTime) * GameMap.CELL_SIZE;
            py = cellY * GameMap.CELL_SIZE + (targetY - cellY) * (movementTime / movementMaxTime) * GameMap.CELL_SIZE;
        }
        batch.draw(texture, px, py);
        batch.setColor(0.0f, 0.0f, 0.0f, alphaHpBar());


        float barX = px, barY = py + MathUtils.sin(innerTimer * 5.0f) * 2;
        batch.draw(textureHp, barX + 1, barY + 51, 58, 10);
        batch.setColor(0.7f, 0.0f, 0.0f, alphaHpBar());
        batch.draw(textureHp, barX + 2, barY + 52, 56, 8);
        batch.setColor(0.0f, 1.0f, 0.0f, alphaHpBar());
        batch.draw(textureHp, barX + 2, barY + 52, (float) hp / hpMax * 56, 8);
        batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font18.draw(batch, "" + hp, barX, barY + 64, 60, 1, false);
    }


    public void findEmptySpawn(int dx, int dy){
        do {
            dx = MathUtils.random(0, gc.getGameMap().getCellsX() - 1);
            dy = MathUtils.random(0, gc.getGameMap().getCellsY() - 1);
        } while (!(isCellEmpty(dx, dy) && Utils.isCellsAreNeighbours(cellX, cellY, dx, dy)));
    }

    public int getTurns() {
        return turns;
    }

    public boolean isCellEmpty(int cx, int cy) {
        return gc.getGameMap().isCellPassable(cx, cy) && gc.getUnitController().isCellFree(cx, cy);
    }

    public boolean amIBlocked() {
        return !(isCellEmpty(cellX - 1, cellY) || isCellEmpty(cellX + 1, cellY) || isCellEmpty(cellX, cellY - 1) || isCellEmpty(cellX, cellY + 1));
    }
}
