package com.app.cards.service;

import com.app.cards.dto.CardsDto;

public interface ICardsService {

    /**
     *
     * @param mobileNumber - Mobile Number of the customer
     */
    void createCard(String mobileNumber);

    /**
     *
     * @param mobileNumber - Input mobile Number
     * @return Card Details based on a given mobileNumber
     */
    CardsDto fetchCard(String mobileNumber);

    /**
     *
     * @param cardsDto - CardsDto Object
     */
    void updateCard(CardsDto cardsDto);

    /**
     *
     * @param mobileNumber - Input Mobile Number
     */
    void deleteCard(String mobileNumber);
}
