package com.demo.apuzzleaday.entity

class PieceU: BasePiece() {
    private val front1 = arrayOf(
        charArrayOf('U', '0', 'U', '0'),
        charArrayOf('U', 'U', 'U', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front2 = arrayOf(
        charArrayOf('U', 'U', '0', '0'),
        charArrayOf('0', 'U', '0', '0'),
        charArrayOf('U', 'U', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front3 = arrayOf(
        charArrayOf('U', 'U', 'U', '0'),
        charArrayOf('U', '0', 'U', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front4 = arrayOf(
        charArrayOf('U', 'U', '0', '0'),
        charArrayOf('U', '0', '0', '0'),
        charArrayOf('U', 'U', '0', '0'),
        charArrayOf('0', '0', '0', '0'))

    override val rotatedTypes: Array<Array<CharArray>>
        get() = arrayOf(
            front1,
            front2,
            front3,
            front4)
    override val symbol: String
        get() = "U"
}