package ru.netology.patient.service.madical;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class MedicalServiceImplTest {

    public PatientInfo patient1 = new PatientInfo("patient1", "Ivan", "Ivanov",
                                            LocalDate.of(1985, 3, 31),
                                            new HealthInfo(new BigDecimal(36.6),
                                                            new BloodPressure(110, 70)));

    @ParameterizedTest
    @ValueSource(doubles = {36.6, 39.9, 43.0, 20.0})
    public void testCheckTemperature(double t) {
        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);

        PatientInfoRepository patientInfoRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepositoryMock.getById("patient1")).thenReturn(patient1);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, alertServiceMock);
        medicalService.checkTemperature("patient1", new BigDecimal(t));

        // Извините, но даже учебные программы не должны содержать явные ошибки.
        //Считайте, что тестировщик уточнил у бизнес-аналитика диапазоны допустимой температуры у человека и вставил проверку в тест
        if (t >= 35.0 && t <= 37.0) {
            Mockito.verify(alertServiceMock, never()).send(String.format("Warning, patient with id: %s, need help", patient1.getId()));
        }
        else {
            Mockito.verify(alertServiceMock, only()).send(String.format("Warning, patient with id: %s, need help", patient1.getId()));
        }
    }

    @ParameterizedTest
    @CsvSource({
            "110, 70",
            "90, 60",
            "110, 80",
            "65, 50",
            "180, 120"
    })
    public void testCheckBloodPressure(int high, int low) {
        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);

        PatientInfoRepository patientInfoRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoRepositoryMock.getById("patient1")).thenReturn(patient1);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, alertServiceMock);
        BloodPressure bloodPressure = new BloodPressure(high, low);

        medicalService.checkBloodPressure("patient1", bloodPressure);
        if (bloodPressure.getHigh() > 69 && bloodPressure.getHigh() <= 120) {
            Mockito.verify(alertServiceMock, never()).send(String.format("Warning, patient with id: %s, need help", patient1.getId()));
        }
        else {
            Mockito.verify(alertServiceMock, times(1)).send(String.format("Warning, patient with id: %s, need help", patient1.getId()));
        }
    }

    /*
    // Этот тест не работает,так как getPatientInfo - private метод
    @Test
    public void testGetPatientInfo_throwException() {
        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);
        PatientInfoRepository patientInfoRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        MedicalService medicalService = new MedicalServiceImpl(patientInfoRepositoryMock, alertServiceMock);

        Assertions.assertThrows(RuntimeException.class, () ->{ ((MedicalServiceImpl) medicalService).getPatientInfo(null);
        });
    }
    */
}
