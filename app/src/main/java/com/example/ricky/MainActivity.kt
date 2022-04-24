package com.example.ricky

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.ricky.databinding.ActivityMainBinding
import com.example.ricky.ui.adapter.RickyAdapter
import com.example.ricky.ui.viewmodel.RickyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerAdapter: RickyAdapter
    private val mainViewModel: RickyViewModel by viewModels()

    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerAdapter = RickyAdapter()

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