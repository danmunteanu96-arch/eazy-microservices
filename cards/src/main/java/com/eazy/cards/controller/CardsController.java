package com.eazy.cards.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eazy.cards.constants.CardsConstants;
import com.eazy.cards.dto.CardsConfigDto;
import com.eazy.cards.dto.CardsDto;
import com.eazy.cards.dto.ResponseDto;
import com.eazy.cards.service.ICardsService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "api", produces = { MediaType.APPLICATION_JSON_VALUE })
@AllArgsConstructor
@Validated
public class CardsController {

        private ICardsService cardsService;
        private CardsConfigDto cardsConfigDto;

        @GetMapping()
        @RequestMapping(path = "/fetch")
        public ResponseEntity<CardsDto> fetchCardDetails(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {

                CardsDto cardsDto = cardsService.fetchCard(mobileNumber);
                return ResponseEntity.status(HttpStatus.OK).body(cardsDto);
        }

        @PostMapping
        @RequestMapping(path = "/create")
        public ResponseEntity<ResponseDto> createCard(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {

                cardsService.createCard(mobileNumber);

                return ResponseEntity.status(HttpStatus.CREATED).body(
                                new ResponseDto(CardsConstants.STATUS_201, CardsConstants.MESSAGE_201));
        }

        @PutMapping("/update")
        public ResponseEntity<ResponseDto> updateCardDetails(@Valid @RequestBody CardsDto cardsDto) {
                boolean isUpdated = cardsService.updateCard(cardsDto);

                return isUpdated ? ResponseEntity.ok(null)
                                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseDto(
                                                                CardsConstants.MESSAGE_417_UPDATE,
                                                                CardsConstants.STATUS_417));
        }

        @DeleteMapping("/delete")
        public ResponseEntity<ResponseDto> deleteCardDetails(
                        @RequestParam @Pattern(regexp = "(^$|[0-9]{7})", message = "Mobile number must be 7 digits") String mobileNumber) {
                boolean isDeleted = cardsService.deleteCard(mobileNumber);

                return isDeleted ? ResponseEntity.ok(null)
                                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                                                new ResponseDto(
                                                                CardsConstants.MESSAGE_500,
                                                                HttpStatus.INTERNAL_SERVER_ERROR.toString()));
        }

        
        @GetMapping("/cards-details")
        public ResponseEntity<CardsConfigDto> getAccountConfig() {
                return ResponseEntity.ok(cardsConfigDto);
        }
}
