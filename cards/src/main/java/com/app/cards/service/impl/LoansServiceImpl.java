package com.app.cards.service.impl;

import com.app.cards.constants.CardsConstants;
import com.app.cards.dto.CardsDto;
import com.app.cards.entity.Cards;
import com.app.cards.exception.CardAlreadyExistsException;
import com.app.cards.exception.ResourceNotFoundException;
import com.app.cards.mapper.CardsMapper;
import com.app.cards.repository.CardsRepository;
import com.app.cards.service.ILoansService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class LoansServiceImpl implements ILoansService {

    private CardsRepository cardsRepository;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> optionalCards = this.cardsRepository.findByMobileNumber(mobileNumber);
        if (optionalCards.isPresent()) {
            throw new CardAlreadyExistsException("Card Already Exists");
        }
        cardsRepository.save(createdNewCard(mobileNumber));
    }

    @Override
    public CardsDto fetchCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );

        return CardsMapper.mapToCardsDto(cards, new CardsDto());
    }


    /**
     *
     * @param cardsDto - CardsDto Object
     */
    @Override
    public void updateCard(CardsDto cardsDto) {
        Cards cards = cardsRepository.findByMobileNumber(cardsDto.getMobileNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", cardsDto.getMobileNumber())
        );
        CardsMapper.mapToCards(cardsDto, cards);
        cardsRepository.save(cards);
    }

    @Override
    public void deleteCard(String mobileNumber) {
        Cards cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );
        cardsRepository.deleteById(cards.getCardId());
    }

    /**
     *
     * @param mobileNumber - Mobile Number of the Customer
     * @return the new card details
     */
    private Cards createdNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        String newCardNumber = generateCardNumber();
        newCard.setCardNumber(newCardNumber);
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(CardsConstants.CREDIT_CARD);
        newCard.setAmountUsed(0);
        newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
        newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);

        return newCard;
    }

    private String generateCardNumber() {
        Random random = new Random();

        String prefix = random.nextBoolean() ? "34" : "37";
        StringBuilder cardNumber = new StringBuilder(prefix);

        for (int i = 0; i < 13; i++) {
            cardNumber.append(random.nextInt(10));
        }

        int checkDigit = getLuhnCheckDigit(cardNumber.toString());
        cardNumber.append(checkDigit);

        return cardNumber.toString();
    }

    private int getLuhnCheckDigit(String number) {
        int sum = 0;
        boolean alternate = true;
        for (int i = number.length() - 1; i >= 0; i--) {
            int n = number.charAt(i) - '0';
            if (alternate) {
                n *= 2;
                if (n > 9) n -= 9;
            }
            sum += n;
            alternate = !alternate;
        }
        return (10 - (sum % 10)) % 10;
    }
}
