package com.revolut

import android.view.View
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers

object IdlingResourceViewActions {
    inline fun <reified T : View> fromViewPredicate(crossinline isIdleViewProvider: T.() -> Boolean): ViewAction {
        return object : ViewAction {
            override fun getDescription() = T::class.java.name

            override fun getConstraints() = ViewMatchers.isAssignableFrom(T::class.java)

            override fun perform(uiController: UiController?, view: View?) {
                val castView = view as T

                IdlingRegistry.getInstance().register(object : IdlingResource {

                    var resourceCallback: IdlingResource.ResourceCallback? = null

                    override fun getName() = "${T::class.java.name} IdlingResource"

                    override fun isIdleNow(): Boolean {
                        val isIdle = isIdleViewProvider(castView)
                        if (isIdle) {
                            resourceCallback?.onTransitionToIdle()
                            IdlingRegistry.getInstance().unregister(this)
                        }
                        return isIdle
                    }

                    override fun registerIdleTransitionCallback(
                        callback: IdlingResource.ResourceCallback?
                    ) {
                        resourceCallback = callback
                    }
                })
            }
        }
    }

    inline fun <reified T : View> fromCustomViewHandler(
        crossinline handlerFuc: (view: T, onIdle: () -> Unit) -> Unit
    ): ViewAction {
        return object : ViewAction {
            override fun getDescription() = T::class.java.name

            override fun getConstraints() = ViewMatchers.isAssignableFrom(T::class.java)

            override fun perform(uiController: UiController?, view: View?) {
                val castView = view as T

                IdlingRegistry.getInstance().register(object : IdlingResource {

                    private var isIdle = false

                    override fun getName() = "${T::class.java.name} IdlingResource"

                    override fun isIdleNow() = isIdle

                    override fun registerIdleTransitionCallback(
                        callback: IdlingResource.ResourceCallback?
                    ) {
                        val transitionToIdle = {
                            callback?.onTransitionToIdle()
                            IdlingRegistry.getInstance().unregister(this)
                            isIdle = true
                        }
                        handlerFuc(castView, transitionToIdle)
                    }
                })
            }
        }
    }
}