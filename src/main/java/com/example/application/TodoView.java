package com.example.application;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import java.time.format.DateTimeFormatter;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Paragraph;


@Route("")
public class TodoView extends VerticalLayout {

    private TodoRepo repo;

    public TodoView(TodoRepo repo) {
        this.repo = repo;

        var task = new TextField();
        var button = new Button("Add");
        var todos = new HorizontalLayout(); // Changed to HorizontalLayout
        todos.setWidthFull(); // Ensure it spans the full width
        todos.setPadding(false);
        

        

        todos.setPadding(false);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.addClickShortcut(Key.ENTER);
        button.addClickListener(click -> {
           var todo = repo.save(new Todo(task.getValue()));
            todos.add(createCheckbox(todo));
            task.clear();
        });

        repo.findAll().forEach(todo -> todos.add(createCheckbox(todo)));

        add(
                new H1("To Do"),
                new HorizontalLayout(task, button),
                todos
        );
    }
    

    private Component createCheckbox(Todo todo) {
        Div todoItemDiv = new Div();
        todoItemDiv.addClassNames("todo-item");
    
        // Checkbox for the todo task
        Checkbox checkbox = new Checkbox(todo.getTask(), todo.isDone());
        checkbox.addValueChangeListener(e -> {
            todo.setDone(e.getValue());
            repo.save(todo);
        });
    
        // Paragraph for the creation time
        Paragraph creationTimeParagraph = new Paragraph("Created on: " +
            todo.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        creationTimeParagraph.addClassNames("creation-time");
    
        // Delete button
        Button deleteButton = new Button("Delete");
        deleteButton.addClassNames("delete-button");
        deleteButton.addClickListener(click -> {
            repo.delete(todo);
            // Use Vaadin's API to remove the component
            todoItemDiv.getElement().removeFromParent();
        });
    
        // Add components to the todo item div
        todoItemDiv.add(checkbox, creationTimeParagraph, deleteButton);
        todoItemDiv.getStyle()
            .set("border", "1px solid #ccc")
            .set("border-radius", "4px")
            .set("padding", "10px")
            .set("margin-bottom", "10px")
            .set("background-color", "#f9f9f9");
    
        return todoItemDiv;
    }
    
}
