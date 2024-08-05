package com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.mapper.impl

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import com.qubacy.hearit.application.data.player.repository._common.source.local._common.model.PlayerInfoDataStoreModel
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.eq

class PlayerInfoDataStorePreferenceMapperImplTest {
  private val _instance = PlayerInfoDataStorePreferenceMapperImpl()

  @Test
  fun mapPreferencesToPlayerInfoDataStoreModelTest() {
    data class TestCase(
      val preferencesCurRadioId: Long?,
      val preferencesIsPlaying: Boolean?,
      val expectedPlayerInfoDataStoreModel: PlayerInfoDataStoreModel
    )

    val testCases = listOf(
      TestCase(
        null,
        null,
        PlayerInfoDataStoreModel(
          curRadioId = PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID,
          isPlaying = false
        )
      ),
      TestCase(
        0L,
        null,
        PlayerInfoDataStoreModel(
          curRadioId = 0L,
          isPlaying = false
        )
      ),
      TestCase(
        null,
        true,
        PlayerInfoDataStoreModel(
          curRadioId = PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID,
          isPlaying = true
        )
      ),
      TestCase(
        0L,
        true,
        PlayerInfoDataStoreModel(
          curRadioId = 0L,
          isPlaying = true
        )
      ),
    )

    for (testCase in testCases) {
      val preferencesMock = Mockito.mock(Preferences::class.java)

      Mockito.`when`(preferencesMock.get(eq(PlayerInfoDataStoreModel.CUR_RADIO_ID_PREFERENCE_KEY)))
        .thenReturn(testCase.preferencesCurRadioId)
      Mockito.`when`(preferencesMock.get(eq(PlayerInfoDataStoreModel.IS_PLAYING_PREFERENCE_KEY)))
        .thenReturn(testCase.preferencesIsPlaying)

      val gottenPlayerInfoDataStoreModel = _instance.map(preferencesMock)

      Assert.assertEquals(testCase.expectedPlayerInfoDataStoreModel, gottenPlayerInfoDataStoreModel)
    }
  }

  @Deprecated("Doesn't pass 'coz MutablePreferences is a final class.")
  @Test
  fun mapPlayerInfoDataStoreModelToPreferencesTest() {
    data class TestCase(
      val playerInfoDataStoreModel: PlayerInfoDataStoreModel
    )

    val testCases = listOf(
      TestCase(
        PlayerInfoDataStoreModel(
          curRadioId = PlayerInfoDataStoreModel.UNKNOWN_RADIO_ID,
          isPlaying = false
        )
      )
    )

    for (testCase in testCases) {
      val preferencesMock = Mockito.mock(MutablePreferences::class.java)

      var gottenCurRadioId: Long? = null
      var gottenIsPlaying: Boolean? = null

      Mockito.`when`(preferencesMock.set(
        eq(PlayerInfoDataStoreModel.CUR_RADIO_ID_PREFERENCE_KEY),
        Mockito.anyLong()
      )).thenAnswer {
        gottenCurRadioId = it.arguments[1] as Long

        Unit
      }
      Mockito.`when`(preferencesMock.set(
        eq(PlayerInfoDataStoreModel.IS_PLAYING_PREFERENCE_KEY),
        Mockito.anyBoolean()
      )).thenAnswer {
        gottenIsPlaying = it.arguments[1] as Boolean

        Unit
      }

      Assert.assertEquals(testCase.playerInfoDataStoreModel.curRadioId, gottenCurRadioId)
      Assert.assertEquals(testCase.playerInfoDataStoreModel.isPlaying, gottenIsPlaying)
    }
  }
}