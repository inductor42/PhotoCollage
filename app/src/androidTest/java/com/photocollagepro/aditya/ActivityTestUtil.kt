package com.photocollagepro.aditya

import android.app.Activity
import android.net.Uri
import android.provider.MediaStore
import android.view.Gravity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.matcher.ViewMatchers.*

object ActivityTestUtil {

  fun checkOptionsMenuVisibility(expectedIsVisible: Boolean) {
    val visibility = if (expectedIsVisible) matches(isDisplayed()) else doesNotExist()

    onView(withId(_root_ide_package_.com.photocollagepro.maker2022.R.id.action_share)).check(visibility)
    onView(withId(_root_ide_package_.com.photocollagepro.maker2022.R.id.action_save)).check(visibility)
    onView(withId(_root_ide_package_.com.photocollagepro.maker2022.R.id.action_reset)).check(visibility)
  }



  fun closeDrawer() {
    onView(withId(_root_ide_package_.com.photocollagepro.maker2022.R.id.drawer_layout))
      .perform(DrawerActions.close())
  }

  fun pressOptionsMenuButton(buttonId: Int) {
    onView(withId(buttonId))
      .perform(ViewActions.click())
  }

  fun countImagesOnDevice(activity: Activity): Int {
    var imageCount = 0

    val columns = arrayOf(MediaStore.Images.Media._ID)
    val orderBy = MediaStore.Images.Media._ID

    val cursor = activity.contentResolver.query(
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
      null, null, orderBy)

    if (cursor != null) imageCount = cursor.count

    cursor?.close()

    return imageCount
  }

  fun getRandomImageUri(activity: Activity): Uri {
    val columns = arrayOf(MediaStore.Images.Media._ID)

    val cursor = activity.contentResolver.query(
      MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
      null, null, "RANDOM()")

    cursor?.moveToFirst()

    val uriString = cursor?.getString(cursor.getColumnIndex(columns[0]))

    cursor?.close()

    return Uri.parse(uriString)
  }



  // ---------------- UTIL ----------------

  private fun openDrawer() {
    onView(withId(_root_ide_package_.com.photocollagepro.maker2022.R.id.drawer_layout))
      .check(matches(isClosed(Gravity.LEFT)))
      .perform(DrawerActions.open())
  }



}