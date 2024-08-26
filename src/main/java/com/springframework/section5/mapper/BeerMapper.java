package com.springframework.section5.mapper;

import com.springframework.section5.dto.BeerDto;
import com.springframework.section5.entity.Beer;
import org.mapstruct.Mapper;

@Mapper
public interface BeerMapper {

	Beer beerDtoToBeer(BeerDto dto);
	BeerDto beerToBeerDto(Beer entity);
}
