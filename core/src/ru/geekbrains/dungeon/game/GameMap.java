package ru.geekbrains.dungeon.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import ru.geekbrains.dungeon.game.units.Unit;
import ru.geekbrains.dungeon.helpers.Assets;
import ru.geekbrains.dungeon.helpers.Utils;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
    public enum CellType {
        GRASS, WATER, TREE
    }

    public enum DropType {
        NONE, GOLD, FOOD
    }

    private class Cell {
        CellType type;

        DropType dropType;
        int dropPower;
        int cellX;
        int cellY;
        int index;

        public Cell() {
            type = CellType.GRASS;
            dropType = DropType.NONE;
            index = 0;
        }

        public void changeType(CellType to) {
            type = to;
            if (type == CellType.TREE) {
                index = MathUtils.random(4);
            }
        }
    }

    public static final int CELLS_X = 22;
    public static final int CELLS_Y = 12;
    public static final int CELL_SIZE = 60;
    public static final int FOREST_PERCENTAGE = 5;

    public int getCellsX() {
        return CELLS_X;
    }

    public int getCellsY() {
        return CELLS_Y;
    }

    private Cell[][] data;
    private List<Cell> freeTrees;
    private TextureRegion grassTexture;
    private TextureRegion goldTexture;
    private TextureRegion berryTexture;
    private TextureRegion[] treesTextures;

    public GameMap() {
        freeTrees = new ArrayList<>();
        this.data = new Cell[CELLS_X][CELLS_Y];
        for (int i = 0; i < CELLS_X; i++) {
            for (int j = 0; j < CELLS_Y; j++) {
                this.data[i][j] = new Cell();
            }
        }
        int treesCount = (int) ((CELLS_X * CELLS_Y * FOREST_PERCENTAGE) / 100.0f);
        for (int i = 0; i < treesCount; i++) {
            int tempX = MathUtils.random(0, CELLS_X - 1);
            int tempY = MathUtils.random(0, CELLS_Y - 1);
            Cell tempCell = this.data[tempX][tempY];
            tempCell.changeType(CellType.TREE);
            freeTrees.add(tempCell);
            tempCell.cellX = tempX;
            tempCell.cellY = tempY;
        }

        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.goldTexture = Assets.getInstance().getAtlas().findRegion("chest").split(60, 60)[0][0];
        this.treesTextures = Assets.getInstance().getAtlas().findRegion("trees").split(60, 90)[0];
        this.berryTexture = Assets.getInstance().getAtlas().findRegion("berry");
    }

    public boolean isCellPassable(int cx, int cy) {
        if (cx < 0 || cx > getCellsX() - 1 || cy < 0 || cy > getCellsY() - 1) {
            return false;
        }
        if (data[cx][cy].type != CellType.GRASS) {
            return false;
        }
        return true;
    }

    public void renderGround(SpriteBatch batch) {
        for (int i = 0; i < CELLS_X; i++) {
            for (int j = CELLS_Y - 1; j >= 0; j--) {
                batch.draw(grassTexture, i * CELL_SIZE, j * CELL_SIZE);
            }
        }
    }

    public void renderObjects(SpriteBatch batch) {
        for (int i = 0; i < CELLS_X; i++) {
            for (int j = CELLS_Y - 1; j >= 0; j--) {
                Cell tempCell = data[i][j];
                if (tempCell.type == CellType.TREE) {
                    batch.draw(treesTextures[tempCell.index], i * CELL_SIZE, j * CELL_SIZE);
                    if(tempCell.dropType.equals(DropType.FOOD)){
                        batch.draw(berryTexture,i*CELL_SIZE+15,j*CELL_SIZE+15);
                    }
                }
                if (tempCell.dropType == DropType.GOLD) {
                    batch.draw(goldTexture, i * CELL_SIZE, j * CELL_SIZE);
                }
            }
        }
    }

    public void addBerryToTree() {
        if(!freeTrees.isEmpty()) {

            int cellIndex = MathUtils.random(freeTrees.size() - 1);
            Cell tempCell = freeTrees.get(cellIndex);
            tempCell.dropType = DropType.FOOD;
            tempCell.dropPower = 50;
            freeTrees.remove(cellIndex);
        }
    }

    public int takeFood(int cellX,int cellY){
        Cell c = data[cellX][cellY];
            c.dropType = DropType.NONE;
            int dropPower = c.dropPower;
            c.dropPower = 0;
            freeTrees.add(c);
            return dropPower;
    }

    public boolean canILootThisCell(Unit unit,int targetX,int targetY){
        if(targetX>=0 && targetY>=0 && targetX<CELLS_X && targetY<CELLS_Y) {
            Cell c = data[targetX][targetY];
            return c.type.equals(CellType.TREE) && Utils.getCellsIntDistance(unit.getCellX(), unit.getCellY(), targetX, targetY) <= 1
                    && c.dropType.equals(DropType.FOOD);
        }
        return false;
    }

    // todo: перенести в калькулятор
    public void generateDrop(int cellX, int cellY, int power) {
        if (MathUtils.random() < 0.5f) {
            DropType randomDropType = DropType.GOLD;

            if (randomDropType == DropType.GOLD) {
                int goldAmount = power + MathUtils.random(power, power * 3);
                data[cellX][cellY].dropType = randomDropType;
                data[cellX][cellY].dropPower = goldAmount;
            }
        }
    }

    public boolean hasDropInCell(int cellX, int cellY) {
        return data[cellX][cellY].dropType != DropType.NONE;
    }

    public void checkAndTakeDrop(Unit unit) {
        Cell currentCell = data[unit.getCellX()][unit.getCellY()];
        if (currentCell.dropType == DropType.NONE) {
            return;
        }
        if (currentCell.dropType == DropType.GOLD) {
            unit.addGold(currentCell.dropPower);
        }
        currentCell.dropType = DropType.NONE;
        currentCell.dropPower = 0;
    }
}
