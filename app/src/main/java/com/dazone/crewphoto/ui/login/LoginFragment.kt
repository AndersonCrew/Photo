package com.dazone.crewphoto.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dazone.crewphoto.R
import com.dazone.crewphoto.base.BaseFragment
import com.dazone.crewphoto.base.DazoneApplication
import com.dazone.crewphoto.databinding.FragmentLoginBinding
import com.dazone.crewphoto.dialog.DialogUtil
import com.dazone.crewphoto.event.Event
import com.dazone.crewphoto.utils.Constants
import com.dazone.crewphoto.utils.TimeUtils
import com.dazone.crewphoto.utils.Utils
import com.google.gson.JsonObject
import java.util.*

class LoginFragment : BaseFragment() {
    private var binding: FragmentLoginBinding?= null
    private val viewModel: LoginViewModel by viewModels ()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun initEvents() {
        binding?.btnLogin?.setOnClickListener {
            if(checkValidation().isNullOrEmpty()) {
                showProgress()
                Utils.setServerSite(binding?.etDomain?.text.toString(), binding?.etUserId?.text.toString(), binding?.etPassword?.text.toString())
                checkSSL()
            } else {
                DialogUtil.getDialogUtil(requireContext()).createMessageDialog(resources.getString(R.string.message), checkValidation()).show()
            }
        }

        binding?.etDomain?.setText(DazoneApplication.getInstance().mPref?.getString(Constants.DOMAIN, ""))
        binding?.etUserId?.setText(DazoneApplication.getInstance().mPref?.getString(Constants.USER_ID, ""))
        binding?.etPassword?.setText(DazoneApplication.getInstance().mPref?.getString(Constants.PASSWORD, ""))
    }

    private fun checkSSL() {
        val params = JsonObject()
        params.addProperty("Domain", Locale.getDefault().language)
        params.addProperty("Applications", "CrewResource")

        viewModel.checkSSL(params)
    }

    private fun postLogin() {
        val params = JsonObject()
        params.addProperty("languageCode", Locale.getDefault().language)
        params.addProperty("timeZoneOffset", TimeUtils.getTimezoneOffsetInMinutes().toString())
        params.addProperty("companyDomain", DazoneApplication.getInstance().mPref?.getString(
            Constants.COMPANY_NAME, "") ?: "")
        params.addProperty("password", binding?.etPassword?.text.toString())
        params.addProperty("userID", binding?.etUserId?.text.toString())
        params.addProperty("mobileOSVersion", "Android" + android.os.Build.VERSION.RELEASE)

        viewModel.login(params)
    }

    override fun initViewModels() {
        viewModel.hasSSL.observe(this, Observer {
            it?.let {
                viewModel.hasSSL.value = null
                postLogin()
            }
        })

        viewModel.loginSuccess.observe(this, Observer {
            if (it != null && it) {
                hideProgress()
                Event.goToMain()
            }
        })

        viewModel.getUserLiveData()?.observe(this, Observer {
            it?.let {

            }
        })

        viewModel.errorMessage.observe(this, Observer {
            it?.let {
                DialogUtil.getDialogUtil(requireContext()).createMessageDialog(resources.getString(R.string.message), it).show()
                viewModel.errorMessage.value = null
                hideProgress()
            }
        })
    }

    private fun checkValidation(): String {
        var error = ""

        when {
            binding?.etDomain?.text.isNullOrEmpty() -> {
                error = resources.getString(R.string.required_domain)
            }

            binding?.etUserId?.text.isNullOrEmpty() -> {
                error = resources.getString(R.string.required_userId)
            }

            binding?.etPassword?.text.isNullOrEmpty() -> {
                error = resources.getString(R.string.required_password)
            }
        }

        return error
    }
}