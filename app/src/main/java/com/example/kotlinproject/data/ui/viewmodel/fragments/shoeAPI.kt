package com.example.kotlinproject.data.ui.viewmodel.fragments

import ShoeAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinproject.R
import com.example.kotlinproject.data.model.Shoe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import org.jsoup.Jsoup

class ShoeApiFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ShoeAdapter
    private val shoes = mutableListOf<Shoe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_shoe_a_p_i, container, false)

        recyclerView = view.findViewById(R.id.shoe_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = ShoeAdapter(shoes)
        recyclerView.adapter = adapter

        fetchShoesFromShoegazing()

        return view
    }

    private fun fetchShoesFromShoegazing() {
        lifecycleScope.launch {
            val fetchedShoes = getShoesFromShoegazing()
            if (fetchedShoes.isNotEmpty()) {
                shoes.clear()
                shoes.addAll(fetchedShoes)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "No shoes found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private suspend fun getShoesFromShoegazing(): List<Shoe> = withContext(Dispatchers.IO) {
        val result = mutableListOf<Shoe>()
        try {
            val html = HttpClient(OkHttp).use { client ->
                client.get("https://shoegazing.com/").bodyAsText()
            }

            val doc = Jsoup.parse(html)
            val articles = doc.select("article")

            for (article in articles) {
                val title = article.select("h2.entry-title a").text()
                val link = article.select("h2.entry-title a").attr("href")
                val imageUrl = article.select("img")
                    .map { it.attr("src") }
                    .firstOrNull { it.startsWith("http") && !it.startsWith("data:") } ?: ""
                val excerpt = article.select("div.entry-summary p").text()

                Log.d("ShoeApiFragment", "Image URL: $imageUrl")

                if (title.isNotBlank() && imageUrl.isNotBlank()) {
                    result.add(Shoe(title, excerpt, imageUrl, link))
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return@withContext result
    }

}
