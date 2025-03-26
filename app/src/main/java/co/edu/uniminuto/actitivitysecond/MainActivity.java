package co.edu.uniminuto.actitivitysecond;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONException;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText etTask;
    private Button btnAdd;
    private ListView listTask;
    private SearchView searchView;

    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;

    private static final int REQUEST_EDIT = 1;


    private void saveTasks() {
        SharedPreferences prefs = getSharedPreferences("Mis Tareas", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        JSONArray jsonArray = new JSONArray(arrayList);
        editor.putString("tareas", jsonArray.toString());
        editor.apply();
    }

    private void loadTasks() {
        SharedPreferences prefs = getSharedPreferences("Mis Tareas", MODE_PRIVATE);
        String tareasGuardadas = prefs.getString("tareas", null);

        if (tareasGuardadas != null) {
            try {
                JSONArray jsonArray = new JSONArray(tareasGuardadas);
                for (int i = 0; i < jsonArray.length(); i++) {
                    arrayList.add(jsonArray.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTask = findViewById(R.id.etTask);
        btnAdd = findViewById(R.id.btnAdd);
        listTask = findViewById(R.id.listTask);
        searchView = findViewById(R.id.searchView);

        arrayList = new ArrayList<>();
        loadTasks();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listTask.setAdapter(adapter);

        btnAdd.setOnClickListener(this::addTask);

        listTask.setOnItemClickListener((parent, view, position, id) -> {
            // Limpiar filtro
            searchView.setQuery("", false);
            searchView.clearFocus();

            String selectedTask = adapter.getItem(position);
            int realIndex = arrayList.indexOf(selectedTask);

            if (realIndex != -1) {
                Intent intent = new Intent(MainActivity.this, EditTaskActivity.class);
                intent.putExtra("index", realIndex);
                intent.putExtra("task", selectedTask);
                startActivityForResult(intent, REQUEST_EDIT);
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }
            @Override public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private void addTask(View view) {
        String task = etTask.getText().toString().trim();
        if (!task.isEmpty()) {
            arrayList.add(task);
            adapter.notifyDataSetChanged();
            saveTasks();
            etTask.setText("");
        } else {
            Toast.makeText(this, "Coloque una tarea", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            int index = data.getIntExtra("index", -1);

            if (index != -1) {
                if (data.hasExtra("delete")) {
                    arrayList.remove(index);
                    saveTasks();
                    Toast.makeText(this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                } else if (data.hasExtra("updatedTask")) {
                    String updatedTask = data.getStringExtra("updatedTask");
                    arrayList.set(index, updatedTask);
                    saveTasks();
                    Toast.makeText(this, "Tarea actualizada", Toast.LENGTH_SHORT).show();
                }

                adapter.notifyDataSetChanged();
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        }
    }
}
