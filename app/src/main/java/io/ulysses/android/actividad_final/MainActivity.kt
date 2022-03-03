package io.ulysses.android.actividad_final

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.tabs.TabLayoutMediator
import io.ulysses.android.actividad_final.databinding.ActivityMainBinding

//Main Activity
class MainActivity : AppCompatActivity() {

    /**
     * binding
     * */
    private lateinit var binding: ActivityMainBinding

    /**
     * Guardo la posición de la ultima tab para restaurarla al volver en el [onResume]
     * */
    companion object {
        var tab_position = 0
    }

    /**
     * Asigno el [binding], creo los fragments y añado los listeners
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Fragments
        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)

        adapter.addFragment(MainToDoListFragment.newInstance(false, getString(R.string.recordatorios)), getString(R.string.recordatorios))
        adapter.addFragment(MainToDoListFragment.newInstance(true, getString(R.string.importantes)), getString(R.string.importantes))
        binding.ulyViewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.ulyViewPager) {tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()

        // Listeners
        binding.floatingActionButton2.setOnClickListener {
            this.startActivity(Intent(this, AddToDoItemActivity::class.java).apply {
                if (binding.tabLayout.selectedTabPosition == 1) {
                    putExtra("importante", true)
                }
            })
        }
    }

    /**
     * Reasigno al ViewPager2 la última posición guardada
     * */
    override fun onResume() {
        super.onResume()
        binding.ulyViewPager.setCurrentItem(tab_position, false)
    }

    /**
     *  Guardo la última pestaña seleccionada en [tab_position]
     */
    override fun onPause() {
        super.onPause()
        tab_position = binding.tabLayout.selectedTabPosition
    }
}