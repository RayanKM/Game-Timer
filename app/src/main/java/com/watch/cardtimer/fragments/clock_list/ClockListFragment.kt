package com.watch.cardtimer.fragments.clock_list

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.watch.cardtimer.R
import com.watch.cardtimer.databinding.FragmentClockListBinding
import com.watch.cardtimer.fragments.clock_list.ClockListViewModel
import com.watch.cardtimer.fragments.clock_list.ClockListViewModelFactory
import com.watch.cardtimer.fragments.settings.SettingsFragment.Companion.PREFERENCES_NAME
import com.watch.cardtimer.utils.BaseFragment
import com.watch.cardtimer.utils.ChessUtils.Companion.CURRENT_CLOCK_KEY

class ClockListFragment : BaseFragment<FragmentClockListBinding>(
    FragmentClockListBinding::inflate
) {

    private lateinit var viewModel: ClockListViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.window?.statusBarColor =
            ContextCompat.getColor(requireContext(), R.color.dark_gray)

        val application = requireActivity().application

        val currentClockId = preferences.getLong(CURRENT_CLOCK_KEY, -1)

        val viewModelFactory = ClockListViewModelFactory(application, currentClockId)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ClockListViewModel::class.java)

        val adapter = ClockListAdapter(currentClockId, getClockItemListener())
        binding.clockList.adapter = adapter

        // OBSERVERS...
        viewModel.clocks.observe(viewLifecycleOwner) { adapter.data = it }

        viewModel.currentClockId.observe(viewLifecycleOwner) {
            preferences.edit().putLong(CURRENT_CLOCK_KEY, it).apply()
            adapter.currentClockId = it
            adapter.notifyDataSetChanged()
        }

        // UI ACTIONS
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.sound_settings -> {
                    val action =
                        ClockListFragmentDirections.actionClockListFragmentToSettingsFragment()
                    findNavController().navigate(action)
                    true
                }
                else -> false
            }
        }

        binding.addClock.setOnClickListener {
            val action = ClockListFragmentDirections.actionClockListFragmentToTimeControlFragment()
            findNavController().navigate(action)
        }
    }

    private fun getClockItemListener(): ClockItemListener = object : ClockItemListener {

        override fun onClickItem(clockId: Long) {
            viewModel.setCurrentClockId(clockId)
            findNavController().navigateUp()
        }

        override fun onEditItem(clockId: Long) {
            val action = ClockListFragmentDirections.actionClockListFragmentToTimeControlFragment(
                clockId = clockId,
                editOption = true
            )
            findNavController().navigate(action)
        }

        override fun onRemoveItem(clockId: Long) {
            val removeItem = viewModel.onRemoveClick()
            if (removeItem) showConfirmDeleteDialog(clockId)
            else showSnackBarOneClock()
        }
    }

    private fun showSnackBarOneClock() {
        Snackbar.make(binding.root, R.string.empty_clock_list_advice, Snackbar.LENGTH_SHORT).show()
    }

    private fun showConfirmDeleteDialog(clockId: Long) {
        val dialog = MaterialAlertDialogBuilder(requireContext(), R.style.AlertDialogTheme)
        dialog.apply {
            setTitle(R.string.delete_clock_title)
            setMessage(R.string.delete_clock_message)
            setPositiveButton(R.string.delete_clock_confirm_button) { _, _ ->
                viewModel.removeItem(clockId)
            }
            setNegativeButton(R.string.cancel_button) { _, _ -> }
            show()
        }
    }
}