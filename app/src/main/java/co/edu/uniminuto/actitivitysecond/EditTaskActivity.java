package co.edu.uniminuto.actitivitysecond;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class EditTaskActivity extends AppCompatActivity {

    private EditText etEditTask;
    private Button btnUpdate, btnDelete;

    private String taskText;
    private int taskIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        etEditTask = findViewById(R.id.etEditTask);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        taskText = getIntent().getStringExtra("task");
        taskIndex = getIntent().getIntExtra("index", -1);

        etEditTask.setText(taskText);

        btnUpdate.setOnClickListener(v -> {
            String updatedText = etEditTask.getText().toString().trim();
            if (!updatedText.isEmpty()) {
                Intent result = new Intent();
                result.putExtra("index", taskIndex);
                result.putExtra("updatedTask", updatedText);
                setResult(Activity.RESULT_OK, result);
                finish();
            }
        });

        btnDelete.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra("index", taskIndex);
            result.putExtra("delete", true);
            setResult(Activity.RESULT_OK, result);
            finish();
        });
    }
}
