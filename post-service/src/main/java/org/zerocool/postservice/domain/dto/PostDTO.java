package org.zerocool.postservice.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private Long user_id;
    private Long media_id;
    private String content;
    private Date created_at;
    private Date updated_at;
}
