package com.paweloot.criminalintent

import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "CrimeListFragment"

class CrimeListFragment : Fragment() {

    /**
     *   Required interface for hosting activities
     */
    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var crimeRecyclerView: RecyclerView

    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view)

        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        crimeRecyclerView.adapter = CrimeAdapter(emptyList())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
                crimes?.let {
                    updateUI(crimes)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    fun updateUI(crimes: List<Crime>) {
        crimeRecyclerView.adapter = CrimeAdapter(crimes)
    }

    private inner class CrimeViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var crime: Crime

        private val crimeTitle: TextView = view.findViewById(R.id.crime_title)
        private val crimeDate: TextView = view.findViewById(R.id.crime_date)
        private val crimeSolved: ImageView = view.findViewById(R.id.crime_solved)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            crimeTitle.text = this.crime.title
            crimeDate.text = DateFormat.getLongDateFormat(context).format(this.crime.date)

            crimeSolved.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
        }

        override fun onClick(view: View?) {
            callbacks?.onCrimeSelected(crime.id)
        }
    }

    private inner class CrimeAdapter(var crimes: List<Crime>) :
        RecyclerView.Adapter<CrimeViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeViewHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)

            return CrimeViewHolder(view)
        }

        override fun getItemCount(): Int = crimes.size

        override fun onBindViewHolder(holder: CrimeViewHolder, position: Int) {
            holder.bind(crimes[position])
        }

    }


    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
}