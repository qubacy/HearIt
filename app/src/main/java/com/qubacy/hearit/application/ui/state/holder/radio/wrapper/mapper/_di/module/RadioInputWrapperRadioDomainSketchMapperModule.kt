package com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper._di.module

import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper._common.RadioInputWrapperRadioDomainSketchMapper
import com.qubacy.hearit.application.ui.state.holder.radio.wrapper.mapper.impl.RadioInputWrapperRadioDomainSketchMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RadioInputWrapperRadioDomainSketchMapperModule {
  @Binds
  fun bindRadioInputWrapperRadioDomainSketchMapper(
    radioInputWrapperRadioDomainSketchMapper: RadioInputWrapperRadioDomainSketchMapperImpl
  ): RadioInputWrapperRadioDomainSketchMapper
}