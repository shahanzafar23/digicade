package com.digicade.service.impl;

import com.digicade.domain.GameBadge;
import com.digicade.repository.GameBadgeRepository;
import com.digicade.service.GameBadgeService;
import com.digicade.service.dto.GameBadgeDTO;
import com.digicade.service.mapper.GameBadgeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameBadge}.
 */
@Service
@Transactional
public class GameBadgeServiceImpl implements GameBadgeService {

    private final Logger log = LoggerFactory.getLogger(GameBadgeServiceImpl.class);

    private final GameBadgeRepository gameBadgeRepository;

    private final GameBadgeMapper gameBadgeMapper;

    public GameBadgeServiceImpl(GameBadgeRepository gameBadgeRepository, GameBadgeMapper gameBadgeMapper) {
        this.gameBadgeRepository = gameBadgeRepository;
        this.gameBadgeMapper = gameBadgeMapper;
    }

    @Override
    public GameBadgeDTO save(GameBadgeDTO gameBadgeDTO) {
        log.debug("Request to save GameBadge : {}", gameBadgeDTO);
        GameBadge gameBadge = gameBadgeMapper.toEntity(gameBadgeDTO);
        gameBadge = gameBadgeRepository.save(gameBadge);
        return gameBadgeMapper.toDto(gameBadge);
    }

    @Override
    public GameBadgeDTO update(GameBadgeDTO gameBadgeDTO) {
        log.debug("Request to update GameBadge : {}", gameBadgeDTO);
        GameBadge gameBadge = gameBadgeMapper.toEntity(gameBadgeDTO);
        gameBadge = gameBadgeRepository.save(gameBadge);
        return gameBadgeMapper.toDto(gameBadge);
    }

    @Override
    public Optional<GameBadgeDTO> partialUpdate(GameBadgeDTO gameBadgeDTO) {
        log.debug("Request to partially update GameBadge : {}", gameBadgeDTO);

        return gameBadgeRepository
            .findById(gameBadgeDTO.getId())
            .map(existingGameBadge -> {
                gameBadgeMapper.partialUpdate(existingGameBadge, gameBadgeDTO);

                return existingGameBadge;
            })
            .map(gameBadgeRepository::save)
            .map(gameBadgeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameBadgeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GameBadges");
        return gameBadgeRepository.findAll(pageable).map(gameBadgeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameBadgeDTO> findOne(Long id) {
        log.debug("Request to get GameBadge : {}", id);
        return gameBadgeRepository.findById(id).map(gameBadgeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GameBadge : {}", id);
        gameBadgeRepository.deleteById(id);
    }
}
