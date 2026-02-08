package com.events.superapp.modules.movies.entity;

import com.events.superapp.common.entity.BaseEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "movies")
@PrimaryKeyJoinColumn(name = "event_id")
@Getter
@Setter
public class Movie extends BaseEvent {
    private String posterUrl;
    private String imdbRating;
}
