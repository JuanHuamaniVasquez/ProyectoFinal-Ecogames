package com.example.semaforo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.appcompat.app.AlertDialog

/*
 Fragmento del menú principal de la app.
 Desde aquí el usuario puede:
 - Iniciar el minijuego de memoria (EcoMemory).
 - Iniciar el minijuego Semáforo de Tachos.
 - Ver las reglas generales de los minijuegos.
 - Salir de la aplicación.
 */
class MenuFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Referencias a los botones del menú
        val btnPlay: Button = view.findViewById(R.id.btnPlay)     // EcoMemory
        val btnPlay2: Button = view.findViewById(R.id.btnPlay2)   // Semáforo de Tachos
        val btnPlay3: Button = view.findViewById(R.id.btnPlay3)   // Cinta Eco
        val btnRules: Button = view.findViewById(R.id.btnRules)   // Ver reglas de los minijuegos
        val btnExit: Button = view.findViewById(R.id.btnExit)     // Salir de la app
        btnPlay2.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_gameFragment)
        }

        btnPlay.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_memoryGameFragment)
        }
        btnPlay3.setOnClickListener {
            findNavController().navigate(R.id.action_menuFragment_to_cintaEcoFragment)
        }

        btnRules.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.rules_title))
                .setMessage(getString(R.string.rules_menu))
                .setPositiveButton(getString(R.string.button_entendido)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        btnExit.setOnClickListener {
            requireActivity().finishAffinity()
            // Posible mejora:
            // - Mostrar un diálogo de confirmación antes de salir:
            //   "¿Seguro que deseas salir de ECOGAMES?"
        }
    }
}