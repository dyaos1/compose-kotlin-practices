package com.example.lemonade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lemonade.ui.theme.LemonadeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LemonadeTheme {
                LemonadeApp()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LemonadeApp() {
    LemonadeTheme {
        LemonadeImages()
    }
}

@Composable
fun LemonadeImages(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(1)}
    var squeezedNumber by remember { mutableStateOf(0) }
    var squeezeNumber by remember { mutableStateOf(0)}
    val treeImage: Int = when (result) {
        1 -> R.drawable.lemon_tree
        2 -> R.drawable.lemon_squeeze
        3 -> R.drawable.lemon_drink
        else -> R.drawable.lemon_restart
    }
    val bottomString: Int = when (result) {
        1 -> R.string.tree
        2 -> R.string.squeeze
        3 -> R.string.drink
        else -> R.string.restart
    }
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = stringResource(id = bottomString), color= Color.DarkGray)

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = treeImage),
                contentDescription = stringResource(id = bottomString),
                modifier = modifier.clickable {
                    if (result ==1 ) {
                        squeezeNumber = (1..6).random()
                        result++
                    } else if (result == 2) {
                        if (squeezedNumber > squeezeNumber) {
                            result ++
                            squeezedNumber = 0
                        } else {
                            squeezedNumber++
                        }
                    } else if (result == 4) {
                        result = 1
                    } else {
                        result++
                    }
                }
            )

        }
    }


}

