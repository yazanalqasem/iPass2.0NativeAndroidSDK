package com.app.ipassplus.ui.dashboard.fragment

import ScenariosListAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ipassplus.MainActivity
import com.app.ipassplus.R
import com.app.ipassplus.ui.dashboard.model.ScenariosItemModel
import com.app.ipassplus.databinding.FragmentDashboardBinding
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.core.configProperties
import com.sdk.ipassplussdk.core.iPassSDKManger
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse

class DashboardFragment : Fragment(), ScenariosListAdapter.OnClickListener {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }
    private lateinit var adapter: ScenariosListAdapter
  //  private val email = "ipassmobsdk@yopmail.com"
    private val email = "ipassandhar@yopmail.com"
//    private val email = "testconsip@yopmail.com"
    private val password = "Admin@123#"
//    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.dGVzdExpYzEyM0B5b3BtYWlsLmNvbUFqYXkga3VtYXIgICA0OTgxM2NiYi05ZDdiLTRlNTEtYWIwYy1hZDkwMzFjMmZmYjk.XPX5jCeTDKwVXL0R-fvV24swfORF2jDNVHsNnReXYCQ"
    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.aXBhc3NhbmRoYXJAeW9wbWFpbC5jb21tb2JpbGUgdGVhbSAgIDFhYzlkYzYyLWFjZmUtNDEwOC04Y2Q2LTExY2I0OTA5NDFmMw.jTDHn4B6yOaPGpK0y2G2vvSxTcybaV7icfGkGltIelo"
    val phoneNumber = "7894563210"
    val flowId = "10031"
    val socialMediaEmail = "ipassmobisdk@yopmail.com"
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configProperties.needHologramDetection(true)
        val scenarioList = arrayListOf(
            ScenariosItemModel(R.drawable.full2,"Full Processing",getString(R.string.processing_scenario_for_obtaining_all_document_data),true),
            ScenariosItemModel(R.drawable.bankkk, "Bank Card",getString(R.string.procesing_scenario_for_obtaning_bank_card_data),false),
            ScenariosItemModel(R.drawable.mrz, "MRZ",getString(R.string.procesing_scenario_for_obtaning_mrz_n_data),false),
            ScenariosItemModel(R.drawable.barcode, "Barcode",getString(R.string.procesing_scenario_for_obtaning_barcode_ndata),false),
            ScenariosItemModel(R.drawable.visual, "Visual OCR",getString(R.string.procesing_scenario_for_obtaning_nvisaul_zone_ocr_results),false)
        )

        adapter = ScenariosListAdapter(scenarioList,requireContext())
        adapter.setOnClickListener(this)

        binding.rvitems.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = this@DashboardFragment.adapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onScenarioPickClick(position: Int, model: ScenariosItemModel) {
        iPassSDKManger.startScanningProcess(
            context = requireContext(),
            email = email, userToken = MainActivity.userToken,
            appToken = apptoken,socialMediaEmail = socialMediaEmail, phoneNumber = phoneNumber, flowId =  flowId , bindingView = binding.root as ViewGroup) {
            status, message ->
            if (status) {
                Log.e("startScanningProcess", message)
                getDocData()
            } else {
                Log.e("startScanningProcess", message)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDocData() {
        iPassSDKManger.getDocumentScannerData(requireContext(), apptoken, object : ResultListener<TransactionDetailResponse> {
            override fun onSuccess(response: TransactionDetailResponse?) {
                if (response?.Apistatus!!) {
                    Log.e("onSuccess", response.Apimessage!!)
                    Log.e("onSuccess", response.data.toString())
                } else {
                    Log.e("error", response.Apimessage!!)
                    Toast.makeText(context, response.Apimessage, Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(exception: String) {
                Log.e("onSuccess", exception)
            }

        })
    }
}