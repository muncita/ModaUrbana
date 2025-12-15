package com.example.modaurbana.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.modaurbana.models.Producto
import com.example.modaurbana.viewmodel.CartViewModel
import com.example.modaurbana.viewmodel.ProductListViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.modaurbana.ui.navigation.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    navController: NavHostController,
    productId: String,
    cartViewModel: CartViewModel
) {
    val productVm: ProductListViewModel = viewModel()
    val ui by productVm.ui.collectAsState()

    LaunchedEffect(Unit) {
        if (ui.productosFiltrados.isEmpty() && !ui.isLoading) {
            productVm.loadProductos()
        }
    }

    val producto: Producto? = remember(ui.productosFiltrados, productId) {
        ui.productosFiltrados.firstOrNull { it.id == productId }
            ?: ui.productos.firstOrNull { it.id == productId }
    }

    var qty by remember { mutableIntStateOf(1) }

    val sizeOptions = remember(producto) {
        listOf("S", "M", "L", "XL", "XXL")
    }
    var selectedSize by remember { mutableStateOf<String?>(producto?.talla) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        when {
            ui.isLoading -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }

            producto == null -> {
                Box(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Producto no encontrado.")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = producto.imagen ?: producto.imagenThumbnail,
                            contentDescription = producto.nombre,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = producto.nombre,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "CLP $${"%,.0f".format(producto.precio ?: 0.0).replace(",", ".")}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(Modifier.height(18.dp))

                    Text("Talla", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        sizeOptions.forEach { s ->
                            FilterChip(
                                selected = selectedSize == s,
                                onClick = { selectedSize = s },
                                label = { Text(s) }
                            )
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { if (qty > 1) qty-- },
                            enabled = qty > 1
                        ) { Text("−") }

                        Text(qty.toString(), style = MaterialTheme.typography.titleMedium)

                        OutlinedButton(onClick = { qty++ }) { Text("+") }

                        Spacer(Modifier.weight(1f))

                        Button(
                            onClick = {
                                // Agrega qty veces con tu VM actual (simple y funcional)
                                repeat(qty) { cartViewModel.addToCart(producto) }
                                navController.navigate(Route.Cart.route)
                            },
                            enabled = selectedSize != null,
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("Agregar")
                        }
                    }

                    Spacer(Modifier.height(22.dp))

                    Text("Detalles", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))

                    val bullets = buildList {
                        producto.material?.let { add("Material: $it") }
                        producto.estilo?.let { add("Estilo: $it") }
                        producto.color?.let { add("Color: $it") }
                        producto.talla?.let { add("Talla sugerida: $it") }
                    }

                    if (bullets.isNotEmpty()) {
                        bullets.forEach { line ->
                            Text("• $line")
                            Spacer(Modifier.height(4.dp))
                        }
                    }

                    Spacer(Modifier.height(18.dp))

                    Text("Descripción", fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.height(8.dp))
                    Text(producto.descripcion ?: "Sin descripción disponible.")

                    Spacer(Modifier.height(60.dp))
                }
            }
        }
    }
}
