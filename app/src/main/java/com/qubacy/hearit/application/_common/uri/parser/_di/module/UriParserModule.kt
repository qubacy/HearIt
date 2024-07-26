package com.qubacy.hearit.application._common.uri.parser._di.module

import com.qubacy.hearit.application._common.uri.parser._common.UriParser
import com.qubacy.hearit.application._common.uri.parser.impl.UriParserImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface UriParserModule {
  @Binds
  fun bindUriParser(uriParser: UriParserImpl): UriParser
}