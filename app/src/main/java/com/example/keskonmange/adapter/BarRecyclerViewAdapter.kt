package com.example.keskonmange.adapter

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.keskonmange.R
import com.example.keskonmange.fragment.BarFragment
import com.example.keskonmange.main.MyDbHelper
import com.example.keskonmange.main.Resto
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_resto.view.*
import kotlinx.android.synthetic.main.fragment_resto_list.view.*

/**
 * [RecyclerView.Adapter] that can display a [Resto] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class BarRecyclerViewAdapter(
    private var restos: ArrayList<Resto>, private var sort: String, private val context: Context,
    private val mListener: BarFragment.OnListFragmentInteractionListener?
) : RecyclerView.Adapter<BarRecyclerViewAdapter.RestoViewHolder>() {

    private var dbHelper: MyDbHelper? = null
    private val mOnClickListener: View.OnClickListener
    private var rv: RecyclerView = RecyclerView(context)

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Resto
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
        dbHelper = MyDbHelper(context, null)
    }

    fun setSort(sort: String) {
        this.sort = sort
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_resto, parent, false)
        rv = parent.rv_resto
        return RestoViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: RestoViewHolder, position: Int) {
        val resto = restos[position]
        holder.bind(resto)
        with(holder.mView) {
            tag = resto
            setOnClickListener(mOnClickListener)
        }

        holder.restoDelete.setOnClickListener {
            restos.removeAt(position)
           this.notifyItemRemoved(position)
            showUndoSnackbar(rv, resto, position)
        }

        holder.npMoins.setOnClickListener {
            var nb = resto.votes!!
            if (nb > 0) {
                nb -= 1
                dbHelper!!.updateVotesRestoBar(resto.name!!, resto.type!!, nb)
                holder.restoVotes.text = Editable.Factory.getInstance().newEditable(nb.toString())
                resto.votes = nb
                if (sort == "votes") {
                    restos = ArrayList(restos.sortedWith(compareByDescending { it.votes }))
                    this.notifyDataSetChanged()
                }
            }
        }

        holder.npPlus.setOnClickListener {
            var nb = Integer.parseInt(holder.restoVotes.text.toString())
            nb += 1
            dbHelper!!.updateVotesRestoBar(resto.name!!, resto.type!!, nb)
            holder.restoVotes.text = Editable.Factory.getInstance().newEditable(nb.toString())
            resto.votes = nb
            if (sort == "votes") {
                restos = ArrayList(restos.sortedWith(compareByDescending { it.votes }))
                this.notifyDataSetChanged()
            }
        }

        holder.restoVetto.setOnClickListener {
            if (!holder.vetto!!) {
                dbHelper!!.updateVotesRestoBar(resto.name!!, resto.type!!, -1)
                dbHelper!!.updateVettoRestoBar(resto.name!!, resto.type!!, true)
                resto.vetto = true
                holder.initVetto()
            } else {
                if (resto.votes == -1) {
                    holder.restoVotes.text = Editable.Factory.getInstance().newEditable("0")
                    resto.votes = 0
                    dbHelper!!.updateVotesRestoBar(resto.name!!, resto.type!!, 0)
                } else {
                    dbHelper!!.updateVotesRestoBar(resto.name!!, resto.type!!, resto.votes!!)
                }
                dbHelper!!.updateVettoRestoBar(resto.name!!, resto.type!!, false)
                resto.vetto = false
                holder.removeVetto()
            }
        }
    }

    private fun moveItem(from: Int, to: Int) {
        val fromResto = restos[from]
        restos.removeAt(from)
        if (to < from) {
            restos.add(to, fromResto)
        } else {
            val to1 = to - 1
            restos.add(to1, fromResto)
        }
    }

    fun reorderItem(from: Int, to: Int) {
        moveItem(from, to)
        var pos: Int
        for (i in 0 until restos.size) {
            pos = i + 1
            restos[i].order = pos
            dbHelper!!.updateOrderRestoBar(restos[i].name!!, restos[i].type!!, pos)
        }
    }

    private fun showUndoSnackbar(rv: RecyclerView, resto: Resto, position: Int) {
        val snackbarUndo = Snackbar.make(rv, "${resto.name} supprimé", Snackbar.LENGTH_LONG)

        snackbarUndo.setActionTextColor(Color.parseColor("#FFFFFF"))
        snackbarUndo.setAction("Annuler") { undoDelete(position, resto) }
        snackbarUndo.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                    dbHelper!!.deleteRestoBar(resto.name!!, resto.type!!)
                }
                super.onDismissed(transientBottomBar, event)
            }
        })
        snackbarUndo.show()
    }

    private fun undoDelete(deletedPosition: Int, deletedResto: Resto) {
        restos.add(deletedPosition, deletedResto)
        notifyItemInserted(deletedPosition)
    }


    override fun getItemCount(): Int = restos.size

    inner class RestoViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val restoName: TextView = mView.resto_name
        val restoType: TextView = mView.resto_type
        val restoVotes: EditText = mView.np_nb
        val npMoins: ImageButton = mView.np_moins
        val npPlus: ImageButton = mView.np_plus
        val restoVetto: ImageButton = mView.resto_non
        val restoDelete: ImageButton = mView.resto_delete
        var vetto: Boolean? = false

        fun initVetto() {
            vetto = true
            npPlus.visibility = View.INVISIBLE
            npMoins.visibility = View.INVISIBLE
            restoVotes.visibility = View.INVISIBLE
            restoVetto.setImageResource(R.drawable.ic_not_interested_red_24dp)
        }

        fun removeVetto() {
            vetto = false
            npPlus.visibility = View.VISIBLE
            npMoins.visibility = View.VISIBLE
            restoVotes.visibility = View.VISIBLE
            restoVetto.setImageResource(R.drawable.ic_not_interested_black_24dp)
        }

        fun bind(resto: Resto) {
            restoName.text = resto.name
            restoType.text = resto.type
            restoVotes.text = Editable.Factory.getInstance().newEditable(resto.votes.toString())
            vetto = resto.vetto
            if (vetto!!)
                initVetto()
            else
                removeVetto()
        }

        override fun toString(): String {
            return super.toString() + " '" + restoName.text + "' '" + restoType.text + "' '" +
            restoVotes.text + "' '" + vetto + "'"
        }
    }
}
