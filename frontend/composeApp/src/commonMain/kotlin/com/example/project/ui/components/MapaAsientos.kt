package com.example.project.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.project.domain.model.Asiento
import com.example.project.domain.model.EstadoAsiento

@Composable
fun MapaAsientos(
    asientos: List<Asiento>,
    columnas: Int,
    asientosSeleccionados: List<Asiento>,
    onAsientoClick: (Asiento) -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Escenario", modifier = Modifier.padding(bottom = 8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 16.dp)
                .background(Color.DarkGray)
                .size(height = 4.dp, width = 100.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnas),
            modifier = Modifier.fillMaxWidth().padding(8.dp)
        ) {
            items(
                items = asientos,
                key = {it.id}
            ) { asiento ->
                val isSeleccionado = asientosSeleccionados.any { it.id == asiento.id }
                AsientoItem(asiento, isSeleccionado, onAsientoClick)
            }
        }

        RowLegend()
    }
}

@Composable
fun AsientoItem(
    asiento: Asiento,
    isSeleccionado: Boolean,
    onClick: (Asiento) -> Unit
) {
    val backgroundColor = when {
        isSeleccionado -> Color(0xFFFFD700)
        asiento.estado == EstadoAsiento.VENDIDO -> Color.Red
        asiento.estado == EstadoAsiento.BLOQUEADO -> Color(0xFFFFA500)
        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .padding(2.dp)
            .aspectRatio(1f)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(1.dp, Color.Gray, CircleShape)
            .clickable { onClick(asiento) },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${asiento.fila}-${asiento.columna}",
            fontSize = 8.sp,
            color = if (asiento.estado == EstadoAsiento.VENDIDO) Color.White else Color.Black
        )
    }
}

@Composable
fun RowLegend() {
    Text("Gris: Libre | Rojo: Ocupado | Amarillo: Tu selección", fontSize = 10.sp)
}