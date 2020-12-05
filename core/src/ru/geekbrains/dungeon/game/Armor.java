package ru.geekbrains.dungeon.game;

import lombok.Data;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;

@Data
public class Armor {
    public enum Type {
        SPEAR_DEF, SWORD_DEF, MACE_DEF, AXE_DEF, BOW_DEF, GOD
    }

    Type type;
    int armor;
    int specialArmor;
    int radius;
    Set<Weapon.Type> weakWeapons = EnumSet.noneOf(Weapon.Type.class);

    public Armor(Type type, int armor, int specialArmor) {
        this.type = type;
        this.armor = armor;
        this.specialArmor = specialArmor;
        switch (type){
            case GOD: {
                weakWeapons.addAll(Weapon.weapons);
                break;
            }
            case SPEAR_DEF: {
                weakWeapons.add(Weapon.Type.SPEAR);
                break;
            }
            case SWORD_DEF: {
                weakWeapons.add(Weapon.Type.SWORD);
                break;
            }
            case MACE_DEF: {
                weakWeapons.add(Weapon.Type.MACE);
                break;
            }
            case AXE_DEF: {
                weakWeapons.add(Weapon.Type.AXE);
                break;
            }
            case BOW_DEF: {
                weakWeapons.add(Weapon.Type.BOW);
                break;
            }
        }
    }

    public int getArmorVsWeapon(Weapon weapon){
        int tempArmor = armor;
        for(Iterator<Weapon.Type> it = weakWeapons.iterator(); it.hasNext();){
            if(it.next().equals(weapon.type)){
                tempArmor += specialArmor;
                return tempArmor;
            }
        }
        return armor;
    }
}
