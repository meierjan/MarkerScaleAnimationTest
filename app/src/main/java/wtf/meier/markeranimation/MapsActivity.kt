package wtf.meier.markeranimation

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.animation.ValueAnimator
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.OvershootInterpolator
import com.google.android.gms.maps.model.*


const val ICON_WIDTH: Int = 80
const val ICON_HEIGHT: Int = 100

const val ICON_SELECTED_SCALE_FACTOR = 2F

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap

    var animation: ValueAnimator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMap.setOnMarkerClickListener(this)
        googleMap.setOnMapClickListener(this)
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        val mapIcon = BitmapFactory.decodeResource(resources, R.drawable.station_nextbike_active)

        val bitmap = Bitmap.createScaledBitmap(mapIcon, ICON_WIDTH, ICON_HEIGHT, false)
        val mapIconBitMapDescriptor = BitmapDescriptorFactory.fromBitmap(bitmap)
        val marker = mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney").icon(mapIconBitMapDescriptor).flat(true))
        marker.tag = bitmap

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))


    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        val marker = p0!!

        val mapIcon = marker.tag as Bitmap


        val animation = ValueAnimator.ofFloat(1F, ICON_SELECTED_SCALE_FACTOR)
        animation.duration = 600
        animation.interpolator = AnticipateOvershootInterpolator()

        animation.addUpdateListener { animationState ->
            val scaleFactor = animationState.animatedValue as Float
            val newBitMap = Bitmap.createScaledBitmap(
                    mapIcon,
                    (ICON_WIDTH * scaleFactor).toInt(),
                    (ICON_HEIGHT * scaleFactor).toInt(),
                    false
            )
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(newBitMap))
        }
        animation.start()

        this.animation = animation

        return true
    }

    override fun onMapClick(p0: LatLng?) {
        animation?.reverse()
        animation = null
    }

}
