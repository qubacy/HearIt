package com.qubacy.hearit.application._common.uri.parser.impl

import android.net.Uri
import com.qubacy.hearit.application._common.uri.parser._common.UriParser
import javax.inject.Inject

class UriParserImpl @Inject constructor() : UriParser {
  override fun parse(uri: String): Uri {
    return Uri.parse(uri)
  }
}