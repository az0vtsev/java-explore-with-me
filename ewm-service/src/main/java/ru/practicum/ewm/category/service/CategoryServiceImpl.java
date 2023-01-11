package ru.practicum.ewm.category.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.CategoryMapper;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.model.EventState;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.NotValidDataException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;
    private final EventRepository eventRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    @Override
    public CategoryDto createCategory(NewCategoryDto categoryDto) {
        Category newCategory = CategoryMapper.mapToCategory(categoryDto);
        return CategoryMapper.mapToCategoryDto(repository.save(newCategory));
    }

    @Override
    public void deleteCategory(int catId) {
        if (!repository.existsById(catId)) {
            throw new NotFoundException("Category id=" + catId + "was not found");
        }
        List<Event> categoryEvents = eventRepository
                .findByCategoryAndStates(catId, List.of(EventState.PENDING, EventState.PUBLISHED));
        if (!categoryEvents.isEmpty()) {
            List<Integer> categoryEventsIds = categoryEvents
                    .stream()
                    .map(Event::getId)
                    .collect(Collectors.toList());
            throw new NotValidDataException("Category id=" + catId + " can't be deleted while has events ids ="
                    + Arrays.toString(categoryEventsIds.toArray()));
        }
        repository.deleteById(catId);

    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto) {
        if (!repository.existsById(categoryDto.getId())) {
            throw new NotFoundException("Category id=" + categoryDto.getId() + " was not found");
        }
        Category updateCategory = CategoryMapper.mapToCategory(categoryDto);
        return CategoryMapper.mapToCategoryDto(repository.save(updateCategory));
    }

    @Override
    public List<CategoryDto> getCategories(int from, int size) {
        int page = from / size;
        PageRequest pageRequest = PageRequest.of(page, size);
        List<CategoryDto> categories = repository.findAll(pageRequest)
                    .stream()
                    .map(CategoryMapper::mapToCategoryDto)
                    .collect(Collectors.toList());
        return categories;
    }

    @Override
    public CategoryDto getCategory(int catId) {
    Category category = repository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id=" + catId + " was not found"));
    return CategoryMapper.mapToCategoryDto(category);
    }
}
