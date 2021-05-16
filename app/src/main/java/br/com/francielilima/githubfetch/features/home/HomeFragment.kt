package br.com.francielilima.githubfetch.features.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import br.com.francielilima.githubfetch.databinding.FragmentHomeBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RepositoryAdapter {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment()
            action.repository = it

            findNavController().navigate(action)
        }

        binding.recyclerViewRepositories.adapter = adapter
        binding.buttonGo.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                homeViewModel.getRepositories(binding.editTextSearch.text.toString()).collectLatest {
                    adapter.submitData(it)
                }
            }
        }
    }
}