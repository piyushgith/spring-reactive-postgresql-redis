package com.example.postgresql.redis.service;

import com.example.postgresql.redis.dto.TutorialDTO;
import com.example.postgresql.redis.entity.Tutorial;
import com.example.postgresql.redis.repository.TutorialRepository;
import com.example.postgresql.redis.util.ReactiveRedisComponent;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class TutorialService {

    public static final String REDIS_KEY = "RK";

    @Autowired
    private ReactiveRedisComponent reactiveRedisComponent;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    TutorialRepository tutorialRepository;


    public Flux<Tutorial> findAll() {
        return tutorialRepository.findAll();
    }

    public Flux<Tutorial> findByTitleContaining(String title) {
        return tutorialRepository.findByTitleContaining(title);
    }

    public Mono<Tutorial> findById(int id) {
        Mono<Tutorial> tutorialMono = tutorialRepository.findById(id);
        //Mono<TutorialDTO> tutorialDTOMono = tutorialMono.map(x ->modelMapper.map(x, TutorialDTO.class));
        //tutorialDTOMono.subscribe(System.out::println);
        tutorialMono.flatMap(x ->
                reactiveRedisComponent.setKeyValues(REDIS_KEY, modelMapper.map(x, TutorialDTO.class)));
        return tutorialMono;
    }

    public Mono<Tutorial> save(Tutorial tutorial) {
        return tutorialRepository.save(tutorial);
    }

    public Mono<Tutorial> update(int id, Tutorial tutorial) {
        return tutorialRepository.findById(id).map(Optional::of).defaultIfEmpty(Optional.empty())
                .flatMap(optionalTutorial -> {
                    if (optionalTutorial.isPresent()) {
                        tutorial.setId(id);
                        return tutorialRepository.save(tutorial);
                    }

                    return Mono.empty();
                });
    }

    public Mono<Void> deleteById(int id) {
        return tutorialRepository.deleteById(id);
    }

    public Mono<Void> deleteAll() {
        return tutorialRepository.deleteAll();
    }

    public Flux<Tutorial> findByPublished(boolean isPublished) {
        return tutorialRepository.findByPublished(isPublished);
    }
}
