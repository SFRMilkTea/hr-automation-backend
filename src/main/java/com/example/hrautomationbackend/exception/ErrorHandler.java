package com.example.hrautomationbackend.exception;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@Slf4j
@RestControllerAdvice()
public class ErrorHandler {

    @ExceptionHandler
    public ResponseEntity handleValidationError(final AccessTokenIsNotValidException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final BuildingAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final BuildingNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final CityAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final CityNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final UnableToDeleteYourselfException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ProductAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ProductAlreadyOrderedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ProductCategoryAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ProductCategoryNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ProductNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ProductNotOrderedException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final QuestionAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final QuestionCategoryAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final QuestionCategoryNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final QuestionNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final RefreshTokenIsNotValidException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }


    @ExceptionHandler
    public ResponseEntity handleValidationError(final RestaurantAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final RestaurantNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }


    @ExceptionHandler
    public ResponseEntity handleValidationError(final RestaurantStatusAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final RestaurantStatusNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ReviewNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final TokenIsNotValidException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final UndefinedLatitudeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final UndefinedLongitudeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final UserAlreadyExistException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final UserNotAdminException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final UserNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final WrongAuthorizationCodeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final AmazonClientException e) {
        return ResponseEntity.status(500).body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final EmptyResultDataAccessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final SdkClientException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final MaxUploadSizeExceededException e) {
        return ResponseEntity.badRequest().body("Превышен максимальный размер картинки");
    }

    @ExceptionHandler
    public ResponseEntity handleValidationError(final ExpiredJwtException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
