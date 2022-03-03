package io.ulysses.android.actividad_final

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import io.ulysses.android.actividad_final.databinding.ActivityEditToDoItemBinding

class EditToDoItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditToDoItemBinding
    private lateinit var dataBase: DataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Recoge la lista de iconos
        val namesArray: Array<String> = resources.getStringArray(R.array.array_emojis)

        // Establece la selección en ninguna (que es -1 o menos)
        var selectionIcon = -1

        binding = ActivityEditToDoItemBinding.inflate(layoutInflater)
        dataBase = DataBase(this, null)
        setContentView(binding.root)

        // Guardamos los datos del intent
        var recordatorio = RecordatorioData (
            intent.getIntExtra("id", -1),
            intent.getStringExtra("titulo",)!!,
            intent.getStringExtra("descripcion")!!,
            intent.getBooleanExtra("importante", false),
            intent.getBooleanExtra("terminado", false),
            intent.getIntExtra("icono", -1)
        )

        // Ponemos los datos guardados
        binding.editTextTextPersonName.setText(recordatorio.titulo)
        binding.editTextTextMultiLine.setText(recordatorio.descripcion)
        cambiarIcono(this, recordatorio.foto)
        selectionIcon = recordatorio.foto

        // Cancelar el editar
        binding.button3.setOnClickListener {
            this.finish()
        }

        // Guarda los datos si el campo titulo no está vacío
        binding.button.setOnClickListener {
            if (!binding.editTextTextPersonName.text.isBlank()) {
                dataBase.acutalizarRecordatorio(
                    recordatorio.id,
                    binding.editTextTextPersonName.text.toString(),
                    binding.editTextTextMultiLine.text.toString(),
                    recordatorio.importante,
                    recordatorio.terminado,
                    selectionIcon)
                this.finish()
            } else {
                Snackbar.make(binding.root, getString(R.string.titulo_vacio), Snackbar.LENGTH_SHORT).show()
            }
        }

        // Selecciona el icono/foto para el recordatorio (AlertDialog)
        binding.button2.setOnClickListener {
            val builder = AlertDialog.Builder(this)

            builder.apply {
                setTitle("Icono")
                setSingleChoiceItems(namesArray, -1) { _, which ->}

                setPositiveButton(android.R.string.ok) { dialog, _ ->
                    val selectedPosition = (dialog as AlertDialog).listView.checkedItemPosition
                    cambiarIcono(context,selectedPosition - 1)
                    selectionIcon = selectedPosition - 1
                }

                setNegativeButton(android.R.string.cancel) { _, _ ->}
            }
            builder.show()
        }
    }

    /**
     * Cambiar el icono dependiendo de seleccionado
     * */
    fun cambiarIcono(context: Context, seleccion: Int) {
        binding.imageView.setImageResource(
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
    }
}