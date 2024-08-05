package com.qubacy.hearit.application.ui.state.state.home.player

import android.os.Parcel
import android.os.Parcelable
import com.qubacy.hearit.application.ui._common.presentation.RadioPresentation

data class PlayerStatePreservable(
  val currentRadioId: Long? = null,
  val isRadioPlaying: Boolean = false
) : Parcelable {
  constructor(parcel: Parcel) : this(
    parcel.readValue(Long::class.java.classLoader) as? Long,
    parcel.readByte() != 0.toByte()
  ) {
  }

  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeValue(currentRadioId)
    parcel.writeByte(if (isRadioPlaying) 1 else 0)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Parcelable.Creator<PlayerStatePreservable> {
    override fun createFromParcel(parcel: Parcel): PlayerStatePreservable {
      return PlayerStatePreservable(parcel)
    }

    override fun newArray(size: Int): Array<PlayerStatePreservable?> {
      return arrayOfNulls(size)
    }
  }
}

data class PlayerState(
  val currentRadio: RadioPresentation? = null,
  val isRadioPlaying: Boolean = false
) {
  fun toPlayerStatePreservable(): PlayerStatePreservable {
    return PlayerStatePreservable(currentRadio?.id, isRadioPlaying)
  }
}