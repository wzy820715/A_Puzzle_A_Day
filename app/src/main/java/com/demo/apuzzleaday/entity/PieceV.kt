package com.demo.apuzzleaday.entity

class PieceV: BasePiece() {
    private val front1 = arrayOf(
        charArrayOf('V', '0', '0', '0'),
        charArrayOf('V', '0', '0', '0'),
        charArrayOf('V', 'V', 'V', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front2 = arrayOf(
        charArrayOf('0', '0', 'V', '0'),
        charArrayOf('0', '0', 'V', '0'),
        charArrayOf('V', 'V', 'V', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front3 = arrayOf(
        charArrayOf('V', 'V', 'V', '0'),
        charArrayOf('0', '0', 'V', '0'),
        charArrayOf('0', '0', 'V', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front4 = arrayOf(
        charArrayOf('V', 'V', 'V', '0'),
        charArrayOf('V', '0', '0', '0'),
        charArrayOf('V', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    override val rotatedTypes: Array<Array<CharArray>>
        get() = arrayOf(
            front1,
            front2,
            front3,
            front4)
    override val symbol: String
        get() = "V"
}