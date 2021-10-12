package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import br.com.francis.currencyexchange.gateway.database.repository.ConversionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ListUserConversionsTests {

    @InjectMocks
    private ListUserConversions usecase;

    @Mock
    private ConversionRepository conversionRepository;

    private static final int LIST_SIZE_USER_1 = 5;
    private static final int LIST_SIZE_USER_2 = 5;
    private static final List<Conversion> LIST_USER_1 = createConversionList(1L, LIST_SIZE_USER_1);
    private static final List<Conversion> LIST_USER_2 = createConversionList(2L, LIST_SIZE_USER_2);

    private static List<Conversion> createConversionList(long userID, Integer listSize) {
        List<Conversion> conversionList = new ArrayList<>(listSize);
        for (int i = 0; i < listSize; i++) {
            Conversion conversion = new Conversion();
            conversion.setUserID(userID);
            conversionList.add(conversion);
        }
        return conversionList;
    }

    @Test
    public void shouldReturnDifferentListsForDifferentUsers() {
        when(conversionRepository.findByUserIDOrderByTransactionIDDesc(1L)).thenReturn(LIST_USER_1);
        when(conversionRepository.findByUserIDOrderByTransactionIDDesc(2L)).thenReturn(LIST_USER_2);

        List<Conversion> result = usecase.execute(1L);
        assertEquals(LIST_SIZE_USER_1, result.size());
        assertEquals(Long.valueOf(1L), result.get(0).getUserID());

        result = usecase.execute(2L);
        assertEquals(LIST_SIZE_USER_2, result.size());
        assertEquals(Long.valueOf(2L), result.get(0).getUserID());
    }

}
