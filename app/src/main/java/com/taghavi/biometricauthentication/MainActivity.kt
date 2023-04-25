package com.taghavi.biometricauthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricPrompt

class MainActivity : AppCompatActivity(), BiometricAuthListener {
    private lateinit var buttonBiometricLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonBiometricLogin = findViewById(R.id.buttonBiometricsLogin)

        showBiometricLoginOption()
    }

    fun onClickBiometrics(view: View) {
        BiometricUtil.showBiometricPrompt(
            activity = this,
            listener = this,
            cryptoObject = null,
            allowDeviceCredential = true
        )
    }

    override fun onBiometricAuthenticationSuccess(result: BiometricPrompt.AuthenticationResult) {
        Toast.makeText(this, "Biometric Success", Toast.LENGTH_SHORT).show()
    }

    override fun onBiometricAuthenticationError(errorCode: Int, errorMessage: String) {
        Toast.makeText(this, "Biometric Login. Error: $errorMessage", Toast.LENGTH_SHORT).show()
    }

    private fun showBiometricLoginOption() {
        buttonBiometricLogin.visibility =
            if (BiometricUtil.isBiometricReady(this)) View.VISIBLE
            else View.GONE
    }
}