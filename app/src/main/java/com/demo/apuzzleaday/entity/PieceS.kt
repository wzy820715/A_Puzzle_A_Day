package com.demo.apuzzleaday.entity

class PieceS: BasePiece() {
    private val front1 = arrayOf(
        charArrayOf('0', 'S', 'S', '0'),
        charArrayOf('0', 'S', '0', '0'),
        charArrayOf('S', 'S', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front2 = arrayOf(
        charArrayOf('S', '0', '0', '0'),
        charArrayOf('S', 'S', 'S', '0'),
        charArrayOf('0', '0', 'S', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary1 = arrayOf(
        charArrayOf('S', 'S', '0', '0'),
        charArrayOf('0', 'S', '0', '0'),
        charArrayOf('0', 'S', 'S', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary2 = arrayOf(
        charArrayOf('0', '0', 'S', '0'),
        charArrayOf('S', 'S', 'S', '0'),
        charArrayOf('S', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))

    override val rotatedTypes: Array<Array<CharArray>>
        get() = arrayOf(
            front1,
            front2,
            contrary1,
            contrary2)
    override val symbol: String
        get() = "S"
}