package com.digicade.service.mapper;

import com.digicade.domain.CoinPackage;
import com.digicade.domain.Player;
import com.digicade.domain.Transaction;
import com.digicade.service.dto.CoinPackageDTO;
import com.digicade.service.dto.PlayerDTO;
import com.digicade.service.dto.TransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transaction} and its DTO {@link TransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransactionMapper extends EntityMapper<TransactionDTO, Transaction> {
    @Mapping(target = "player", source = "player", qualifiedByName = "playerId")
    @Mapping(target = "coinPackage", source = "coinPackage", qualifiedByName = "coinPackageId")
    TransactionDTO toDto(Transaction s);

    @Named("playerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlayerDTO toDtoPlayerId(Player player);

    @Named("coinPackageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CoinPackageDTO toDtoCoinPackageId(CoinPackage coinPackage);
}
