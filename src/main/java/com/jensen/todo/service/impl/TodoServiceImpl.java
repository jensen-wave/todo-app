package com.jensen.todo.service.impl;

import com.jensen.todo.dto.TodoDTO;
import com.jensen.todo.entity.Todo;
import com.jensen.todo.exception.ResourceNotFoundException;
import com.jensen.todo.repository.TodoRepository;
import com.jensen.todo.service.TodoService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Service
public class TodoServiceImpl implements TodoService {

    private TodoRepository todoRepository;
    private ModelMapper modelMapper;

    @Override
    public TodoDTO addTodo(TodoDTO todoDTO) {

        Todo todo = modelMapper.map(todoDTO, Todo.class);

        Todo saveTodo = todoRepository.save(todo);

        TodoDTO todoDTO1 = modelMapper.map(saveTodo, TodoDTO.class);

        return todoDTO1;
    }

    @Override
    public TodoDTO getTodo(Long id) {
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
        return modelMapper.map(todo, TodoDTO.class);
    }

    @Override
    public List<TodoDTO> getAllTodo() {
        List<Todo> todos = todoRepository.findAll();
        List<TodoDTO> collect = todos.stream().map(todo -> modelMapper.map(todo, TodoDTO.class)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public TodoDTO updateTodo(TodoDTO todoDTO, Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
        todo.setTitle(todoDTO.getTitle());
        todo.setDescription(todoDTO.getDescription());
        todo.setCompleted(todoDTO.getCompleted());
        Todo save = todoRepository.save(todo);
        return modelMapper.map(save, TodoDTO.class);
    }

    @Override
    public void deleteTodo(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
        todoRepository.deleteById(id);
    }

    @Override
    public TodoDTO completeTodo(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
        todo.setCompleted(Boolean.TRUE);
        Todo saveTodo = todoRepository.save(todo);

        return modelMapper.map(saveTodo, TodoDTO.class);
    }

    @Override
    public TodoDTO imcompleteTodo(Long id) {
        Todo todo = todoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Todo not found with id:" + id));
        todo.setCompleted(Boolean.FALSE);
        Todo saveTodo = todoRepository.save(todo);

        return modelMapper.map(saveTodo, TodoDTO.class);
    }
}
