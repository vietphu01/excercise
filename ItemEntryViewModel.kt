package com.example.myapplication.lab10

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ItemEntryViewModel(private val itemsRepository: ItemsRepository) : ViewModel() {

    // Trạng thái giao diện hiện tại
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    // Cập nhật trạng thái khi người dùng gõ phím
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    // ĐÂY LÀ HÀM ĐÃ ĐƯỢC THÊM LỆNH LƯU VÀO DATABASE
    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertItem(itemUiState.itemDetails.toItem())
        }
    }

    // Hàm kiểm tra xem người dùng đã nhập đủ dữ liệu chưa
    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}

// =======================================================
// CÁC DATA CLASS HỖ TRỢ CHUYỂN ĐỔI DỮ LIỆU BÊN DƯỚI
// =======================================================

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
)

// Chuyển từ giao diện (String) sang dạng dữ liệu chuẩn (Double, Int) để lưu DB
fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0
)

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString()
)