package com.taghavi.biometricauthentication

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object BiometricUtil {

    fun hasBiometricCapability(context: Context): Int {
        val biometricManager = BiometricManager.from(context)
        return biometricManager.canAuthenticate()
    }

    fun isBiometricReady(context: Context) =
        hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS

    fun setBiometricPromptInfo(
        title: String,
        subtitle: String,
        description: String,
        allowDeviceCredential: Boolean,
    ): BiometricPrompt.PromptInfo {
        val builder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setDescription(description)

        builder.apply {
            if (allowDeviceCredential) setDeviceCredentialAllowed(true)
            else setNegativeButtonText("Cancel")
        }

        return builder.build()
    }

    fun initBiometricPrompt(
        activity: AppCompatActivity,
        listener: BiometricAuthListener,
    ): BiometricPrompt {
        val executor = ContextCompat.getMainExecutor(activity)

        val callBack = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                listener.onBiometricAuthenticationError(errorCode, errString.toString())
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.w(this.javaClass.simpleName, "Authentication failed for an unknown reason")
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                listener.onBiometricAuthenticationSuccess(result)
            }
        }

        return BiometricPrompt(activity, executor, callBack)
    }

    fun showBiometricPrompt(
        title: String = "Biometric Authentication",
        subtitle: String = "Enter biometric credentials to proceed.",
        description: String = "Input your Fingerprint or FaceID to ensure it's you!",
        activity: AppCompatActivity,
        listener: BiometricAuthListener,
        cryptoObject: BiometricPrompt.CryptoObject? = null,
        allowDeviceCredential: Boolean = false
    ) {
        val promptInfo = setBiometricPromptInfo(
            title,
            subtitle,
            description,
            allowDeviceCredential
        )

        val biometricPrompt = initBiometricPrompt(activity, listener)

        biometricPrompt.apply {
            if (cryptoObject == null) authenticate(promptInfo)
            else authenticate(promptInfo, cryptoObject)
        }
    }

    fun lunchBiometricSettings(context: Context) {
        ActivityCompat.startActivity(
            context,
            Intent(android.provider.Settings.ACTION_SETTINGS),
            null
        )
    }
}