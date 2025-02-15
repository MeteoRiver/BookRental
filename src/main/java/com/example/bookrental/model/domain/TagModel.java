package com.example.bookrental.model.domain;

import com.example.bookrental.model.entity.Tags;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagModel {

    private Long tagId;

    private String name;

    public static TagModel fromEntity(Tags tag) {
        return TagModel.builder()
                .tagId(tag.getTagId())
                .name(tag.getName())
                .build();
    }
}