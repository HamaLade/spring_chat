package com.hs.persistence.entity.chatroom;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "room")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50, columnDefinition = "VARCHAR")
    private String roomName;

    @Column(nullable = false, columnDefinition = "TINYINT")
    @ColumnDefault("0")
    private Boolean isPrivate;

    @OneToMany(mappedBy = "room", fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Participant> participants = new ArrayList<>();

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    private LocalDateTime updateDate;

    public Room(String roomName, boolean isPrivate) {
        this.roomName = roomName;
        this.isPrivate = isPrivate;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
        participant.relationWithRoom(this);
    }


}
