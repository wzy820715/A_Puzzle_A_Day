package com.demo.apuzzleaday.entity

class PieceT: BasePiece() {
    private val front1 = arrayOf(
        charArrayOf('0', '0', 'T', '0'),
        charArrayOf('T', 'T', 'T', 'T'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front2 = arrayOf(
        charArrayOf('0', 'T', '0', '0'),
        charArrayOf('T', 'T', '0', '0'),
        charArrayOf('0', 'T', '0', '0'),
        charArrayOf('0', 'T', '0', '0'))
    private val front3 = arrayOf(
        charArrayOf('T', 'T', 'T', 'T'),
        charArrayOf('0', 'T', '0', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front4 = arrayOf(
        charArrayOf('T', '0', '0', '0'),
        charArrayOf('T', '0', '0', '0'),
        charArrayOf('T', 'T', '0', '0'),
        charArrayOf('T', '0', '0', '0'))
    private val contrary1 = arrayOf(
        charArrayOf('T', 'T', 'T', 'T'),
        charArrayOf('0', '0', 'T', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary2 = arrayOf(
        charArrayOf('T', '0', '0', '0'),
        charArrayOf('T', 'T', '0', '0'),
        charArrayOf('T', '0', '0', '0'),
        charArrayOf('T', '0', '0', '0'))
    private val contrary3 = arrayOf(
        charArrayOf('0', 'T', '0', '0'),
        charArrayOf('T', 'T', 'T', 'T'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary4 = arrayOf(
        charArrayOf('0', 'T', '0', '0'),
        charArrayOf('0', 'T', '0', '0'),
        charArrayOf('T', 'T', '0', '0'),
        charArrayOf('0', 'T', '0', '0'))
    override val rotatedTypes: Array<Array<CharArray>>
        get() = arrayOf(
            front1,
            front2,
            front3,
            front4,
            contrary1,
            contrary2,
            contrary3,
            contrary4)
    override val symbol: String
        get() = "T"
}