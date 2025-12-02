package com.example.modaurbana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
//import androidx.compose.material3.ExposedDropdownMenu   // 👈 IMPORT CLAVE
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.modaurbana.models.Producto
import com.example.modaurbana.ui.navigation.Route
import com.example.modaurbana.viewmodel.CartViewModel
import com.example.modaurbana.viewmodel.ProductListUiState
import com.example.modaurbana.viewmodel.ProductListViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavHostController,
    productListViewModel: ProductListViewModel = viewModel(),
    cartViewModel: CartViewModel
) {
    val ui by productListViewModel.ui.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val cartUi by cartViewModel.ui.collectAsState()
    val totalItems = cartUi.items.sumOf { it.quantity }

    // 🔍 estado de la barra de búsqueda
    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Catálogo de productos") },
                actions = {
                    IconButton(onClick = { navController.navigate(Route.Cart.route) }) {
                        BadgedBox(
                            badge = {
                                if (totalItems > 0) {
                                    Badge {
                                        Text(totalItems.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Ver carrito"
                            )
                        }
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
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
                    // Filtrado por búsqueda sobre los productos ya filtrados por tipo/estilo
                    val productosMostrados = remember(ui.productosFiltrados, searchQuery) {
                        if (searchQuery.isBlank()) {
                            ui.productosFiltrados
                        } else {
                            ui.productosFiltrados.filter { producto ->
                                producto.nombre.contains(searchQuery, ignoreCase = true)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // 🔍 Barra de búsqueda
                        SearchBarCatalogo(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it }
                        )

                        Spacer(Modifier.height(12.dp))

                        // Filtros por tipo / estilo
                        FiltrosProductos(
                            ui = ui,
                            onChange = { tipo, estilo ->
                                productListViewModel.aplicarFiltros(tipo, estilo)
                            }
                        )

                        Spacer(Modifier.height(16.dp))

                        ListaProductos(
                            productos = productosMostrados,
                            onAgregarAlCarrito = { producto ->
                                cartViewModel.addToCart(producto)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        message = "Producto agregado al carrito",
                                        withDismissAction = false
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SearchBarCatalogo(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text("Buscar producto") },
        placeholder = { Text("Ej: Hoodie negro, polera, jeans…") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar"
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltrosProductos(
    ui: ProductListUiState,
    onChange: (String?, String?) -> Unit
) {
    var tipoExpanded by remember { mutableStateOf(false) }
    var estiloExpanded by remember { mutableStateOf(false) }

    Column {
        Text("Filtros", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        // 🔹 TIPO DE PRENDA
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

        // 🔹 ESTILO
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
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items(productos) { producto ->
                ProductCard(
                    producto = producto,
                    onAgregarAlCarrito = onAgregarAlCarrito
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    producto: Producto,
    onAgregarAlCarrito: (Producto) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            // Imagen del producto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = producto.imagen,
                    contentDescription = producto.nombre,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(Modifier.height(8.dp))

            Text(
                text = producto.nombre,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            producto.precio?.let {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "$$it",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = { onAgregarAlCarrito(producto) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar al carrito")
            }
        }
    }
}
