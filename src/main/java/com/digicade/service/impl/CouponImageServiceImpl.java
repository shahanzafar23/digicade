package com.digicade.service.impl;

import com.digicade.domain.CouponImage;
import com.digicade.repository.CouponImageRepository;
import com.digicade.service.CouponImageService;
import com.digicade.service.dto.CouponImageDTO;
import com.digicade.service.mapper.CouponImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CouponImage}.
 */
@Service
@Transactional
public class CouponImageServiceImpl implements CouponImageService {

    private final Logger log = LoggerFactory.getLogger(CouponImageServiceImpl.class);

    private final CouponImageRepository couponImageRepository;

    private final CouponImageMapper couponImageMapper;

    public CouponImageServiceImpl(CouponImageRepository couponImageRepository, CouponImageMapper couponImageMapper) {
        this.couponImageRepository = couponImageRepository;
        this.couponImageMapper = couponImageMapper;
    }

    @Override
    public CouponImageDTO save(CouponImageDTO couponImageDTO) {
        log.debug("Request to save CouponImage : {}", couponImageDTO);
        CouponImage couponImage = couponImageMapper.toEntity(couponImageDTO);
        couponImage = couponImageRepository.save(couponImage);
        return couponImageMapper.toDto(couponImage);
    }

    @Override
    public CouponImageDTO update(CouponImageDTO couponImageDTO) {
        log.debug("Request to update CouponImage : {}", couponImageDTO);
        CouponImage couponImage = couponImageMapper.toEntity(couponImageDTO);
        couponImage = couponImageRepository.save(couponImage);
        return couponImageMapper.toDto(couponImage);
    }

    @Override
    public Optional<CouponImageDTO> partialUpdate(CouponImageDTO couponImageDTO) {
        log.debug("Request to partially update CouponImage : {}", couponImageDTO);

        return couponImageRepository
            .findById(couponImageDTO.getId())
            .map(existingCouponImage -> {
                couponImageMapper.partialUpdate(existingCouponImage, couponImageDTO);

                return existingCouponImage;
            })
            .map(couponImageRepository::save)
            .map(couponImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CouponImageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CouponImages");
        return couponImageRepository.findAll(pageable).map(couponImageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CouponImageDTO> findOne(Long id) {
        log.debug("Request to get CouponImage : {}", id);
        return couponImageRepository.findById(id).map(couponImageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CouponImage : {}", id);
        couponImageRepository.deleteById(id);
    }
}
