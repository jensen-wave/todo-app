package com.jensen.todo.controller;

import com.jensen.todo.dto.TodoDTO;
import com.jensen.todo.service.TodoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private TodoService todoService;


    @PostMapping
    public ResponseEntity<TodoDTO> addTodo(@RequestBody TodoDTO todoDTO){
        TodoDTO todoDTO1 = todoService.addTodo(todoDTO);
        return new ResponseEntity<>(todoDTO1, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getTodo(@PathVariable ("id") Long todoId){
        TodoDTO todo = todoService.getTodo(todoId);
        return new ResponseEntity<>(todo,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<TodoDTO>> getAllToDo(){
        List<TodoDTO> allTodo = todoService.getAllTodo();
        return ResponseEntity.ok(allTodo);
    }

    @PutMapping("{id}")
    public ResponseEntity<TodoDTO> updateTodo(@RequestBody TodoDTO todoDTO,@PathVariable Long id){
        TodoDTO updatedTodo = todoService.updateTodo(todoDTO, id);
        return ResponseEntity.ok(updatedTodo);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteTodo(@PathVariable ("id") Long todoId){
        todoService.deleteTodo(todoId);
        return ResponseEntity.ok("Todo deleted successfully");
    }

    @PatchMapping("/{id}/completed")
    public ResponseEntity<TodoDTO> completeTodo(@PathVariable("id") Long todoId){
        TodoDTO todoDTO = todoService.completeTodo(todoId);
        return ResponseEntity.ok(todoDTO);
    }

    @PatchMapping("/{id}/incomplete")
    public ResponseEntity<TodoDTO> imcompleteTodo(@PathVariable("id") Long todoId){
        TodoDTO todoDTO = todoService.imcompleteTodo(todoId);
        return ResponseEntity.ok(todoDTO);
    }
}
