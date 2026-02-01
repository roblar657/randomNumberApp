package com.example.randomNumberApp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.randomNumberApp.ui.theme.Oving2Theme


class MainActivity : ComponentActivity() {


    //Del av UI rammeverket til jetpack compose
    private var randomvalueActivityResult by mutableStateOf("?")
    private var randomValueInfo by mutableStateOf("Tilfeldig tall")
    private var tempRequestCode: Int = -1

    private val REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_100 = 1
    private val REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_50  = 2

    /**
     * Brukt for å tilpasse krav om bruk av
     * onActivityResult i Main, med at en ikke
     * skal bruke deprecated startActivityForResult.
     */
    private fun runActivityForResult(intent: Intent, requestCode: Int) {
        tempRequestCode = requestCode
        activityForResultStarter.launch(intent)
    }

    /**
     * Bytter ut vanlige metoder, med tilsvarende gjennom Activity Result API (å gjøre direkte er deprecated)
     * @see https://developer.android.com/training/basics/intents/result
     * @see https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts
     * @see https://developer.android.com/reference/androidx/activity/result/contract/ActivityResultContracts.StartActivityForResult
     *
     */
    private val activityForResultStarter = registerForActivityResult(
        //Tilsvarende som før, men gjennom Activity result API
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Request kode gir info om hvor en kommer fra
        // Result kode gir informasjon om hvordan resultatet skal behandles
        // Siste argument er resultatet fra aktiviteten som er kjørt
        onActivityResult(tempRequestCode, result.resultCode, result.data)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Oving2Theme {
                RandomNumberComposable()
            }
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            Log.i("onResult", "resultCode = RESULT_OK")
            when (requestCode) {
                REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_100 -> {
                    Log.i("onResult", "requestCode = REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_100")
                    data?.let {
                        val randomNumber = it.getIntExtra("RANDOM_VALUE", -1)
                        randomValueInfo = "Tilfeldig tall (0-100)"
                        randomvalueActivityResult = "$randomNumber"
                        Log.i("onResult", "Tilfeldig tall = $randomNumber")
                    }
                }
                REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_50 -> {
                    Log.i("onResult", "requestCode = REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_50")
                    data?.let {
                        val randomNumber = it.getIntExtra("RANDOM_VALUE", -1)

                        randomValueInfo = "Tilfeldig tall    (0-50)"
                        randomvalueActivityResult = "$randomNumber"
                        Log.i("onResult", "Tilfeldig tall = $randomNumber")
                    }
                }
            }
        }
    }
    @Preview
    @Composable
    private fun RandomNumberComposable() {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = randomValueInfo,
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp),
                color    =  Color(0xFF3B5998),
                fontWeight = FontWeight.Bold,
                style = TextStyle(
                    lineHeight = 60.sp
                )

            )
            Text(
                text = randomvalueActivityResult,
                fontSize = 48.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 32.dp),
                style = TextStyle(
                    lineHeight = 50.sp
                )
            )

            Button(
                onClick = {

                    val intent = Intent("RandomvalueActivity")
                    runActivityForResult(intent, REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_100)


                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Generer tilfeldig tall (0-100)")
            }

            // Test med egendefinert grense (50)
            Button(
                onClick = {

                    val intent = Intent("RandomvalueActivity")
                    intent.putExtra("UPPER_LIMIT", 50)
                    runActivityForResult(intent, REQ_CODE_MAKE_RANDOM_UPPER_LIMIT_50)


                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Color.White,
                    containerColor = Color.DarkGray
                ),
                shape = RoundedCornerShape(0.dp)
            ) {
                Text("Generer tilfeldig tall (0-50)")
            }
        }
    }
}