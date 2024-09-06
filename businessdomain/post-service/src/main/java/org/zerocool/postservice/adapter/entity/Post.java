package org.zerocool.postservice.adapter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "posts")
public class Post {
    @Transient
    public static final String SEQUENCE_NAME = "posts_sequence";
    @Id
    private Long id;
    private Long idUser;
    private String content;
    private Date created;
    private Date updated;
}
