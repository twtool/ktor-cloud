package icu.twtool.ksp.ext

import java.io.OutputStreamWriter


operator fun OutputStreamWriter.plusAssign(str: String) {
    write(str)
}

class LineDef(
    val str: String,
    var tabs: Int = 0,
    var lines: Int = 0,
)

infix fun Int.tabs(str: String) = LineDef(str, this, 0)
infix fun String.enter(lines: Int) = LineDef(this, 0, lines)
infix fun LineDef.enter(lines: Int) = this.apply { this.lines = lines }

operator fun OutputStreamWriter.plusAssign(def: LineDef) {
    repeat(def.tabs) {
        write('\t'.code)
    }
    write(def.str)
    repeat(def.lines) {
        write('\n'.code)
    }
}