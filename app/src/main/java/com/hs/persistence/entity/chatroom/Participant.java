package com.hs.persistence.entity.chatroom;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Getter
@Table(name = "participant")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participant {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

    @Column(nullable = false)
    private Long memberId;

    private Boolean invitable;

    public Participant(long memberId, boolean invitable) {
        this.memberId = memberId;
        this.invitable = invitable;
    }

    public void relationWithRoom(Room room) {
        if (this.room != null) {
            this.room.getParticipants().remove(this);
        }

        this.room = room;
        if (room != null) {
            room.getParticipants().add(this);
        }
    }
}
