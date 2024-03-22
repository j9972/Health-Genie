package com.example.healthgenie.boundedContext.ptrecord.service;

import com.example.healthgenie.base.exception.PtProcess.PtProcessException;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcess;
import com.example.healthgenie.boundedContext.ptrecord.entity.PtProcessPhoto;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessPhotoRepository;
import com.example.healthgenie.boundedContext.ptrecord.repository.PtProcessRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PtProcessPhotoService {
    private final PtProcessPhotoRepository ptProcessPhotoRepository;
    private final PtProcessRepository ptProcessRepository;

    @Transactional
    public PtProcessPhoto save(Long processId, String path) {
        PtProcess process = ptProcessRepository.findById(processId)
                .orElseThrow(() -> PtProcessException.NO_PROCESS_HISTORY);

        PtProcessPhoto photo = PtProcessPhoto.builder()
                .processPhotoPath(path)
                .process(process)
                .build();

        return ptProcessPhotoRepository.save(photo);
    }

    @Transactional
    public List<PtProcessPhoto> saveAll(Long processId, List<String> photoPaths) {
        PtProcess process = ptProcessRepository.findById(processId)
                .orElseThrow(() -> PtProcessException.NO_PROCESS_HISTORY);

        List<PtProcessPhoto> photos = photoPaths.stream()
                .map(path -> PtProcessPhoto.builder()
                        .processPhotoPath(path)
                        .process(process)
                        .build())
                .toList();

        // 객체 그래프 탐색용
        for (PtProcessPhoto photo : photos) {
            process.addProcess(photo);
        }

        return ptProcessPhotoRepository.saveAll(photos);
    }
}
