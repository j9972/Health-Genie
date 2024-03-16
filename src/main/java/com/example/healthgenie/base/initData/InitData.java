package com.example.healthgenie.base.initData;

import static com.example.healthgenie.boundedContext.routine.entity.Day.FRIDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.MONDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.SATURDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.THURSDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.TUESDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Day.WEDNESDAY;
import static com.example.healthgenie.boundedContext.routine.entity.Level.BEGINNER;
import static com.example.healthgenie.boundedContext.routine.entity.Level.EXPERT;
import static com.example.healthgenie.boundedContext.routine.entity.Level.INTERMEDIATE;

import com.example.healthgenie.boundedContext.community.comment.entity.Comment;
import com.example.healthgenie.boundedContext.community.comment.repository.CommentRepository;
import com.example.healthgenie.boundedContext.community.post.entity.Post;
import com.example.healthgenie.boundedContext.community.post.repository.PostRepository;
import com.example.healthgenie.boundedContext.matching.entity.Matching;
import com.example.healthgenie.boundedContext.matching.entity.MatchingUser;
import com.example.healthgenie.boundedContext.matching.entity.enums.MatchingState;
import com.example.healthgenie.boundedContext.matching.repository.MatchingRepository;
import com.example.healthgenie.boundedContext.matching.repository.MatchingUserRepository;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewRepository;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.todo.repository.TodoRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.AuthProvider;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;


@Profile("dev")
@Configuration
public class InitData {

    @Bean
    CommandLineRunner init(
            UserRepository userRepository,
            PostRepository postRepository,
            RoutineRepository routineRepository,
            CommentRepository commentRepository,
            MatchingRepository matchingRepository,
            TodoRepository todoRepository,
            PtReviewRepository ptReviewRepository,
            PtProcessRepository ptProcessRepository,
            MatchingUserRepository matchingUserRepository
    ) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) {
                User user = createUser("user@test.com", "user", Role.USER); // 일반유저
                User trainer = createUser("trainer@test.com", "trainer", Role.TRAINER); // 트레이너

                for (int i = 1; i <= 2000; i++) {
                    User writer = createUser(i + "@test.com", "test" + i, Role.EMPTY);
                    Post post = createPost(i + "번째 게시글 제목", i + "번째 게시글 내용", writer);
                    Comment comment = createComment(post, post.getId() + "번째 게시글의 댓글", writer);
                    Matching matching = createMatching(
                            LocalDateTime.of(2024, (i % 12) + 1, (i % 30) + 1, i % 24, i % 60, 0), "매칭 설명", "장소",
                            MatchingState.DEFAULT);
                    MatchingUser matchingUser = createMatchingUser(user, matching);
                    MatchingUser matchingTrainer = createMatchingUser(trainer, matching);

                    Todo todo = createTodo(LocalDate.now(), LocalTime.now(), i + "title", i + "description", user);
                    PtReview review = createReview(i + "content", i + "reason", 5.0, user, trainer);
                    PtProcess process = createProcess(LocalDate.now(), i + "title", i + "content", user, trainer);
                }

                createGenieRoutine();
            }

            private MatchingUser createMatchingUser(User user, Matching matching) {
                MatchingUser matchinguser = MatchingUser.builder()
                        .user(user)
                        .matching(matching)
                        .build();

                return matchingUserRepository.save(matchinguser);
            }

            private Matching createMatching(LocalDateTime date, String description, String place, MatchingState state) {
                Matching matching = Matching.builder()
                        .date(date)
                        .description(description)
                        .place(place)
                        .state(state)
                        .build();

                return matchingRepository.save(matching);
            }

            private Comment createComment(Post post, String content, User writer) {
                Comment comment = Comment.builder()
                        .post(post)
                        .content(content)
                        .writer(writer)
                        .build();

                return commentRepository.save(comment);
            }

            private User createUser(String email, String name, Role role) {
                User user = User.builder()
                        .email(email)
                        .name(name)
                        .nickname(name)
                        .role(role)
                        .authProvider(AuthProvider.EMPTY)
                        .build();

                return userRepository.save(user);
            }

            private Post createPost(String title, String content, User user) {
                Post post = Post.builder()
                        .title(title)
                        .content(content)
                        .writer(user)
                        .build();

                return postRepository.save(post);
            }

            private Routine createRoutine(Level level, Day day, String content, String part, String workoutName, int kg,
                                          int sets, int reps) {
                WorkoutRecipe recipe = new WorkoutRecipe(workoutName, kg, sets, reps);
                Routine routine = Routine.builder()
                        .level(level)
                        .day(day)
                        .content(content)
                        .parts(part)
                        .workoutRecipe(recipe)
                        .build();

                return routineRepository.save(routine);
            }

            private Todo createTodo(LocalDate date, LocalTime time, String title, String description, User user) {

                Todo todo = Todo.builder()
                        .date(date)
                        .time(time)
                        .title(title)
                        .description(description)
                        .member(user)
                        .build();

                return todoRepository.save(todo);
            }

            private PtProcess createProcess(LocalDate date, String title, String content,
                                            List<PtProcessPhoto> photos,
                                            User user, User trainer) {
                PtProcess process = PtProcess.builder()
                        .date(date)
                        .title(title)
                        .content(content)
                        .ptProcessPhotos(photos)
                        .ptProcessPhotos(new ArrayList<>())
                        .member(user)
                        .trainer(trainer)
                        .build();

                return ptProcessRepository.save(process);
            }

            public PtProcess createProcess(LocalDate date, String title, String content,
                                           User user, User trainer) {
                return createProcess(date, title, content, null, user, trainer);
            }

            private PtReview createReview(String content, String stopReason, Double reviewScore,
                                          User user, User trainer) {
                PtReview review = PtReview.builder()
                        .content(content)
                        .stopReason(stopReason)
                        .reviewScore(reviewScore)
                        .member(user)
                        .trainer(trainer)
                        .build();

                return ptReviewRepository.save(review);
            }

            private void createGenieRoutine() {
                createRoutine(BEGINNER, MONDAY, "초급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "스쿼트", 120, 4, 12);
                createRoutine(BEGINNER, MONDAY, "초급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "레그 프레스", 120, 4, 12);
                createRoutine(BEGINNER, MONDAY, "초급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "레그 익스텐션", 120, 4, 12);
                createRoutine(BEGINNER, TUESDAY, "초급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "밀리터리 프레스", 10, 4, 12);
                createRoutine(BEGINNER, TUESDAY, "초급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "사이드 레터럴 레이즈", 10, 4, 12);
                createRoutine(BEGINNER, TUESDAY, "초급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "페이스 풀", 10, 4, 15);
                createRoutine(BEGINNER, WEDNESDAY, "초급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴", "인클라인 덤벨 프레스", 4, 4, 15);
                createRoutine(BEGINNER, WEDNESDAY, "초급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴", "체스트 프레스", 4, 4, 15);
                createRoutine(BEGINNER, WEDNESDAY, "초급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두", "라잉 트라이셉스 익스텐션", 4, 4, 15);
                createRoutine(BEGINNER, THURSDAY, "초급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "시티드 로우", 4, 4, 15);
                createRoutine(BEGINNER, THURSDAY, "초급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "래풀다운", 4, 4, 15);
                createRoutine(BEGINNER, THURSDAY, "초급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "덤벨 컬", 4, 4, 15);

                createRoutine(INTERMEDIATE, MONDAY, "중급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "스쿼트", 120, 4, 12);
                createRoutine(INTERMEDIATE, MONDAY, "중급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "레그 프레스", 120, 4, 12);
                createRoutine(INTERMEDIATE, MONDAY, "중급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "레그 익스텐션", 120, 4, 12);
                createRoutine(INTERMEDIATE, TUESDAY, "중급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "밀리터리 프레스", 10, 4, 12);
                createRoutine(INTERMEDIATE, TUESDAY, "중급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "사이드 레터럴 레이즈", 10, 4, 12);
                createRoutine(INTERMEDIATE, TUESDAY, "중급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "페이스 풀", 10, 4, 15);
                createRoutine(INTERMEDIATE, WEDNESDAY, "중급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴", "인클라인 덤벨 프레스", 4, 4,
                        15);
                createRoutine(INTERMEDIATE, WEDNESDAY, "중급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴", "체스트 프레스", 4, 4, 15);
                createRoutine(INTERMEDIATE, WEDNESDAY, "중급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두", "라잉 트라이셉스 익스텐션", 4, 4,
                        15);
                createRoutine(INTERMEDIATE, THURSDAY, "중급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "시티드 로우", 4, 4, 15);
                createRoutine(INTERMEDIATE, THURSDAY, "중급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "래풀다운", 4, 4, 15);
                createRoutine(INTERMEDIATE, THURSDAY, "중급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "덤벨 컬", 4, 4, 15);
                createRoutine(INTERMEDIATE, FRIDAY, "중급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴", "팩덱 플라이", 3, 4,
                        15);
                createRoutine(INTERMEDIATE, FRIDAY, "중급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴", "인클라인 바벨 프레스", 4,
                        4, 15);
                createRoutine(INTERMEDIATE, FRIDAY, "중급자님님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴", "케이블", 4, 4,
                        15);

                createRoutine(EXPERT, MONDAY, "고급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "스쿼트", 120, 4, 12);
                createRoutine(EXPERT, MONDAY, "고급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "레그 프레스", 120, 4, 12);
                createRoutine(EXPERT, MONDAY, "고급자님! 오늘은 하체 데이입니다! 화아팅 하세요", "하체", "레그 익스텐션", 120, 4, 12);
                createRoutine(EXPERT, TUESDAY, "고급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "밀리터리 프레스", 10, 4, 12);
                createRoutine(EXPERT, TUESDAY, "고급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "사이드 레터럴 레이즈", 10, 4, 12);
                createRoutine(EXPERT, TUESDAY, "고급자님! 오늘은 어꺠 데이입니다! 화아팅 하세요", "어꺠", "페이스 풀", 10, 4, 15);
                createRoutine(EXPERT, WEDNESDAY, "고급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴", "인클라인 덤벨 프레스", 4, 4, 15);
                createRoutine(EXPERT, WEDNESDAY, "고급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "가슴", "체스트 프레스", 4, 4, 15);
                createRoutine(EXPERT, WEDNESDAY, "고급자님! 오늘은 가슴, 삼두 데이입니다! 화아팅 하세요", "삼두", "라잉 트라이셉스 익스텐션", 4, 4, 15);
                createRoutine(EXPERT, THURSDAY, "고급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "시티드 로우", 4, 4, 15);
                createRoutine(EXPERT, THURSDAY, "고급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "래풀다운", 4, 4, 15);
                createRoutine(EXPERT, THURSDAY, "고급자님! 오늘은 등, 이두 데이입니다! 화아팅 하세요", "등, 이두", "덤벨 컬", 4, 4, 15);
                createRoutine(EXPERT, FRIDAY, "고급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴", "팩덱 플라이", 3, 4, 15);
                createRoutine(EXPERT, FRIDAY, "고급자님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴", "인클라인 바벨 프레스", 4, 4,
                        15);
                createRoutine(EXPERT, FRIDAY, "고급자님님! 오늘은 어깨 후면, 가슴 윗면 데이입니다! 화아팅 하세요", "어께, 가슴", "케이블", 4, 4, 15);
                createRoutine(EXPERT, SATURDAY, "고급자님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "삼두, 이두", "덤벨 오버헤드 익스텐션", 3, 4,
                        15);
                createRoutine(EXPERT, SATURDAY, "고급자님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "삼두, 이두", "바벨 컬", 3, 4, 15);
                createRoutine(EXPERT, SATURDAY, "고급자님! 오늘은 삼두, 이두 슈퍼 세트 데이입니다! 화아팅 하세요", "삼두, 이두", "헤머컬", 3, 4, 15);
            }
        };
    }

}