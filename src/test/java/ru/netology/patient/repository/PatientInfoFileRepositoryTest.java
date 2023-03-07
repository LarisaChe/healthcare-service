package ru.netology.patient.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.PatientInfo;

public class PatientInfoFileRepositoryTest {
    @Test
    public void testGetById_throwException() {
        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        final PatientInfo patient1 = patientInfoFileRepositoryMock.getById("patient1");
        Assertions.assertNull(patient1);
    }
}
