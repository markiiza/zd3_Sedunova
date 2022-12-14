package com.example.criminalintent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"
class CrimeListFragment: Fragment() {
    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null
    private val crimeListViewModel:CrimeListViewModel by lazy{
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG,"Total crimes: ${crimeListViewModel.crimes.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list,container,false)
        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()
        return view
    }

    private fun updateUI(){
        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter=adapter
    }

    private open inner class CrimeHolder(view: View):RecyclerView.ViewHolder(view),View.OnClickListener{
        val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        init{
            itemView.setOnClickListener(this)
        }
        private lateinit var crime:Crime
        fun bind (crime:Crime)
        {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View?) {
            Toast.makeText(context,"${crime.title} pressed!",Toast.LENGTH_SHORT).show()
        }
    }

    private inner class CrimePoliceHolder(view : View) : CrimeHolder(view){
        val police:Button = itemView.findViewById(R.id.police_button)
        init {
            police.setOnClickListener{view:View ->
                Toast.makeText(context,"I call the police!!",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private inner class CrimeAdapter(var crimes:List<Crime>):RecyclerView.Adapter<CrimeHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = when (viewType){
                0 -> layoutInflater.inflate(R.layout.list_item_crime,parent,false)
                1 -> layoutInflater.inflate(R.layout.list_item_crime_police,parent,false)
                else ->layoutInflater.inflate(R.layout.list_item_crime,parent,false)
            }
            if (viewType == 0){
                return CrimeHolder(view)
            }
            return CrimePoliceHolder(view)

        }

        override fun getItemCount() = crimes.size
        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemViewType(position: Int): Int {
            if (crimes[position].requiresPolice()) {
                return 1;
            }
            return 0
        }

    }


    companion object{
        fun newInstance():CrimeListFragment{
            return CrimeListFragment()
        }
    }
}