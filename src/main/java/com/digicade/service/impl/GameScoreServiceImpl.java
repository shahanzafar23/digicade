package com.digicade.service.impl;

import com.digicade.domain.Game;
import com.digicade.domain.GameScore;
import com.digicade.domain.Player;
import com.digicade.repository.GameRepository;
import com.digicade.repository.GameScoreRepository;
import com.digicade.repository.PlayerRepository;
import com.digicade.service.GameScoreService;
import com.digicade.service.dto.GameScoreDTO;
import com.digicade.service.mapper.GameMapper;
import com.digicade.service.mapper.GameScoreMapper;
import com.digicade.service.mapper.PlayerMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link GameScore}.
 */
@Service
@Transactional
public class GameScoreServiceImpl implements GameScoreService {

    private final Logger log = LoggerFactory.getLogger(GameScoreServiceImpl.class);

    private final GameScoreRepository gameScoreRepository;

    private final GameScoreMapper gameScoreMapper;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public GameScoreServiceImpl(
        GameScoreRepository gameScoreRepository,
        GameScoreMapper gameScoreMapper,
        GameRepository gameRepository,
        PlayerRepository playerRepository
    ) {
        this.gameScoreRepository = gameScoreRepository;
        this.gameScoreMapper = gameScoreMapper;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public GameScoreDTO save(GameScoreDTO gameScoreDTO) {
        log.debug("Request to save GameScore : {}", gameScoreDTO);
        Optional<Game> gameOptional = gameRepository.findById(gameScoreDTO.getGame().getId());
        Optional<Player> playerOptional = playerRepository.findById(gameScoreDTO.getPlayer().getId());

        GameScore gameScore = gameScoreMapper.toEntity(gameScoreDTO);

        Game game = gameOptional.get();
        Player player = playerOptional.get();
        log.debug("Response to save Game : {}", game);
        log.debug("Response to save Player : {}", player);

        gameScore.setGame(game);
        gameScore.setPlayer(player);

        gameScore = gameScoreRepository.save(gameScore);

        return gameScoreMapper.toDto(gameScore);
    }

    @Override
    public GameScoreDTO update(GameScoreDTO gameScoreDTO) {
        log.debug("Request to update GameScore : {}", gameScoreDTO);
        GameScore gameScore = gameScoreMapper.toEntity(gameScoreDTO);
        gameScore = gameScoreRepository.save(gameScore);
        return gameScoreMapper.toDto(gameScore);
    }

    @Override
    public Optional<GameScoreDTO> partialUpdate(GameScoreDTO gameScoreDTO) {
        log.debug("Request to partially update GameScore : {}", gameScoreDTO);

        return gameScoreRepository
            .findById(gameScoreDTO.getId())
            .map(existingGameScore -> {
                gameScoreMapper.partialUpdate(existingGameScore, gameScoreDTO);

                return existingGameScore;
            })
            .map(gameScoreRepository::save)
            .map(gameScoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<GameScoreDTO> findAll(Pageable pageable) {
        log.debug("Request to get all GameScores");
        return gameScoreRepository.findAll(pageable).map(gameScoreMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GameScoreDTO> findOne(Long id) {
        log.debug("Request to get GameScore : {}", id);
        return gameScoreRepository.findById(id).map(gameScoreMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete GameScore : {}", id);
        gameScoreRepository.deleteById(id);
    }
}
