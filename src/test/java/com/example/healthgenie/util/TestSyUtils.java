package com.example.healthgenie.util;

import com.example.healthgenie.boundedContext.ptrecord.dto.PtProcessRequestDto;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessPhotoRepository;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import com.example.healthgenie.boundedContext.ptreview.dto.PtReviewRequestDto;
import com.example.healthgenie.boundedContext.ptreview.entity.PtReview;
import com.example.healthgenie.boundedContext.ptreview.repository.PtReviewRepository;
import com.example.healthgenie.boundedContext.routine.dto.RoutineRequestDto;
import com.example.healthgenie.boundedContext.routine.entity.Day;
import com.example.healthgenie.boundedContext.routine.entity.Level;
import com.example.healthgenie.boundedContext.routine.entity.Routine;
import com.example.healthgenie.boundedContext.routine.entity.WorkoutRecipe;
import com.example.healthgenie.boundedContext.routine.repository.RoutineRepository;
import com.example.healthgenie.boundedContext.todo.dto.TodoRequestDto;
import com.example.healthgenie.boundedContext.todo.entity.Status;
import com.example.healthgenie.boundedContext.todo.entity.Todo;
import com.example.healthgenie.boundedContext.todo.repository.TodoRepository;
import com.example.healthgenie.boundedContext.trainer.dto.ProfileRequestDto;
import com.example.healthgenie.boundedContext.trainer.entity.TrainerInfo;
import com.example.healthgenie.boundedContext.trainer.repository.TrainerProfileRepository;
import com.example.healthgenie.boundedContext.user.entity.Role;
import com.example.healthgenie.boundedContext.user.entity.User;
import com.example.healthgenie.boundedContext.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestSyUtils {

    private final RoutineRepository routineRepository;
    private final TodoRepository todoRepository;
    private final PtReviewRepository ptReviewRepository;
    private final TrainerProfileRepository trainerProfileRepository;
    private final PtProcessRepository ptProcessRepository;
    private final PtProcessPhotoRepository ptProcessPhotoRepository;


    public RoutineRequestDto createOwnRoutineRequest(Day day, String content,
                                                     List<String> parts, WorkoutRecipe recipe, String writer) {
        return RoutineRequestDto.builder()
                .day(day)
                .content(content)
                .parts(parts)
                .workoutName(recipe.getName())
                .kg(recipe.getKg())
                .sets(recipe.getSets())
                .reps(recipe.getReps())
                .writer(writer)
                .build();
    }


    public Routine writeRoutine(Day day, String content,
                                List<String> parts, WorkoutRecipe recipe, User writer) {

        Routine routine = Routine.builder()
                .day(day)
                .content(content)
                .part(parts)
                .workoutRecipe(recipe)
                .member(writer)
                .build();

        return routineRepository.save(routine);
    }

    public Routine genieRoutine(Level level, Day day, String content, List<String> parts, WorkoutRecipe recipe) {
        Routine routine = Routine.builder()
                .level(level)
                .day(day)
                .content(content)
                .part(parts)
                .workoutRecipe(recipe)
                .build();

        return routineRepository.save(routine);
    }

    public TodoRequestDto TodoRequestDto(String title, String description, Status status) {
        return TodoRequestDto(null, null, title, description, status, null, false);
    }

    public TodoRequestDto TodoRequestDto(LocalDate date, LocalTime time, String title, String description,
                                         Status status, String userNickname, boolean pt) {

        return TodoRequestDto.builder()
                .date(date)
                .time(time)
                .title(title)
                .description(description)
                .status(status)
                .userNickname(userNickname)
                .pt(pt)
                .build();
    }

    public Todo createTodo(LocalDate date, LocalTime time, String title, String description,
                           Status status, User user, boolean pt) {

        Todo todo = Todo.builder()
                .date(date)
                .time(time)
                .title(title)
                .description(description)
                .status(status)
                .member(user)
                .pt(pt)
                .build();

        return todoRepository.save(todo);
    }
    public ProfileRequestDto createProfileDto(String introduction,String career,
                                               int cost,int month, String nickname) {
        return ProfileRequestDto.builder()
                .introduction(introduction)
                .career(career)
                .cost(cost)
                .month(month)
                .nickname(nickname)
                .build();
    }

    public TrainerInfo createProfile(String introduction,String career,
                                     int cost,int month, User user) {
        TrainerInfo profile = TrainerInfo.builder()
                .introduction(introduction)
                .career(career)
                .cost(cost)
                .careerMonth(month)
                .member(user)
                .build();

        return trainerProfileRepository.save(profile);
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

    public PtReview createReview(String content, String stopReason, Double reviewScore,
                                 User user, User trainer) {
        PtReview review =  PtReview.builder()
                .content(content)
                .stopReason(stopReason)
                .reviewScore(reviewScore)
                .member(user)
                .trainer(trainer)
                .build();

        return ptReviewRepository.save(review);
    }

    public PtProcessRequestDto createProcessDto(LocalDate date, String title, String content,
                                        String userNickname, String trainerNickname) {
        return createProcessDto(date, title, content, null, userNickname, trainerNickname);
    }

    public PtProcessRequestDto createProcessDto(LocalDate date, String title, String content,
                                                List<MultipartFile> photos,
                                                String userNickname, String trainerNickname) {
        return PtProcessRequestDto.builder()
                .date(date)
                .content(content)
                .title(title)
                .photos(photos)
                .userNickName(userNickname)
                .trainerNickName(trainerNickname)
                .build();
    }

    public PtProcess createProcess(LocalDate date, String title, String content,
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

    public PtProcess createProcess(String title, String content, User user, User trainer) {
        return createProcess(null, title, content, null, user, trainer);
    }

    public PtProcessPhoto createProcessPhoto(PtProcess process, String path) {
        PtProcessPhoto processPhoto = PtProcessPhoto.builder()
                .process(process)
                .processPhotoPath(path)
                .build();

        return ptProcessPhotoRepository.save(processPhoto);
    }

}
