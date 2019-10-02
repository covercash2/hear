package dev.covercash.aaudiotests.view.unit_slider

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import dev.covercash.aaudiotests.R
import kotlinx.android.synthetic.main.unit_dialog.*

class UnitDialog(private val label: String, val onUnitSaved: (DialogFragment, String) -> Unit) : DialogFragment() {
    private val TAG = this.javaClass.simpleName

    private fun setupViews() {
        unit_dialog_label!!.text = label
        cancel_button!!.setOnClickListener {
            dialog!!.dismiss()
        }
        ok_button!!.setOnClickListener {
            onUnitSaved(this, unit_field!!.text.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.unit_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupViews()
    }
}