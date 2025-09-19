package com.islands.toh

import groovy.sql.Sql

class SQLManager {
    private static Sql conn

    static void initialize(String file) {
        conn = Sql.newInstance("jdbc:sqlite:$file")
    }
}
