package com.example.modaurbana.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.modaurbana.models.Producto
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.CartViewModel
import com.example.modaurbana.viewmodel.ProductListUiState
import com.example.modaurbana.viewmodel.ProductListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavHostController,
    productListViewModel: ProductListViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val ui by productListViewModel.ui.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de productos") },
                actions = {
                    IconButton(onClick = { navController.navigate(Route.Cart.route) }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Ver carrito"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                ui.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                ui.error != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Error: ${ui.error}")
                        Spacer(Modifier.height(8.dp))
                        Button(onClick = { productListViewModel.loadProductos() }) {
                            Text("Reintentar")
                        }
                    }
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        FiltrosProductos(
                            ui = ui,
                            onChange = { tipo, estilo ->
                                productListViewModel.aplicarFiltros(tipo, estilo)
                            }
                        )

                        Spacer(Modifier.height(16.dp))

                        ListaProductos(
                            productos = ui.productosFiltrados,
                            onAgregarAlCarrito = { producto ->
                                cartViewModel.addToCart(producto)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltrosProductos(
    ui: ProductListUiState,
    onChange: (String?, String?) -> Unit // tipoPrenda, estilo
) {
    var tipoExpanded by remember { mutableStateOf(false) }
    var estiloExpanded by remember { mutableStateOf(false) }

    Column {
        Text("Filtros", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        // 🔹 TIPO DE PRENDA (Hoodies, Poleras, etc.)
        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = tipoExpanded,
                onExpandedChange = { tipoExpanded = !tipoExpanded }
            ) {
                TextField(
                    value = ui.tipoSeleccionado ?: "Tipo de prenda",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Tipo de prenda") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = tipoExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = tipoExpanded,
                    onDismissRequest = { tipoExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos") },
                        onClick = {
                            tipoExpanded = false
                            onChange(null, ui.estiloSeleccionado)
                        }
                    )
                    ui.tiposDisponibles.forEach { tipo ->
                        DropdownMenuItem(
                            text = { Text(tipo) },
                            onClick = {
                                tipoExpanded = false
                                onChange(tipo, ui.estiloSeleccionado)
                            }
                        )
                    }
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // 🔹 ESTILO (Streetwear, Minimalista, etc.)
        Box(modifier = Modifier.fillMaxWidth()) {
            ExposedDropdownMenuBox(
                expanded = estiloExpanded,
                onExpandedChange = { estiloExpanded = !estiloExpanded }
            ) {
                TextField(
                    value = ui.estiloSeleccionado ?: "Estilo",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estilo") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = estiloExpanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = estiloExpanded,
                    onDismissRequest = { estiloExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Todos") },
                        onClick = {
                            estiloExpanded = false
                            onChange(ui.tipoSeleccionado, null)
                        }
                    )
                    ui.estilosDisponibles.forEach { estilo ->
                        DropdownMenuItem(
                            text = { Text(estilo) },
                            onClick = {
                                estiloExpanded = false
                                onChange(ui.tipoSeleccionado, estilo)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListaProductos(
    productos: List<Producto>,
    onAgregarAlCarrito: (Producto) -> Unit
) {
    if (productos.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay productos para los filtros seleccionados.")
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(productos) { producto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { /* Detalle más adelante */ }
                ) {
                    Column {
                        // 🔹 IMAGEN DEL PRODUCTO
                        AsyncImage(
                            model = producto.imagen,
                            contentDescription = producto.nombre,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(320.dp),
                            contentScale = ContentScale.Crop
                        )

                        // 🔹 TEXTO Y BOTÓN
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(producto.nombre, fontWeight = FontWeight.Bold)
                            producto.descripcion?.let {
                                Spacer(Modifier.height(4.dp))
                                Text(it)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text("Talla: ${producto.talla ?: "-"}")
                            Text("Material: ${producto.material ?: "-"}")
                            Text("Estilo: ${producto.estilo ?: "-"}")
                            producto.precio?.let {
                                Spacer(Modifier.height(4.dp))
                                Text("Precio: $it")
                            }
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { onAgregarAlCarrito(producto) }
                            ) {
                                Text("Agregar al carrito")
                            }
                        }
                    }
                }
            }
        }
    }
}
