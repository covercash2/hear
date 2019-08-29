package dev.covercash.hear

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.covercash.hear.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    companion object {
        init {
            System.loadLibrary("hear")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }
    }

}
