package com.publicmaders.android.chessopeningsquiz.delegates

import com.publicmaders.android.chessopeningsquiz.models.ChessPiece
import com.publicmaders.android.chessopeningsquiz.models.Square

interface ChessDelegate {
    fun pieceAt(square: Square) : ChessPiece?
    fun movePiece(from: Square, to: Square)
}