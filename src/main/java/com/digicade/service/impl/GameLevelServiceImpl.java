package com.digicade.service.impl;

import com.digicade.domain.GameLevel;
import com.digicade.repository.GameLevelRepository;
import com.digicade.service.GameLevelService;
import com.digicade.service.dto.GameLevelDTO;
import com.digicade.service.mapper.GameLevelMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameLevel}.
 */
@Service
@Transactional
public class GameLevelServiceImpl implements GameLevelService {

    private final Logger log = LoggerFactory.getLogger(GameLevelServiceImpl.class);

    private final GameLevelRepository gameLevelRepository;

    private final GameLevelMapper gameLevelMapper;

    public GameLevelServiceImpl(GameLevelRepository gameLevelRepository, GameLevelMapper gameLevelMapper) {
        this.gameLevelRepository = gameLevelRepository;
        this.gameLevelMapper = gameLevelMapper;
    }

    @Override
    public GameLevelDTO save(GameLevelDTO gameLevelDTO) {
        log.debug("Request to save GameLevel : {}", gameLevelDTO);
        GameLevel gameLevel = gameLevelMapper.toEntity(gameLevelDTO);
        gameLevel = gameLevelRepository.save(gameLevel);
        return gameLevelMapper.toDto(gameLevel);
    }

    @Override
    public GameLevelDTO update(GameLevelDTO gameLevelDTO) {
        log.debug("Request to update GameLevel : {}", gameLevelDTO);
        GameLevel gameLevel = gameLevelMapper.toEntity(gameLevelDTO);
        gameLevel = gameLevelRepository.save(gameLevel);
        return gameLevelMapper.toDto(gameLevel);
    }

    @Override
    public Optional<GameLevelDTO> partialUpdate(GameLevelDTO gameLevelDTO) {
        log.debug("Request to partially update GameLevel : {}", gameLevelDTO);

        return gameLevelRepository
            .findById(gameLevelDTO.getId())
            .map(existingGameLevel -> {
                gameLevelMapper.partialUpdate(existingGameLevel, gameLevelDTO);

                return existingGameLevel;
            })
            .map(gameLevelRepository::save)
            .map(gameLevelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameLevelDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GameLevels");
        return gameLevelRepository.findAll(pageable).map(gameLevelMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameLevelDTO> findOne(Long id) {
        log.debug("Request to get GameLevel : {}", id);
        return gameLevelRepository.findById(id).map(gameLevelMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GameLevel : {}", id);
        gameLevelRepository.deleteById(id);
    }
}
