package com.example.healthgenie.base.initData;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;


//@Profile("dev")
//@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(
            EntityManager em,
            UserRepository userRepository,
            CommunityPostRepository communityPostRepository
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                for(int i=1; i<=10; i++) {
                    User user = createUser(i + "@test.com", "test" + i);
                    CommunityPost post = createPost(i + "번째 게시글 제목", i + "번째 게시글 내용", user);
                }
            }

            private User createUser(String email, String name) {
                User user = User.builder()
                        .email(email)
                        .name(name)
                        .role(Role.EMPTY)
                        .authProvider(AuthProvider.EMPTY)
                        .build();

                return userRepository.save(user);
            }

            private CommunityPost createPost(String title, String content, User user) {
                CommunityPost post = CommunityPost.builder()
                        .title(title)
                        .content(content)
                        .member(user)
                        .build();

                return communityPostRepository.save(post);
            }
        };
    }


}
/*
      Book book1 = createBook("JPA1 BOOK", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 BOOK", 20000, 100);
            em.persist(book2);
 */
