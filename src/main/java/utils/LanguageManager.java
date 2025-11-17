package utils;

import java.util.Locale;
import dao.LocalizationDao;
import javafx.application.Platform;
import javafx.geometry.NodeOrientation;
import javafx.scene.layout.AnchorPane;

import java.util.Map;

public class LanguageManager {

    private static Locale currentLocale = new Locale("en", "US");
    private static Map<String, String> localizedStrings;
    private static LocalizationDao localizationDao = new LocalizationDao();

    public static void setLanguage(String language, String country) {
        currentLocale = new Locale(language, country);
        localizedStrings = localizationDao.getLocalizedStrings(currentLocale);
    }

   public static Map<String, String> getLocalizedStrings() {
       return localizedStrings;
   }


//    private void applyTextDirection(Locale currentLocale) {
//
//        String lang = currentLocale.getLanguage();
//        boolean isRTL = lang.equals("fa") || lang.equals("ur") || lang.equals("ar") || lang.equals("he");
//
//        Platform.runLater(() -> {
//            if (rootVBox != null) {
//                // Flip the full layout direction
//                rootVBox.setNodeOrientation(
//                        isRTL ? NodeOrientation.RIGHT_TO_LEFT : NodeOrientation.LEFT_TO_RIGHT
//                );
//            }
//
//            // Align text fields
//            tfWeight.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
//            tfHeight.setStyle(isRTL ? "-fx-text-alignment: right;" : "-fx-text-alignment: left;");
//        });
//    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }
    public static String getTranslation(String key) {
        if (localizedStrings == null) {
            setLanguage("en", "US");
        }
        if (!localizedStrings.containsKey(key)) {
            System.out.println("Translation key" + key + " not found for language: " + currentLocale.getLanguage());

            return "TRANSLATION_MISSING";
        }

        return localizedStrings.get(key);
    }
}