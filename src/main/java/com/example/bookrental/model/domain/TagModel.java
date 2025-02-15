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
//@Schema(title="태그")
public class TagModel {

    //@Schema(title="태그 고유 ID")
    private Long tagId;

    //@Schema(title="태그명")
    private String name;

    public static TagModel fromEntity(Tags tag) {
        return TagModel.builder()
                .tagId(tag.getTagId())
                .name(tag.getName())
                .build();
    }
}