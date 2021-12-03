package jp.ac.it_college.std.s20001.quiz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Parcel
import android.os.Parcelable
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import androidx.core.os.HandlerCompat
import jp.ac.it_college.std.s20001.quiz.databinding.ActivitySubBinding
import java.io.BufferedReader
import java.io.InputStreamReader
import com.opencsv.CSVIterator
import com.opencsv.CSVReader


class SubActivity : AppCompatActivity() {
    companion object {
        val KEY_STATE = "key_state"
    }

    private lateinit var binding: ActivitySubBinding

    // 選択された主キーを表す
    private var _Id = -1

    //

    // 問題
    private var a: Int = 0

    // 正解
    private var y: Int = 0

    var n: Long = 0


    private val quizList: MutableList<Array<String>> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mode = intent.getIntExtra("MODE", 11)

        n = if(mode == 11) {
            11L
        } else 6L

        val fileReader = BufferedReader(InputStreamReader(assets.open("s20001.csv")))
        val csvIter = CSVIterator(CSVReader(fileReader))
        for(row in csvIter) {
            if(row == null) break

            row.toString().split(",").toTypedArray()
            quizList.add(row)
        }
        quizList.removeAt(0)

        gameStart(quizList)



    }

    fun gameStart(quiz: MutableList<Array<String>>) {
        binding.quiz.text = "${a + 1}問目: ${quiz[a][0]}"
        var choices: MutableList<String> = mutableListOf()
        for(i in 2..5) {
            choices.add(quiz[a][i])
        }

        choices.shuffle()
        binding.s1.text = choices[0]
        binding.s2.text = choices[1]
        binding.s3.text = choices[2]
        binding.s4.text = choices[3]


    }
}
