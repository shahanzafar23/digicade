package com.digicade.service.impl;

import com.digicade.domain.CouponReward;
import com.digicade.repository.CouponRewardRepository;
import com.digicade.service.CouponRewardService;
import com.digicade.service.dto.CouponRewardDTO;
import com.digicade.service.mapper.CouponRewardMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CouponReward}.
 */
@Service
@Transactional
public class CouponRewardServiceImpl implements CouponRewardService {

    private final Logger log = LoggerFactory.getLogger(CouponRewardServiceImpl.class);

    private final CouponRewardRepository couponRewardRepository;

    private final CouponRewardMapper couponRewardMapper;

    public CouponRewardServiceImpl(CouponRewardRepository couponRewardRepository, CouponRewardMapper couponRewardMapper) {
        this.couponRewardRepository = couponRewardRepository;
        this.couponRewardMapper = couponRewardMapper;
    }

    @Override
    public CouponRewardDTO save(CouponRewardDTO couponRewardDTO) {
        log.debug("Request to save CouponReward : {}", couponRewardDTO);
        CouponReward couponReward = couponRewardMapper.toEntity(couponRewardDTO);
        couponReward = couponRewardRepository.save(couponReward);
        return couponRewardMapper.toDto(couponReward);
    }

    @Override
    public CouponRewardDTO update(CouponRewardDTO couponRewardDTO) {
        log.debug("Request to update CouponReward : {}", couponRewardDTO);
        CouponReward couponReward = couponRewardMapper.toEntity(couponRewardDTO);
        couponReward = couponRewardRepository.save(couponReward);
        return couponRewardMapper.toDto(couponReward);
    }

    @Override
    public Optional<CouponRewardDTO> partialUpdate(CouponRewardDTO couponRewardDTO) {
        log.debug("Request to partially update CouponReward : {}", couponRewardDTO);

        return couponRewardRepository
            .findById(couponRewardDTO.getId())
            .map(existingCouponReward -> {
                couponRewardMapper.partialUpdate(existingCouponReward, couponRewardDTO);

                return existingCouponReward;
            })
            .map(couponRewardRepository::save)
            .map(couponRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponRewardDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CouponRewards");
        return couponRewardRepository.findAll(pageable).map(couponRewardMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CouponRewardDTO> findOne(Long id) {
        log.debug("Request to get CouponReward : {}", id);
        return couponRewardRepository.findById(id).map(couponRewardMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CouponReward : {}", id);
        couponRewardRepository.deleteById(id);
    }
}
