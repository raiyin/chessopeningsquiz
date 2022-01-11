package com.publicmaders.android.chessopeningsquiz

// Добавить ECO.

open class Opening()
{
    var name: String = ""
        private set
    var pgn: String = ""
        private set

    constructor(name: String, pgn: String) : this() {
        this.name = name
        this.pgn = pgn
    }

    override fun toString(): String = name
}