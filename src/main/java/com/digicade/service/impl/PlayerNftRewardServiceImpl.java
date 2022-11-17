package com.digicade.service.impl;

import com.digicade.domain.PlayerNftReward;
import com.digicade.repository.PlayerNftRewardRepository;
import com.digicade.service.PlayerNftRewardService;
import com.digicade.service.dto.PlayerNftRewardDTO;
import com.digicade.service.mapper.PlayerNftRewardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlayerNftReward}.
 */
@Service
@Transactional
public class PlayerNftRewardServiceImpl implements PlayerNftRewardService {

    private final Logger log = LoggerFactory.getLogger(PlayerNftRewardServiceImpl.class);

    private final PlayerNftRewardRepository playerNftRewardRepository;

    private final PlayerNftRewardMapper playerNftRewardMapper;

    public PlayerNftRewardServiceImpl(PlayerNftRewardRepository playerNftRewardRepository, PlayerNftRewardMapper playerNftRewardMapper) {
        this.playerNftRewardRepository = playerNftRewardRepository;
        this.playerNftRewardMapper = playerNftRewardMapper;
    }

    @Override
    public PlayerNftRewardDTO save(PlayerNftRewardDTO playerNftRewardDTO) {
        log.debug("Request to save PlayerNftReward : {}", playerNftRewardDTO);
        PlayerNftReward playerNftReward = playerNftRewardMapper.toEntity(playerNftRewardDTO);
        playerNftReward = playerNftRewardRepository.save(playerNftReward);
        return playerNftRewardMapper.toDto(playerNftReward);
    }

    @Override
    public PlayerNftRewardDTO update(PlayerNftRewardDTO playerNftRewardDTO) {
        log.debug("Request to update PlayerNftReward : {}", playerNftRewardDTO);
        PlayerNftReward playerNftReward = playerNftRewardMapper.toEntity(playerNftRewardDTO);
        playerNftReward = playerNftRewardRepository.save(playerNftReward);
        return playerNftRewardMapper.toDto(playerNftReward);
    }

    @Override
    public Optional<PlayerNftRewardDTO> partialUpdate(PlayerNftRewardDTO playerNftRewardDTO) {
        log.debug("Request to partially update PlayerNftReward : {}", playerNftRewardDTO);

        return playerNftRewardRepository
            .findById(playerNftRewardDTO.getId())
            .map(existingPlayerNftReward -> {
                playerNftRewardMapper.partialUpdate(existingPlayerNftReward, playerNftRewardDTO);

                return existingPlayerNftReward;
            })
            .map(playerNftRewardRepository::save)
            .map(playerNftRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlayerNftRewardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlayerNftRewards");
        return playerNftRewardRepository.findAll(pageable).map(playerNftRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerNftRewardDTO> findOne(Long id) {
        log.debug("Request to get PlayerNftReward : {}", id);
        return playerNftRewardRepository.findById(id).map(playerNftRewardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlayerNftReward : {}", id);
        playerNftRewardRepository.deleteById(id);
    }
}
