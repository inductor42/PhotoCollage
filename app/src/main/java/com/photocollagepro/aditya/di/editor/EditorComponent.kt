package com.photocollagepro.aditya.di.editor

import com.photocollagepro.aditya.ui.editor.EditorFragment
import dagger.Subcomponent

@Subcomponent
interface EditorComponent {

  @Subcomponent.Factory
  interface Factory {
    fun create(): EditorComponent
  }

  fun inject(fragment: EditorFragment)
}