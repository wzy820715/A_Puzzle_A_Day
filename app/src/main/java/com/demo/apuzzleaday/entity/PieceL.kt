package com.demo.apuzzleaday.entity

class PieceL: BasePiece() {
    private val front1 = arrayOf(
        charArrayOf('L', 'L', 'L', 'L'),
        charArrayOf('L', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front2 = arrayOf(
        charArrayOf('L', '0', '0', '0'),
        charArrayOf('L', '0', '0', '0'),
        charArrayOf('L', '0', '0', '0'),
        charArrayOf('L', 'L', '0', '0'))
    private val front3 = arrayOf(
        charArrayOf('0', '0', '0', 'L'),
        charArrayOf('L', 'L', 'L', 'L'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front4 = arrayOf(
        charArrayOf('L', 'L', '0', '0'),
        charArrayOf('0', 'L', '0', '0'),
        charArrayOf('0', 'L', '0', '0'),
        charArrayOf('0', 'L', '0', '0'))
    private val contrary1 = arrayOf(
        charArrayOf('L', 'L', 'L', 'L'),
        charArrayOf('0', '0', '0', 'L'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary2 = arrayOf(
        charArrayOf('L', 'L', '0', '0'),
        charArrayOf('L', '0', '0', '0'),
        charArrayOf('L', '0', '0', '0'),
        charArrayOf('L', '0', '0', '0'))
    private val contrary3 = arrayOf(
        charArrayOf('L', '0', '0', '0'),
        charArrayOf('L', 'L', 'L', 'L'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary4 = arrayOf(
        charArrayOf('0', 'L', '0', '0'),
        charArrayOf('0', 'L', '0', '0'),
        charArrayOf('0', 'L', '0', '0'),
        charArrayOf('L', 'L', '0', '0'))

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
        get() = "L"

}