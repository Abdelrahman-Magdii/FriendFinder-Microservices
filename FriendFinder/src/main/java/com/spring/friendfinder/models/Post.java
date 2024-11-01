package com.spring.friendfinder.models;

import com.spring.commonlib.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "posts")
public class Post extends BaseEntity {

    @Column(name = "image_path")
    private String image;

    @Column(name = "text")
    private String text;

    @Column(name = "likes")
    private int likes;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
