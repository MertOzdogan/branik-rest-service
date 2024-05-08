package com.branik.updater.database.model.db;

import lombok.*;

import javax.persistence.*;

@Entity(name = "players")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class PlayerEntity extends BaseEntity {
    @EqualsAndHashCode.Include
    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private TeamEntity teamEntity;

}
