package com.example.mobiletest.ui.alphabet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.graphics.Color
import android.graphics.Typeface
import android.view.MotionEvent
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mobiletest.adapter.GenericAdapter
import com.example.mobiletest.databinding.FragmentAlphabeticalBinding
import com.example.mobiletest.model.GenericItem

class AlphabeticalFragment : Fragment() {

    private var _binding: FragmentAlphabeticalBinding? = null
    private val binding get() = _binding!!

    private val alphabet = ('A'..'Z').toList()
    private lateinit var adapter: GenericAdapter
    private val items = mutableListOf<GenericItem>()
    private var lastSelectedIndex = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlphabeticalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupAlphabetIndex()
    }

    private fun setupList() {
        alphabet.forEach { char ->
            repeat(3) { i ->
                items.add(GenericItem(items.size, "$char Item $i", "Alphabetical item starting with $char"))
            }
        }
        adapter = GenericAdapter(items)
        binding.alphabetRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.alphabetRecyclerView.adapter = adapter
    }

    private fun setupAlphabetIndex() {
        alphabet.forEach { letter ->
            val textView = TextView(context).apply {
                text = letter.toString()
                setPadding(8, 4, 8, 4)
                textSize = 12f
                gravity = android.view.Gravity.CENTER
                isClickable = false
            }
            binding.indexBar.addView(textView)
        }

        binding.indexBar.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    val y = event.y
                    // Calculate index based on touch Y coordinate relative to the indexBar height
                    val index = (y / v.height * alphabet.size).toInt().coerceIn(0, alphabet.size - 1)
                    if (index != lastSelectedIndex) {
                        lastSelectedIndex = index
                        selectLetter(index)
                    }
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.performClick()
                    lastSelectedIndex = -1
                    // Hide only the center overlay, but keep the letter highlight/zoom
                    binding.selectedLetterOverlay.visibility = View.GONE
                }
            }
            true
        }
    }

    private fun selectLetter(index: Int) {
        val letter = alphabet[index]
        scrollToSection(letter)

        // Update Overlay
        binding.selectedLetterOverlay.text = letter.toString()
        binding.selectedLetterOverlay.visibility = View.VISIBLE

        for (i in 0 until binding.indexBar.childCount) {
            val child = binding.indexBar.getChildAt(i) as TextView
            if (i == index) {
                child.animate().scaleX(2.0f).scaleY(2.0f).setDuration(100).start()
                child.setTextColor(Color.RED)
                child.setTypeface(null, Typeface.BOLD)
            } else {
                child.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
                child.setTextColor(Color.BLACK)
                child.setTypeface(null, Typeface.NORMAL)
            }
        }
    }

    private fun resetLetters() {
        // This is now only used if we want to clear everything, 
        // but normally we keep the last selection highlighted.
        binding.selectedLetterOverlay.visibility = View.GONE
        for (i in 0 until binding.indexBar.childCount) {
            val child = binding.indexBar.getChildAt(i) as TextView
            child.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start()
            child.setTextColor(Color.BLACK)
            child.setTypeface(null, Typeface.NORMAL)
        }
    }

    private fun scrollToSection(letter: Char) {
        val index = items.indexOfFirst { it.title.startsWith(letter) }
        if (index != -1) {
            (binding.alphabetRecyclerView.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(index, 0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
