package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.process.photo.dto.ProcessPhotoRequest;
import com.example.healthgenie.boundedContext.process.photo.entity.ProcessPhoto;
import com.example.healthgenie.boundedContext.process.photo.repository.ProcessPhotoRepository;
import com.example.healthgenie.boundedContext.process.process.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.process.process.entity.PtProcess;
import com.example.healthgenie.boundedContext.process.process.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.review.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.review.dto.PtReviewUpdateRequest;
import com.example.healthgenie.boundedContext.review.entity.PtReview;
import com.example.healthgenie.boundedContext.review.repository.PtReviewRepository;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.dto.RoutineUpdateRequestDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.dto.TodoUpdateRequest;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.todo.repository.TodoRepository;
import com.example.healthgenie.boundedContext.trainer.photo.dto.ProfilePhotoRequest;
import com.example.healthgenie.boundedContext.trainer.photo.entity.TrainerPhoto;
import com.example.healthgenie.boundedContext.trainer.photo.entity.enums.PurposeOfUsing;
import com.example.healthgenie.boundedContext.trainer.photo.repository.TrainerProfilePhotoRepository;
import com.example.healthgenie.boundedContext.trainer.profile.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.profile.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.profile.repository.ProfileRepository;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.entity.enums.Role;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class TestSyUtils {

    private final RoutineRepository routineRepository;
    private final TodoRepository todoRepository;
    private final PtReviewRepository ptReviewRepository;
    private final ProfileRepository profileRepository;
    private final TrainerProfilePhotoRepository trainerProfilePhotoRepository;
    private final PtProcessRepository ptProcessRepository;
    private final ProcessPhotoRepository processPhotoRepository;
    private final UserRepository userRepository;

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    public User createUser(String name, String nickname, Role role, String uniName, String email) {
        User user = User.builder()
                .name(name)
                .nickname(nickname)
                .role(role)
                .uniName(uniName)
                .email(email)
                .build();

        return userRepository.save(user);
    }

    public User createUser(String name, String nickname, String uniName, String email) {
        User user = User.builder()
                .name(name)
                .nickname(nickname)
                .uniName(uniName)
                .email(email)
                .build();

        return userRepository.save(user);
    }

    public RoutineRequestDto createOwnRoutineRequest(Day day, String parts, List<WorkoutRecipe> recipes,
                                                     String writer) {

        return RoutineRequestDto.builder()
                .day(day)
                .parts(parts)
                .workoutRecipe(recipes)
                .writer(writer)
                .build();
    }

    public RoutineUpdateRequestDto createOwnRoutineUpdateRequest(Day day, String parts, List<WorkoutRecipe> recipes) {
        return RoutineUpdateRequestDto.builder()
                .day(day)
                .parts(parts)
                .workoutRecipe(recipes)
                .build();
    }


    public Routine writeRoutine(Day day, String parts, List<WorkoutRecipe> recipes, User writer) {

        Routine saved = new Routine();

        for (WorkoutRecipe data : recipes) {

            WorkoutRecipe recipe = new WorkoutRecipe(data.getName(), data.getKg(), data.getSets(), data.getReps());

            Routine routine = Routine.builder()
                    .day(day)
                    .parts(parts)
                    .workoutRecipe(recipe)
                    .member(writer)
                    .build();

            saved = routineRepository.save(routine);
        }
        return saved;

    }

    public Routine writeRoutine(Day day, String parts, WorkoutRecipe recipes, User writer) {

        WorkoutRecipe recipe = new WorkoutRecipe(recipes.getName(), recipes.getKg(), recipes.getSets(),
                recipes.getReps());

        Routine routine = Routine.builder()
                .day(day)
                .parts(parts)
                .workoutRecipe(recipe)
                .member(writer)
                .build();

        return routineRepository.save(routine);

    }

    public Routine genieRoutine(Level level, Day day, String content, String parts, WorkoutRecipe recipe) {
        Routine routine = Routine.builder()
                .level(level)
                .day(day)
                .content(content)
                .parts(parts)
                .workoutRecipe(recipe)
                .build();

        return routineRepository.save(routine);
    }

    public TodoUpdateRequest updateTodoRequest(String title, String description) {
        return updateTodoDto(null, null, title, description);
    }

    public TodoRequestDto TodoRequestDto(LocalDate date, LocalTime time, String title, String description) {

        return TodoRequestDto.builder()
                .date(date)
                .time(time)
                .title(title)
                .description(description)
                .build();
    }


    public TodoUpdateRequest updateTodoDto(LocalDate date, LocalTime time, String title, String description) {
        return TodoUpdateRequest.builder()
                .date(date)
                .time(time)
                .title(title)
                .description(description)
                .build();
    }

    public Todo createTodo(LocalDate date, LocalTime time, String title, String description, User user) {

        Todo todo = Todo.builder()
                .date(date)
                .time(time)
                .title(title)
                .description(description)
                .member(user)
                .build();

        return todoRepository.save(todo);
    }

    public PtReviewRequestDto createReviewDto(String content, String stopReason, Double reviewScore,
                                              String userNickname, String trainerNickname) {
        return PtReviewRequestDto.builder()
                .content(content)
                .stopReason(stopReason)
                .reviewScore(reviewScore)
                .userNickName(userNickname)
                .trainerNickName(trainerNickname)
                .build();
    }

    public PtReviewUpdateRequest updateReviewDto(String content, String stopReason, Double reviewScore) {
        return PtReviewUpdateRequest.builder()
                .content(content)
                .stopReason(stopReason)
                .reviewScore(reviewScore)
                .build();
    }


    public PtReview createReview(String content, String stopReason, Double reviewScore,
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


    public PtProcessRequestDto createProcessDto(LocalDate date, String title, String content,
                                                String userNickName, String trainerNickName) {
        return PtProcessRequestDto.builder()
                .date(date)
                .content(content)
                .title(title)
                .userNickName(userNickName)
                .trainerNickName(trainerNickName)
                .build();
    }

    public PtProcess createProcess(LocalDate date, String title, String content,
                                   List<ProcessPhoto> photos,
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

    public PtProcess createProcess(String title, String content, User user, User trainer) {
        return createProcess(null, title, content, null, user, trainer);
    }

    public ProcessPhotoRequest createPhotoDto(List<MultipartFile> photos) {
        return ProcessPhotoRequest.builder()
                .photos(photos)
                .build();
    }

    public ProcessPhoto createProcessPhoto(PtProcess process, String path, String originalName) {
        ProcessPhoto processPhoto = ProcessPhoto.builder()
                .process(process)
                .processPhotoPath(path)
                .name(originalName)
                .build();

        return processPhotoRepository.save(processPhoto);
    }


    public ProfileRequestDto createProfileDto(String introduction, String career, String university,
                                              LocalTime startTime, LocalTime endTime, Double reviewAvg,
                                              int cost, int month, String nickname) {
        return ProfileRequestDto.builder()
                .introduction(introduction)
                .career(career)
                .university(university)
                .startTime(startTime)
                .endTime(endTime)
                .reviewAvg(reviewAvg)
                .cost(cost)
                .month(month)
                .nickname(nickname)
                .build();
    }


    public TrainerInfo createProfile(String introduction, String career, String university,
                                     LocalTime startTime, LocalTime endTime, Double reviewAvg,
                                     int cost, int month, User user) {
        TrainerInfo profile = TrainerInfo.builder()
                .introduction(introduction)
                .career(career)
                .cost(cost)
                .careerMonth(month)
                .university(university)
                .startTime(startTime)
                .endTime(endTime)
                .reviewAvg(reviewAvg)
                .member(user)
                .build();

        return profileRepository.save(profile);
    }

    public ProfilePhotoRequest ProfilePhotoRequest(List<MultipartFile> photos) {

        return ProfilePhotoRequest.builder()
                .photos(photos)
                .build();
    }

    public TrainerPhoto createProfilePhoto(TrainerInfo profile, String path, String originalName,
                                           PurposeOfUsing purpose) {
        TrainerPhoto profilePhoto = TrainerPhoto.builder()
                .info(profile)
                .infoPhotoPath(path)
                .name(originalName)
                .purpose(purpose)
                .build();

        return trainerProfilePhotoRepository.save(profilePhoto);
    }

}
