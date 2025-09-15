package com.islands.toh.constants

import java.awt.Color

enum SoulElement {
    CHOR("EC69B2"),
    SHAPT("B7226C"),
    WIST("A75BD3"),
    PHOST("FFDB47"),
    METTLE("F7902C"),
    EREB("7A66CC"),
    HEALTH("FF4552"),
    FET("5D7ED0"),
    TRIST("7BA2BD")

    Color color

    SoulElement(String hex) {
        this.color = Color.decode(hex)
    }
}
