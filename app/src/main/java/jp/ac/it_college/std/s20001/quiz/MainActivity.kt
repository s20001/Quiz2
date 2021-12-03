package jp.ac.it_college.std.s20001.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import jp.ac.it_college.std.s20001.quiz.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val quizService by lazy { createService() }
    private val helper = DatabaseHelper(this@MainActivity)
    private lateinit var quiz: Array<Quiz>
    private val Id = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchQuizData()

        val db = helper.writableDatabase
        val sql = "SELECT * FROM quize WHERE _id = ${Id}"
        val cursor = db.rawQuery(sql, null)
        var question = ""
        while (cursor.moveToNext()) {
            val index = cursor.getColumnIndex("question")
            question = cursor.getString(index)
        }
        cursor.close()

        binding.nextButton.setOnClickListener {
            quizActivity(11)
        }
    }

    override fun onDestroy() {
        helper.close()
        super.onDestroy()
    }

    fun quizActivity(select: Int) {
        val mode = select
        val intent = Intent(this@MainActivity, SubActivity:: class.java)
        intent.putExtra("MODE", mode)
        intent.putExtra(SubActivity.KEY_STATE, quiz)
        startActivity(intent)
        finish()
    }

    private fun fetchQuizData() {
        quizService.getQuizProperties().enqueue(
            object: Callback<List<Quiz>> {
                override fun onFailure(call: Call<List<Quiz>>, t: Throwable) {

                }

                override fun onResponse(call: Call<List<Quiz>>, response: Response<List<Quiz>>) {
                    insertSql(response)
                }
            }
        )
    }

    fun insertSql(response: Response<List<Quiz>>){
        val quizDates = response.body()

        val insert = """INSERT INTO quiz (
            _id, question, answers,
            choicei, choiceii, choiceiii, choiceiv, choicev, choicevi
            ) VALUES (?,?,?<?,?<?,?,?,?)""".trimIndent()
        val stmt = helper.writableDatabase.compileStatement(insert)
        for (i in 0..quizDates!!.size - 1) {
            stmt.let {
                it.bindLong(1, (i + 1).toLong())
                it.bindLong(3, quizDates[i].answers.toLong())
                it.bindString(2, quizDates[i].question.toString())
                it.bindString(4, quizDates[i].choices[0].toString())
                it.bindString(5, quizDates[i].choices[1].toString())
                it.bindString(6, quizDates[i].choices[2].toString())
                it.bindString(7, quizDates[i].choices[3].toString())
                it.bindString(8, quizDates[i].choices[4].toString())
                it.bindString(9, quizDates[i].choices[5].toString())

                it.executeInsert()
            }
        }
    }
}
