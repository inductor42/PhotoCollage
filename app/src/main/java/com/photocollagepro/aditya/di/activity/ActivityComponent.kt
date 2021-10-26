package com.photocollagepro.aditya.di.activity

import com.photocollagepro.aditya.ui.MainActivity
import dagger.Subcomponent

@Subcomponent
interface ActivityComponent {

  @Subcomponent.Factory
  interface Factory {
    fun create(): ActivityComponent
  }

  fun inject(mainActivity: MainActivity)
}