package baidu.com.testlibproject.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import baidu.com.commontools.utils.LogHelper
import baidu.com.testlibproject.R

data class Student(val name: String, val age: Int, val sex: String, val score: Int)

class KotlinActivity : AppCompatActivity() {

    private val students: List<Student>

    init {
        val jilen = Student("Jilen", 30, "m", 85)
        val shaw = Student("Shaw", 18, "m", 90)
        val yison = Student("Yison", 40, "f", 59)
        val jack = Student("Jack", 30, "m", 70)
        val lisa = Student("Lisa", 25, "f", 88)
        val pan = Student("Pan", 36, "f", 55)
        students = listOf(jilen, shaw, yison, jack, lisa, pan)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        testMapApi()
        testFilterApi()
        testSumByApi()
        testGroupByApi()
    }

    private fun testMapApi() {
        val list = intArrayOf(1, 2, 3, 4, 5)
        val newList = list.map { it * 2 }
        list.reduce { acc, i ->  acc + i}
        LogHelper.d(LOG_TAG, "map newList = $newList")
    }

    private fun testFilterApi() {
        val newStudents = students.filter { it.age > 35 }
        LogHelper.d(LOG_TAG, "filter age > 35 : $newStudents")
    }

    private fun testSumByApi() {
        val totalScore = students.sumBy { it.score }
        LogHelper.d(LOG_TAG, "total score by sumBy = $totalScore")
    }

    private fun testGroupByApi() {
        val group = students.groupBy { it.sex }
        for (key in group.keys) {
            LogHelper.d(LOG_TAG, "groupBy value = " + group[key])
        }
    }

    companion object {
        private const val LOG_TAG = "KotlinActivityTAG"
    }
}