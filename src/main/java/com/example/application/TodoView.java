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
    
        Checkbox checkbox = new Checkbox(todo.getTask(), todo.isDone());
        checkbox.addValueChangeListener(e -> {
            todo.setDone(e.getValue());
            repo.save(todo);
        });
    
        Button deleteButton = new Button("Delete");
        deleteButton.addClickListener(click -> {
            repo.delete(todo);
            // Cast the parent component to HorizontalLayout and remove it from its own parent.
            checkbox.getParent().ifPresent(parentComponent -> {
                Component grandParentComponent = parentComponent.getParent().orElse(null);
                if (grandParentComponent instanceof VerticalLayout) {
                    ((VerticalLayout) grandParentComponent).remove(parentComponent);
                }
            });
        });
    
        HorizontalLayout todoLayout = new HorizontalLayout(checkbox, deleteButton);
        todoLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        return todoLayout;
    }
    
}
