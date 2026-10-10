package com.dev.a24012011080_mad_pr5

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Pink = Color(0xFFDA3D93)

@Composable
fun GuniTheme(content: @Composable () -> Unit) {
    val scheme = if (isSystemInDarkTheme()) darkColorScheme(
        background = Color(0xFF0F121C), surfaceVariant = Color(0xFF454856),
        onSurfaceVariant = Color(0xFFE0E2EF), primary = Color(0xFFA9C1FF), onPrimary = Color(0xFF002A78),
    ) else lightColorScheme(
        background = Color(0xFFFAF8FF), surfaceVariant = Color(0xFFDFE2F3),
        onSurfaceVariant = Color(0xFF2F3140), primary = Color(0xFF0058CB), onPrimary = Color.White,
    )
    MaterialTheme(colorScheme = scheme, content = content)
}

@Composable
private fun AuthScaffold(
    footerText: String, footerAction: String, onFooter: () -> Unit,
    card: @Composable ColumnScope.() -> Unit,
) {
    Column(
        Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
            .systemBarsPadding().imePadding().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painterResource(R.drawable.guni_pink), contentDescription = "GUNI",
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 8.dp),
            contentScale = ContentScale.FillWidth,
        )
        Column(
            Modifier.fillMaxWidth().padding(horizontal = 2.dp)
                .shadow(6.dp, RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                .padding(20.dp),
            content = card,
        )
        Spacer(Modifier.height(48.dp))
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
            Text(footerText, color = MaterialTheme.colorScheme.onBackground, fontSize = 17.sp)
            Spacer(Modifier.width(16.dp))
            Text(footerAction, color = Pink, fontWeight = FontWeight.Bold, fontSize = 20.sp,
                modifier = Modifier.clickable(onClick = onFooter))
        }
    }
}

@Composable
private fun FormRow(
    label: String, value: String, onChange: (String) -> Unit, error: String?,
    keyboard: KeyboardType = KeyboardType.Text, password: Boolean = false,
) {
    Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.Top) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 20.sp,
            modifier = Modifier.weight(0.38f).padding(top = 14.dp))
        OutlinedTextField(
            value = value, onValueChange = onChange, singleLine = true,
            placeholder = { Text("Enter $label") },
            isError = error != null,
            supportingText = error?.let { { Text(it) } },
            keyboardOptions = KeyboardOptions(keyboardType = if (password) KeyboardType.Password else keyboard),
            visualTransformation = if (password) PasswordVisualTransformation() else VisualTransformation.None,
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.weight(0.62f),
        )
    }
}

@Composable
private fun PrimaryButton(text: String, onClick: () -> Unit) = Button(
    onClick = onClick, modifier = Modifier.fillMaxWidth().height(56.dp),
) { Text(text, fontSize = 20.sp, fontWeight = FontWeight.Bold) }

private fun emailError(e: String) =
    if (e.isBlank()) "Email is required"
    else if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) "Invalid email" else null

@Composable
fun LoginScreen(onSignUp: () -> Unit) {
    val ctx = LocalContext.current
    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }
    val eErr = if (submitted) emailError(email) else null
    val pErr = if (submitted && pass.isEmpty()) "Password is required" else null

    AuthScaffold("Don't have an account?", "SIGN UP", onSignUp) {
        FormRow("Email", email, { email = it }, eErr, KeyboardType.Email)
        FormRow("Password", pass, { pass = it }, pErr, password = true)
        Text("Forgot Password?", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 20.sp,
            modifier = Modifier.align(Alignment.End).clickable {
                Toast.makeText(ctx, "Password reset link sent (demo)", Toast.LENGTH_SHORT).show()
            })
        Spacer(Modifier.height(48.dp))
        PrimaryButton("Login") {
            submitted = true
            if (emailError(email) == null && pass.isNotEmpty())
                Toast.makeText(ctx, "Login successful", Toast.LENGTH_SHORT).show()
        }
    }
}

@Composable
fun RegisterScreen(onLogin: () -> Unit) {
    val ctx = LocalContext.current
    var name by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var city by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var pass by rememberSaveable { mutableStateOf("") }
    var confirm by rememberSaveable { mutableStateOf("") }
    var submitted by rememberSaveable { mutableStateOf(false) }

    val nameBad = name.isBlank()
    val phoneBad = !phone.matches(Regex("\\d{10}"))
    val cityBad = city.isBlank()
    val emailMsg = emailError(email)
    val passBad = pass.length < 6
    val confirmBad = confirm != pass

    AuthScaffold("Do you have an account?", "LOGIN", onLogin) {
        FormRow("Name", name, { name = it }, "Name is required".takeIf { submitted && nameBad })
        FormRow("Phone Number", phone, { phone = it }, "Enter 10 digit number".takeIf { submitted && phoneBad }, KeyboardType.Phone)
        FormRow("City", city, { city = it }, "City is required".takeIf { submitted && cityBad })
        FormRow("Email", email, { email = it }, emailMsg.takeIf { submitted }, KeyboardType.Email)
        FormRow("Password", pass, { pass = it }, "Min 6 characters".takeIf { submitted && passBad }, password = true)
        FormRow("Confirm Password", confirm, { confirm = it }, "Passwords don't match".takeIf { submitted && confirmBad }, password = true)
        Spacer(Modifier.height(32.dp))
        PrimaryButton("Register") {
            submitted = true
            if (!(nameBad || phoneBad || cityBad || emailMsg != null || passBad || confirmBad)) {
                Toast.makeText(ctx, "Registered successfully", Toast.LENGTH_SHORT).show()
                onLogin()
            }
        }
    }
}