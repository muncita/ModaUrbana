package com.example.modaurbana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
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

    var searchQuery by rememberSaveable { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = { navController.navigate(Route.Cart.route) }) {
                        BadgedBox(
                            badge = {
                                if (totalItems > 0) {
                                    Badge { Text(totalItems.toString()) }
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
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                    val productosMostrados = remember(ui.productosFiltrados, searchQuery) {
                        if (searchQuery.isBlank()) {
                            ui.productosFiltrados
                        } else {
                            ui.productosFiltrados.filter {
                                it.nombre.contains(searchQuery, ignoreCase = true)
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {

                        SearchBarCatalogo(
                            query = searchQuery,
                            onQueryChange = { searchQuery = it }
                        )

                        Spacer(Modifier.height(12.dp))

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
                                    snackbarHostState.showSnackbar("Producto agregado al carrito")
                                }
                            },
                            onOpenDetail = { productId ->
                                navController.navigate(
                                    Route.ProductDetail.createRoute(productId)
                                )
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
        placeholder = { Text("Ej: hoodie negro, polera, jeans…") },
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Filtrar por",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyMedium
            )
            TextButton(onClick = { onChange(null, null) }) {
                Text("Limpiar filtros")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(Modifier.weight(1f)) {
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

            Box(Modifier.weight(1f)) {
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
}

@Composable
private fun ListaProductos(
    productos: List<Producto>,
    onAgregarAlCarrito: (Producto) -> Unit,
    onOpenDetail: (String) -> Unit
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
                    onAgregarAlCarrito = onAgregarAlCarrito,
                    onOpenDetail = onOpenDetail
                )
            }
        }
    }
}

@Composable
private fun ProductCard(
    producto: Producto,
    onAgregarAlCarrito: (Producto) -> Unit,
    onOpenDetail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = producto.id != null) {
                producto.id?.let { onOpenDetail(it) }
            },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(8.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(3f / 4f)
                    .clip(RoundedCornerShape(12.dp))
            ) {
                AsyncImage(
                    model = producto.imagenThumbnail ?: producto.imagen,
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
                    text = "CLP $${"%,.0f".format(it).replace(",", ".")}",
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
