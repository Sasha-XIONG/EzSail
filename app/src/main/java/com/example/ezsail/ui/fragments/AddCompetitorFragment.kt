package com.example.ezsail.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.ezsail.R
import com.example.ezsail.databinding.FragmentAddCompetitiorBinding
import com.example.ezsail.db.entities.Boat
import com.example.ezsail.db.entities.OverallResult
import com.example.ezsail.db.entities.RaceResult
import com.example.ezsail.db.entities.Series
import com.example.ezsail.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddCompetitorFragment: Fragment(R.layout.fragment_add_competitior) {
    // Set up view binding
    private var addCompetitiorBinding: FragmentAddCompetitiorBinding? = null
    private val binding get() = addCompetitiorBinding!!

    private val viewModel: MainViewModel by activityViewModels()

    lateinit var boatClass: String
    lateinit var sailNo: String
    lateinit var helm: String
    lateinit var crew: String
    lateinit var club: String
    lateinit var fleet: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addCompetitiorBinding = FragmentAddCompetitiorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentSeries = viewModel.currentSeries

        // Set onClickListener for save button
        binding.saveBtn.setOnClickListener {
            getDataFromEditInfoFragment()
            // Save boat to DB
            val boat = Boat(sailNo, boatClass, helm, crew, club, fleet)
            viewModel.upsertBoat(boat)

            // Save overall result to DB
            val overallResult = OverallResult(sailNo, currentSeries.id)
            viewModel.upsertOverallResult(overallResult)

            // Save updated current series to DB
            currentSeries.participants += 1
            viewModel.upsertSeries(currentSeries)

            findNavController().popBackStack()
        }

        // Set onClickListener for cancel button
        binding.cancelBtn.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        addCompetitiorBinding = null
    }

    private fun getDataFromEditInfoFragment() {
        val child = this.childFragmentManager.findFragmentById(R.id.inputFragment)
        val view = child?.view
        boatClass = view?.findViewById<EditText>(R.id.boatClass)?.text.toString()
        sailNo = view?.findViewById<EditText>(R.id.sailNo)?.text.toString()
        helm = view?.findViewById<EditText>(R.id.helm)?.text.toString()
        crew = view?.findViewById<EditText>(R.id.crew)?.text.toString()
        club = view?.findViewById<EditText>(R.id.club)?.text.toString()
        fleet = view?.findViewById<EditText>(R.id.fleet)?.text.toString()
    }
}