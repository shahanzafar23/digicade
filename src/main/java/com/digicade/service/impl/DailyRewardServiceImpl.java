package com.digicade.service.impl;

import com.digicade.domain.DailyReward;
import com.digicade.repository.DailyRewardRepository;
import com.digicade.service.DailyRewardService;
import com.digicade.service.dto.DailyRewardDTO;
import com.digicade.service.mapper.DailyRewardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link DailyReward}.
 */
@Service
@Transactional
public class DailyRewardServiceImpl implements DailyRewardService {

    private final Logger log = LoggerFactory.getLogger(DailyRewardServiceImpl.class);

    private final DailyRewardRepository dailyRewardRepository;

    private final DailyRewardMapper dailyRewardMapper;

    public DailyRewardServiceImpl(DailyRewardRepository dailyRewardRepository, DailyRewardMapper dailyRewardMapper) {
        this.dailyRewardRepository = dailyRewardRepository;
        this.dailyRewardMapper = dailyRewardMapper;
    }

    @Override
    public DailyRewardDTO save(DailyRewardDTO dailyRewardDTO) {
        log.debug("Request to save DailyReward : {}", dailyRewardDTO);
        DailyReward dailyReward = dailyRewardMapper.toEntity(dailyRewardDTO);
        dailyReward = dailyRewardRepository.save(dailyReward);
        return dailyRewardMapper.toDto(dailyReward);
    }

    @Override
    public DailyRewardDTO update(DailyRewardDTO dailyRewardDTO) {
        log.debug("Request to update DailyReward : {}", dailyRewardDTO);
        DailyReward dailyReward = dailyRewardMapper.toEntity(dailyRewardDTO);
        dailyReward = dailyRewardRepository.save(dailyReward);
        return dailyRewardMapper.toDto(dailyReward);
    }

    @Override
    public Optional<DailyRewardDTO> partialUpdate(DailyRewardDTO dailyRewardDTO) {
        log.debug("Request to partially update DailyReward : {}", dailyRewardDTO);

        return dailyRewardRepository
            .findById(dailyRewardDTO.getId())
            .map(existingDailyReward -> {
                dailyRewardMapper.partialUpdate(existingDailyReward, dailyRewardDTO);

                return existingDailyReward;
            })
            .map(dailyRewardRepository::save)
            .map(dailyRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DailyRewardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all DailyRewards");
        return dailyRewardRepository.findAll(pageable).map(dailyRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DailyRewardDTO> findOne(Long id) {
        log.debug("Request to get DailyReward : {}", id);
        return dailyRewardRepository.findById(id).map(dailyRewardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete DailyReward : {}", id);
        dailyRewardRepository.deleteById(id);
    }
}
