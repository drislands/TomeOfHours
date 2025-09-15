package com.islands.toh.constants

import java.awt.Color

enum Principle implements Aspect {
    GRAIL("FF614F"),
    LANTERN("FFE300"),
    WINTER("BDEFFF"),
    HEART("FF7F8C"),
    FORGE("FF8E3F"),
    EDGE("D6DE4A"),
    MOTH("F1E9C2"),
    KNOCK("B54EFC"),
    ROSE("EF63FF"),
    SCALE("CB9F4D"),
    SKY("2C69E1"),
    MOON("CBBCD6"),
    NECTAR("2CD391")

    Color color

    Principle(String hex) {
        this.color = Color.decode(hex)
    }
}
