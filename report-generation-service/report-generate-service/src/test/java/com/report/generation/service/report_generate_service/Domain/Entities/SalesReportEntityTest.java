package com.report.generation.service.report_generate_service.Domain.Entities;

import com.report.generation.service.report_generate_service.Domain.Exceptions.CustomException.ProducerReportException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class SalesReportEntityTest {

    private SalesReportEntity salesReportEntity;

    @BeforeEach
    void setup_test_entity() {
        // Given
        String initialId = UUID.randomUUID().toString();
        String initialProduct = "Product A";
        LocalDate initialSaleDate = LocalDate.of(2023, 1, 15);
        String initialDetails = "Details for Product A";

        salesReportEntity = new SalesReportEntity(initialId, initialProduct, initialSaleDate, initialDetails);

        assertNotNull(salesReportEntity);
    }

    // --- Testes de Construtores ---

    @Test
    @DisplayName("should_create_sales_report_entity_with_default_constructor")
    void should_create_sales_report_entity_with_default_constructor() {
        // Given (nothing specific to arrange, testing default state)

        // When
        SalesReportEntity defaultEntity = new SalesReportEntity();

        // Then
        assertNotNull(defaultEntity);
        assertNull(defaultEntity.getId());
        assertNull(defaultEntity.getProduct());
        assertNull(defaultEntity.getSaleDate());
        assertNull(defaultEntity.getDetails());
    }

    @Test
    @DisplayName("should_create_sales_report_entity_with_all_args_constructor")
    void should_create_sales_report_entity_with_all_args_constructor() {
        // Given
        String testId = UUID.randomUUID().toString();
        String testProduct = "Test Product";
        LocalDate testSaleDate = LocalDate.now();
        String testDetails = "Test Details";

        // When
        SalesReportEntity newEntity = new SalesReportEntity(testId, testProduct, testSaleDate, testDetails);

        // Then
        assertEquals(testId, newEntity.getId());
        assertEquals(testProduct, newEntity.getProduct());
        assertEquals(testSaleDate, newEntity.getSaleDate());
        assertEquals(testDetails, newEntity.getDetails());
    }

    @Test
    @DisplayName("should_create_sales_report_entity_with_partial_constructor")
    void should_create_sales_report_entity_with_partial_constructor() {
        // Given
        String newProduct = "New Product";
        LocalDate newSaleDate = LocalDate.of(2024, 5, 20);
        String newDetails = "New Details";

        // When
        SalesReportEntity partialEntity = new SalesReportEntity(newProduct, newSaleDate, newDetails);

        // Then
        assertNull(partialEntity.getId()); // ID should be null before persistence/generation
        assertEquals(newProduct, partialEntity.getProduct());
        assertEquals(newSaleDate, partialEntity.getSaleDate());
        assertEquals(newDetails, partialEntity.getDetails());
    }

    // --- Testes de Getters e Setters ---

    @Test
    @DisplayName("should_set_and_get_id")
    void should_set_and_get_id() {
        // Given
        String newId = UUID.randomUUID().toString();

        // When
        salesReportEntity.setId(newId);

        // Then
        assertEquals(newId, salesReportEntity.getId());
    }

    @Test
    @DisplayName("should_set_and_get_product")
    void should_set_and_get_product() {
        // Given
        String newProduct = "New Product Name";

        // When
        salesReportEntity.setProduct(newProduct);

        // Then
        assertEquals(newProduct, salesReportEntity.getProduct());
    }

    @Test
    @DisplayName("should_set_and_get_sale_date")
    void should_set_and_get_sale_date() {
        // Given
        LocalDate newDate = LocalDate.of(2024, 10, 25);

        // When
        salesReportEntity.setSaleDate(newDate);

        // Then
        assertEquals(newDate, salesReportEntity.getSaleDate());
    }

    @Test
    @DisplayName("should_set_and_get_details")
    void should_set_and_get_details() {
        // Given
        String newDetails = "Updated details for sale";

        // When
        salesReportEntity.setDetails(newDetails);

        // Then
        assertEquals(newDetails, salesReportEntity.getDetails());
    }

    // --- Testes de Validação (validateSelf) ---

    @Test
    @DisplayName("should_pass_validation_when_entity_is_valid")
    void should_pass_validation_when_entity_is_valid() {

        assertDoesNotThrow(() -> salesReportEntity.validateSelf());
    }

    @Test
    @DisplayName("should_throw_exception_when_product_is_null")
    void should_throw_exception_when_product_is_null() {
        // Given
        salesReportEntity.setProduct(null);

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Product cannot be null or empty."));
    }

    @Test
    @DisplayName("should_throw_exception_when_product_is_empty")
    void should_throw_exception_when_product_is_empty() {
        // Given
        salesReportEntity.setProduct("");

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Product cannot be null or empty."));
    }

    @Test
    @DisplayName("should_throw_exception_when_product_is_blank")
    void should_throw_exception_when_product_is_blank() {
        // Given
        salesReportEntity.setProduct("   ");

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Product cannot be null or empty."));
    }

    @Test
    @DisplayName("should_throw_exception_when_sale_date_is_null")
    void should_throw_exception_when_sale_date_is_null() {
        // Given
        salesReportEntity.setSaleDate(null);

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Sale date cannot be null."));
    }

    @Test
    @DisplayName("should_throw_exception_when_sale_date_is_in_future")
    void should_throw_exception_when_sale_date_is_in_future() {
        // Given
        salesReportEntity.setSaleDate(LocalDate.now().plusDays(1)); // Date in the future

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Sale date cannot be in the future."));
    }

    @Test
    @DisplayName("should_throw_exception_when_details_are_null")
    void should_throw_exception_when_details_are_null() {
        // Given
        salesReportEntity.setDetails(null);

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Details cannot be null or empty."));
    }

    @Test
    @DisplayName("should_throw_exception_when_details_are_empty")
    void should_throw_exception_when_details_are_empty() {
        // Given
        salesReportEntity.setDetails("");

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Details cannot be null or empty."));
    }

    @Test
    @DisplayName("should_throw_exception_when_details_are_blank")
    void should_throw_exception_when_details_are_blank() {
        // Given
        salesReportEntity.setDetails("   ");

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        assertTrue(exception.getMessage().contains("Details cannot be null or empty."));
    }

    @Test
    @DisplayName("should_throw_exception_with_multiple_error_messages_for_invalid_fields")
    void should_throw_exception_with_multiple_error_messages_for_invalid_fields() {
        // Given
        salesReportEntity.setProduct(null);
        salesReportEntity.setSaleDate(LocalDate.now().plusDays(1));
        salesReportEntity.setDetails("");

        // When
        ProducerReportException exception = assertThrows(ProducerReportException.class,
                () -> salesReportEntity.validateSelf());

        // Then
        String errorMessage = exception.getMessage();
        assertTrue(errorMessage.contains("Product cannot be null or empty."));
        assertTrue(errorMessage.contains("Sale date cannot be in the future."));
        assertTrue(errorMessage.contains("Details cannot be null or empty."));
    }

    // --- Testes do método toString() ---

    @Test
    @DisplayName("should_return_correct_string_representation")
    void should_return_correct_string_representation() {
        // Given
        String expectedId = "123e4567-e89b-12d3-a456-426614174000";
        String expectedProduct = "Widget X";
        LocalDate expectedSaleDate = LocalDate.of(2023, 12, 1);
        String expectedDetails = "Batch 101 sold to customer Y";
        SalesReportEntity entityForToString = new SalesReportEntity(expectedId, expectedProduct, expectedSaleDate,
                expectedDetails);

        // When
        String actualToString = entityForToString.toString();

        // Then
        String expectedStringResult = expectedId + ", product=" + expectedProduct + ", saleDate=" + expectedSaleDate
                + ", details=" + expectedDetails;
        assertEquals(expectedStringResult, actualToString);
    }
}