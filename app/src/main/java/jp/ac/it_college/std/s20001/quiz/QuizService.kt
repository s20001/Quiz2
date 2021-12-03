package jp.ac.it_college.std.s20001.quiz

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface QuizService {
    @GET("exec?f=data")
    fun getQuizProperties():
            Call<List<Quiz>>
}

fun createService(): QuizService {
    val BASE_URL = "https://script.google.com/macros/s/AKfycbznWpk2m8q6lbLWSS6qaz3uS6j3L4zPwv7CqDEiC433YOgAdaFekGJmjoAO60quMg6l/"

    val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()

    return retrofit.create(QuizService::class.java)
}