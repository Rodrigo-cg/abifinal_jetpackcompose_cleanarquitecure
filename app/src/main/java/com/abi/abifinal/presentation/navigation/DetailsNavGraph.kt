package com.abi.abifinal.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.abi.abifinal.presentation.screens.medicamentos_edit.MedicamentosEditScreen
import com.abi.abifinal.presentation.screens.profile_edit.ProfileEditScreen

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController){

    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.ProfileEdit.route
    ){
        composable(
            route = DetailsScreen.NewPost.route
        ){
            //NewPostScreen(navController)
        }

        composable(
            route = DetailsScreen.ProfileEdit.route,
            arguments = listOf(navArgument("user"){
                type = NavType.StringType
            })
        ){
            it.arguments?.getString("user")?.let { user ->
                ProfileEditScreen(navController, user = user)
            }
        }
        composable(
            route = DetailsScreen.MedicamentosEdit.route,
            arguments = listOf(navArgument("user"){
                type = NavType.StringType
            })
        ){
            it.arguments?.getString("user")?.let { user ->
                MedicamentosEditScreen(navController, user = user)
            }
        }

        composable(
            route = DetailsScreen.DetailPost.route,
            arguments = listOf(navArgument("post"){
                type = NavType.StringType
            })
        ){
            it.arguments?.getString("post")?.let { it ->
              // DetailPostScreen(navController = navController, post = it)
            }
        }

        composable(
            route = DetailsScreen.UpdatePost.route,
            arguments = listOf(navArgument("post"){
                type = NavType.StringType
            })
        ){
            it.arguments?.getString("post")?.let { it ->
                //UpdatePostScreen(navController = navController, post = it)
            }
        }


    }

}

sealed class DetailsScreen(val route: String){

    object NewPost: DetailsScreen("post/new")

    object ProfileEdit: DetailsScreen("profile/edit/{user}"){
        fun passUser(user: String) = "profile/edit/${user}"
    }

    object MedicamentosEdit: DetailsScreen("medicamentos/edit/{user}"){
        fun passUser(user: String) = "medicamentos/edit/${user}"
    }

    object DetailPost: DetailsScreen("posts/detail/{post}"){
        fun passPost(post: String) = "posts/detail/${post}"
    }

    object UpdatePost: DetailsScreen("posts/update/{post}"){
        fun passPost(post: String) = "posts/update/${post}"
    }

}