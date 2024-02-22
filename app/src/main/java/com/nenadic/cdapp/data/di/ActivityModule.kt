package com.nenadic.cdapp.data.di

import android.content.Context
import android.view.LayoutInflater
import com.nenadic.cdapp.databinding.ActivityAddEditBinding
import com.nenadic.cdapp.databinding.ActivityMainBinding
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    @ActivityScoped
    fun provideMainBinding(@ActivityContext context: Context): ActivityMainBinding {
        return ActivityMainBinding.inflate(LayoutInflater.from(context))
    }

    @Provides
    @ActivityScoped
    fun provideAddEditBinding(@ActivityContext context: Context): ActivityAddEditBinding {
        return ActivityAddEditBinding.inflate(LayoutInflater.from(context))
    }
}