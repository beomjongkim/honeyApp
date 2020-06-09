package com.dmonster.darling.honey.util

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast

import androidx.core.content.ContextCompat
import com.dmonster.darling.honey.R
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import java.util.ArrayList

/**
 * Created by culturetech on 2017-04-28.
 */

class GpsManager private constructor() : LocationListener {
    override fun onLocationChanged(location: Location?) {
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }

    companion object {
        val instance: GpsManager
            get() = SingleTone.instance
    }

    private object SingleTone {
        val instance = GpsManager()
    }

    // 현재 GPS 사용유무
    internal var isGPSEnabled = false

    // 네트워크 사용유무
    internal var isNetworkEnabled = false


    // GPS 정보 업데이트 거리 10미터
    private val MIN_DISTANCE_UPDATES: Long = 1000

    // GPS 정보 업데이트 시간 1/1000
    private val MIN_TIME_UPDATES = (1000 * 60 * 1).toLong()

    // GPS 상태값
    var isGetLocation = false
        internal set

    private var location: Location? = null
    private var lat: Double = 0.toDouble() // 위도
    private var lon: Double = 0.toDouble() // 경도

    private var locationManager: LocationManager? = null

    /**
     * 위도값
     */
    val latitude: Double
        get() {
            if (location != null) {
                lat = location!!.latitude
            }
            return lat
        }

    /**
     * 경도값
     */
    val longitude: Double
        get() {
            if (location != null) {
                lon = location!!.longitude
            }
            return lon
        }


    fun getLocation(mContext: Context): Location? {

        try {
            if (Build.VERSION.SDK_INT >= 23) {

                if (ContextCompat.checkSelfPermission(
                        mContext,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                        mContext,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    locationManager =
                        mContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager

                    isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)

                    isNetworkEnabled =
                        locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


                    if (!isGPSEnabled && !isNetworkEnabled) {
                    } else {
                        this.isGetLocation = true
                        if (isNetworkEnabled) {
                            locationManager!!.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                MIN_TIME_UPDATES,
                                MIN_DISTANCE_UPDATES.toFloat(), this
                            )

                            if (locationManager != null) {
                                location = locationManager!!
                                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                            }
                        }

                        if (isGPSEnabled) {
                            if (location == null) {
                                locationManager!!
                                    .requestLocationUpdates(
                                        LocationManager.GPS_PROVIDER,
                                        MIN_TIME_UPDATES,
                                        MIN_DISTANCE_UPDATES.toFloat(),
                                        this
                                    )
                                if (locationManager != null) {
                                    location = locationManager!!
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER)

                                }
                            }
                        }
                    }
                } else {

                }

            } else {
                locationManager = mContext
                    .getSystemService(Context.LOCATION_SERVICE) as LocationManager

                isGPSEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.GPS_PROVIDER)

                isNetworkEnabled = locationManager!!
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER)


                if (!isGPSEnabled && !isNetworkEnabled) {
                } else {
                    this.isGetLocation = true
                    if (isNetworkEnabled) {
                        locationManager!!.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_UPDATES,
                            MIN_DISTANCE_UPDATES.toFloat(), this
                        )

                        if (locationManager != null) {
                            location =
                                locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

                        }
                    } else {
                        Utility.instance.showToast(
                            mContext,
                            mContext.getString(R.string.alert_network)
                        )
                    }

                    if (isGPSEnabled) {
                        if (location == null) {
                            locationManager!!
                                .requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_UPDATES,
                                    MIN_DISTANCE_UPDATES.toFloat(),
                                    this
                                )
                            if (locationManager != null) {
                                location = locationManager!!
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            }
                        }
                    } else {
                        Utility.instance.showToast(
                            mContext,
                            mContext.getString(R.string.alert_gps)
                        )
                    }
                }

            }

        } catch (e: Exception) {
            e.printStackTrace()
        }



        return location
    }

    fun permissionRequest(mContext: Context) {
        if (ContextCompat.checkSelfPermission(
                mContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissionlistener = object : PermissionListener {
                override fun onPermissionGranted() {

                }

                override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {

                }

            }

            TedPermission(mContext)
                .setPermissionListener(permissionlistener)
                .setRationaleMessage(mContext.getString(R.string.notice_gps_allow_before))
                .setDeniedMessage(mContext.getString(R.string.notice_gps_denied))
                .setPermissions(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .check()
        }
    }


    fun calDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {

        val theta: Double
        var dist: Double
        theta = lon1 - lon2
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + (Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta)))
        dist = Math.acos(dist)
        dist = rad2deg(dist)

        dist = dist * 60.0 * 1.1515
        dist = dist * 1.609344    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0      // 단위  km 에서 m 로 변환

        return dist
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }
}