package com.example.menulab;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView imageView;

    // История изменений фигур
    private List<Integer> shapeHistory = new ArrayList<>();
    private int currentIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        // Регистрация ImageView для вызова контекстного меню при долгом нажатии
        registerForContextMenu(imageView);

        // Установка начальной фигуры
        applyShape(R.drawable.shape_circle);
    }

    // Options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        int shapeResId = -1;

        if (id == R.id.action_circle) {
            shapeResId = R.drawable.shape_circle;
        } else if (id == R.id.action_oval) {
            shapeResId = R.drawable.shape_oval;
        } else if (id == R.id.action_ring) {
            shapeResId = R.drawable.shape_ring;
        }

        // Если выбрана фигура, сохраняем в историю и применяем
        if (shapeResId != -1) {
            addToHistory(shapeResId);
            applyShape(shapeResId);
        }
        return true;
    }

    // Context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.context_menu, menu);
        menu.setHeaderTitle("Действия с фигурой");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.context_undo) {
            undo();
            return true;
        } else if (id == R.id.context_redo) {
            redo();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    // История (undo/redo)
    private void addToHistory(int shapeId) {
        // Если пользователь откатился назад и применяет новую фигуру,
        // удаляем все состояния, которые были после текущей точки
        if (currentIndex < shapeHistory.size() - 1) {
            shapeHistory.subList(currentIndex + 1, shapeHistory.size()).clear();
        }
        shapeHistory.add(shapeId);
        currentIndex = shapeHistory.size() - 1;
    }

    private void undo() {
        if (currentIndex > 0) {
            currentIndex--;
            applyShape(shapeHistory.get(currentIndex));
        }
    }

    private void redo() {
        if (currentIndex < shapeHistory.size() - 1) {
            currentIndex++;
            applyShape(shapeHistory.get(currentIndex));
        }
    }

    // Применение ресурса к ImageView
    private void applyShape(int resId) {
        imageView.setImageResource(resId);
    }
}