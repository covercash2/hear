package dev.covercash.aaudiotests.audio.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import dev.covercash.aaudiotests.R
import dev.covercash.aaudiotests.audio.AudioEngineModel
import dev.covercash.aaudiotests.view.unit_slider.FloatRange
import dev.covercash.aaudiotests.view.unit_slider.SLIDER_MAX
import dev.covercash.aaudiotests.view.unit_slider.SLIDER_MIN
import kotlinx.android.synthetic.main.filter_fragment.view.*

class FilterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.filter_fragment, container, false)

        val model = activity!!.run {
            ViewModelProviders.of(this).get(AudioEngineModel::class.java)
        }

        val rcRange = FloatRange(0f, 100f)

        view.rc_seek_bar!!.apply {
            min = SLIDER_MIN
            max = SLIDER_MAX

            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        val scaledValue = rcRange.valueOf(progress)
                        model.rc = scaledValue
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }

        return view
    }
}
