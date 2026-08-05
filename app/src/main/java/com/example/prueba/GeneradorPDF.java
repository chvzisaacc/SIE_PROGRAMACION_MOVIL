package com.example.prueba;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class GeneradorPDF {

    public static void generarPDFInventario(Context context, List<ReporteInventario> lista) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("Reporte de Inventario Disponible (> 0)", 40, 50, paint);

        paint.setTextSize(10);
        paint.setFakeBoldText(false);
        canvas.drawText("ID | Producto | Almacén | Cantidad | Precio | Total", 40, 80, paint);

        paint.setColor(Color.GRAY);
        canvas.drawLine(40, 90, 550, 90, paint);

        paint.setColor(Color.BLACK);
        int y = 110;
        for (ReporteInventario item : lista) {
            String fila = item.getIdProducto() + " | " + item.getNombreProducto() + " | " +
                    item.getNombreAlmacen() + " | " + item.getCantidadActual() +
                    " | L. " + item.getPrecioUnitario() + " | L. " + item.getValorTotal();
            canvas.drawText(fila, 40, y, paint);
            y += 20;
            if (y > 800) break;
        }

        pdfDocument.finishPage(page);
        guardarYCompartirPDF(context, pdfDocument, "Reporte_Inventario.pdf");
    }

    public static void generarPDFCriticos(Context context, List<ReporteCritico> lista) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("Reporte de Productos Críticos (< 5)", 40, 50, paint);

        paint.setTextSize(10);
        paint.setFakeBoldText(false);
        canvas.drawText("ID | Producto | Almacén | Stock Actual | Stock Mínimo", 40, 80, paint);

        paint.setColor(Color.GRAY);
        canvas.drawLine(40, 90, 550, 90, paint);

        paint.setColor(Color.BLACK);
        int y = 110;
        for (ReporteCritico item : lista) {
            String fila = item.getIdProducto() + " | " + item.getNombreProducto() + " | " +
                    item.getNombreAlmacen() + " | " + item.getCantidadActual() +
                    " | " + item.getStockMinimo();
            canvas.drawText(fila, 40, y, paint);
            y += 20;
            if (y > 800) break;
        }

        pdfDocument.finishPage(page);
        guardarYCompartirPDF(context, pdfDocument, "Reporte_Stock_Critico.pdf");
    }

    public static void generarPDFMovimientos(Context context, List<ReporteMovimiento> lista) {
        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);

        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();

        paint.setTextSize(18);
        paint.setFakeBoldText(true);
        canvas.drawText("Reporte de Movimientos por Período", 40, 50, paint);

        paint.setTextSize(10);
        paint.setFakeBoldText(false);
        canvas.drawText("ID | Tipo | Producto | Cantidad | Almacén | Fecha", 40, 80, paint);

        paint.setColor(Color.GRAY);
        canvas.drawLine(40, 90, 550, 90, paint);

        paint.setColor(Color.BLACK);
        int y = 110;
        for (ReporteMovimiento item : lista) {
            String fila = item.getIdMovimiento() + " | " + item.getTipoMovimiento() + " | " +
                    item.getNombreProducto() + " | " + item.getCantidad() + " | " +
                    item.getNombreAlmacen() + " | " + item.getFechaMovimiento();
            canvas.drawText(fila, 40, y, paint);
            y += 20;
            if (y > 800) break;
        }

        pdfDocument.finishPage(page);
        guardarYCompartirPDF(context, pdfDocument, "Reporte_Movimientos.pdf");
    }

    private static void guardarYCompartirPDF(Context context, PdfDocument pdfDocument, String nombreArchivo) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), nombreArchivo);
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            pdfDocument.close();

            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Error al generar PDF: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}