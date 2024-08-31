package org.zerocool.postservice.common.mapper;

import org.springframework.beans.BeanUtils;
import org.zerocool.postservice.adapter.entity.Post;
import org.zerocool.postservice.domain.dto.PostDTO;

public class MapperPost {
    public static PostDTO postEntityToDTO(Post post){
        PostDTO postDTO = new PostDTO();
        BeanUtils.copyProperties(post, postDTO);
        return postDTO;
    }

    public static Post postDTOToEntity (PostDTO postDTO){
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        return post;
    }
}
