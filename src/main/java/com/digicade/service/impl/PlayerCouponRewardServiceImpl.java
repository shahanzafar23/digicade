package com.digicade.service.impl;

import com.digicade.domain.PlayerCouponReward;
import com.digicade.repository.PlayerCouponRewardRepository;
import com.digicade.service.PlayerCouponRewardService;
import com.digicade.service.dto.PlayerCouponRewardDTO;
import com.digicade.service.mapper.PlayerCouponRewardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PlayerCouponReward}.
 */
@Service
@Transactional
public class PlayerCouponRewardServiceImpl implements PlayerCouponRewardService {

    private final Logger log = LoggerFactory.getLogger(PlayerCouponRewardServiceImpl.class);

    private final PlayerCouponRewardRepository playerCouponRewardRepository;

    private final PlayerCouponRewardMapper playerCouponRewardMapper;

    public PlayerCouponRewardServiceImpl(
        PlayerCouponRewardRepository playerCouponRewardRepository,
        PlayerCouponRewardMapper playerCouponRewardMapper
    ) {
        this.playerCouponRewardRepository = playerCouponRewardRepository;
        this.playerCouponRewardMapper = playerCouponRewardMapper;
    }

    @Override
    public PlayerCouponRewardDTO save(PlayerCouponRewardDTO playerCouponRewardDTO) {
        log.debug("Request to save PlayerCouponReward : {}", playerCouponRewardDTO);
        PlayerCouponReward playerCouponReward = playerCouponRewardMapper.toEntity(playerCouponRewardDTO);
        playerCouponReward = playerCouponRewardRepository.save(playerCouponReward);
        return playerCouponRewardMapper.toDto(playerCouponReward);
    }

    @Override
    public PlayerCouponRewardDTO update(PlayerCouponRewardDTO playerCouponRewardDTO) {
        log.debug("Request to update PlayerCouponReward : {}", playerCouponRewardDTO);
        PlayerCouponReward playerCouponReward = playerCouponRewardMapper.toEntity(playerCouponRewardDTO);
        playerCouponReward = playerCouponRewardRepository.save(playerCouponReward);
        return playerCouponRewardMapper.toDto(playerCouponReward);
    }

    @Override
    public Optional<PlayerCouponRewardDTO> partialUpdate(PlayerCouponRewardDTO playerCouponRewardDTO) {
        log.debug("Request to partially update PlayerCouponReward : {}", playerCouponRewardDTO);

        return playerCouponRewardRepository
            .findById(playerCouponRewardDTO.getId())
            .map(existingPlayerCouponReward -> {
                playerCouponRewardMapper.partialUpdate(existingPlayerCouponReward, playerCouponRewardDTO);

                return existingPlayerCouponReward;
            })
            .map(playerCouponRewardRepository::save)
            .map(playerCouponRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlayerCouponRewardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all PlayerCouponRewards");
        return playerCouponRewardRepository.findAll(pageable).map(playerCouponRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerCouponRewardDTO> findOne(Long id) {
        log.debug("Request to get PlayerCouponReward : {}", id);
        return playerCouponRewardRepository.findById(id).map(playerCouponRewardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete PlayerCouponReward : {}", id);
        playerCouponRewardRepository.deleteById(id);
    }
}
