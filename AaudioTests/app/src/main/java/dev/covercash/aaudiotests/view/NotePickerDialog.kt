package dev.covercash.aaudiotests.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import dev.covercash.aaudiotests.*
import kotlinx.android.synthetic.main.note_picker_dialog.*

class NotePickerDialog(private val defaultNote: Note, val onSave: (Note) -> Unit) : DialogFragment() {

    private fun setupViews() {
        ok_button!!.setOnClickListener {
            // TODO get value from spinner
            onSave(spinner!!.selectedItem as Note)
            dismiss()
        }
        cancel_button!!.setOnClickListener {
            dismiss()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.note_picker_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val allNotes = generateAllNotes().toList().toTypedArray()
        allNotes.forEach { println(it) }
        ArrayAdapter(context!!, android.R.layout.simple_spinner_item, allNotes)
            .apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner!!.adapter = this
                spinner.setSelection(this.getPosition(defaultNote))
            }
        setupViews()
    }
}