package com.watch.cardtimer.utils

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.watch.cardtimer.R
import com.watch.cardtimer.fragments.settings.SettingsFragment

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {

    private var _binding: VB? = null
    val binding get() = _binding!!

    lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)

        preferences = requireActivity().getSharedPreferences(
            SettingsFragment.PREFERENCES_NAME, Context.MODE_PRIVATE
        )

        return binding.root
    }

    fun View.gone() {
        this.visibility = View.GONE
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.setBgColor(@ColorRes color: Int) {
        this.setBackgroundColor(ContextCompat.getColor(requireContext(), color))
    }

    fun CardView.setCardViewBgColor(@ColorRes color: Int) {
        this.setCardBackgroundColor(getContextCompactColor(color))
    }

    fun TextView.setTextViewColor(@ColorRes color: Int = R.color.white) {
        this.setTextColor(getContextCompactColor(color))
    }

    fun getContextCompactColor(@ColorRes color: Int): Int =
        ContextCompat.getColor(requireContext(), color)

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}