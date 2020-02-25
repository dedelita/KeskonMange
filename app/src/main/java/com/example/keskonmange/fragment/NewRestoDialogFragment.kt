package com.example.keskonmange.fragment

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment

import com.example.keskonmange.R
import com.example.keskonmange.main.MyDbHelper
import com.example.keskonmange.main.Resto
import kotlinx.android.synthetic.main.fragment_new_resto.*
import kotlinx.android.synthetic.main.fragment_new_resto.view.*


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [NewRestoDialogFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [NewRestoDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewRestoDialogFragment : DialogFragment() {
    private var dbHelper: MyDbHelper? = null
    private var listener: OnFragmentInteractionListener? = null
    private var ou: String = ""

    fun setOu(value: String) {
        ou = value
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dbHelper =
            MyDbHelper(activity!!.baseContext, null)
        val view = inflater.inflate(R.layout.fragment_new_resto, container, false)
        val list_ou = arrayListOf("Bar", "Maison")
        val list_type = arrayListOf("Resto", "Livraison", "A emporter")

        view.spinner_ou.adapter = ArrayAdapter(activity!!.baseContext, R.layout.spinner_item, list_ou)
        view.spinner_type.adapter = ArrayAdapter(activity!!.baseContext, R.layout.spinner_item, list_type)


        if(ou.equals("Bar"))
            view.spinner_ou.setSelection(0)
        else
            view.spinner_ou.setSelection(1)

        view.spinner_ou.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(spinner_ou.selectedItem.equals("Maison"))
                    list_type.add("Plat")
                else
                    list_type.remove("Plat")
            }

        }
        view.buttonAjout.setOnClickListener {
            val resto = Resto()
            resto.name = resto_name.text.trim().toString()
            resto.type = spinner_type.selectedItem.toString()
            val success = if(spinner_ou.selectedItem == "Bar") {
                dbHelper!!.addRestoBar(resto)
            } else
                dbHelper!!.addRestoMaison(resto)


            if(success) {
                targetFragment!!.onActivityResult(targetRequestCode, Activity.RESULT_OK, null)
                dismiss()
            }
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    interface DialogListener {
        fun onFinishEditDialog(inputText:String)
    }

}
