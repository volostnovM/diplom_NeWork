package ru.netology.neworknetology.screens.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.neworknetology.screens.main.MainActivityArgs
import ru.netology.neworknetology.R
import ru.netology.neworknetology.databinding.ActivityMainBinding
import ru.netology.neworknetology.screens.main.tabs.TabsFragment
import java.util.regex.Pattern

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    // nav controller of the current screen
    private var navController: NavController? = null

    private val topLevelDestinations = setOf(getTabsDestination(), getSignInDestination())

     //fragment listener is sued for tracking current nav controller
    private val fragmentListener = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is TabsFragment || f is NavHostFragment) return
            onNavControllerActivated(f.findNavController())
        }
    }

    private fun onNavControllerActivated(navController: NavController) {
        if (this.navController == navController) return
        this.navController?.removeOnDestinationChangedListener(destinationListener)
        navController.addOnDestinationChangedListener(destinationListener)
        this.navController = navController
    }

    private fun isSignedIn(): Boolean {
        val bundle = intent.extras ?: throw IllegalStateException("No required arguments")
        val args = MainActivityArgs.fromBundle(bundle)
        return args.isSignedIn
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        setSupportActionBar(binding.toolbar)

        // preparing root nav controller
        val navController = getRootNavController()
        prepareRootNavController(isSignedIn(), navController)
        onNavControllerActivated(navController)

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

        // updating username in the toolbar
//        viewModel.username.observe(this) {
//            binding.usernameTextView.text = it
//        }
    }

    override fun onDestroy() {
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
        navController = null
        super.onDestroy()
    }

//    override fun onBackPressed() {
//        if (isStartDestination(navController?.currentDestination)) {
//            super.onBackPressed()
//        } else {
//            navController?.popBackStack()
//        }
//    }

    override fun onSupportNavigateUp(): Boolean = (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun prepareRootNavController(isSignedIn: Boolean, navController: NavController) {
        val graph = navController.navInflater.inflate(getMainNavigationGraphId())
        graph.setStartDestination(
            if (isSignedIn) {
                getTabsDestination()
            } else {
                getSignInDestination()
            }
        )
        navController.graph = graph
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

    private val destinationListener = NavController.OnDestinationChangedListener { _, destination, arguments ->
//        supportActionBar?.title = prepareTitle(destination.label, arguments)
        supportActionBar?.setDisplayHomeAsUpEnabled(!isStartDestination(destination))
    }

    private fun isStartDestination(destination: NavDestination?): Boolean {
        if (destination == null) return false
        val graph = destination.parent ?: return false
        val startDestinations = topLevelDestinations + graph.startDestinationId
        return startDestinations.contains(destination.id)
    }

//    private fun prepareTitle(label: CharSequence?, arguments: Bundle?): String {
//
//        // code for this method has been copied from Google sources :)
//
//        if (label == null) return ""
//        val title = StringBuffer()
//        val fillInPattern = Pattern.compile("\\{(.+?)\\}")
//        val matcher = fillInPattern.matcher(label)
//        while (matcher.find()) {
//            val argName = matcher.group(1)
//            if (arguments != null && arguments.containsKey(argName)) {
//                matcher.appendReplacement(title, "")
//                title.append(arguments[argName].toString())
//            } else {
//                throw IllegalArgumentException(
//                    "Could not find $argName in $arguments to fill label $label"
//                )
//            }
//        }
//        matcher.appendTail(title)
//        return title.toString()
//    }

    private fun getMainNavigationGraphId(): Int = R.navigation.main_graph

    private fun getTabsDestination(): Int = R.id.tabsFragment

    private fun getSignInDestination(): Int = R.id.signInFragment
}