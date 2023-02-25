package com.crudgroup.f9mobile.presentation.fragments.roleFragments

import android.annotation.SuppressLint
import android.content.Context.WINDOW_SERVICE
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.*
import android.view.View.OnTouchListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.crudgroup.f9mobile.R
import com.crudgroup.f9mobile.databinding.FragmentWorkerBinding
import com.crudgroup.f9mobile.presentation.otherComponents.AllAnimations
import com.crudgroup.f9mobile.presentation.otherComponents.dialog.TimberCalculatorDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Runnable


class WorkerFragment : Fragment() {

    private lateinit var binding: FragmentWorkerBinding
    private lateinit var allAnimations: AllAnimations
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var calculatorDialog: TimberCalculatorDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        allAnimations = AllAnimations(requireContext())
        calculatorDialog = TimberCalculatorDialog(requireContext())

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWorkerBinding.inflate(inflater, container, false)
        navHostFragment = childFragmentManager.findFragmentById(R.id.worker_container) as NavHostFragment
        NavigationUI.setupWithNavController(binding.workerBottomNav, navHostFragment.navController)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility", "RtlHardcoded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        allAnimations.bottomAnimation(binding.workerBottomNav)

        binding.fabCalculatorBtn.setOnClickListener {
            calculatorDialog.showDialog()
        }


    }
}