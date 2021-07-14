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
            charArrayOf('Z', '0'),
            charArrayOf('Z', '0'),
            charArrayOf('Z', 'Z'),
            charArrayOf('0', 'Z'))

        val frontP = arrayOf(
            charArrayOf('P', '0'),
            charArrayOf('P', 'P'),
            charArrayOf('P', 'P'))

        val frontI = arrayOf(
            charArrayOf('I', 'I'),
            charArrayOf('I', 'I'),
            charArrayOf('I', 'I'))

        val frontV = arrayOf(
            charArrayOf('V', '0', '0'),
            charArrayOf('V', '0', '0'),
            charArrayOf('V', 'V', 'V'))

        val frontU = arrayOf(
            charArrayOf('U', 'U'),
            charArrayOf('U', '0'),
            charArrayOf('U', 'U'))

        val frontT = arrayOf(
            charArrayOf('0', 'T'),
            charArrayOf('T', 'T'),
            charArrayOf('0', 'T'),
            charArrayOf('0', 'T'))

        val frontS = arrayOf(
            charArrayOf('0', 'S', 'S'),
            charArrayOf('0', 'S', '0'),
            charArrayOf('S', 'S', '0'))

        val frontL = arrayOf(
            charArrayOf('L', '0'),
            charArrayOf('L', '0'),
            charArrayOf('L', '0'),
            charArrayOf('L', 'L'))

    }

}