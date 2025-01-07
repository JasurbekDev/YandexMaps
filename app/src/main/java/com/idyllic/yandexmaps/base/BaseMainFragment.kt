package com.idyllic.yandexmaps.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.idyllic.common.dialog.DialogUtil
import com.idyllic.core.ktx.gone
import com.idyllic.core.ktx.visible
import com.idyllic.yandexmaps.R

abstract class BaseMainFragment(
    @LayoutRes contentLayoutId: Int
) : Fragment(contentLayoutId) {
    abstract val viewModel: BaseMainVM
    private lateinit var mainNavigation: NavController
    private var globalView: View? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainNavigation = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)


        globalView = layoutInflater.inflate(
            com.idyllic.ui_module.R.layout.view_global_loading, null, false
        )

//        snowflake = layoutInflater.inflate(
//            com.idyllic.ui_module.R.layout.view_snowflake, null, false
//        )

        (requireActivity()).addContentView(
            globalView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

//        (requireActivity()).addContentView(
//            snowflake, ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
//            )
//        )

        globalView?.gone()

        viewModel.apply {
            globalErrorLiveData.observe(viewLifecycleOwner, globalErrorObserver)
            internalServerErrorLiveData.observe(viewLifecycleOwner, internalServerErrorObserver)
            logoutScreenLiveData.observe(viewLifecycleOwner, logoutScreenObserver)

            updateAppLiveData.observe(viewLifecycleOwner, updateAppObserver)
            blockUserLiveData.observe(viewLifecycleOwner, blockUserObserver)
            baseGlobalLoadingLiveData.observe(viewLifecycleOwner, baseGlobalLoadingObserver)
            ddosLiveData.observe(viewLifecycleOwner, ddosObserver)
            technicalBreakLiveData.observe(viewLifecycleOwner, technicalBreakObserver)
            errorToServerLiveData.observe(viewLifecycleOwner, errorToServerObserver)
            errorServerErrorLiveData.observe(viewLifecycleOwner, errorServerErrorObserver)

            navigateToLoginScreenLiveData.observe(viewLifecycleOwner, navigateLoginScreen)
            deviceNotActivatedLiveData.observe(viewLifecycleOwner, navigateMySessionsScreenObserver)
        }
    }

    private val baseGlobalLoadingObserver = Observer<Boolean> {
        if (it) showGlobalLoading()
        else hideGlobalLoading()
    }


    private val ddosObserver = Observer<Unit> {
        DialogUtil.info(
            requireContext(), getString(com.idyllic.ui_module.R.string.lbl_try_again_later)
        ) { dialog ->
            dialog.dismiss()
        }.show()
    }

    private val internalServerErrorObserver = Observer<Unit> {
        DialogUtil.info(
            requireContext(),
            getString(com.idyllic.ui_module.R.string.lbl_try_again_later)
        ) { dialog ->
            dialog.dismiss()
        }.show()
    }

    private val errorToServerObserver = Observer<String> {
        DialogUtil.info(requireContext(), it) { dialog ->
            dialog.dismiss()
        }.show()
    }

    private val errorServerErrorObserver = Observer<Unit> {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://checkNetworkScreen".toUri()).build()

//        val homeNavGraphResourceId = resources.getIdentifier(
//            "main_nav", "id", requireContext().packageName
//        )

        val navigationOptions = NavOptions.Builder()
//            .setPopUpTo(destinationId = homeNavGraphResourceId, inclusive = true)
            .setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
            .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()

        mainNavigation.navigate(navigateToOtherModuleRequest, navigationOptions)
    }

    private val updateAppObserver = Observer<Unit> {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://updateAppScreen".toUri()).build()

        val homeNavGraphResourceId = resources.getIdentifier(
            "main_nav", "id", requireContext().packageName
        )

        val navigationOptions = NavOptions.Builder()
            .setPopUpTo(destinationId = homeNavGraphResourceId, inclusive = true)
            .setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
            .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()

        mainNavigation.navigate(navigateToOtherModuleRequest, navigationOptions)
    }

    private val blockUserObserver = Observer<String> {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://blockAppScreen".toUri()).build()

        val homeNavGraphResourceId = resources.getIdentifier(
            "main_nav", "id", requireContext().packageName
        )

        val navigationOptions = NavOptions.Builder()
            .setPopUpTo(destinationId = homeNavGraphResourceId, inclusive = true)
            .setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
            .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()

        mainNavigation.navigate(navigateToOtherModuleRequest, navigationOptions)
    }

    private val technicalBreakObserver = Observer<Unit> {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://technicalWorkScreen".toUri()).build()

        val homeNavGraphResourceId = resources.getIdentifier(
            "main_nav", "id", requireContext().packageName
        )

        val navigationOptions = NavOptions.Builder()
            .setPopUpTo(destinationId = homeNavGraphResourceId, inclusive = true)
            .setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
            .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()

        mainNavigation.navigate(navigateToOtherModuleRequest, navigationOptions)
    }


    private val globalErrorObserver = Observer<String> {
        DialogUtil.info(requireContext(), it) { dialog ->
            dialog.dismiss()
        }.show()
    }

    private val logoutScreenObserver = Observer<Unit> {
//        val navigateToOtherModuleRequest =
//            NavDeepLinkRequest.Builder.fromUri("myapp-app://app/phoneNumberScreen".toUri()).build()
//
//        val homeNavGraphResourceId = resources.getIdentifier(
//            "main_nav", "id", requireContext().packageName
//        )
//
//        val navigationOptions = NavOptions.Builder().setPopUpTo(
//            destinationId = homeNavGraphResourceId, inclusive = true
//        ).setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
//            .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
//            .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
//            .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()
//
//        mainNavigation.navigate(
//            navigateToOtherModuleRequest, navigationOptions
//        )
    }

    private val navigateLoginScreen = Observer<Unit> {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://app/phoneNumberScreen".toUri()).build()

        val homeNavGraphResourceId = resources.getIdentifier(
            "main_nav", "id", requireContext().packageName
        )

        val navigationOptions = NavOptions.Builder().setPopUpTo(
            destinationId = homeNavGraphResourceId, inclusive = true
        ).setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
            .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()

        mainNavigation.navigate(
            navigateToOtherModuleRequest, navigationOptions
        )
    }

    private val navigateMySessionsScreenObserver = Observer<Unit> {
        val navigateToOtherModuleRequest =
            NavDeepLinkRequest.Builder.fromUri("myapp-app://mySessionsScreen".toUri()).build()

        val navigationOptions = NavOptions.Builder()
            .setEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_right_to_left)
            .setExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_right_to_left)
            .setPopEnterAnim(com.idyllic.ui_module.R.anim.activity_open_from_left_to_right)
            .setPopExitAnim(com.idyllic.ui_module.R.anim.activity_close_from_left_to_right).build()

        mainNavigation.navigate(navigateToOtherModuleRequest, navigationOptions)
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

        mainNavigation.navigate(
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
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
        val focus = activity?.currentFocus
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
}