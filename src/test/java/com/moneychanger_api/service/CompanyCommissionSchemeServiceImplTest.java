package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.model.CompanyCommissionScheme;
import com.moneychanger_api.model.MoneyChanger;
import com.moneychanger_api.repository.CommissionRateRepository;
import com.moneychanger_api.repository.CompanyCommissionSchemeRepository;
import com.moneychanger_api.repository.MoneyChangerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class CompanyCommissionSchemeServiceImplTest {

    @Mock
    private CompanyCommissionSchemeRepository repository;

    @Mock
    private MoneyChangerRepository moneyChangerRepository;

    @Mock
    private CommissionRateRepository commissionRateRepository;

    private CompanyCommissionSchemeServiceImpl service;

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
        service = new CompanyCommissionSchemeServiceImpl(repository, commissionRateRepository, moneyChangerRepository);
    }

    @Test
    void testListAll() {
        CompanyCommissionScheme activeScheme = new CompanyCommissionScheme();
        activeScheme.setIsDeleted(false);

        CompanyCommissionScheme deletedScheme = new CompanyCommissionScheme();
        deletedScheme.setIsDeleted(true);

        // The repository returns both active and deleted records
        when(repository.findAll()).thenReturn(List.of(activeScheme, deletedScheme));

        // The service should return only the non-deleted ones
        List<CompanyCommissionScheme> result = service.listAll();

        Assertions.assertEquals(1, result.size());
        Assertions.assertFalse(result.get(0).getIsDeleted());
    }

    @Test
    void testGet_Success() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();
        item.setId(1);
        item.setIsDeleted(false);  // important to set not deleted
        when(repository.findById(1)).thenReturn(Optional.of(item));

        CompanyCommissionScheme result = service.get(1);
        Assertions.assertEquals(1, result.getId());
    }

    @Test
    void testGet_Deleted_ThrowsException() {
        CompanyCommissionScheme deletedItem = new CompanyCommissionScheme();
        deletedItem.setId(1);
        deletedItem.setIsDeleted(true);  // marked as deleted
        when(repository.findById(1)).thenReturn(Optional.of(deletedItem));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.get(1));
    }

    @Test
    void testGet_NotFound_ThrowsException() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.get(1));
    }


    @Test
    void testSaveSuccess() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();

        MoneyChanger mc = new MoneyChanger();
        mc.setId(10L);
        item.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(20);
        item.setCommissionRateId(cr);

        // Mock the existence checks
        when(moneyChangerRepository.existsById(10L)).thenReturn(true);
        when(commissionRateRepository.existsById(20)).thenReturn(true);

        // No existing record with same moneyChangerId
        when(repository.findAll()).thenReturn(List.of());
        when(repository.save(item)).thenReturn(item);

        CompanyCommissionScheme saved = service.save(item);
        Assertions.assertEquals(10L, saved.getMoneyChangerId().getId());
        Assertions.assertEquals(20, saved.getCommissionRateId().getId());
    }

    @Test
    void testSaveThrowsIllegalArgumentExceptionWhenNullIds() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();

        // moneyChangerId null
        item.setMoneyChangerId(null);
        item.setCommissionRateId(new CommissionRate());
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.save(item));

        // commissionRateId null
        item.setMoneyChangerId(new MoneyChanger());
        item.setCommissionRateId(null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> service.save(item));
    }

    @Test
    void testSaveThrowsResourceNotFoundWhenMoneyChangerNotFound() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();

        MoneyChanger mc = new MoneyChanger();
        mc.setId(10L);
        item.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(20);
        item.setCommissionRateId(cr);

        when(moneyChangerRepository.existsById(10L)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.save(item));
    }

    @Test
    void testSaveThrowsResourceNotFoundWhenCommissionRateNotFound() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();

        MoneyChanger mc = new MoneyChanger();
        mc.setId(10L);
        item.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(20);
        item.setCommissionRateId(cr);

        when(moneyChangerRepository.existsById(10L)).thenReturn(true);
        when(commissionRateRepository.existsById(20)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.save(item));
    }

    @Test
    void testSaveDuplicateThrowsException() {
        CompanyCommissionScheme existing = new CompanyCommissionScheme();

        MoneyChanger mc = new MoneyChanger();
        mc.setId(10L);
        existing.setMoneyChangerId(mc);

        CompanyCommissionScheme newItem = new CompanyCommissionScheme();
        newItem.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(20);
        newItem.setCommissionRateId(cr);

        when(moneyChangerRepository.existsById(10L)).thenReturn(true);
        when(commissionRateRepository.existsById(20)).thenReturn(true);

        when(repository.findAll()).thenReturn(List.of(existing));

        Assertions.assertThrows(DuplicateResourceException.class, () -> service.save(newItem));
    }

    @Test
    void testDeleteSuccess() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();
        item.setId(1);
        item.setIsDeleted(false);

        when(repository.findById(1)).thenReturn(Optional.of(item));
        when(repository.save(any())).thenAnswer(i -> i.getArgument(0)); // Return saved entity

        service.delete(1);

        verify(repository).findById(1);
        verify(repository).save(argThat(saved -> saved.getIsDeleted() != null && saved.getIsDeleted()));

        // Assert the original object's isDeleted is set
        Assertions.assertTrue(item.getIsDeleted());
    }

    @Test
    void testDeleteThrowsResourceNotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(1));

        verify(repository, never()).save(any());
    }

}