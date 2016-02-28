package com.example.job94_000.lists;

import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {

    /**
     * Create global variables
     */
    ArrayList<String> todoList = new ArrayList<>();
    ListView indexListView;
    EditText inputText;
    Button addButton;
    ArrayAdapter<String> myAdapter;

    /**
     * onSaveInstanceState() saves the state of the activity when it gets terminated.
     * It also writes a file with the items of the list stored, so it can be reaccessed later.
     * @param outState defines the state of the program
     */
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save todolist items
        outState.putStringArrayList("todolist", todoList);

        // write to a file
        try {
            PrintStream out = new PrintStream(openFileOutput("todolist.txt", MODE_PRIVATE));
            for (String item : todoList) {
                out.println(item);
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * onCreate() checks for earlier states of the program to find data for the todolist and
     * then creates a program that is a list of items which you can edit by adding items or
     * deleting items.
     * @param savedInstanceState; if available is the last known state of the program
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Check for any previous states and edit todoList
        if(savedInstanceState != null) {
            todoList = savedInstanceState.getStringArrayList("todolist");
        }

//        Try to get data from "todolist.txt" and if this is not possible, create an example
        else {
            try {
                Scanner scan = new Scanner(openFileInput("todolist.txt"));
                while (scan.hasNextLine()) {
                    String line = scan.nextLine();
                    todoList.add(line);
                }
            } catch (Exception e) {
                todoList.add("This is an example:"); todoList.add("Get groceries");
                todoList.add("Call my grandmother"); todoList.add("Finish homework");
            }

        }

//        Finding the ListView and adding an ArrayAdapter for the items in the list
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoList);
        indexListView = (ListView) findViewById(R.id.indexList);
        indexListView.setLongClickable(true);
        indexListView.setAdapter(myAdapter);

//        Make the user be able to remove items from the list by long clicking them
        indexListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                todoList.remove(position);
                myAdapter.notifyDataSetChanged();
                return true;
            }
        });

//        Find the EditText and Button to add items to the list with the String put in inputText
        inputText = (EditText) findViewById(R.id.inputText);
        addButton = (Button) findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todoList.add(inputText.getText().toString());
                myAdapter.notifyDataSetChanged();
            }
        });
    }

}
