package com.mm.xtv




import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.mm.xtv.adapters.ChannelAdapter
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.net.URL


class MainActivity : AppCompatActivity() {
    private lateinit var tvRecyclerView: RecyclerView
    private lateinit var refresh:SwipeRefreshLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvRecyclerView=findViewById(R.id.tvRecycler)
        refresh=findViewById(R.id.refresh)
        refresh.isRefreshing = true
        doSync()
        refresh.setOnRefreshListener { this.doSync() }
        supportActionBar?.hide()

    }

    private fun doSync(){
        doAsync {
            val jsons =
                URL("https://raw.githubusercontent.com/koarkarphyoe/appjson/main/streammyanmar.json").readText()

            uiThread {
                val channel = Gson().fromJson(jsons, Array<Channels>::class.java).toList()

                Log.d("my_message", jsons)
                val layoutManager = GridLayoutManager(this@MainActivity, 3)
                tvRecyclerView.layoutManager = layoutManager
                val adapter = ChannelAdapter(this@MainActivity, channel)
                tvRecyclerView.adapter = adapter
                refresh.isRefreshing=false
            }
        }

    }

    override fun onBackPressed() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle(R.string.app_name)
        builder.setIcon(R.mipmap.ic_launcher)
        builder.setMessage("Do you want to exit?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _, _ -> finish() }
            .setNegativeButton(
                "No"
            ) { dialog, _ -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }

}



