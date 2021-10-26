package com.photocollagepro.aditya.di.app

import android.app.Application
import com.photocollagepro.aditya.di.activity.ActivityComponent
import com.photocollagepro.aditya.di.editor.EditorComponent
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [GlideModule::class, AppSubcomponents::class, ImageUtilModule::class])
interface AppComponent {

  @Component.Builder
  interface Builder {
    fun setMainDispatcher(
      @BindsInstance @Named("thread_main") dispatcher: CoroutineDispatcher): Builder

    fun setDefaultDispatcher(
      @BindsInstance @Named("thread_default") dispatcher: CoroutineDispatcher): Builder

    fun setIoDispatcher(
      @BindsInstance @Named("thread_io") dispatcher: CoroutineDispatcher): Builder

    fun setApplication(
      @BindsInstance application: Application): Builder

    fun build(): AppComponent
  }

  // Types that can be retrieved from the graph
  fun activityComponent(): ActivityComponent.Factory
  fun editorComponent(): EditorComponent.Factory
}