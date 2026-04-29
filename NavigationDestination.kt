package com.example.myapplication.lab10

interface NavigationDestination {
    /**
     * Tên đường dẫn duy nhất (route) để điều hướng tới màn hình này
     */
    val route: String

    /**
     * ID của string resource dùng làm tiêu đề trên thanh TopAppBar
     */
    val titleRes: Int
}