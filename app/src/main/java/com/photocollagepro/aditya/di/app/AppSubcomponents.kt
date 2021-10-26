package com.photocollagepro.aditya.di.app

import com.photocollagepro.aditya.di.activity.ActivityComponent
import com.photocollagepro.aditya.di.editor.EditorComponent
import dagger.Module

@Module(subcomponents = [ActivityComponent::class, EditorComponent::class])
interface AppSubcomponents