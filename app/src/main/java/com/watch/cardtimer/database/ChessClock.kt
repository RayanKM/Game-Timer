package com.watch.cardtimer.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.watch.cardtimer.utils.ChessUtils.Companion.BLITZ
import com.watch.cardtimer.utils.ChessUtils.Companion.BULLET
import com.watch.cardtimer.utils.ChessUtils.Companion.CLASSIC
import com.watch.cardtimer.utils.ChessUtils.Companion.RAPID
import com.watch.cardtimer.utils.CountDownTimer.Companion.ONE_MINUTE
import kotlin.math.max

@Entity(tableName = "chess_clock")
data class ChessClock(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    @ColumnInfo
    var firstPlayerTime: Long = 0L,
    @ColumnInfo
    var secondPlayerTime: Long = 0L,
    @ColumnInfo
    var gameType: Int = 0,
    @ColumnInfo
    var increment: Long = 0
) {
    init {
        val maxTime = max(firstPlayerTime, secondPlayerTime) / ONE_MINUTE
        gameType = when {
            maxTime < BULLET -> BULLET
            maxTime < BLITZ -> BLITZ
            maxTime < RAPID -> RAPID
            else -> CLASSIC
        }
    }
}