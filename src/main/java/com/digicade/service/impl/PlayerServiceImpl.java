package com.digicade.service.impl;

import com.digicade.domain.Player;
import com.digicade.repository.DigiUserRepository;
import com.digicade.repository.PlayerRepository;
import com.digicade.service.PlayerService;
import com.digicade.service.dto.PlayerDTO;
import com.digicade.service.mapper.PlayerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Player}.
 */
@Service
@Transactional
public class PlayerServiceImpl implements PlayerService {

    private final Logger log = LoggerFactory.getLogger(PlayerServiceImpl.class);

    private final PlayerRepository playerRepository;

    private final PlayerMapper playerMapper;

    private final DigiUserRepository digiUserRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository, PlayerMapper playerMapper, DigiUserRepository digiUserRepository) {
        this.playerRepository = playerRepository;
        this.playerMapper = playerMapper;
        this.digiUserRepository = digiUserRepository;
    }

    @Override
    public PlayerDTO save(PlayerDTO playerDTO) {
        log.debug("Request to save Player : {}", playerDTO);
        Player player = playerMapper.toEntity(playerDTO);
        Long digiUserId = playerDTO.getDigiUser().getId();
        digiUserRepository.findById(digiUserId).ifPresent(player::digiUser);
        player = playerRepository.save(player);
        return playerMapper.toDto(player);
    }

    @Override
    public PlayerDTO update(PlayerDTO playerDTO) {
        log.debug("Request to update Player : {}", playerDTO);
        Player player = playerMapper.toEntity(playerDTO);
        Long digiUserId = playerDTO.getDigiUser().getId();
        digiUserRepository.findById(digiUserId).ifPresent(player::digiUser);
        player = playerRepository.save(player);
        return playerMapper.toDto(player);
    }

    @Override
    public Optional<PlayerDTO> partialUpdate(PlayerDTO playerDTO) {
        log.debug("Request to partially update Player : {}", playerDTO);

        return playerRepository
            .findById(playerDTO.getId())
            .map(existingPlayer -> {
                playerMapper.partialUpdate(existingPlayer, playerDTO);

                return existingPlayer;
            })
            .map(playerRepository::save)
            .map(playerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PlayerDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Players");
        return playerRepository.findAll(pageable).map(playerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlayerDTO> findOne(Long id) {
        log.debug("Request to get Player : {}", id);
        return playerRepository.findById(id).map(playerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Player : {}", id);
        playerRepository.deleteById(id);
    }
}
