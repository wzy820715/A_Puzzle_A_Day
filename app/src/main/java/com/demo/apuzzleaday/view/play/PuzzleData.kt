package com.demo.apuzzleaday.view.play

class PuzzleData {
    companion object{

        val boundaryArray = listOf(
            charArrayOf('0', '0', '0', '0', '0', '0', 'x'),
            charArrayOf('0', '0', '0', '0', '0', '0', 'x'),
            charArrayOf('0', '0', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '0', '0', '0', '0', '0'),
            charArrayOf('0', '0', '0', 'x', 'x', 'x', 'x')
        )

        val frontZ = arrayOf(
            charArrayOf('0', 'Z'),
            charArrayOf('0', 'Z'),
            charArrayOf('Z', 'Z'),
            charArrayOf('Z', '0'))

        val frontP = arrayOf(
            charArrayOf('P', 'P'),
            charArrayOf('P', 'P'),
            charArrayOf('0', 'P'))

        val frontI = arrayOf(
            charArrayOf('I', 'I'),
            charArrayOf('I', 'I'),
            charArrayOf('I', 'I'))

        val frontV = arrayOf(
            charArrayOf('0', '0', 'V'),
            charArrayOf('0', '0', 'V'),
            charArrayOf('V', 'V', 'V'))

        val frontU = arrayOf(
            charArrayOf('U', 'U'),
            charArrayOf('0', 'U'),
            charArrayOf('U', 'U'))

        val frontT = arrayOf(
            charArrayOf('T', '0'),
            charArrayOf('T', '0'),
            charArrayOf('T', 'T'),
            charArrayOf('T', '0'))

        val frontS = arrayOf(
            charArrayOf('S', 'S', '0'),
            charArrayOf('0', 'S', '0'),
            charArrayOf('0', 'S', 'S'))

        val frontL = arrayOf(
            charArrayOf('L', 'L'),
            charArrayOf('L', '0'),
            charArrayOf('L', '0'),
            charArrayOf('L', '0'))

    }

}