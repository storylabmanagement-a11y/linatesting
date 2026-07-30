package com.explorefaraya.app.ui.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.explorefaraya.app.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

/**
 * Shown only once a real Web Client ID is wired in (see strings.xml google_web_client_id) —
 * until then this renders nothing, since Google Sign-In can't work with a placeholder ID.
 */
@Composable
fun GoogleSignInButton(onIdToken: (String) -> Unit, onError: (String) -> Unit) {
    val context = LocalContext.current
    val webClientId = context.getString(R.string.google_web_client_id)
    if (webClientId == "REPLACE_WITH_REAL_WEB_CLIENT_ID") return

    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }
    val client = remember { GoogleSignIn.getClient(context, gso) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(com.google.android.gms.common.api.ApiException::class.java)
            val idToken = account.idToken
            if (idToken != null) onIdToken(idToken) else onError("No Google ID token returned.")
        } catch (e: Exception) {
            onError(e.message ?: "Google sign-in was cancelled.")
        }
    }

    OutlinedButton(
        onClick = { launcher.launch(client.signInIntent) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Continue with Google")
    }
}
