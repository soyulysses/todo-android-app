package io.ulysses.android.actividad_final

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.ulysses.android.actividad_final.databinding.ActivityReadToDoBinding

class ReadToDoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReadToDoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadToDoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textView7.text = intent.getStringExtra("titulo")
        binding.textView8.text = intent.getStringExtra("descripcion")
        cambiarIcono(this, intent.getIntExtra("icono", -1))
    }

    /**
     * Cambiar el icono dependiendo de seleccionado
     * */
    fun cambiarIcono(context: Context, seleccion: Int) {
        binding.imageView4.setImageResource(
            when (seleccion) {
                0 -> context.resources.getIdentifier("bullseye", "drawable", context.packageName)
                1 -> context.resources.getIdentifier("martial_arts_uniform", "drawable", context.packageName)
                2 -> context.resources.getIdentifier("party_popper", "drawable", context.packageName)
                3 -> context.resources.getIdentifier("pinyata", "drawable", context.packageName)
                4 -> context.resources.getIdentifier("sparkles", "drawable", context.packageName)
                5 -> context.resources.getIdentifier("sports_medal", "drawable", context.packageName)
                6 -> context.resources.getIdentifier("teddy_bear", "drawable", context.packageName)
                7 -> context.resources.getIdentifier("ticket", "drawable", context.packageName)
                8 -> context.resources.getIdentifier("video_game", "drawable", context.packageName)
                9 -> context.resources.getIdentifier("face_in_clouds", "drawable", context.packageName)
                10 -> context.resources.getIdentifier("face_with_medical_mask", "drawable", context.packageName)
                11 -> context.resources.getIdentifier("face_with_tears_of_joy", "drawable", context.packageName)
                12 -> context.resources.getIdentifier("loudly_crying_face", "drawable", context.packageName)
                13 -> context.resources.getIdentifier("sleeping_face", "drawable", context.packageName)
                14 -> context.resources.getIdentifier("smiling_face_with_sunglasses", "drawable", context.packageName)
                else -> context.resources.getIdentifier("ic_launcher_round", "mipmap", context.packageName)
            }
        )

        if  (seleccion < 0) {
            binding.imageView4.visibility = View.GONE
        }
    }
}