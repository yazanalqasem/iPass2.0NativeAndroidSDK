package com.app.ipassplus.ui.dashboard.fragment

import ScenariosListAdapter
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.ipassplus.MainActivity
import com.app.ipassplus.R
import com.app.ipassplus.ui.dashboard.model.ScenariosItemModel
import com.app.ipassplus.databinding.FragmentDashboardBinding
import com.sdk.ipassplussdk.apis.ResultListener
import com.sdk.ipassplussdk.core.iPassSDKManger
import com.sdk.ipassplussdk.model.response.transaction_details.TransactionDetailResponse

class DashboardFragment : Fragment(), ScenariosListAdapter.OnClickListener {

    private val binding by lazy { FragmentDashboardBinding.inflate(layoutInflater) }
    private lateinit var adapter: ScenariosListAdapter
  //  private val email = "ipassmobsdk@yopmail.com"
    private val email = "ipassmoband@yopmail.com"
    private val password = "Admin@123#"
  //  private val token = "eyJhbGciOiJIUzI1NiJ9.aXBhc3Ntb2JzZGtAeW9wbWFpbC5jb21pcGFzcyBpcGFzcyAgIDcxNWFkYTI4LWFmODEtNGM5MC1iY2IyLTJmZjc1Mjg1YzhkYg.OfsTPtj41geOVQ9riQdTpCVEgWqfoqfpva93xez2xJk"
    private val apptoken = "eyJhbGciOiJIUzI1NiJ9.aXBhc3Ntb2JhbmRAeW9wbWFpbC5jb21tb2JpbGUgdGVhbSAgIGJiNDY0MGNhLTUwNDQtNGRlMy05MjVlLWI4YmU0NzFiZTZkNA.qG6NtAPJWFgyS30lqHe4LZSu0X-97XnWlvZqQ22HoeM"
    val phoneNumber = "7894563210"
    val flowId = "10015"
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
        iPassSDKManger.startScanningProcess(requireContext(),email, MainActivity.userToken, apptoken, socialMediaEmail , phoneNumber, flowId ,binding.root as ViewGroup) {
            status, message ->
            if (status) {
                Log.e("startScanningProcess", message)
                getDocData()
            } else {
                Log.e("startScanningProcess", message)
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
                }
            }

            override fun onError(exception: String) {
                Log.e("onSuccess", exception)
            }

        })
    }
}