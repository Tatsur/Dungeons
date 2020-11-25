package ru.geekbrains.dungeon;

import com.badlogic.gdx.math.MathUtils;
import org.graalvm.compiler.loop.MathUtil;
import ru.geekbrains.dungeon.units.Unit;

public class BattleCalc {
    public static int attack(Unit attacker, Unit target) {
        int out = attacker.getDamage();
        out -= target.getDefence();
        if (out < 0) {
            out = 0;
        }
        return out;
    }

    public static int checkCounterAttack(Unit attacker, Unit target) {
        if (MathUtils.random() < 0.2f) {
            int amount = attack(target, attacker);
            return amount;
        }
        return 0;
    }
    public static float destination(Unit thisUnit, Unit target){
        return (float) Math.sqrt(Math.pow(thisUnit.getCellX()-target.getCellX(),2)-Math.pow(thisUnit.getCellY()-target.getCellY(),2));
    }
}
