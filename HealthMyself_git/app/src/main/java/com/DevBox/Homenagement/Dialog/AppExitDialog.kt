package com.DevBox.Homenagement.Dialog;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.DevBox.Homenagement.R
import kotlinx.android.synthetic.main.appexit_dialog.*

class AppExitDialog : DialogFragment() {

    interface AppExitDialogInterface{
        fun exit()
        fun cancelExit()
    }

    private var appExitDialogInterface: AppExitDialogInterface? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.appexit_dialog, container, false)
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListener()
    }

    fun AddAppExitDialogInterface(listener: AppExitDialogInterface){
        appExitDialogInterface = listener
    }

    private fun setupListener(){
        exit_no.setOnClickListener{
            appExitDialogInterface?.cancelExit()
            dismiss()
        }
        exit_yes.setOnClickListener{
            appExitDialogInterface?.exit()
            dismiss()
        }
    }
}