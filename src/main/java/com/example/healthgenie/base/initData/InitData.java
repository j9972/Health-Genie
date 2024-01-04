package com.example.healthgenie.base.initData;

import com.example.healthgenie.boundedContext.community.entity.CommunityPost;
import com.example.healthgenie.boundedContext.community.repository.CommunityPostRepository;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.user.entity.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;

import static com.example.healthgenie.boundedContext.routine.entity.Day.*;
import static com.example.healthgenie.boundedContext.routine.entity.Level.*;


@Profile("dev")
//@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(
            UserRepository userRepository,
            CommunityPostRepository communityPostRepository,
            RoutineRepository routineRepository
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
                for(int i=1; i<=10; i++) {
                    User user = createUser(i + "@test.com", "test" + i);
                    CommunityPost post = createPost(i + "번째 게시글 제목", i + "번째 게시글 내용", user);
                }

                createRoutine(BEGINNER, MONDAY, "초급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","스쿼트",120,4,12);
                createRoutine(BEGINNER, MONDAY, "초급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 프레스",120,4,12);
                createRoutine(BEGINNER, MONDAY, "초급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 익스텐션",120,4,12);
                createRoutine(BEGINNER, TUESDAY, "초급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","밀리터리 프레스", 10,4,12);
                createRoutine(BEGINNER, TUESDAY, "초급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","사이드 레터럴 레이즈", 10,4,12);
                createRoutine(BEGINNER, TUESDAY, "초급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","페이스 풀", 10,4,15);
                createRoutine(BEGINNER, WEDNESDAY, "초급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","인클라인 덤벨 프레스",4,4,15);
                createRoutine(BEGINNER, WEDNESDAY, "초급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","체스트 프레스",4,4,15);
                createRoutine(BEGINNER, WEDNESDAY, "초급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두","라잉 트라이셉스 익스텐션",4,4,15);
                createRoutine(BEGINNER, THURSDAY, "초급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","시티드 로우",4, 4,15);
                createRoutine(BEGINNER, THURSDAY, "초급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","래풀다운",4, 4,15);
                createRoutine(BEGINNER, THURSDAY, "초급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","덤벨 컬",4, 4,15);

                createRoutine(INTERMEDIATE, MONDAY, "중급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","스쿼트",120,4,12);
                createRoutine(INTERMEDIATE, MONDAY, "중급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 프레스",120,4,12);
                createRoutine(INTERMEDIATE, MONDAY, "중급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 익스텐션",120,4,12);
                createRoutine(INTERMEDIATE, TUESDAY, "중급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","밀리터리 프레스", 10,4,12);
                createRoutine(INTERMEDIATE, TUESDAY, "중급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","사이드 레터럴 레이즈", 10,4,12);
                createRoutine(INTERMEDIATE, TUESDAY, "중급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","페이스 풀", 10,4,15);
                createRoutine(INTERMEDIATE, WEDNESDAY, "중급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","인클라인 덤벨 프레스",4,4,15);
                createRoutine(INTERMEDIATE, WEDNESDAY, "중급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","체스트 프레스",4,4,15);
                createRoutine(INTERMEDIATE, WEDNESDAY, "중급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두","라잉 트라이셉스 익스텐션",4,4,15);
                createRoutine(INTERMEDIATE, THURSDAY, "중급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","시티드 로우",4, 4,15);
                createRoutine(INTERMEDIATE, THURSDAY, "중급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","래풀다운",4, 4,15);
                createRoutine(INTERMEDIATE, THURSDAY, "중급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","덤벨 컬",4, 4,15);
                createRoutine(INTERMEDIATE, FRIDAY, "중급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴","팩덱 플라이",3, 4,15);
                createRoutine(INTERMEDIATE, FRIDAY, "중급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴","인클라인 바벨 프레스",4,4,15);
                createRoutine(INTERMEDIATE, FRIDAY, "중급자님님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴","케이블",4,4,15);

                createRoutine(EXPERT, MONDAY, "고급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","스쿼트",120,4,12);
                createRoutine(EXPERT, MONDAY, "고급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 프레스",120,4,12);
                createRoutine(EXPERT, MONDAY, "고급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체","레그 익스텐션",120,4,12);
                createRoutine(EXPERT, TUESDAY, "고급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","밀리터리 프레스", 10,4,12);
                createRoutine(EXPERT, TUESDAY, "고급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","사이드 레터럴 레이즈", 10,4,12);
                createRoutine(EXPERT, TUESDAY, "고급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠","페이스 풀", 10,4,15);
                createRoutine(EXPERT, WEDNESDAY, "고급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","인클라인 덤벨 프레스",4,4,15);
                createRoutine(EXPERT, WEDNESDAY, "고급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴","체스트 프레스",4,4,15);
                createRoutine(EXPERT, WEDNESDAY, "고급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두","라잉 트라이셉스 익스텐션",4,4,15);
                createRoutine(EXPERT, THURSDAY, "고급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","시티드 로우",4, 4,15);
                createRoutine(EXPERT, THURSDAY, "고급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","래풀다운",4, 4,15);
                createRoutine(EXPERT, THURSDAY, "고급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두","덤벨 컬",4, 4,15);
                createRoutine(EXPERT, FRIDAY, "고급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴","팩덱 플라이",3, 4,15);
                createRoutine(EXPERT, FRIDAY, "고급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴","인클라인 바벨 프레스",4,4,15);
                createRoutine(EXPERT, FRIDAY, "고급자님님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴","케이블",4,4,15);
                createRoutine(EXPERT, SATURDAY, "고급자님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "삼두, 이두","덤벨 오버헤드 익스텐션",3, 4,15);
                createRoutine(EXPERT, SATURDAY, "고급자님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "삼두, 이두","바벨 컬",3, 4,15);
                createRoutine(EXPERT, SATURDAY, "고급자님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "삼두, 이두","헤머컬",3, 4,15);
            }

            private User createUser(String email, String name) {
                User user = User.builder()
                        .email(email)
                        .name(name)
                        .nickname(name)
                        .role(Role.EMPTY)
                        .authProvider(AuthProvider.EMPTY)
                        .build();

                return userRepository.save(user);
            }

            private CommunityPost createPost(String title, String content, User user) {
                CommunityPost post = CommunityPost.builder()
                        .title(title)
                        .content(content)
                        .writer(user)
                        .build();

                return communityPostRepository.save(post);
            }

            private Routine createRoutine(Level level, Day day, String content, String part, String workoutName, int kg, int sets, int reps) {
                WorkoutRecipe recipe = new WorkoutRecipe(workoutName, kg ,sets, reps);
                Routine routine = Routine.builder()
                        .level(level)
                        .day(day)
                        .content(content)
                        .parts(part)
                        .workoutRecipe(recipe)
                        .build();

                return routineRepository.save(routine);
            }
        };
    }

}