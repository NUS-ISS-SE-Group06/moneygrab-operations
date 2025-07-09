package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.model.ComputeRateId;
import com.moola.fx.moneychanger.operations.repository.ComputeRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ComputeRateServiceImplTest {

    private ComputeRateRepository repo;
    private ComputeRateServiceImpl service;

    @BeforeEach
    void setUp() {
        repo = mock(ComputeRateRepository.class);
        service = new ComputeRateServiceImpl(repo);
    }

    @Test
    void get_shouldReturnComputeRate() {
        ComputeRateId id = new ComputeRateId();
        id.setCurrencyCode("USD");
        id.setMoneyChangerId(1L);

        ComputeRate expected = new ComputeRate();
        expected.setId(id);

        when(repo.findByIdCurrencyCodeAndIdMoneyChangerId("USD", 1L))
                .thenReturn(Optional.of(expected));

        ComputeRate actual = service.get("USD", 1L);
        assertEquals(expected, actual);
    }

    @Test
    void get_shouldThrowWhenNotFound() {
        when(repo.findByIdCurrencyCodeAndIdMoneyChangerId("USD", 1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.get("USD", 1L));
    }

    @Test
    void saveAll_shouldSaveAllRecords() {
        ComputeRateId id1 = new ComputeRateId();
        id1.setCurrencyCode("USD");
        id1.setMoneyChangerId(1L);
        ComputeRate rate1 = new ComputeRate();
        rate1.setId(id1);

        ComputeRateId id2 = new ComputeRateId();
        id2.setCurrencyCode("EUR");
        id2.setMoneyChangerId(2L);
        ComputeRate rate2 = new ComputeRate();
        rate2.setId(id2);

        List<ComputeRate> rates = List.of(rate1, rate2);

        when(repo.saveAll(rates)).thenReturn(rates);

        List<ComputeRate> result = service.saveAll(rates);

        assertEquals(2, result.size());
        verify(repo).saveAll(rates);
    }

    @Test
    void delete_shouldRemoveIfExists() {
        ComputeRateId id = new ComputeRateId();
        id.setCurrencyCode("USD");
        id.setMoneyChangerId(1L);

        ComputeRate rate = new ComputeRate();
        rate.setId(id);

        when(repo.findByIdCurrencyCodeAndIdMoneyChangerId("USD", 1L)).thenReturn(Optional.of(rate));

        service.delete("USD", 1L);
        verify(repo).delete(rate);
    }


    @Test
    void testDeleteOrphanedRatesByMoneyChanger_shouldCallRepositoryWithCorrectId() {
        // Arrange
        Long moneyChangerId = 123L;
        when(repo.deleteOrphanedComputeRates(moneyChangerId)).thenReturn(3);

        // Act
        service.deleteOrphanedRates(moneyChangerId);

        // Assert
        verify(repo, times(1)).deleteOrphanedComputeRates(moneyChangerId);
    }
}
