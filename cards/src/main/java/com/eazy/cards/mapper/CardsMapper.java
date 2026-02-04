package com.eazy.cards.mapper;

import com.eazy.cards.dto.CardsDto;
import com.eazy.cards.entity.Cards;

public class CardsMapper {

    public static CardsDto mapToLoansDto(Cards cards) {
        return new CardsDto(
                cards.getMobileNumber(),
                cards.getCardNumber(),
                cards.getCardType(),
                cards.getTotalLimit(),
                cards.getAvailableAmount(),
                cards.getAmountUsed());
    }

    public static Cards mapToLoans(CardsDto cardsDto, Cards cards) {
        cards.setCardNumber(cardsDto.cardNumber());
        cards.setCardType(cardsDto.cardType());
        cards.setMobileNumber(cardsDto.mobileNumber());
        cards.setTotalLimit(cardsDto.totalLimit());
        cards.setAvailableAmount(cardsDto.availableAmount());
        cards.setAmountUsed(cardsDto.amountUsed());
        return cards;
    }

}
