package com.example.healthgenie.repository;

import com.example.healthgenie.domain.community.entity.CommunityPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommunityPostRepositoryTest {

    @Autowired
    CommunityPostRepository postRepository;

/*
    @Test
    public void addPost(){

        //given
        CommunityPost post = CommunityPost.builder().build();

        //when
        CommunityPost result = postRepository.save(post);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    public void getPost(){
        //given
        CommunityPost post = CommunityPost.builder().build();
        CommunityPost saveResult = postRepository.save(post);

        //when
        Optional<CommunityPost> optionalResult = postRepository.findById(saveResult.getId());


        //then
        assertThat(optionalResult.get().getId()).isNotNull();


    }
    @Test
    public void getPostList(){

        //given
        for(int i=0;i<30;i++){
            CommunityPost post = CommunityPost.builder().build();

            CommunityPost saveResult = postRepository.save(post);
        }
        //when
        Page<CommunityPost> pageList = postRepository.findAll(PageRequest.of(0,20,Sort.by(Sort.Direction.DESC,"createdDate")));


        //then
        assertThat(pageList).isNotNull();
        assertThat(pageList).isNotEmpty();
        assertThat(pageList.getTotalElements()).isEqualTo(30);
        assertThat(pageList.getContent().size()).isEqualTo(20);
        assertThat(pageList.getTotalPages()).isEqualTo(2);
        assertThat(pageList.getContent().get(0).getId()).isEqualTo(30);
    }

 */
}
