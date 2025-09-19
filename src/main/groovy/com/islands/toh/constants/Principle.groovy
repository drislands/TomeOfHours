package com.islands.toh.constants

import java.awt.Color

class Principle extends Aspect {
    final static List<Principle> principles = []


    Color color

    Principle(String hex,String name,String description) {
        super(name,description)
        this.color = Color.decode(hex)
    }

    static setPrinciples() {
        principles << new Principle('FF614F','Grail','')
        principles << new Principle('FFE300','Lantern','')
        principles << new Principle('BDEFFF','Winter','')
        principles << new Principle('FF7F8C','Heart','')
        principles << new Principle('FF8E3F','Forge','')
        principles << new Principle('D6DE4A','Edge','')
        principles << new Principle('F1E9C2','Moth','')
        principles << new Principle('B54EFC','Knock','')
        principles << new Principle('EF63FF','Rose','')
        principles << new Principle('CB9F4D','Scale','')
        principles << new Principle('2C69E1','Sky','')
        principles << new Principle('CBBCD6','Moon','')
        principles << new Principle('2CD391','Nectar','')
    }

    static Principle getByName(String name) {
        principles.find { it.name == name }
    }
}
