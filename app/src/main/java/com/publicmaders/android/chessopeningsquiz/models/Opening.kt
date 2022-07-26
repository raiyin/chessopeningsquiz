package com.publicmaders.android.chessopeningsquiz.models
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
open class Opening() {
    var ru_type: String = ""
    var en_type: String = ""
    var ru_name: String = ""
    var en_name: String = ""
    var ru_pgn: String = ""
    var en_pgn: String = ""
    var eco: String = ""

    constructor(ru_type: String, en_type: String, ru_name: String, en_name: String, ru_pgn: String, en_pgn: String, eco: String) : this() {
        this.ru_type = ru_type
        this.en_type = en_type
        this.ru_name = ru_name
        this.en_name = en_name
        this.ru_pgn = ru_pgn
        this.en_pgn = en_pgn
        this.eco = eco
    }

    override fun toString(): String
    {
        return if(Locale.getDefault().isO3Language =="rus") ru_name else en_name
    }
}