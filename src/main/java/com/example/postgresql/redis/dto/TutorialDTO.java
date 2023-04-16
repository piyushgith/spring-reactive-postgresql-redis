package com.example.postgresql.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@RedisHash
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TutorialDTO implements Serializable {

    private int id;

    private String title;

    private String description;

    private boolean published;

    @Override
    public String toString() {
        return "TutorialDTO{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", published=" + published +
                '}';
    }
}
