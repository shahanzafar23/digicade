package com.digicade.service.impl;

import com.digicade.domain.HighScore;
import com.digicade.repository.HighScoreRepository;
import com.digicade.service.HighScoreService;
import com.digicade.service.dto.HighScoreDTO;
import com.digicade.service.mapper.HighScoreMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link HighScore}.
 */
@Service
@Transactional
public class HighScoreServiceImpl implements HighScoreService {

    private final Logger log = LoggerFactory.getLogger(HighScoreServiceImpl.class);

    private final HighScoreRepository highScoreRepository;

    private final HighScoreMapper highScoreMapper;

    public HighScoreServiceImpl(HighScoreRepository highScoreRepository, HighScoreMapper highScoreMapper) {
        this.highScoreRepository = highScoreRepository;
        this.highScoreMapper = highScoreMapper;
    }

    @Override
    public HighScoreDTO save(HighScoreDTO highScoreDTO) {
        log.debug("Request to save HighScore : {}", highScoreDTO);
        HighScore highScore = highScoreMapper.toEntity(highScoreDTO);
        highScore = highScoreRepository.save(highScore);
        return highScoreMapper.toDto(highScore);
    }

    @Override
    public HighScoreDTO update(HighScoreDTO highScoreDTO) {
        log.debug("Request to update HighScore : {}", highScoreDTO);
        HighScore highScore = highScoreMapper.toEntity(highScoreDTO);
        highScore = highScoreRepository.save(highScore);
        return highScoreMapper.toDto(highScore);
    }

    @Override
    public Optional<HighScoreDTO> partialUpdate(HighScoreDTO highScoreDTO) {
        log.debug("Request to partially update HighScore : {}", highScoreDTO);

        return highScoreRepository
            .findById(highScoreDTO.getId())
            .map(existingHighScore -> {
                highScoreMapper.partialUpdate(existingHighScore, highScoreDTO);

                return existingHighScore;
            })
            .map(highScoreRepository::save)
            .map(highScoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<HighScoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all HighScores");
        return highScoreRepository.findAll(pageable).map(highScoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<HighScoreDTO> findOne(Long id) {
        log.debug("Request to get HighScore : {}", id);
        return highScoreRepository.findById(id).map(highScoreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete HighScore : {}", id);
        highScoreRepository.deleteById(id);
    }
}
