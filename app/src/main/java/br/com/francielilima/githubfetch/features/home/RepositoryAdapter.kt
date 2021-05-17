package br.com.francielilima.githubfetch.features.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.francielilima.githubfetch.databinding.RowRepositoryBinding
import br.com.francielilima.githubfetch.entities.Repository

class RepositoryAdapter(private val onClick: (Repository) -> Unit) :
    PagingDataAdapter<Repository, RepositoryAdapter.RepositoryViewHolder>(RepositoryComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryViewHolder {
        val binding =
            RowRepositoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepositoryViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: RepositoryViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class RepositoryViewHolder(
        private val binding: RowRepositoryBinding,
        private val onClick: (Repository) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(repository: Repository) {

            with(binding) {
                textViewName.text = repository.name
                textViewOwner.text = repository.owner?.login
                textViewStars.text = repository.stars.toString()
                root.setOnClickListener { onClick(repository) }
            }
        }
    }

    object RepositoryComparator : DiffUtil.ItemCallback<Repository>() {
        override fun areItemsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Repository, newItem: Repository): Boolean {
            return oldItem == newItem
        }
    }
}
