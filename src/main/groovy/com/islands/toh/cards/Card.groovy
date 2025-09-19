package com.islands.toh.cards

import com.islands.toh.constants.Aspect
import com.islands.toh.constants.Named

class Card implements Named{
    Map<Aspect,Integer> aspects = [:]

    Card(String name, String description) {
        this.name = name
        this.description = description
    }
}
