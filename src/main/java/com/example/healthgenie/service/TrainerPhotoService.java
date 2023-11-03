package com.example.healthgenie.service;

import com.example.healthgenie.domain.trainer.entity.TrainerPhoto;

import java.util.Collection;
import java.util.List;

public interface TrainerPhotoService {

    public void trainerPhotoSaveAll(Collection<TrainerPhoto> photoList);
    public List<TrainerPhoto> findAllPhotoTrainerId(Long Id);
}
