package com.qubacy.hearit.application.ui.visual.controller.activity.main

import android.app.Instrumentation
import android.content.Intent
import android.content.IntentFilter
import android.provider.MediaStore
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.qubacy.hearit.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

@HiltAndroidTest
class MainActivityTest {
  private val _hiltRule = HiltAndroidRule(this)
  private val _composeRule = createAndroidComposeRule<MainActivity>()

  @get:Rule
  val rules: RuleChain = RuleChain
    .outerRule(_hiltRule)
    .around(_composeRule)

  @Before
  fun setup() {

  }

  @Test
  fun homeScreenPresentedOnStartTest() {
    val homeTopAppBarDescription = _composeRule.activity
      .getString(R.string.home_screen_top_app_bar_description)

    _composeRule.onNode(hasContentDescription(homeTopAppBarDescription)).assertExists()
  }

  @Deprecated("Doesn't work. Is there a way to test it correctly?")
  @Test
  fun pickImageTest() {
    val imagePickerActivityBrowsers = listOf(
      Instrumentation.ActivityMonitor(
        IntentFilter(MediaStore.ACTION_PICK_IMAGES), null, false
      ),
      Instrumentation.ActivityMonitor(
        IntentFilter(Intent.ACTION_OPEN_DOCUMENT), null, false
      )
    )

    imagePickerActivityBrowsers.forEach {
      InstrumentationRegistry.getInstrumentation().addMonitor(it)
    }

    _composeRule.runOnUiThread {
      _composeRule.activity.pickImage {  }
    }

    for (imagePickerActivityBrowser in imagePickerActivityBrowsers) {
      val imagePickerActivity = imagePickerActivityBrowser.waitForActivityWithTimeout(1000)

      if (imagePickerActivity != null) return
    }

    throw IllegalStateException()
  }
}