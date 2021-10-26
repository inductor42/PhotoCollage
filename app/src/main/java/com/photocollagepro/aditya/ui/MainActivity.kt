package com.photocollagepro.aditya.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.photocollagepro.collageview.util.ImageUtil
import com.photocollagepro.collageview.views.AbstractCollageView
import com.photocollagepro.aditya.BaseApplication
import com.photocollagepro.aditya.R
import com.photocollagepro.aditya.ui.editor.EditorFragment
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import javax.inject.Inject

class MainActivity :
  AppCompatActivity(),
  ImageUtil.ImageSavedListener,
  NavController.OnDestinationChangedListener {

  // -------------------- PROPERTIES --------------------

  private lateinit var appBarConfig: AppBarConfiguration
  private var menu: Menu? = null
  private var collageContainer: FrameLayout? = null
  private var progressBarContainer: FrameLayout? = null

  @Inject lateinit var imageUtil: ImageUtil

  private var savedImageUri: Uri? = null

  lateinit var mAdView : AdView
  private var mInterstitialAd: InterstitialAd? = null


  // -------------------- INIT --------------------

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    MobileAds.initialize(this) {}
    mAdView = findViewById(R.id.adView)
    val adRequest = AdRequest.Builder().build()
    mAdView.loadAd(adRequest)

    InterstitialAd.load(
      this,
      "ca-app-pub-3389769914405265/8700574024",
      adRequest,
      object : InterstitialAdLoadCallback() {
        override fun onAdFailedToLoad(adError: LoadAdError) {
          Log.d(TAG, adError?.message)
          mInterstitialAd = null
        }

        override fun onAdLoaded(interstitialAd: InterstitialAd) {
          Log.d(TAG, "Ad was loaded.")
          mInterstitialAd = interstitialAd
        }
      })

    (application as BaseApplication).getAppComponent().activityComponent().create().inject(this)

    initUi()

    imageUtil.printDispatchers()
  }
private fun showAD(){
  if (mInterstitialAd != null) {
    mInterstitialAd?.show(this)
  } else {
    Log.d("TAG", "The interstitial ad wasn't ready yet.")
  }
}
  private fun loadAD(){
      val adRequest = AdRequest.Builder().build()
      InterstitialAd.load(this, "ca-app-pub-3389769914405265/8700574024", adRequest,
        object : InterstitialAdLoadCallback() {
          override fun onAdLoaded(interstitialAd: InterstitialAd) {
            // The mInterstitialAd reference will be null until
            // an ad is loaded.
            mInterstitialAd = interstitialAd
            Log.i("TAG", "onAdLoaded")
          }

          override fun onAdFailedToLoad(loadAdError: LoadAdError) {
            // Handle the error
            Log.i("TAG", loadAdError.message)
            mInterstitialAd = null
          }
        })

  }
  private fun initUi() {
    setSupportActionBar(findViewById(R.id.editor_toolbar))
  }





  // -------------------- NAVIGATION --------------------

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    this.menu = menu
    menuInflater.inflate(R.menu.main_options_menu, menu)
    return super.onCreateOptionsMenu(menu)
  }

  override fun onDestinationChanged(
    controller: NavController,
    dest: NavDestination, args: Bundle?
  ) {

  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (getCurrentFragment() !is EditorFragment) {
      Log.d(TAG, "onOptionsItemSelected: current fragment is not the editor!")
      return super.onOptionsItemSelected(item)
    }

    when (item.itemId) {
      R.id.action_share -> shareImage()
      R.id.action_save -> saveCollageToGallery()
      R.id.action_reset -> resetImage()
      else -> return false
    }
    return true
  }

  override fun onSupportNavigateUp(): Boolean {
    val navController = findNavController(R.id.nav_host_fragment)
    return navController.navigateUp(appBarConfig) || super.onSupportNavigateUp()
  }



  // -------------------- SHARE COLLAGE --------------------

  private fun shareImage() {
    showAD()
    prepareToCaptureCollage()
    imageUtil.prepareViewForSharing(this, collageContainer!!, this)
    loadAD()
  }

  override fun onReadyToShareImage(uri: Uri?) {
    setProgressBarVisibility(View.GONE)

    if (uri == null) toastErrorAndLog("Unable to share.", "Unable to share!")
    else {
      savedImageUri = uri
      startShareIntent(uri)
    }
  }

  private fun startShareIntent(uri: Uri?) {
    val shareIntent: Intent = Intent().apply {
      action = Intent.ACTION_SEND
      putExtra(Intent.EXTRA_STREAM, uri)
      addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
      type = "image/jpeg"
    }
    startActivity(Intent.createChooser(shareIntent, "Share to..."))
  }



  // -------------------- SAVE COLLAGE --------------------

  private fun saveCollageToGallery() {
    showAD()
    prepareToCaptureCollage()
    imageUtil.saveViewToGallery(this, collageContainer!!, this)
    loadAD()

  }

  override fun onCollageSavedToGallery(isSaveSuccessful: Boolean, uri: Uri?) {
    setProgressBarVisibility(View.GONE)

    savedImageUri = uri

    if (isSaveSuccessful) {
      Toast.makeText(this, "Saved to gallery!", Toast.LENGTH_LONG).show()
    }
    else toastErrorAndLog("Write to gallery failed.", "Unable to save :(")
  }



  // -------------------- UTIL --------------------

  private fun resetImage() {
    showAD()
    if (collageContainer == null) collageContainer = findViewById(R.id.collage_container)
    (collageContainer!!.getChildAt(0) as AbstractCollageView).reset()
    (getCurrentFragment() as EditorFragment).reset()
    loadAD()
  }

  private fun getCurrentFragment(): Fragment {
    val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
    return navHostFragment!!.childFragmentManager.fragments[0]
  }

  private fun prepareToCaptureCollage() {
    savedImageUri = null
    setProgressBarVisibility(View.VISIBLE)
    if (collageContainer == null) collageContainer = findViewById(R.id.collage_container)
  }

  private fun setProgressBarVisibility(visibility: Int) {
    if (progressBarContainer == null) {
      progressBarContainer = findViewById(R.id.progress_bar_container)
    }

    progressBarContainer?.visibility = visibility
  }

  private fun toastErrorAndLog(logMsg: String, toastMsg: String = "Something went wrong :(") {
    Log.d(TAG, "toastErrorAndLog: $logMsg")
    Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show()
  }

  companion object {
    private const val TAG = "MainActivity"
  }
}