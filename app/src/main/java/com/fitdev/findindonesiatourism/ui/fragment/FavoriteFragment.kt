package com.fitdev.findindonesiatourism.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.fitdev.findindonesiatourism.ui.adapter.FavoriteViewAdapter
import com.fitdev.findindonesiatourism.ui.viewmodel.FavoriteViewModel
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val mFavoriteViewModel by activityViewModels<FavoriteViewModel> {
        com.fitdev.findindonesiatourism.ui.viewmodel.ViewModelFactory.getInstance(requireActivity().application)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.favorite)

        binding.favoriteRv.layoutManager = LinearLayoutManager(requireContext())

        observeFavoriteData()
    }

    @SuppressLint("SetTextI18n")
    private fun observeFavoriteData(){
        mFavoriteViewModel.getAllFavorites().observe(viewLifecycleOwner){favoriteData ->
            val adapter = FavoriteViewAdapter(favoriteData)
            binding.favoriteRv.adapter = adapter
            binding.favoriteShowing.text = "Showing ${favoriteData?.size} tourist destinations in my favorite data"
        }
    }
}