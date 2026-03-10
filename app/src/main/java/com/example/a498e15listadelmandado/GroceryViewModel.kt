package com.example.a498e15listadelmandado

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GroceryItem(
    val name: String,
    val category: String,
    var isChecked: Boolean = false
)

class GroceryViewModel : ViewModel() {
    private val _items = mutableStateListOf<GroceryItem>(
        // Abarrotes
        GroceryItem("Manzana", "Abarrotes"),
        GroceryItem("Leche", "Abarrotes"),
        GroceryItem("Arroz", "Abarrotes"),
        GroceryItem("Frijol", "Abarrotes"),
        GroceryItem("Huevo", "Abarrotes"),
        GroceryItem("Pan", "Abarrotes"),
        
        // Limpieza
        GroceryItem("Jabón", "Limpieza"),
        GroceryItem("Cloro", "Limpieza"),
        GroceryItem("Detergente", "Limpieza"),
        GroceryItem("Desinfectante", "Limpieza"),
        GroceryItem("Esponja", "Limpieza"),
        GroceryItem("Suavizante", "Limpieza"),
        
        // Hogar
        GroceryItem("Focos", "Hogar"),
        GroceryItem("Pilas", "Hogar"),
        GroceryItem("Velas", "Hogar"),
        GroceryItem("Escoba", "Hogar"),
        GroceryItem("Trapeador", "Hogar"),
        
        // Otros
        GroceryItem("Comida para perro", "Otros"),
        GroceryItem("Carbón", "Otros"),
        GroceryItem("Papel Aluminio", "Otros"),
        GroceryItem("Servilletas", "Otros")
    )
    
    val categories = listOf("Abarrotes", "Limpieza", "Hogar", "Otros")
    
    private val _selectedCategory = MutableStateFlow("Abarrotes")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    fun selectCategory(category: String) {
        _selectedCategory.value = category
    }

    fun getItemsForCategory(category: String): List<GroceryItem> {
        return _items.filter { it.category == category }.sortedBy { it.name }
    }

    fun addItem(name: String, category: String) {
        if (name.isNotBlank()) {
            _items.add(GroceryItem(name, category))
        }
    }

    fun toggleItemCheck(item: GroceryItem) {
        val index = _items.indexOf(item)
        if (index != -1) {
            _items[index] = item.copy(isChecked = !item.isChecked)
        }
    }

    fun getSelectedItems(): List<GroceryItem> {
        return _items.filter { it.isChecked }.sortedBy { it.name }
    }

    fun removeItem(item: GroceryItem) {
        _items.remove(item)
    }
}
