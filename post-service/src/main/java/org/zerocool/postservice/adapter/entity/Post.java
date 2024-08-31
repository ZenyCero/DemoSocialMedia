package org.zerocool.postservice.adapter.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collation = "posts")
public class Post {
    @Id
    private Long id;
    private Long user_id;
    private String content;
    private Date created_at;
    private Date updated_at;
}
