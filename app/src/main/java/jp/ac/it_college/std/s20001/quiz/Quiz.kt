package jp.ac.it_college.std.s20001.quiz

data class Quiz(
    val id: Int,
    val question: String,
    val answers: Int,
    val choices: List<String>
)
