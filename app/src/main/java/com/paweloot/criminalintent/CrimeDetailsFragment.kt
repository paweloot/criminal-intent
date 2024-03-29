package com.paweloot.criminalintent

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

private const val ARG_CRIME_ID = "crime_id"
private const val DIALOG_DATE = "DialogDate"
private const val DIALOG_TIME = "DialogTime"
private const val REQUEST_DATE = 0
private const val REQUEST_TIME = 1

class CrimeDetailsFragment : Fragment(), DatePickerFragment.Callbacks,
    TimePickerFragment.Callbacks {

    private lateinit var crime: Crime

    private lateinit var titleField: EditText
    private lateinit var dateButton: Button
    private lateinit var timeButton: Button
    private lateinit var solvedCheckBox: CheckBox

    private val crimeDetailsViewModel: CrimeDetailsViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeDetailsViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        crime = Crime()

        val crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailsViewModel.loadCrime(crimeId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_details, container, false)

        titleField = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        timeButton = view.findViewById(R.id.crime_time) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox

        dateButton.text = DateFormat.getDateInstance(DateFormat.FULL).format(crime.date)
        timeButton.text = DateFormat.getTimeInstance(DateFormat.FULL).format(crime.date)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeDetailsViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crime = crime
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                crime.title = sequence.toString()
            }
        }

        titleField.addTextChangedListener(titleWatcher)

        solvedCheckBox.setOnCheckedChangeListener { _, isChecked ->
            crime.isSolved = isChecked
        }

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeDetailsFragment, REQUEST_DATE)
                show(this@CrimeDetailsFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        timeButton.setOnClickListener {
            TimePickerFragment.newInstance(crime.date).apply {
                setTargetFragment(this@CrimeDetailsFragment, REQUEST_TIME)
                show(this@CrimeDetailsFragment.requireFragmentManager(), DIALOG_TIME)
            }
        }
    }

    override fun onStop() {
        super.onStop()

        crimeDetailsViewModel.saveCrime(crime)
    }

    override fun onDateSelected(date: Date) {
        crime.date = date
        updateUI()
    }

    override fun onTimeSelected(hour: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.time = crime.date

        calendar.set(Calendar.HOUR, hour)
        calendar.set(Calendar.MINUTE, minute)
        crime.date = calendar.time
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(crime.title)
        dateButton.text = DateFormat.getDateInstance(DateFormat.FULL).format(crime.date)
        timeButton.text = DateFormat.getTimeInstance(DateFormat.SHORT).format(crime.date)

        solvedCheckBox.apply {
            isChecked = crime.isSolved
            jumpDrawablesToCurrentState()
        }
    }

    companion object {
        fun newInstance(crimeId: UUID): CrimeDetailsFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }

            return CrimeDetailsFragment().apply {
                arguments = args
            }
        }
    }
}