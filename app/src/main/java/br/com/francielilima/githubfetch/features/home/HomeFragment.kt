package br.com.francielilima.githubfetch.features.home

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.francielilima.githubfetch.R
import br.com.francielilima.githubfetch.databinding.FragmentHomeBinding
import br.com.francielilima.githubfetch.extensions.hide
import br.com.francielilima.githubfetch.extensions.show
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), DialogInterface.OnClickListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: RepositoryAdapter
    private val homeViewModel by viewModel<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }

    private var coroutineJob: Job? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RepositoryAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            action.repository = it

            findNavController().navigate(action)
        }

        setRecyclerView()

        binding.inputSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                loadRepositories()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        loadRepositories()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(context)

        binding.recyclerViewRepositories.layoutManager = layoutManager

        val dividerItemDecoration = DividerItemDecoration(
            binding.recyclerViewRepositories.context,
            layoutManager.orientation
        )
        binding.recyclerViewRepositories.addItemDecoration(dividerItemDecoration)

        binding.recyclerViewRepositories.adapter = adapter

        adapter.addLoadStateListener { state ->
            binding.textViewEmptyState.hide()
            if (state.append.endOfPaginationReached) {
                if (adapter.itemCount < 1) binding.textViewEmptyState.show()
            }
        }
    }

    private fun loadRepositories() {
        coroutineJob?.cancel()
        coroutineJob = lifecycleScope.launch {
            val query = binding.inputSearch.text.toString().trim()
            val result =
                if (query.isEmpty()) {
                    binding.textViewHeader.text = getString(R.string.popular_repos)
                    homeViewModel.getTrendingRepositories()
                } else {
                    binding.textViewHeader.text = getString(R.string.search_header, query)
                    homeViewModel.getRepositories(query)
                }

            result.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onClick(dialog: DialogInterface?, which: Int) {
        loadRepositories()
    }
}