package controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import utils.LanguageManager;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class, ApplicationExtension.class})
class BaseControllerTest {

    @Test
    void testUpdateLanguage_CallsGetLocalizedStrings() {
        BaseController baseController = new BaseController() {
        };

        try (MockedStatic<LanguageManager> mockedStatic = mockStatic(LanguageManager.class)) {

            Map<String, String> dummyMap = new HashMap<>();
            dummyMap.put("test_key", "test_value");
            mockedStatic.when(LanguageManager::getLocalizedStrings).thenReturn(dummyMap);


            baseController.updateLanguage();


            mockedStatic.verify(LanguageManager::getLocalizedStrings, times(1));
        }
    }
}
