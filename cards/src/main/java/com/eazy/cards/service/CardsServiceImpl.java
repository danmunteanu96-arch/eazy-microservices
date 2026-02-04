package com.eazy.cards.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.eazy.cards.constants.CardsConstants;
import com.eazy.cards.dto.CardsDto;
import com.eazy.cards.entity.Cards;
import com.eazy.cards.exceptions.CardAlreadyExistException;
import com.eazy.cards.exceptions.ResourceNotFoundException;
import com.eazy.cards.mapper.CardsMapper;
import com.eazy.cards.repository.CardsRepository;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

        private CardsRepository cardsRepository;

        @Override
        public void createCard(String mobileNumber) {
                Optional<Cards> optionalCards = cardsRepository.findByMobileNumber(mobileNumber);
                if (optionalCards.isPresent()) {
                        throw new CardAlreadyExistException(
                                        "Card already registered with given mobileNumber " + mobileNumber);
                }
                cardsRepository.save(createNewLoan(mobileNumber));
        }

        private Cards createNewLoan(String mobileNumber) {
                Cards newLoan = new Cards();
                long randomLoanNumber = 100000000000L + new Random().nextInt(900000000);
                newLoan.setCardNumber(Long.toString(randomLoanNumber));
                newLoan.setMobileNumber(mobileNumber);
                newLoan.setCardType(CardsConstants.CREDIT_CARD);
                newLoan.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
                newLoan.setAmountUsed(0);
                newLoan.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
                return newLoan;
        }

        @Override
        public CardsDto fetchCard(String mobileNumber) {
                Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
                return CardsMapper.mapToLoansDto(cards);
        }

        @Override
        public boolean updateCard(CardsDto cardsDto) {
                Cards cards = cardsRepository.findByCardNumber(cardsDto.cardNumber()).orElseThrow(
                                () -> new ResourceNotFoundException("Card", "cardNumber", cardsDto.cardNumber()));
                CardsMapper.mapToLoans(cardsDto, cards);
                cardsRepository.save(cards);
                return true;
        }

        @Override
        public boolean deleteCard(String mobileNumber) {
                Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber));
                cardsRepository.deleteById(cards.getCardId());

                return true;
        }

}