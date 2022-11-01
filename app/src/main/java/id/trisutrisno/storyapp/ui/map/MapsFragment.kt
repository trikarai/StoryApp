package id.trisutrisno.storyapp.ui.map

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import id.trisutrisno.storyapp.R
import id.trisutrisno.storyapp.databinding.FragmentMapsBinding
import id.trisutrisno.storyapp.ui.MainActivity
import id.trisutrisno.storyapp.utils.SharedViewModel
import id.trisutrisno.storyapp.utils.UserViewModelFactory


class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val mapsViewModel: MapsViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }
    private val sharedViewModel: SharedViewModel by viewModels {
        UserViewModelFactory.getInstance(requireContext())
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            getCurrentLocation()
        }
    }

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        getCurrentLocation()
        markStoryLocation()
        setMapStyle()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
    }

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext().applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 8f))
                } else {
                    Toast.makeText(requireContext(), getString(R.string.please_activate_location_message), Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun markStoryLocation() {
        val token = sharedViewModel.user.value?.token ?: ""

        Log.e(TAG, token)

        mapsViewModel.fetchAllStoryWithLocation(token)
            .observe(viewLifecycleOwner){ result ->
            result.data?.let { storyResponse ->
                storyResponse.forEach { story ->
                    if (story.lat != null && story.lon != null) {
                        val latLng = LatLng(story.lat, story.lon)

                        Glide.with(this)
                            .asBitmap()
                            .load(story.photoUrl)
                            .circleCrop()
                            .into(object : SimpleTarget<Bitmap?>() {
                                override fun onResourceReady(
                                    resource: Bitmap,
                                    transition: Transition<in Bitmap?>?
                                ){
                                    val scale = context!!.resources.displayMetrics.density
                                    val pixels = (50 * scale + 0.5f).toInt()
                                    val bitmap =
                                        Bitmap.createScaledBitmap(resource, pixels, pixels, true)

                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(story.name)
                                            .snippet(story.description)
                                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                                    )
                                }
                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    mMap.addMarker(
                                        MarkerOptions()
                                            .position(latLng)
                                            .title(story.name)
                                            .snippet(story.description)
                                     )
                                }
                            })
                    }
                }
            }
        }
    }

    private fun setMapStyle() {
        try {
            val success =
                mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
            if (!success) {
                Log.e(TAG, getString(R.string.style_map_failed))
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, getString(R.string.style_map_error, exception))
        }
    }

    companion object {
        const val TAG = "Maps_Fragment"
    }

}