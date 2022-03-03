package io.ulysses.android.actividad_final

import android.database.Cursor
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import io.ulysses.android.actividad_final.databinding.FragmentMainToDoListBinding
import android.content.Intent
import android.net.Uri
import android.widget.Toast


class MainToDoListFragment : Fragment() {

    private var importante: Boolean = false
    private var titulo: String = ""

    /**
     * Recoge los datos
     * */
    companion object {
        @JvmStatic
        fun newInstance(importante: Boolean, titulo: String) =
            MainToDoListFragment().apply {
                arguments = Bundle().apply {
                    putString("titulo", titulo)
                    putBoolean("importante", importante)
                }
            }
    }

    /* Fragment */

    private lateinit var binding: FragmentMainToDoListBinding
    private lateinit var dataBase: DataBase

    private lateinit var recyclerAdapter: RecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Guarda los datos recogidos
        arguments?.let {
            titulo = if ( it.getString("titulo") != null ) it.getString("titulo")!! else ""
            importante = it.getBoolean("importante")
        }
    }

    /**
     * Crea la vista
     * */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainToDoListBinding.inflate(layoutInflater)
        dataBase = DataBase(requireContext(), null)
        return binding.root
    }

    /**
     * Asigna el título
     * */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textView2.text = titulo
    }

    /**
     * Carga los recycler views con los datos obtenidos de la base de datos o muestra un mensaje si está vacío
     */
    override fun onResume() {
        super.onResume()
        RecyclerAdapter.actionMode?.finish()

        // Mensaje de no hay nada
        binding.textView4.setOnClickListener(null)
        when {
            (1..5).random() == 5 -> {
                val countDownTimer: CountDownTimer = object : CountDownTimer(1500, 100) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.textView4.text = if (binding.textView4.text == getString(R.string.feliz_kamoji_0)) getString(R.string.feliz_kamoji_1) else getString(R.string.feliz_kamoji_0)
                    }
                    override fun onFinish() {
                        binding.textView4.text = "${getString(R.string.nada_aun)} ${getString(R.string.feliz_kamoji_0)}"
                        binding.textView4.setOnClickListener {
                            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://youtu.be/eXBqf4Gfuo0"))
                            startActivity(browserIntent)
                        }
                    }
                }
                countDownTimer.start()

            }
            (1..5).random() == 5 -> {
                val countDownTimer: CountDownTimer = object : CountDownTimer(1000, 500) {
                    override fun onTick(millisUntilFinished: Long) {

                        binding.textView4.text = if (binding.textView4.text == getString(R.string.gafas_kamoji_0)) getString(R.string.gafas_kamoji_1) else getString(R.string.gafas_kamoji_0)
                    }
                    override fun onFinish() {
                        binding.textView4.text = "${getString(R.string.nada_aun)} ${getString(R.string.gafas_kamoji_1)}"
                    }
                }
                countDownTimer.start()

            }
            else -> {
                binding.textView4.text = getString(R.string.nada_aun)

            }
        }


        // Carga los recycler views con los datos obtenidos
        iniciarBibliotecaRecyclerView()
    }

    override fun onPause() {
        super.onPause()

        binding.ulyRecordatorios.visibility = View.GONE
        binding.textView4.visibility = View.GONE
        binding.progressBar2.visibility = View.VISIBLE
        binding.ulyRecordatorios.adapter = null
    }


    /**
     * Carga los recycler views con los datos obtenidos
     * */
    private fun iniciarBibliotecaRecyclerView() {
        recyclerAdapter = RecyclerAdapter(this.requireView())

        val cursor: Cursor = dataBase.obtenerRecordatorios(importante)
        binding.ulyRecordatorios.visibility = View.GONE
        binding.textView4.visibility = View.GONE
        binding.progressBar2.visibility = View.VISIBLE

        if (cursor.count > 0) {
            binding.ulyRecordatorios.setHasFixedSize(true)
            binding.ulyRecordatorios.layoutManager = LinearLayoutManager(requireContext())
            recyclerAdapter.RecyclerAdapter(cursor, requireContext(), importante)
            binding.ulyRecordatorios.adapter = recyclerAdapter

            binding.ulyRecordatorios.visibility = View.VISIBLE
        } else {
            binding.textView4.visibility = View.VISIBLE
        }

        binding.progressBar2.visibility = View.GONE
    }
}