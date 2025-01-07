package com.idyllic.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.*
import androidx.navigation.fragment.findNavController
import com.idyllic.ui_module.R
import com.idyllic.common.dialog.DialogUtil
import com.idyllic.common.util.safeNavigate
import com.idyllic.common.util.safeNavigateFromMainNav
import com.idyllic.core.ktx.gone
import com.idyllic.core.ktx.visible


abstract class BaseFragment(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {
    abstract val viewModel: BaseVM
    private lateinit var mainNavigation: NavController
    private var globalView: View? = null
    private var globalLoadingWithText: View? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = findNavController()

        globalView = layoutInflater.inflate(R.layout.view_global_loading, null, false)
        (requireActivity()).addContentView(
            globalView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        globalLoadingWithText =
            layoutInflater.inflate(R.layout.view_global_loading_with_text, null, false)
        (requireActivity()).addContentView(
            globalLoadingWithText, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        globalView?.gone()
        globalLoadingWithText?.gone()

        viewModel.apply {
            globalErrorLiveData.observe(viewLifecycleOwner, globalErrorObserver)
            internalServerErrorLiveData.observe(viewLifecycleOwner, internalServerErrorObserver)

            baseGlobalLoadingLiveData.observe(viewLifecycleOwner, baseGlobalLoadingObserver)
            baseGlobalLoadingWithTextLiveData.observe(
                viewLifecycleOwner,
                baseGlobalLoadingWithTextObserver
            )
            ddosLiveData.observe(viewLifecycleOwner, ddosObserver)
            errorToServerLiveData.observe(viewLifecycleOwner, errorToServerObserver)
            errorServerErrorLiveData.observe(viewLifecycleOwner, errorServerErrorObserver)
        }
    }

    private val baseGlobalLoadingObserver = Observer<Boolean> {
        if (it) showGlobalLoading()
        else hideGlobalLoading()
    }

    private val baseGlobalLoadingWithTextObserver = Observer<Boolean> {
        if (it) showGlobalLoadingWithText()
        else hideGlobalLoadingWithText()
    }

    private val errorServerErrorObserver = Observer<Unit> {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://checkNetworkScreen".toUri()).build()

//        val mainNavGraphResourceId = resources.getIdentifier(
//            "main_nav", "id", requireContext().packageName
//        )

        val navigationOptions = NavOptions.Builder()
//            .setPopUpTo(destinationId = mainNavGraphResourceId, inclusive = true)
            .setEnterAnim(R.anim.activity_open_from_right_to_left)
            .setExitAnim(R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(R.anim.activity_close_from_left_to_right).build()

        mainNavigation.safeNavigateFromMainNav(navigateToOtherModuleRequest, navigationOptions)
    }

    private val ddosObserver = Observer<Unit> {
        DialogUtil.info(requireContext(), getString(R.string.lbl_try_again_later)) { dialog ->
            dialog.dismiss()
        }.show()
    }

    private val internalServerErrorObserver = Observer<Unit> {
        DialogUtil.info(requireContext(), getString(R.string.lbl_try_again_later)) { dialog ->
            dialog.dismiss()
        }.show()
    }

    open val errorToServerObserver = Observer<String> {
        DialogUtil.info(
            requireContext(), it
        ) { dialog ->
            dialog.dismiss()
        }.show()
    }

    private val globalErrorObserver = Observer<String> {
        DialogUtil.info(requireContext(), it) { dialog ->
            dialog.dismiss()
        }.show()
    }

    fun toHomeScreen() {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://app/mainScreen".toUri()).build()

        val homeNavGraphResourceId = resources.getIdentifier(
            "main_nav", "id", requireContext().packageName
        )

        val navigationOptions = NavOptions.Builder().setPopUpTo(
            destinationId = homeNavGraphResourceId, inclusive = true
        ).build()

        /*
        .setEnterAnim(R.anim.activity_open_from_right_to_left)
            .setExitAnim(R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(R.anim.activity_close_from_left_to_right)
         */

        mainNavigation.safeNavigateFromMainNav(
            navigateToOtherModuleRequest, navigationOptions
        )
    }

    protected fun statusBarColorLight() {
        statusBarColor(false)
    }

    protected fun statusBarColorDark() {
        statusBarColor(true)
    }

    private fun statusBarColor(boolean: Boolean) {
        val window = requireActivity().window
//        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, requireView()).isAppearanceLightStatusBars = boolean
    }

    protected fun hideSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, requireView()).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    protected fun showSystemUI() {
        val window = requireActivity().window
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(
            window, requireView()
        ).show(WindowInsetsCompat.Type.systemBars())
    }

    protected fun showKeyboard() {
        val imm: InputMethodManager? =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        val focus = requireActivity().currentFocus
        if (focus != null) imm?.hideSoftInputFromWindow(
            focus.windowToken, InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    open fun hideKeyboard() {
        val activity = requireActivity()
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) view = View(activity)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showGlobalLoading() {
        globalView?.visible()
    }

    fun hideGlobalLoading() {
        globalView?.gone()
    }

    fun showGlobalLoadingWithText() {
        globalLoadingWithText?.visible()
    }

    fun hideGlobalLoadingWithText() {
        globalLoadingWithText?.gone()
    }

    override fun onDestroy() {
        super.onDestroy()
        hideGlobalLoading()
    }

    fun navigationBarColor(@ColorRes color: Int) {
        requireActivity().window.navigationBarColor =
            ContextCompat.getColor(requireContext(), color)

    }

    fun statusBarColor(@ColorRes color: Int) {
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), color)
    }

    enum class ErrorType {
        NETWORK, GLOBAL
    }

    fun navigateTo(directions: NavDirections) {
        findNavController().safeNavigate(directions)
    }

    fun navigateToDeepLink(
        link: String,
        @IdRes destinationId: Int,
        inclusive: Boolean = false,
        saveState: Boolean = false
    ) {
        val navigateToOtherModuleRequest = NavDeepLinkRequest.Builder.fromUri(link.toUri()).build()
        val navigationOptions = NavOptions.Builder()

            .setEnterAnim(R.anim.activity_open_from_right_to_left)
            .setExitAnim(R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(R.anim.activity_close_from_left_to_right).build()

        findNavController().safeNavigateFromMainNav(navigateToOtherModuleRequest, navigationOptions)
    }
}
