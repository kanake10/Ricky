package com.example.ricky.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.ricky.R
import com.example.ricky.databinding.FragmentCharactersBinding
import com.example.ricky.ui.adapter.RickyAdapter
import com.example.ricky.ui.viewmodel.RickyViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerAdapter: RickyAdapter
    private val mainViewModel: RickyViewModel by viewModels()
    private var searchJob: Job? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        _binding = FragmentCharactersBinding.inflate(layoutInflater)
        return binding.root
        recyclerAdapter = RickyAdapter()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpAdapter()
        startSearchJob()

    }



    private fun startSearchJob() {

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            mainViewModel.getListData()
                .collectLatest {
                    recyclerAdapter.submitData(it)
                }
        }
    }

    private fun setUpAdapter() {


        binding.recyclerview.apply {
            adapter = recyclerAdapter
        }
        recyclerAdapter.addLoadStateListener { loadState ->

            if (loadState.refresh is LoadState.Loading) {

                if (recyclerAdapter.snapshot().isEmpty()) {
                    binding.progressIndicator.isVisible = true
                }
                binding.errorTextView.isVisible = false

            } else {
                binding.progressIndicator.isVisible = false

                val error = when {
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                error?.let {
                    if (recyclerAdapter.snapshot().isEmpty()) {
                        binding.errorTextView.isVisible = true
                        binding.errorTextView.text = it.error.localizedMessage
                    }
                }

            }
        }
    }
}