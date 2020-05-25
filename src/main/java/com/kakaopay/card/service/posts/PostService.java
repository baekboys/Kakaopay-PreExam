package com.kakaopay.card.service.posts;

import com.kakaopay.card.domain.posts.Posts;
import com.kakaopay.card.domain.posts.PostsRepository;
import com.kakaopay.card.web.dto.PostsListResponseDto;
import com.kakaopay.card.web.dto.PostsResponseDto;
import com.kakaopay.card.web.dto.PostsSaveRequestDto;
import com.kakaopay.card.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("해당사용자가 없습니다. id=" + id) );

        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow( () -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + id) );

        postsRepository.delete(posts);
    }

    public PostsResponseDto findById(Long id) {;
        Posts entity = postsRepository.findById(id).orElseThrow( () ->  new IllegalArgumentException("해당사용자가 없습니다. id=" + id) );

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly=true)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream().map(PostsListResponseDto::new).collect(Collectors.toList());
    }
}
