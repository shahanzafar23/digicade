package com.digicade.service.impl;

import com.digicade.domain.CoinPackage;
import com.digicade.repository.CoinPackageRepository;
import com.digicade.service.CoinPackageService;
import com.digicade.service.dto.CoinPackageDTO;
import com.digicade.service.mapper.CoinPackageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CoinPackage}.
 */
@Service
@Transactional
public class CoinPackageServiceImpl implements CoinPackageService {

    private final Logger log = LoggerFactory.getLogger(CoinPackageServiceImpl.class);

    private final CoinPackageRepository coinPackageRepository;

    private final CoinPackageMapper coinPackageMapper;

    public CoinPackageServiceImpl(CoinPackageRepository coinPackageRepository, CoinPackageMapper coinPackageMapper) {
        this.coinPackageRepository = coinPackageRepository;
        this.coinPackageMapper = coinPackageMapper;
    }

    @Override
    public CoinPackageDTO save(CoinPackageDTO coinPackageDTO) {
        log.debug("Request to save CoinPackage : {}", coinPackageDTO);
        CoinPackage coinPackage = coinPackageMapper.toEntity(coinPackageDTO);
        coinPackage = coinPackageRepository.save(coinPackage);
        return coinPackageMapper.toDto(coinPackage);
    }

    @Override
    public CoinPackageDTO update(CoinPackageDTO coinPackageDTO) {
        log.debug("Request to update CoinPackage : {}", coinPackageDTO);
        CoinPackage coinPackage = coinPackageMapper.toEntity(coinPackageDTO);
        coinPackage = coinPackageRepository.save(coinPackage);
        return coinPackageMapper.toDto(coinPackage);
    }

    @Override
    public Optional<CoinPackageDTO> partialUpdate(CoinPackageDTO coinPackageDTO) {
        log.debug("Request to partially update CoinPackage : {}", coinPackageDTO);

        return coinPackageRepository
            .findById(coinPackageDTO.getId())
            .map(existingCoinPackage -> {
                coinPackageMapper.partialUpdate(existingCoinPackage, coinPackageDTO);

                return existingCoinPackage;
            })
            .map(coinPackageRepository::save)
            .map(coinPackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CoinPackageDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CoinPackages");
        return coinPackageRepository.findAll(pageable).map(coinPackageMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CoinPackageDTO> findOne(Long id) {
        log.debug("Request to get CoinPackage : {}", id);
        return coinPackageRepository.findById(id).map(coinPackageMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CoinPackage : {}", id);
        coinPackageRepository.deleteById(id);
    }
}
