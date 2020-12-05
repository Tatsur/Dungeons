package ru.geekbrains.dungeon.game;

import lombok.Data;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

@Data
public class Weapon {
    public enum Type {
        SPEAR, SWORD, MACE, AXE, BOW
    }

    Type type;
    int damage;
    int radius;

    final static Set<Type> weapons = EnumSet.allOf(Type.class);

    public Weapon(Type type, int damage, int radius) {
        this.type = type;
        this.damage = damage;
        this.radius = radius;
    }
}
