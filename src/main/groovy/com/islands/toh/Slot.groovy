package com.islands.toh

import com.islands.toh.constants.Aspect

class Slot {
    Tuple2<Aspect,Integer> required
    List<Tuple2<Aspect,Integer>> any
    List<Aspect> forbidden
}
