package com.qubacy.hearit.application.ui.state.holder.radio.validator._di.module

import android.webkit.URLUtil
import com.qubacy.hearit.application.ui.state.holder.radio.validator._common.RadioInputWrapperValidator
import com.qubacy.hearit.application.ui.state.holder.radio.validator.impl.RadioInputWrapperValidatorImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
object RadioInputWrapperValidatorModule {
  @Provides
  fun provideRadioInputWrapperValidator(): RadioInputWrapperValidator {
    return RadioInputWrapperValidatorImpl { URLUtil.isValidUrl(it) }
  }
}