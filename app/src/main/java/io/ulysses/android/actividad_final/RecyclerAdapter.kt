package io.ulysses.android.actividad_final

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.media.MediaPlayer
import android.opengl.Visibility
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class RecyclerAdapter(private val parent: View) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>(){

    /**
     * Para saber si estás en la pestaña importantes
     * */
    private var _importante: Boolean = false


    private lateinit var cursor: Cursor
    private lateinit var context: Context
    private lateinit var dataBase: DataBase

    companion object {
        var actionMode: ActionMode? = null
        private var actionRecodatorioid = -1

        /**
         * Cambia el subtitulo de el action mode
         * */
        private fun cambiarSubtitulo(recordatorio: RecordatorioData, actionMode: ActionMode?, importante: Boolean, context: Context) {
            if (actionRecodatorioid == recordatorio.id) {
                if (importante) {
                    actionMode?.finish()
                } else {
                    actionMode?.subtitle = if (recordatorio.importante) context.getString(R.string.importante) else context.getString(R.string.recordatorio)
                }
            }
        }
    }

    /**
     * Guarda todos los argumentos a las variables
     * */
    fun RecyclerAdapter(cursor: Cursor, context: Context, mImportante: Boolean) {
        this.cursor = cursor
        this.context = context
        this._importante = mImportante
        this.dataBase = DataBase(context, null)
    }


    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view:View = LayoutInflater.from(viewGroup.context).inflate(R.layout.recordatorio_item, viewGroup, false)
        return ViewHolder(view)
    }

    /**
     * Leo el cursor, guardo los datos y se los paso al viewHolder
     * */
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)

        var recordatorio = RecordatorioData(
            cursor.getInt(0),
            cursor.getString(1),
            cursor.getString(2),
            cursor.getInt(3) > 0,
            cursor.getInt(4) > 0,
            cursor.getInt(5)
        )

        viewHolder.bind(recordatorio, context)
    }

    override fun getItemCount(): Int {
        return if (cursor != null) cursor.count else 0
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        val cardView: CardView
        val textView: TextView
        val imageButton2: ImageButton
        val imageButton: ImageButton
        val imageView: ImageView

        private lateinit var recordatorio: RecordatorioData

        /**
         * El constructor donde guardo todos los componentes de la interfaz de usuario utilizando el
         * findViewById en vez de usar el binding porque me confundí mucho al principio y me da
         * pereza cambiarlo ahora.
         * */
        constructor(itemView: View) : super(itemView) {
            cardView = itemView.findViewById(R.id.ulyCard)
            textView = itemView.findViewById(R.id.textView)
            imageButton2 = itemView.findViewById(R.id.imageButton2)
            imageButton = itemView.findViewById(R.id.imageButton)
            imageView = itemView.findViewById(R.id.imageView2)
        }

        /**
         * Cambiar el icono dependiendo de seleccionado
         * */
        fun cambiarIcono(context: Context,seleccion: Int) {
            if (seleccion > -1) {
                imageView.setImageResource(
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
            } else {
                imageView.visibility = View.GONE
            }
        }

        /**
         * Cambia todos los textos e imágenes usando los datos de [recordatorioData] y los listeners
         * */
        fun bind(recordatorioData: RecordatorioData, context: Context) {
            this.recordatorio = recordatorioData

            /* RECORDATORIO - TÍTULO */
            textView.text = recordatorio.titulo

            /* RECORDATORIO - ICONO / FOTO */
            cambiarIcono(context, recordatorio.foto)


            /* RECORDATORIO - IMPORTANTE */
            if (recordatorio.importante) {
                imageButton.setImageResource(context.resources.getIdentifier("round_star_black_36", "drawable", context.packageName))
            }

            imageButton.setOnClickListener {
                if (recordatorio.importante) {
                    imageButton.setImageResource(context.resources.getIdentifier("round_star_border_black_36", "drawable", context.packageName))
                } else {
                    imageButton.setImageResource(context.resources.getIdentifier("round_star_black_36", "drawable", context.packageName))
                    //MediaPlayer.create(context,R.raw.ping).start()
                }

                recordatorio.importante = !recordatorio.importante
                dataBase.importanteRecordatorio(recordatorio.id, recordatorio.importante)
                cambiarSubtitulo(recordatorio, actionMode, _importante, context)

                cursor.close()
                cursor = dataBase.obtenerRecordatorios(_importante)

                if (_importante) {
                    notifyItemRemoved(adapterPosition)
                    notifyItemRangeChanged(adapterPosition, cursor.count)
                }
            }

            /* RECORDATORIO - TERMINAR */
            if (recordatorio.terminado) {
                imageButton2.setImageResource(context.resources.getIdentifier("round_task_alt_black_36", "drawable", context.packageName))
            }

            imageButton2.setOnClickListener {
                if (recordatorio.terminado) {
                    imageButton2.setImageResource(context.resources.getIdentifier("round_radio_button_unchecked_black_36", "drawable", context.packageName))
                } else {
                    imageButton2.setImageResource(context.resources.getIdentifier("round_task_alt_black_36", "drawable", context.packageName))
                    MediaPlayer.create(context,R.raw.ping).start()
                }

                recordatorio.terminado = !recordatorio.terminado
                dataBase.terminarRecordatorio(recordatorio.id, recordatorio.terminado)
                cursor.close()
                cursor = dataBase.obtenerRecordatorios(_importante)
            }

            /* VER RECORDATORIO (INTENT) */
            itemView.setOnClickListener {
                context.startActivity(Intent(context, ReadToDoActivity::class.java).apply {
                    putExtra("titulo", recordatorio.titulo)
                    putExtra("descripcion", recordatorio.descripcion)
                    putExtra("icono", recordatorio.foto)
                })
            }

            /* ACTIONMODE ( ELIMINAR / MODIFICAR ) */
            itemView.setOnLongClickListener {
                when (actionMode) {
                    null -> {
                        actionMode = it.startActionMode(actionModeCallback)
                        it.isSelected = true
                        true
                    }
                    else -> false
                }
                true
            }
        }

        private val actionModeCallback = object : ActionMode.Callback {

            /**
             * Ejecuta acción dependiendo de lo seleccionado
             * */
            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                return when (item!!.itemId) {

                    /* ELIMINAR */
                    R.id.delete1 -> {
                        var retorno = true

                        val builder = AlertDialog.Builder(context)
                        builder.apply {
                            setTitle(context.getString(R.string.eliminar))
                            setMessage(context.getString(R.string.eliminar_pregunta))
                            setPositiveButton(android.R.string.ok)  { _, _ ->
                                dataBase.eliminarRecordatorio(recordatorio.id)
                                cursor.close()
                                cursor = dataBase.obtenerRecordatorios(_importante)
                                notifyItemRemoved(adapterPosition)
                                notifyItemRangeChanged(adapterPosition, cursor.count + 1)

                                Toast.makeText(context, context.getString(R.string.recordatorio_eliminado), Toast.LENGTH_SHORT).show()

                                if (itemCount <= 0) parent.findViewById<TextView>(R.id.textView4).visibility = View.VISIBLE
                                mode!!.finish()
                            }
                            setNegativeButton(android.R.string.cancel) { _, _ ->
                                retorno = false
                            }
                        }
                        builder.show()
                        return retorno
                    }

                    /* MODIFICAR (INTENT) */
                    R.id.tag2 -> {
                        context.startActivity(Intent(context, EditToDoItemActivity::class.java).apply {
                            putExtra("id", recordatorio.id)
                            putExtra("titulo", recordatorio.titulo)
                            putExtra("descripcion", recordatorio.descripcion)
                            putExtra("importante", recordatorio.importante)
                            putExtra("terminado", recordatorio.terminado)
                            putExtra("icono", recordatorio.foto)
                        })
                        return true
                    }
                    else -> false
                }
            }

            /**
             * Crea el action mode
             * */
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                val inflater = mode?.menuInflater
                inflater?.inflate(R.menu.menu_recordatorio_item, menu)
                mode?.title = recordatorio.titulo
                mode?.subtitle = if (recordatorio.importante) context.getString(R.string.importante) else context.getString(R.string.recordatorio)
                actionRecodatorioid = recordatorio.id
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                actionRecodatorioid = recordatorio.id
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode?) {
                actionMode = null
            }
        }
    }
}