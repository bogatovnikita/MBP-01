package com.entertainment.event.ssearch.presentation.di

import android.content.Context
import android.content.SharedPreferences
import android.opengl.GLSurfaceView
import com.entertainment.event.ssearch.domain.utility.GL_RENDERER
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

@Module
@InstallIn(SingletonComponent::class)
class GLProviderModule {

    @Provides
    @Singleton
    fun provideGLSurfaceView(
        @ApplicationContext context: Context,
        render: GLSurfaceView.Renderer
    ): GLSurfaceView =
        GLSurfaceView(context).apply {
            setEGLConfigChooser(8, 8, 8, 8, 16, 0)
            setRenderer(render)
        }

    @Provides
    @Singleton
    fun provideGLRenderer(prefs: SharedPreferences): GLSurfaceView.Renderer =
        object : GLSurfaceView.Renderer {
            override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
                prefs.edit().putString(GL_RENDERER, gl.glGetString(GL10.GL_RENDERER)).apply()
            }

            override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            }

            override fun onDrawFrame(gl: GL10) {
            }
        }
}