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
@Entity(name = "friends")
public class Friend extends BaseEntity {

    @Column(name = "friend_id")
    private int idFriend;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
