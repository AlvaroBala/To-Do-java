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
import com.vaadin.flow.component.html.Span;
import java.time.format.DateTimeFormatter;


@Route("")
public class TodoView extends VerticalLayout {

    private TodoRepo repo;

    public TodoView(TodoRepo repo) {
        this.repo = repo;

        var task = new TextField();
        var button = new Button("Add");
        var todos = new VerticalLayout();

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
        // Checkbox for the todo task
        Checkbox checkbox = new Checkbox(todo.getTask(), todo.isDone());
        checkbox.addValueChangeListener(e -> {
            todo.setDone(e.getValue());
            repo.save(todo);
        });
        
        // Span for the creation time
        Span creationTimeLabel = new Span("Created on: " + 
            todo.getCreationTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        
        // Delete button
        Button deleteButton = new Button("Delete");
        // No need to define the listener here, it will be added after todoLayout
        
        // Use HorizontalLayout for the checkbox and delete button
        HorizontalLayout actionsLayout = new HorizontalLayout(checkbox, deleteButton);
        actionsLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        
        // Use VerticalLayout to stack the timestamp label and the actions horizontally
        VerticalLayout todoLayout = new VerticalLayout(creationTimeLabel, actionsLayout);
        todoLayout.setPadding(false); 
        
        // Now set the click listener for the delete button
        deleteButton.addClickListener(click -> {
            repo.delete(todo);
            todoLayout.getParent().ifPresent(parentComponent -> {
                if (parentComponent instanceof VerticalLayout) {
                    ((VerticalLayout) parentComponent).remove(todoLayout);
                }
            });
        });
    
        return todoLayout;
    }
    
}
