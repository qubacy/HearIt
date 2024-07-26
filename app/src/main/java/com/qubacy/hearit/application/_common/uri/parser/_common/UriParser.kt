package com.qubacy.hearit.application._common.uri.parser._common

import android.net.Uri

interface UriParser {
  fun parse(uri: String): Uri
}