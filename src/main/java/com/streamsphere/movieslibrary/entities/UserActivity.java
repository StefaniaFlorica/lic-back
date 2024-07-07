package com.streamsphere.movieslibrary.entities;

import com.streamsphere.movieslibrary.entities.enums.ActivityType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ActivityType type;

    private String action;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String movieTitle;

    private Long movieId;

    private Long reviewId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private AppUser user;
}
