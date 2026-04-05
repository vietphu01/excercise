

sealed class Screen(val rout: String) {
    object SignIn : Screen("SignIn")
    object Home : Screen("Home")
    object SignUp : Screen("SignUp")
}
