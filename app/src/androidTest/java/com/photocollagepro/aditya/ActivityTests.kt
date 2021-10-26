package com.photocollagepro.aditya

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.photocollagepro.aditya.ActivityTestUtil.checkOptionsMenuVisibility
import com.photocollagepro.aditya.ActivityTestUtil.closeDrawer
import com.photocollagepro.aditya.ActivityTestUtil.openDrawerAndNavToEditorFragment
import com.photocollagepro.aditya.ActivityTestUtil.openDrawerAndNavToMoreAppsFragment
import com.photocollagepro.maker2022.ui.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivityTests {

  @Rule
  @JvmField
  val asr = ActivityScenarioRule<MainActivity>(MainActivity::class.java)

  @Test
  fun should_showOptionsMenuInEditorFragment_when_returningFromMoreAppsFragment() {
    openDrawerAndNavToMoreAppsFragment()
    closeDrawer()
    openDrawerAndNavToEditorFragment()
    checkOptionsMenuVisibility(true)
  }

  @Test
  fun should_hideOptionsMenu_when_inMoreAppsFragment() {
    openDrawerAndNavToMoreAppsFragment()
    checkOptionsMenuVisibility(false)
  }
}