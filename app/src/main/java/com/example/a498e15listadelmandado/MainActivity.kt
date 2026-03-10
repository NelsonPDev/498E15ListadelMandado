package com.example.a498e15listadelmandado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a498e15listadelmandado.ui.theme._498E15ListadelMandadoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _498E15ListadelMandadoTheme {
                GroceryApp()
            }
        }
    }
}

@Composable
fun GroceryApp(viewModel: GroceryViewModel = viewModel()) {
    var currentScreen by remember { mutableStateOf("Main") }

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        if (currentScreen == "Main") {
            MainScreen(
                viewModel = viewModel,
                onNavigateToFilter = { currentScreen = "Filter" }
            )
        } else {
            FilterScreen(
                viewModel = viewModel,
                onBack = { currentScreen = "Main" }
            )
        }
    }
}

@Composable
fun MainScreen(viewModel: GroceryViewModel, onNavigateToFilter: () -> Unit) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val items = viewModel.getItemsForCategory(selectedCategory)
    var showAddDialog by remember { mutableStateOf(false) }
    var newItemName by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.statusBarsPadding().padding(16.dp)) {
                Text(
                    text = "22270498 E 1.5 ListadelMandado",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("Categorías", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(viewModel.categories) { category ->
                        Button(
                            onClick = { viewModel.selectCategory(category) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedCategory == category) 
                                    MaterialTheme.colorScheme.primary 
                                else MaterialTheme.colorScheme.secondary
                            )
                        ) {
                            Text(category)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { showAddDialog = true },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar a $selectedCategory")
                }
                Button(
                    onClick = onNavigateToFilter,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Filtrar (Ver Lista)")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(items) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = item.isChecked,
                        onCheckedChange = { viewModel.toggleItemCheck(item) }
                    )
                    Text(
                        text = item.name,
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 18.sp
                    )
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("Agregar Item") },
                text = {
                    TextField(
                        value = newItemName,
                        onValueChange = { newItemName = it },
                        label = { Text("Nombre del artículo") }
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.addItem(newItemName, selectedCategory)
                        newItemName = ""
                        showAddDialog = false
                    }) {
                        Text("Agregar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAddDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun FilterScreen(viewModel: GroceryViewModel, onBack: () -> Unit) {
    val selectedItems = viewModel.getSelectedItems()

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text("Lista actual", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .navigationBarsPadding(),
                contentAlignment = Alignment.Center
            ) {
                Button(onClick = onBack) {
                    Text("Regresar")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(selectedItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = true,
                        onCheckedChange = { 
                            viewModel.toggleItemCheck(item) 
                        }
                    )
                    Text(
                        text = "${item.name} (${item.category})",
                        modifier = Modifier.padding(start = 8.dp),
                        fontSize = 18.sp
                    )
                }
            }
            if (selectedItems.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillParentMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No hay artículos seleccionados")
                    }
                }
            }
        }
    }
}
