package com.demo.apuzzleaday.entity

abstract class BasePiece {
    abstract val rotatedTypes: Array<Array<CharArray>>
    abstract val symbol: String
}