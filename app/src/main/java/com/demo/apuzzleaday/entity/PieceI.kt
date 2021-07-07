package com.demo.apuzzleaday.entity

class PieceI: BasePiece() {
    private val front1 = arrayOf(
        charArrayOf('I', 'I', 'I', '0'),
        charArrayOf('I', 'I', 'I', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front2 = arrayOf(
        charArrayOf('I', 'I', '0', '0'),
        charArrayOf('I', 'I', '0', '0'),
        charArrayOf('I', 'I', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    override val rotatedTypes: Array<Array<CharArray>>
        get() = arrayOf(
            front1,
            front2)
    override val symbol: String
        get() = "I"
}