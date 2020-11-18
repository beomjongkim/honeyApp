package com.dmonster.darling.honey.point.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil

import com.dmonster.darling.honey.R
import com.dmonster.darling.honey.common.command.SpinnerInterface
import com.dmonster.darling.honey.custom_recyclerview.view.CustomAdapter
import com.dmonster.darling.honey.dialog.ReservePaymentPopup
import com.dmonster.darling.honey.databinding.FragmentPointBinding
import com.dmonster.darling.honey.point.viewmodel.PointSpinnerVM
import com.dmonster.darling.honey.point.viewmodel.PointViewModel
import com.dmonster.darling.honey.util.AppKeyValue
import com.dmonster.darling.honey.util.Utility

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PointFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding: FragmentPointBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_point, container, false)
        activity
        binding.pointViewModel = activity?.let { it ->
            PointViewModel(
                Utility.instance.getPref(it, AppKeyValue.instance.savePrefID), lifecycle, it,
                ReservePaymentPopup(it, this), CustomAdapter(R.layout.layout_point_log, this)
            )
        }
        binding.spinnerVM = PointSpinnerVM(
            ArrayAdapter<String>(
                context!!,
                R.layout.custom_dropdown
            ).also {
                it.addAll(listOf("50포인트", "100포인트", "150포인트"))
            },
            object : SpinnerInterface {
                override fun onItemSelected() {
                    binding.pointViewModel?.let { pointViewModel ->
                        binding.spinnerVM?.let { pointSpinnerVM ->
                            pointViewModel.chargePoint.value = pointSpinnerVM.price.value.toString()
                            pointViewModel.inappType = pointSpinnerVM.position.value!!
                            pointViewModel.price_won.value = pointSpinnerVM.price.value?.times(110)

                        }
                    }
                }

                override fun onNothingSelected() {
                }

            }
        )
        binding.lifecycleOwner = this

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItemFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PointFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//
//    override fun onPurchasesUpdated(billingResult: BillingResult, purchases: MutableList<Purchase>?) {
//        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
//            for (purchase in purchases) {
//                billingClient.consumeAsync(ConsumeParams())
//                context?.let { binding.pointViewModel?.buy_inApp(it,2) }
//            }
//        } else if (billingResult.responseCode == BillingClient.BillingResponseCode.USER_CANCELED) {
//            // Handle an error caused by a user cancelling the purchase flow.
//        } else {
//            // Handle any other error codes.
//        }
//    }
}
