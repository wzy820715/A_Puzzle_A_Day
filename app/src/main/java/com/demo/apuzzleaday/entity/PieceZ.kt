package com.demo.apuzzleaday.entity

class PieceZ: BasePiece() {
    private val front1 = arrayOf(
        charArrayOf('Z', '0', '0', '0'),
        charArrayOf('Z', '0', '0', '0'),
        charArrayOf('Z', 'Z', '0', '0'),
        charArrayOf('0', 'Z', '0', '0'))
    private val front2 = arrayOf(
        charArrayOf('0', 'Z', '0', '0'),
        charArrayOf('0', 'Z', '0', '0'),
        charArrayOf('Z', 'Z', '0', '0'),
        charArrayOf('Z', '0', '0', '0'))
    private val front3 = arrayOf(
        charArrayOf('Z', 'Z', '0', '0'),
        charArrayOf('0', 'Z', 'Z', 'Z'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val front4 = arrayOf(
        charArrayOf('0', 'Z', 'Z', 'Z'),
        charArrayOf('Z', 'Z', '0', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary1 = arrayOf(
        charArrayOf('Z', 'Z', 'Z', '0'),
        charArrayOf('0', '0', 'Z', 'Z'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary2 = arrayOf(
        charArrayOf('0', '0', 'Z', 'Z'),
        charArrayOf('Z', 'Z', 'Z', '0'),
        charArrayOf('0', '0', '0', '0'),
        charArrayOf('0', '0', '0', '0'))
    private val contrary3 = arrayOf(
        charArrayOf('0', 'Z', '0', '0'),
        charArrayOf('Z', 'Z', '0', '0'),
        charArrayOf('Z', '0', '0', '0'),
        charArrayOf('Z', '0', '0', '0'))
    private val contrary4 = arrayOf(
        charArrayOf('Z', '0', '0', '0'),
        charArrayOf('Z', 'Z', '0', '0'),
        charArrayOf('0', 'Z', '0', '0'),
        charArrayOf('0', 'Z', '0', '0'))
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
        get() = "Z"
}