package com.example.healthgenie.repository;

import com.example.healthgenie.entity.CommunityPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CommunityPostRepositoryTest {

    @Autowired
    CommunityPostRepository postRepository;


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
}
