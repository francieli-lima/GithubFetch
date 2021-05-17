package br.com.francielilima.githubfetch.features.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import br.com.francielilima.githubfetch.R
import br.com.francielilima.githubfetch.databinding.FragmentDetailBinding
import br.com.francielilima.githubfetch.extensions.setVisibility
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso

class DetailFragment : BottomSheetDialogFragment() {

    private val args: DetailFragmentArgs by navArgs()
    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = args.repository

        with(binding) {
            repository?.let { repository ->
                textViewName.text = repository.name
                textViewOwner.text = repository.owner?.login
                textViewDescription.text = repository.description
                textViewStars.text = repository.stars.toString()
                textViewLanguage.text = repository.language

                textViewLanguage.setVisibility(!repository.language.isNullOrEmpty())
                textViewDescription.setVisibility(!repository.description.isNullOrEmpty())
                textViewOwner.setVisibility(!repository.owner?.login.isNullOrEmpty())

                ContextCompat.getDrawable(requireContext(), R.drawable.ic_account)?.let {
                    Picasso.get()
                        .load(repository.owner?.avatarUrl)
                        .placeholder(it)
                        .error(it)
                        .into(imageViewOwner)
                }
            }
        }
    }
}