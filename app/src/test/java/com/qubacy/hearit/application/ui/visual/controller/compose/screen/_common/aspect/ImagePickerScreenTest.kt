package com.qubacy.hearit.application.ui.visual.controller.compose.screen._common.aspect

import android.app.Activity
import com.qubacy.hearit.application.ui.visual.controller.activity._common.aspect.ImagePickerActivity
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

class ImagePickerScreenTest {
  private abstract class ImagePickerActivityMock : Activity(), ImagePickerActivity { }

  @Test
  fun pickImageTest() {
    val imagePickerActivityMock = Mockito.mock(ImagePickerActivityMock::class.java)

    ImagePickerScreen.pickImage(imagePickerActivityMock) {  }

    Mockito.verify(imagePickerActivityMock).pickImage(any())
  }
}