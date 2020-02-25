package com.example.keskonmange.fragment

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.keskonmange.adapter.BarRecyclerViewAdapter
import com.example.keskonmange.R
import com.example.keskonmange.main.MyDbHelper
import com.example.keskonmange.main.Resto
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.fragment_resto_list.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.collections.ArrayList

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the
 * [BarFragment.OnListFragmentInteractionListener] interface.
 */
class BarFragment : Fragment() {
    private var dbHelper: MyDbHelper? = null
    private var listener: OnListFragmentInteractionListener? = null
    private var types = arrayListOf("Resto", "Livraison", "A emporter")
    private var choix = arrayListOf<Resto>()
    private var sort: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_resto_list, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_activity_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_reset -> reset_votes()
            R.id.sort_asc -> rv_resto.adapter =
                BarRecyclerViewAdapter(getRestos("asc"), "asc", activity!!, listener)
            R.id.sort_desc -> rv_resto.adapter =
                BarRecyclerViewAdapter(getRestos("desc"), "desc", activity!!, listener)
            R.id.sort_type -> rv_resto.adapter =
                BarRecyclerViewAdapter(getRestos("type"), "type", activity!!, listener)
            R.id.sort_votes -> rv_resto.adapter =
                BarRecyclerViewAdapter(getRestos("votes"), "votes", activity!!, listener)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dbHelper = MyDbHelper(this.context!!, null)
        sort = dbHelper!!.getSort()
        rv_resto.adapter = BarRecyclerViewAdapter(getRestos(sort), sort, activity!!, listener)

        val touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback((ItemTouchHelper.UP or ItemTouchHelper.DOWN), 0) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder): Boolean {
                val adapter = recyclerView.adapter as BarRecyclerViewAdapter
                val sourcePos = viewHolder.adapterPosition
                val targetPos = target.adapterPosition

                adapter.reorderItem(sourcePos, targetPos)
                adapter.notifyItemMoved(sourcePos, targetPos)
                sort = "perso"
                adapter.setSort(sort)
                dbHelper!!.updateSort(sort)
                return true
            }
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
        })
        touchHelper.attachToRecyclerView(rv_resto)


        fab_add.setOnClickListener { _ ->
            val dialogFragment = NewRestoDialogFragment()
            dialogFragment.setOu("Bar")
            val ft = activity!!.supportFragmentManager.beginTransaction()

            val prev = activity!!.supportFragmentManager.findFragmentByTag("dialog")
            if (prev != null) {
                ft.remove(prev)
            }
            ft.addToBackStack(null)
            dialogFragment.setTargetFragment(this, 1)
            dialogFragment.show(ft, "dialog")
        }
        fab_resto_choix.setOnClickListener {
            choix = getSeletedResto()
            if(choix.isEmpty()) {
                choix = getRestos("")
                val listChoix = CopyOnWriteArrayList(choix)
                for(c: Resto in listChoix) {
                    if(c.votes == -1)
                        choix.remove(c)
                }
            }
            val resultat = choix[((0 until choix.size).random())].name
            val builder = AlertDialog.Builder(activity!!)
            builder.setTitle("On mange")
            builder.setMessage(resultat)
            builder.setPositiveButton("OK") { _, _ -> reset_votes() }
            builder.setNegativeButton("Encore") { _, _ -> fab_resto_choix.performClick() }
            builder.show()
        }

        for (i in 0 until cg_type.childCount) {
            val chippy = cg_type.getChildAt(i) as Chip
            chippy.setOnClickListener {
                if (chippy.isChecked)
                    types.add(chippy.text.toString())
                else
                    types.remove(chippy.text.toString())

                rv_resto.adapter =
                    BarRecyclerViewAdapter(getRestos(sort), sort, activity!!, listener)
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
            listener = context
        } else throw RuntimeException("$context must implement OnListFragmentInteractionListener")
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == 1) {
            rv_resto.adapter = BarRecyclerViewAdapter(getRestos(sort), sort, activity!!, listener)
        }
    }
    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Resto?)
    }

    private fun reset_votes() {
        val restos = getRestos(sort)
        for(r in restos) {
            dbHelper!!.updateRestoBar(r.name!!, r.type!!, 0)
        }
        rv_resto.adapter = BarRecyclerViewAdapter(getRestos(sort), sort, activity!!, listener)
    }

    private fun getResto(cursor: Cursor): Resto {
        val resto = Resto()
        resto.id = cursor.getInt(cursor.getColumnIndex(MyDbHelper.COLUMN_ID))
        resto.name = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_NAME))
        resto.type = cursor.getString(cursor.getColumnIndex(MyDbHelper.COLUMN_TYPE))
        resto.votes = cursor.getInt(cursor.getColumnIndex(MyDbHelper.COLUMN_VOTES))
        resto.order = cursor.getInt(cursor.getColumnIndex(MyDbHelper.COLUMN_POS))
        resto.vetto = cursor.getInt(cursor.getColumnIndex(MyDbHelper.COLUMN_VETTO)) == 1
        return resto
    }

    private fun getRestos(sort: String): ArrayList<Resto> {
        if(sort != this.sort) {
            this.sort = sort
            dbHelper!!.updateSort(sort)
        }

        val cursor = dbHelper!!.getAllRestoBar()
        var listRestos = arrayListOf<Resto>()
        var resto : Resto

        cursor!!.moveToFirst()
        if (cursor.count > 0) {
            resto = getResto(cursor)
            if (resto.type!! in types)
                listRestos.add(resto)
        }
        while (cursor.moveToNext()) {
            resto = getResto(cursor)
            if (resto.type!! in types)
                listRestos.add(resto)
        }
        cursor.close()

        when(sort) {
            "asc" -> listRestos = ArrayList(listRestos.sortedWith(compareBy { it.name }))
            "desc" -> listRestos = ArrayList(listRestos.sortedWith(compareByDescending {it.name}))
            "type" -> listRestos = ArrayList(listRestos.sortedWith(compareBy ({it.type}, {it.name})))
            "perso" -> listRestos = ArrayList(listRestos.sortedWith(compareBy {it.order}))
            "votes" -> listRestos = ArrayList(listRestos.sortedWith(compareByDescending {it.votes}))
        }

        return listRestos
    }

    private fun getSeletedResto(): ArrayList<Resto> {
        val cursor = dbHelper!!.getSelectedRestoBar()
        val listRestos = arrayListOf<Resto>()
        var resto : Resto

        cursor!!.moveToFirst()
        if (cursor.count > 0) {
            resto = getResto(cursor)
            if(resto.votes!! != -1)
                if (resto.type!! in types)
                    for(i in 0 until resto.votes!!)
                    listRestos.add(resto)
        }
        while (cursor.moveToNext()) {
            resto = getResto(cursor)
            if(resto.votes!! != -1)
                if (resto.type!! in types)
                    for(i in 0 until resto.votes!!)
                        listRestos.add(resto)
        }
        cursor.close()
        return listRestos
    }
}
