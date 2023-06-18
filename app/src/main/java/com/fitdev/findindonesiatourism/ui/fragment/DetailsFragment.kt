package com.fitdev.findindonesiatourism.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.fitdev.findindonesiatourism.database.Favorite
import com.fitdev.findindonesiatourism.remote.api.gmaps.GoogleMapsConfig
import com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails.PlaceDetailsResponse
import com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails.Result
import com.fitdev.findindonesiatourism.ui.adapter.MorePhotosViewAdapter
import com.fitdev.findindonesiatourism.ui.adapter.OpeningDaysHoursViewAdapter
import com.fitdev.findindonesiatourism.ui.adapter.ReviewsViewAdapter
import com.fitdev.findindonesiatourism.ui.viewmodel.FavoriteViewModel
import com.fitdev.myapplication.BuildConfig
import com.fitdev.myapplication.R
import com.fitdev.myapplication.databinding.FragmentDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val mFavoriteViewModel by activityViewModels<FavoriteViewModel> {
        com.fitdev.findindonesiatourism.ui.viewmodel.ViewModelFactory.getInstance(requireActivity().application)
    }

    private val _byDetailsData = MutableLiveData<Result?>()
    private val byDetailsData: LiveData<Result?> = _byDetailsData

    private lateinit var myDialog: Dialog
    private var placeId: String? = null
    private var placeName: String? = null
    private var placePosition: LatLng? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        myDialog = Dialog(requireActivity())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = activity as AppCompatActivity
        activity.supportActionBar?.title = getString(R.string.details)
        arguments?.let {
            placeId = it.getString(ARG_PLACE_ID)
        }

        binding.detailsOpeningDaysHoursValueRv.layoutManager = LinearLayoutManager(requireContext())
        binding.detailsMorePhotosValueRv.layoutManager = LinearLayoutManager(requireContext())
        binding.detailsReviewsValueRv.layoutManager = LinearLayoutManager(requireContext())

        placeId?.let {
            getDetailsData(it)
            favoriteCheckAndAction(it)
        }
        observeDetailsData()
    }

    private fun getDetailsData(idPlace: String){
        showLoading(true)
        val getDetail = GoogleMapsConfig.getGoogleMapsService().getPlaceDetails(BuildConfig.API_KEY, idPlace)
        getDetail.enqueue(object: Callback<PlaceDetailsResponse>{
            override fun onResponse(call: Call<PlaceDetailsResponse>, response: Response<PlaceDetailsResponse>)
            {
                showLoading(false)
                if (response.isSuccessful){
                    _byDetailsData.value = response.body()?.result
                    Log.d("Response (Details)", response.body()?.result.toString())
                }
                else{
                    showLoading(false)
                    Log.d("Response Gagal Details", response.toString())
                    Toast.makeText(requireActivity(), "Fetching data failed!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PlaceDetailsResponse>, t: Throwable)
            {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure Details: ${t.message}")
                Toast.makeText(requireActivity(), t.message, Toast.LENGTH_LONG).show()
            }

        })
    }

    @SuppressLint("SetTextI18n")
    private fun observeDetailsData(){
        byDetailsData.observe(viewLifecycleOwner){details ->
            binding.detailsPlaceName.text = details?.name

            binding.detailsFirstImage.load(photoUrl(details?.photos?.get(0)?.photoReference))

            binding.detailsRatingStars.rating = details?.rating.toString().toFloat()
            binding.detailsRatingCount.text = "${details?.userRatingsTotal} Reviews"
            if(details?.currentOpeningHours?.openNow == true) {
                binding.detailsPlaceStatusValue.text = "Open Now"
            }
            else {
                binding.detailsPlaceStatusValue.text = "Close Now"
            }
            binding.detailsOpeningDaysHoursValueRv.adapter = OpeningDaysHoursViewAdapter(details?.currentOpeningHours?.weekdayText)
            binding.detailsAddressValue.text = details?.formattedAddress

            val mapFragment = SupportMapFragment.newInstance()
            val activity = activity as AppCompatActivity
            activity.supportFragmentManager.beginTransaction().add(R.id.details_maps_value, mapFragment).commit()
            placeName = details?.name
            placePosition = LatLng(details?.geometry?.location?.lat.toString().toDouble(), details?.geometry?.location?.lng.toString().toDouble())
            mapFragment.getMapAsync { googleMap ->
                placePosition?.let {
                    googleMap.addMarker(
                        MarkerOptions()
                            .title(placeName)
                            .position(it)
                    )
                }
                val swBound = LatLng((details?.geometry?.viewport?.southwest?.lat.toString().toDouble()), details?.geometry?.viewport?.southwest?.lng.toString().toDouble())
                val neBound = LatLng((details?.geometry?.viewport?.northeast?.lat.toString().toDouble()), details?.geometry?.viewport?.northeast?.lng.toString().toDouble())
                val viewBound = LatLngBounds(swBound, neBound)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(viewBound.center, 12f))
            }

            var isPhone = true
            var isWebsite = true
            if(details?.internationalPhoneNumber?.isNotEmpty() == true){
                binding.detailsActionValueTelepon.setOnClickListener{
                    val dialIntent = Intent(Intent.ACTION_DIAL)
                    dialIntent.data = Uri.parse("tel:${details.internationalPhoneNumber.toString().replace(" ", "")
                        .replace("-", "")}")
                    startActivity(dialIntent)
                }

                binding.detailsActionValueWhatsapp.setOnClickListener{
                    val whatsappIntent = Intent(Intent.ACTION_VIEW)
                    whatsappIntent.data = Uri.parse("https://api.whatsapp.com/send?phone=${
                        details.internationalPhoneNumber.replace(" ", "").replace("-", "")
                    }")
                    startActivity(whatsappIntent)
                }
            }
            else{
                isPhone = false
                binding.detailsActionValueTelepon.visibility = View.INVISIBLE
                binding.detailsActionValueWhatsapp.visibility = View.INVISIBLE
            }

            if(details?.website?.isNotEmpty() == true){
                binding.detailsActionValueWebsite.setOnClickListener {
                    val webIntent = Intent(Intent.ACTION_VIEW)
                    webIntent.data = Uri.parse(details.website)
                    startActivity(webIntent)
                }
            }
            else{
                isWebsite = false
                binding.detailsActionValueWebsite.visibility = View.INVISIBLE
            }

            if(!isPhone && !isWebsite){
                binding.detailsActionLabel.visibility = View.INVISIBLE
            }

            binding.detailsActionValueTraveloka.setOnClickListener{
                val travelokaIntent = Intent(Intent.ACTION_VIEW)
                travelokaIntent.data = Uri.parse("https://www.traveloka.com/en-id/")
                startActivity(travelokaIntent)
            }

            binding.detailsActionValueTiketcom.setOnClickListener{
                val tiketcomIntent = Intent(Intent.ACTION_VIEW)
                tiketcomIntent.data = Uri.parse("https://www.tiket.com/")
                startActivity(tiketcomIntent)
            }

            binding.detailsActionValuePegipegi.setOnClickListener{
                val pegipegiIntent = Intent(Intent.ACTION_VIEW)
                pegipegiIntent.data = Uri.parse("https://www.pegipegi.com/")
                startActivity(pegipegiIntent)
            }

            binding.detailsActionValueNusatrip.setOnClickListener{
                val nusatripIntent = Intent(Intent.ACTION_VIEW)
                nusatripIntent.data = Uri.parse("https://www.nusatrip.com/en")
                startActivity(nusatripIntent)
            }

            binding.detailsMorePhotosValueRv.adapter = MorePhotosViewAdapter(details?.photos)
            binding.detailsReviewsValueRv.adapter = ReviewsViewAdapter(details?.reviews)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun favoriteCheckAndAction(idPlace: String){
        var favoriteData = Favorite()
        var isFavorited = false
        mFavoriteViewModel.getFavoriteByPlaceId(idPlace).observe(viewLifecycleOwner){ favorite ->
            if(favorite != null){
                binding.detailsBtnFavorite.text = "Remove from My Favorite"
                isFavorited = true
                favoriteData = favorite
            }
            else{
                binding.detailsBtnFavorite.text = "Add to My Favorite"
                isFavorited = false
                byDetailsData.observe(viewLifecycleOwner){detailsData ->
                    detailsData?.userRatingsTotal?.let { userRatingsTotal ->
                        favoriteData = Favorite(0, detailsData.placeId, detailsData.name, userRatingsTotal, detailsData.rating, detailsData.photos?.get(0)?.photoReference)
                    }
                }
            }
        }
        binding.detailsBtnFavorite.setOnClickListener {
            if (isFavorited){
                favoriteData.let { mFavoriteViewModel.delete(it) }
                Toast.makeText(requireActivity(), "Successfully deleted one favorite data", Toast.LENGTH_SHORT).show()
            }
            else{
                favoriteData.let { mFavoriteViewModel.insert(it) }
                Toast.makeText(requireActivity(), "Successfully added one favorite data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun photoUrl(photoReference: String?): String{
        val key = "AIzaSyBl416wxXDeyiRk3ZuTsLXFjRhx_1e_QXg"
        val maxheight = "250"
        return if(photoReference.isNullOrEmpty()){
            "https://aplikasijpm.online/fitproject/default/defaultimage.png"
        } else{
            "https://maps.googleapis.com/maps/api/place/photo?key=$key&photo_reference=$photoReference&maxheight=$maxheight"
        }
    }

    @SuppressLint("InflateParams")
    private fun showLoading(con: Boolean){
        val dialogBinding = layoutInflater.inflate(R.layout.loading_dialog, null)
        myDialog.setContentView(dialogBinding)
        myDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        if(con){
            myDialog.setCancelable(false)
            myDialog.show()
        }
        else{
            myDialog.setCancelable(true)
            myDialog.dismiss()
        }
    }

    companion object{
        const val ARG_PLACE_ID = "place_id"
    }
}